import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Database extends RemoteServer implements RegRemoteInterface {
    //class that identify the user
    public class DataObject {
        private String passwd;
        private transient boolean onlineStatus;
        private int score;
        private ArrayList<String> friendList;

        //inet address
        private transient InetAddress inetAddr;
        //UDP port
        private transient int UDPport;

        //builder
        public DataObject(String passwd) {
            this.passwd = passwd;
            this.score = 0;
            this.friendList = new ArrayList<>();
        }

        public String getPasswd() {
            return passwd;
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

        public void setScore(int newScore) {
            this.score += newScore;
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

        public void setInetAddress(InetAddress addr) {
            this.inetAddr = addr;
        }
        public InetAddress getInetAdrress() {
            return this.inetAddr;
        }
        public void setUDPport(int port) {
            this.UDPport = port;
        }
        public int getUDPport() {
            return this.UDPport;
        }
    }
    //class that identify users in the rank
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
    private static String jsonpath = "./data.json";

    //constructor
    public Database() {
        this.dataMap = new HashMap<>();
        gwriter = new GsonBuilder().setPrettyPrinting().create();
        if(Files.exists(Paths.get(jsonpath))) {
            try {
                FileReader rd = new FileReader(jsonpath);
                Gson greader = new Gson();
                dataMap = greader.fromJson(rd, new TypeToken<HashMap<String, DataObject>>(){}.getType());
                rd.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //userRegister
    @Override
    public synchronized ReturnCodes.Codex userRegistration(String nickname, String passwd) throws RemoteException, NullPointerException {
        if(nickname == null || passwd == null) throw new NullPointerException("Invalid username or password");
        if(nickname.equals("") || passwd.equals("")) return ReturnCodes.Codex.EMPTY_NICK_OR_PASS;
        //if user already exists
        if(dataMap.containsKey(nickname)) return ReturnCodes.Codex.ALREADY_REGISTERED;
        dataMap.put(nickname, new DataObject(passwd));
        //write on JSON...
        try {
            FileWriter wrt = new FileWriter(jsonpath);
            gwriter.toJson(dataMap, wrt);
            wrt.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ReturnCodes.Codex.SUCCESS;
    }
    //userLogin
    public synchronized ReturnCodes.Codex userLogin(String nickname, String passwd, InetAddress addr, int UDPport) throws NullPointerException{
        if(nickname == null || passwd == null) throw new NullPointerException("Invalid username or password");
        //if user exists
        if(dataMap.containsKey(nickname)) {
            DataObject user = dataMap.get(nickname);
            //if passwd correspond to the user's password
            if(user.getPasswd().equals(passwd)) {
                //if user is not online
                if (!user.getStatus()) {
                    user.setOnLine();
                    user.setInetAddress(addr);
                    user.setUDPport(UDPport);
                    return ReturnCodes.Codex.SUCCESS;
                } else return ReturnCodes.Codex.ALREADY_LOGGED_IN;
            }
            else return ReturnCodes.Codex.WRONG_PASSWORD;
        }
        else return ReturnCodes.Codex.USER_NOT_FOUND;
    }
    //userLogout
    public synchronized ReturnCodes.Codex userLogout(String nickname) throws NullPointerException {
        if(nickname == null) throw new NullPointerException("Invalid username");
        //if user exists
        if(dataMap.containsKey(nickname)) {
            DataObject user = dataMap.get(nickname);
            //if user is online
            if(user.getStatus()) {
                user.setOffLine();
                return ReturnCodes.Codex.SUCCESS;
            }
            else return ReturnCodes.Codex.ALREADY_LOGGED_OUT;
        }
        else return ReturnCodes.Codex.USER_NOT_FOUND;
    }
    //userAddFriend
    public synchronized ReturnCodes.Codex userAddFriend(String nickname, String nickfriend) throws NullPointerException {
        if(nickname == null || nickfriend == null) throw new NullPointerException("Invalid username or nickfriend");
        //if user exists
        if(dataMap.containsKey(nickfriend)) {
            //already friends
            if(dataMap.get(nickname).addFriend(nickfriend) == -1 || dataMap.get(nickfriend).addFriend(nickname) == -1)
                return ReturnCodes.Codex.ALREADY_FRIENDS;
            //write on JSON
            try {
                FileWriter wrt = new FileWriter(jsonpath);
                gwriter.toJson(dataMap, wrt);
                wrt.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ReturnCodes.Codex.SUCCESS;
        }
        else return ReturnCodes.Codex.USER_NOT_FOUND;
    }
    //userListFriends
    public synchronized JsonArray userListFriends(String nickname) throws NullPointerException {
        if(nickname == null) throw new NullPointerException("Invalid username");
        //if user exists
        if(dataMap.containsKey(nickname))
            return new Gson().toJsonTree(dataMap.get(nickname).getFriendList()).getAsJsonArray();
        else return null;
    }
    //userChallenge
    public ReturnCodes.Codex userChallenge(String nickname, String nickfriend, DatagramSocket dataSock, int challengePort) throws NullPointerException {
        if(nickname == null || nickfriend == null) throw new NullPointerException("Invalid username or nickfriend");
        //if user exists
        if(dataMap.containsKey(nickfriend)) {
            //if nickfriend is a nickname's friend
            if(dataMap.get(nickname).getFriendList().contains(nickfriend)) {
                //sending request if nickfriend is online
                if(dataMap.get(nickfriend).getStatus()) {
                    String line = "Challenge " + nickname + " " + challengePort;
                    DatagramPacket message = new DatagramPacket(line.getBytes(), line.getBytes().length, dataMap.get(nickfriend).getInetAdrress(), dataMap.get(nickfriend).getUDPport());
                    try {
                        dataSock.send(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return ReturnCodes.Codex.SUCCESS;
                }
                else return ReturnCodes.Codex.USER_NOT_ONLINE;
            }
            else return ReturnCodes.Codex.NOT_A_FRIEND;
        }
        else return ReturnCodes.Codex.USER_NOT_FOUND;
    }
    //challengeRequestResult
    public void challengeRequestResult(String nickname, String nickfriend, String result, DatagramSocket dataSock, int challengePort, int kwords) {
        String line = result;
        //sending the timeout to the challenged
        if(result.equals("Timeout")) {
            line = line + " by " + nickname;
            DatagramPacket message = new DatagramPacket(line.getBytes(), line.getBytes().length, dataMap.get(nickfriend).getInetAdrress(), dataMap.get(nickfriend).getUDPport());
            try {
                dataSock.send(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            //sending the accepted or declined request to the challenger
            if (result.equals("Accepted")) line = line + " " + kwords + " " + challengePort;
            DatagramPacket message = new DatagramPacket(line.getBytes(), line.getBytes().length, dataMap.get(nickname).getInetAdrress(), dataMap.get(nickname).getUDPport());
            try {
                dataSock.send(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //showUserScore
    public int showUserScore(String nickname) throws NullPointerException {
        if(nickname == null) throw new NullPointerException("Invalid username");
        //if user exists
        if(dataMap.containsKey(nickname))
            return dataMap.get(nickname).getScore();
        else return -1;
    }
    //showRanking
    public synchronized JsonArray showRanking(String nickname) throws NullPointerException {
        if(nickname == null) throw new NullPointerException("Invalid username");
        ArrayList<UserRank> rankingList = new ArrayList<>();
        //creating an iterator on the list of nickname's friends
        for (String index : dataMap.get(nickname).getFriendList()) {
            //creating a new UserRank object and adding it to the ranking list
            rankingList.add(new UserRank(index, dataMap.get(index).getScore()));
        }
        rankingList.add(new UserRank(nickname, dataMap.get(nickname).getScore()));
        //sorting
        Collections.sort(rankingList);
        return new Gson().toJsonTree(rankingList).getAsJsonArray();
    }
    //retrieving user
    public synchronized DataObject getUser(String nickname) throws NullPointerException {
        if(nickname == null) throw new NullPointerException("Invalid username");
        return dataMap.get(nickname);
    }

}
