import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ChallengeHandler extends Thread {
    //port shared between the clients
    private int sharedPort;
    private Selector selector;
    private ServerSocket serverSocket;
    private ServerSocketChannel serverSocketChannel;

    //json array to store the italian words red from file
    private JsonArray wordArray;

    //array for chosen words and translated words
    private ArrayList<String> chosenWords;
    private ArrayList<String> englishWords;

    //array to store the client's key
    private ArrayList<SelectionKey> clientkeys;

    private Database database;
    public int kWords = 8;

    //atomic variable to know if the client has finished
    private volatile AtomicInteger users;
    public volatile AtomicInteger timeout;

    //points
    private int correct = 5;
    private int wrong = -2;
    private int bonus = 3;

    //class used to represent user during the challenge
    private class Item {
        private String username;
        private int challengePoints;
        private int indexWord;
        private String word;

        public Item(String word, int indexWord) {
            this.username = null;
            this.challengePoints = 0;
            this.indexWord = indexWord;
            this.word = word;
        }

        public String getUser() {
            return this.username;
        }
        public void setUser(String name) {
            this.username = name;
        }
        public int getPoints() {
            return this.challengePoints;
        }
        public void setPoints(int point) {
            this.challengePoints += point;
        }
        public int getIndex() {
            return this.indexWord;
        }
        public void incIndex() {
            this.indexWord++;
        }
        public String getWord() {
            return this.word;
        }
        public void setWord(String word) {
            this.word = word;
        }
    }

    //constructor
    public ChallengeHandler(Database database, int port) {
        this.sharedPort = port;
        this.database = database;
        this.chosenWords = new ArrayList<>();
        this.clientkeys = new ArrayList<>();
        this.users = new AtomicInteger(0);
        this.timeout = new AtomicInteger(0);
    }

    public void run() {
        //reading italian words from the dictionary
        try {
            Gson greader = new Gson();
            FileReader rd = new FileReader("./dictionary.json");
            wordArray = greader.fromJson(rd, new TypeToken<JsonArray>(){}.getType());
            rd.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        //chosing k words
        for(int i = 0; i < kWords; i++) {
            chosenWords.add(String.valueOf(wordArray.get((int) ((Math.random() * wordArray.size())))).replaceAll("\"", ""));
        }

        //creating socket
        try {
            serverSocketChannel = ServerSocketChannel.open();
            //configuring non-blocking
            serverSocketChannel.configureBlocking(false);
            serverSocket = serverSocketChannel.socket();
            InetSocketAddress address = new InetSocketAddress(sharedPort);
            //binding address
            serverSocket.bind(address);
            selector = Selector.open();
            //Key's operation's registration
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //if there are two user connected and this thread is active
        while(users.get() != 2 && !Thread.currentThread().isInterrupted()) {
            try {
                //waiting on select
                selector.select();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            //set of ready channels
            Set<SelectionKey> ready = selector.selectedKeys();
            Iterator<SelectionKey> itr = ready.iterator();

            //checking each key
            while(itr.hasNext()) {
                SelectionKey key = (SelectionKey) itr.next();
                itr.remove();
                try {
                    if(key.isAcceptable()) {
                        //retrieving the channel and creating
                        ServerSocketChannel sockCh = (ServerSocketChannel) key.channel();
                        SocketChannel client = sockCh.accept();
                        client.configureBlocking(false);
                        //creating new key linked to socket client and saving it
                        SelectionKey cKey = client.register(selector, SelectionKey.OP_WRITE, new Item(null, 0));
                        clientkeys.add(cKey);
                        if(englishWords == null) {
                            englishWords = new ArrayList<>();
                            //getting translation of each word
                            getTranslation();
                            //creating the timer for the challenge
                            new MyTimer(60, this);
                        }
                    }
                    if(key.isWritable()) {
                        //retrieving the channel that is writable
                        SocketChannel client = (SocketChannel) key.channel();
                        //retrieving the user state attached
                        Item newItem = (Item) key.attachment();
                        //check if there is something else to write
                        if(newItem.getWord() == null) {
                            //set the new word if no one has terminated or the time is not over
                            if(newItem.getIndex() < kWords && users.get() != 1 && timeout.get() == 0) {
                                newItem.setWord(chosenWords.get(newItem.getIndex()));
                            }
                            else {
                                String token = null;
                                //time is over
                                if(timeout.get() == 1) {
                                    token = "Timeout";
                                }
                                //challenge is ended
                                else {
                                    token = "End";
                                }
                                Item first = (Item) clientkeys.get(0).attachment();
                                Item second= (Item) clientkeys.get(1).attachment();
                                //check the scores
                                if(first.getPoints() == second.getPoints()) {
                                    newItem.setWord(token + " " + newItem.getPoints() + " TIE");
                                }
                                else if(first.getPoints() < second.getPoints() && newItem.getUser().equals(second.getUser())) {
                                    newItem.setWord(token + " " + newItem.getPoints() + " WIN");
                                    Database.DataObject user = database.getUser(newItem.getUser());
                                    user.setScore(bonus);
                                }
                                else if(first.getPoints() > second.getPoints() && newItem.getUser().equals(first.getUser())) {
                                    newItem.setWord(token + " " + newItem.getPoints() + " WIN");
                                    Database.DataObject user = database.getUser(newItem.getUser());
                                    user.setScore(bonus);
                                }
                                else {
                                    newItem.setWord(token + " " + newItem.getPoints() + " LOSE");
                                }
                                users.incrementAndGet();
                            }
                        }
                        //writing the word into the channel
                        ByteBuffer toWrite = ByteBuffer.wrap(newItem.getWord().getBytes());
                        int byteWritten = client.write(toWrite);
                        //check the byte written
                        if(byteWritten == newItem.getWord().length()) {
                            //if the whole word has been written, reset the key
                            newItem.setWord(null);
                            key.attach(newItem);
                            key.interestOps(SelectionKey.OP_READ);
                        }
                        else if(byteWritten == -1) {
                            //if the write return -1 or the client has closed the socket
                            key.cancel();
                            key.channel().close();
                        }
                        else {
                            //there is something to write
                            toWrite.flip();
                            newItem.setWord(StandardCharsets.UTF_8.decode(toWrite).toString());
                            key.attach(newItem);
                        }
                    }
                    if(key.isReadable()) {
                        //retrieving the channel that is readable
                        SocketChannel client = (SocketChannel) key.channel();
                        //retrieving the user state attached
                        Item newItem = (Item) key.attachment();
                        String token = "";
                        //retrieving word if present
                        if(newItem.getWord() != null) token = newItem.getWord();
                        ByteBuffer toRead = ByteBuffer.allocate(1024);
                        toRead.clear();
                        int byteRed = client.read(toRead);
                        //check if the buffer is full
                        if(byteRed == 1024) {
                            toRead.flip();
                            token = token + StandardCharsets.UTF_8.decode(toRead).toString();
                            //setting the remaining word and re-attaching the item
                            newItem.setWord(token);
                            key.attach(newItem);
                        }
                        //if everything has been red
                        if(byteRed < 1024) {
                            toRead.flip();
                            token = token + StandardCharsets.UTF_8.decode(toRead).toString();
                            if(token.equals("Exit")) {
                                users.incrementAndGet();
                                key.cancel();
                                key.channel().close();
                            }
                            else {
                                //tokenizing the string
                                String[] tokens = token.split(" ");
                                //saving name
                                if(newItem.getUser() == null) {
                                    newItem.setUser(tokens[0]);
                                    Item u = new Item(null, 0);
                                    u.setUser(tokens[0]);
                                }
                                if(users.get() != 2 && timeout.get() == 0) {
                                    checkTranslation(tokens[1], englishWords.get(newItem.getIndex()), tokens[0], newItem);
                                }
                                //resetting item
                                newItem.setWord(null);
                                newItem.incIndex();
                                key.attach(newItem);
                                key.interestOps(SelectionKey.OP_WRITE);
                            }
                        }
                        //if client closes the socket
                        if(byteRed == -1) {
                            key.cancel();
                            key.channel().close();
                        }
                    }
                } catch (Exception e) {
                    key.cancel();
                    try {
                        key.channel().close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
        if (users.get() == 2 || timeout.get() == 1) {
            database.updateChallenge();
        }
    }

    //function to check the translation
    public void checkTranslation(String word, String translation, String name, Item item) {
        Database.DataObject user = database.getUser(name);
        System.out.println(word);
        System.out.println(translation);
        if(word.equals(translation)) {
            item.setPoints(correct);
            user.setScore(correct);
        }
        else {
            item.setPoints(wrong);
            user.setScore(wrong);
        }
    }

    //function to get the translation
    public void getTranslation() throws IOException {
        //http request for each word to be translated
        for(int i = 0; i < kWords; i++) {
            URL newUrl = new URL("https://api.mymemory.translated.net/get?q=" + chosenWords.get(i) + "&langpair=it|en");
            HttpsURLConnection connection = (HttpsURLConnection) newUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            if(connection.getResponseCode() != 200) {
                throw new RuntimeException("Request failed: " + connection.getResponseCode());
            }
            //if request does not fail
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = reader.readLine();
            try {
                JSONParser parser = new JSONParser();
                JSONObject o = (JSONObject) parser.parse(line);
                JSONObject obj = (JSONObject) o.get("responseData");
                englishWords.add(i, (String) obj.get("translatedText").toString().toLowerCase());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            connection.disconnect();
        }
    }

    //class that manages the timer
    public class MyTimer {
        private Timer timer;
        private ChallengeHandler challenge;

        public MyTimer(int timeInSeconds, ChallengeHandler challenge) {
            this.challenge = challenge;
            timer = new Timer();
            timer.schedule(new Remind(), timeInSeconds * 1000);
        }

        public class Remind extends TimerTask {
            @Override
            public void run() {
                if(challenge.isAlive()) {
                    challenge.timeout.incrementAndGet();
                }
            }
        }
    }
}
