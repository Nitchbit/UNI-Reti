import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google. gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class Database extends RemoteServer implements RegRemoteInterface {
    private class DataObject {
        private String passwd;
        private boolean onlineStatus;
        private int score;
        private ArrayList<String> friendList;

        //builder
        public DataObject(String passwd) {
            this.passwd = passwd;
            this.score = 0;
            this.friendList = new ArrayList<>();
        }
        public String getPasswd() {
            return passwd;
        }
        public void setScore(int newScore) {
            this.score = newScore;
        }
        public int getScore() {
            return this.score;
        }
        public int addFriend(String nickfriend) {
            if(friendList.contains(nickfriend)) return -1;
            friendList.add(nickfriend);
            return 0;
        }
        public ArrayList<String> getFriendList() {
            return this.friendList;
        }
        public void setOnLine() {
            this.onlineStatus = true;
        }
        public void setOffLine() {
            this.onlineStatus = false;
        }
        public boolean getStatus() {
            return onlineStatus;
        }
    }
    private class UserRank implements Comparable<UserRank> {
        public String nickname;
        public int score;

        public UserRank(String nickname, int score) {
            this.nickname = nickname;
            this.score = score;
        }

        @Override
        public int compareTo(UserRank o) {
            return o.score - this.score;
        }
    }

    //DataObject's HashMap
    private HashMap<String, DataObject> dataMap;
    private Gson gwriter;
    private Gson greader;
    private static String jsonpath = "./data.json";
    /*public void printHashMap() {
        try {
            FileWriter wrt = new FileWriter("./backup.json");
            gwriter.toJson(dataMap, wrt);
            wrt.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(dataMap);
        for(String item : dataMap.keySet()) {
            System.out.println(item);
            System.out.println(dataMap.get(item).passwd);
        }
    }*/

    //builder
    public Database() {
        this.dataMap = new HashMap<>();
        gwriter = new GsonBuilder().setPrettyPrinting().create();
        if(Files.exists(Paths.get(jsonpath))) {
            try {
                FileReader rd = new FileReader(jsonpath);
                greader = new Gson();
                dataMap = greader.fromJson(rd, new TypeToken<HashMap<String, DataObject>>(){}.getType());
                rd.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //userRegister
    @Override
    public synchronized int userRegistration(String nickname, String passwd) throws NullPointerException {
        if(nickname == null || passwd == null) throw new NullPointerException("Invalid username or password");
        //code error to manage...
        if(nickname.equals("") || passwd.equals("")) return -1;
        if(dataMap.containsKey(nickname)) return -1;
        dataMap.put(nickname, new DataObject(passwd));
        //write on JSON...
        try {
            FileWriter wrt = new FileWriter(jsonpath);
            gwriter.toJson(dataMap, wrt);
            wrt.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
    //userLogin
    public synchronized int userLogin(String nickname, String passwd) throws NullPointerException{
        if(nickname == null || passwd == null) throw new NullPointerException("Invalid nickname or password");
        //code error to manage...
        if(dataMap.containsKey(nickname)) {
            DataObject user = dataMap.get(nickname);
            if(user.getPasswd().equals(passwd) && !user.getStatus()) {
                user.setOnLine();
                return 0;
            }
            else return -1;
        }
        else return -1;
    }
    //userLogout
    public synchronized int userLogout(String nickname) throws NullPointerException {
        if(nickname == null) throw new NullPointerException("Invalid nickname");
        //error code to manage
        if(dataMap.containsKey(nickname)) {
            DataObject user = dataMap.get(nickname);
            if(user.getStatus()) {
                user.setOffLine();
                return 0;
            }
            else return -1;
        }
        else return -1;
    }
    //userAddFriend
    public synchronized int userAddFriend(String nickname, String nickfriend) throws NullPointerException, IOException {
        if(nickname == null || nickfriend == null) throw new NullPointerException("Invalid nickname");
        //error code to manage
        if(dataMap.containsKey(nickname) && dataMap.containsKey(nickfriend)) {
            if(dataMap.get(nickname).addFriend(nickfriend) == -1) return -1;
            if(dataMap.get(nickfriend).addFriend(nickname) == -1) return -1;
            try {
                FileWriter wrt = new FileWriter(jsonpath);
                gwriter.toJson(dataMap, wrt);
                wrt.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }
        else return -1;
    }
    //userListFriends
    public synchronized JsonArray userListFriends(String nickname) throws NullPointerException {
        if(nickname == null) throw new NullPointerException("Invalid nickname");
        //error code to manage
        if(dataMap.containsKey(nickname))
            return new Gson().toJsonTree(dataMap.get(nickname).getFriendList()).getAsJsonArray();
        else return null;
    }
    //userChallenge
    //showUserScore
    public int showUserScore(String nickname) throws NullPointerException {
        if(nickname == null) throw new NullPointerException("Invalid nickname");
        //error code to manage
        if(dataMap.containsKey(nickname))
            return dataMap.get(nickname).getScore();
        else return -1;
    }
    //showRanking
    public synchronized JsonArray showRanking(String nickname) throws NullPointerException {
        if(nickname == null) throw new NullPointerException();
        ArrayList<UserRank> rankingList = new ArrayList<>();
        //creating an iterator on the list of nickname's friends
        Iterator<String> itr = dataMap.get(nickname).getFriendList().iterator();
        while(itr.hasNext()) {
            //creating a new UserRank object and adding it to the ranking list
            String index = itr.next();
            rankingList.add(new UserRank(index, dataMap.get(index).getScore()));
        }
        rankingList.add(new UserRank(nickname, dataMap.get(nickname).getScore()));
        Collections.sort(rankingList);
        return new Gson().toJsonTree(rankingList).getAsJsonArray();
    }

}
