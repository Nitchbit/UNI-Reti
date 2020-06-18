import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.util.ArrayList;
import java.util.HashMap;

public class Database extends RemoteServer implements RegRemoteInterface {
    private class DataObject {
        private String passwd;
        private int status;
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
        public void setOnLine() {
            this.status = 1;
        }
        public void setOffLine() {
            this.status = 0;
        }
        public int getStatus() {
            return status;
        }
    }
    //DataObject's HashMap
    private HashMap<String, DataObject> dataMap;

    //builder
    public Database() {
        this.dataMap = new HashMap<>();
        //JSON loader
    }

    //userRegister
    @Override
    public synchronized int userRegistration(String nickname, String passwd) throws RemoteException, NullPointerException {
        if(nickname == null || passwd == null) throw new NullPointerException("Invalid username or password");
        //code error to manage
        if(nickname.equals("") || passwd.equals("")) return -1;
        if(dataMap.containsKey(nickname)) return -1;
        dataMap.put(nickname, new DataObject(passwd));
        return 0;
    }
    //userLogin
    public synchronized int userLogin(String nickname, String passwd) throws NullPointerException{
        if(nickname == null || passwd == null) throw new NullPointerException("Invalid nickname or password");
        //code error to manage
        if(nickname.equals("") || passwd.equals("")) return -1;
        if(dataMap.containsKey(nickname)) {
            DataObject user = dataMap.get(nickname);
            if(user.getPasswd().equals(passwd)) {
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
        if(nickname.equals("")) return -1;
        if(dataMap.containsKey(nickname)) {
            DataObject user = dataMap.get(nickname);
            if(user.getStatus() == 1) {
                user.setOffLine();
                return 0;
            }
            else return -1;
        }
        else return -1;
    }
    //userAddFriend
    public synchronized int userAddFriend(String nickname, String nickfriend) throws NullPointerException {
        if(nickname == null || nickfriend == null) throw new NullPointerException("Invalid nickname");
        if(nickname.equals("") || nickfriend.equals("")) return -1;
        if(!dataMap.containsKey(nickname) || !dataMap.containsKey(nickfriend)) {
            return -1;
        }
        else return dataMap.get(nickname).addFriend(nickfriend);
    }
    //userListFriends
    //userChallenge
    //userScore
    //userRanking

}
