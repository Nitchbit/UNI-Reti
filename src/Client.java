import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javax.swing.*;
import java.io.*;
import java.net.DatagramSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class Client {
    public static RegRemoteInterface userReg;
    //socket for communications
    private Socket sockTCP;
    private DatagramSocket UDPSocket;
    //port for notification
    private int portUDP;
    //port for communications
    private static int portSocket = 6000;
    //RMI port
    private static int portRMI = 7000;
    //server address
    private static String serverInetAddress = "localhost";
    private BufferedWriter writer;
    private BufferedReader reader;
    public String userNickname;
    //challenge port
    public int challengePort;
    private Notify notificationThread;
    //GUI view
    private static RegLogView reglogView;
    private static MainView mainView;
    private static ChallengeView challengeView;

    public static void main(String[] args) {
        //building RMI service
        Registry reg;
        Remote remObject;
        try {
            reg = LocateRegistry.getRegistry(portRMI);
            remObject = reg.lookup("RegistrationService");
            userReg = (RegRemoteInterface) remObject;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        //Graphic Interface Setting
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //starting the first view
        new StartingView();
    }

    public void gotoRegLogView() {
        reglogView = new RegLogView();
        reglogView.setInstance(this);
    }

    public void gotoMainView() {
        mainView = new MainView();
        mainView.setInstance(this);
        notificationThread.setView(mainView);
    }

    public void gotoChallenge() {
        challengeView = new ChallengeView();
        challengeView.setInstance(this);
    }

    public ReturnCodes.Codex login(String nickname, String passwd) {
        ReturnCodes.Codex result = null;
        try {
            //creating socket
            sockTCP = new Socket(serverInetAddress, portSocket);
            //creating port for notifications
            portUDP = (int) ((Math.random() * (65535 - 1024) + 1) + 1024);
            writer = new BufferedWriter(new OutputStreamWriter(sockTCP.getOutputStream()));
            reader= new BufferedReader(new InputStreamReader(sockTCP.getInputStream()));

            //sending request to server
            writer.write("Login " + nickname + " " + passwd + " " + sockTCP.getInetAddress().getHostAddress() + " " + portUDP);
            writer.newLine();
            writer.flush();

            //response
            result = ReturnCodes.toCodex(reader.readLine());
            if(!result.equals(ReturnCodes.Codex.SUCCESS)) {
                sockTCP.close();
                writer.close();
                reader.close();
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        notificationThread = new Notify(this, UDPSocket);
        notificationThread.start();
        this.userNickname = nickname;
        return result;
    }

    public ReturnCodes.Codex logout() {
        try {
            //sending request to server
            writer.write("Logout " + userNickname);
            writer.newLine();
            writer.flush();
            return ReturnCodes.toCodex(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ReturnCodes.Codex.SUCCESS;
    }

    public ReturnCodes.Codex addFriend(String nickname) {
        try {
            //sending request to server
            writer.write("Add " + userNickname + " " + nickname);
            writer.newLine();
            writer.flush();
            return ReturnCodes.toCodex(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ReturnCodes.Codex.SUCCESS;
    }

    public ArrayList<String> listFriend() {
        try {
            //sending request to server
            writer.write("List " + userNickname);
            writer.newLine();
            writer.flush();

            //response
            JsonArray arrayTmp = new JsonParser().parse(reader.readLine()).getAsJsonArray();
            ArrayList<String> list = new ArrayList<>();
            for(int i=0; i<arrayTmp.size(); i++) {
                list.add(arrayTmp.get(i).getAsString());
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int score() {
        try {
            //sending request to server
            writer.write("Score " + userNickname);
            writer.newLine();
            writer.flush();

            return Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public ArrayList<String> rank() {
        try {
            //sending request to server
            writer.write("Rank " + userNickname);
            writer.newLine();
            writer.flush();

            //response
            JsonArray arrayTmp = new JsonParser().parse(reader.readLine()).getAsJsonArray();
            ArrayList<String> list = new ArrayList<>();
            for(int i=0; i<arrayTmp.size(); i++) {
                JsonObject item = arrayTmp.get(i).getAsJsonObject();
                list.add(item.get("nickname").toString() + ": " + item.get("score").toString());
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ReturnCodes.Codex challenge(String nickname) {
        try {
            //sending request to server
            writer.write("Challenge " + userNickname + " " + nickname);
            writer.newLine();
            writer.flush();
            return ReturnCodes.toCodex(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ReturnCodes.Codex.SUCCESS;
    }
}
