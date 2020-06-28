import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class Client {
    public static RegRemoteInterface userReg;
    private Socket sockTCP;
    private int portUDP;
    private static int portSocket = 6000;
    private static int portRMI = 7000;
    private static String serverInetAddress = "localhost";
    private BufferedWriter writer;
    private BufferedReader reader;
    private String userNickname;
    private static RegLogView reglogView;
    private static MainView mainView;

    public static void main(String[] args) {
        Registry reg;
        Remote remObject;
        try {
            reg = LocateRegistry.getRegistry(portRMI);
            remObject = reg.lookup("RegistrationService");
            userReg = (RegRemoteInterface) remObject;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }

        //Graphic Interface Starting
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new StartingView();
    }

    public void gotoRegLogView() {
        reglogView = new RegLogView();
        reglogView.setInstance(this);
    }

    public void gotoMainView() {
        mainView = new MainView();
        mainView.setInstance(this);
    }

    public ReturnCodes.Codex login(String nickname, String passwd) {
        ReturnCodes.Codex result = null;
        try {
            sockTCP = new Socket(serverInetAddress, portSocket);
            writer = new BufferedWriter(new OutputStreamWriter(sockTCP.getOutputStream()));
            reader= new BufferedReader(new InputStreamReader(sockTCP.getInputStream()));

            writer.write("Login " + nickname + " " + passwd + " " + sockTCP.getInetAddress().getHostAddress());
            writer.newLine();
            writer.flush();

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
        this.userNickname = nickname;
        return result;
    }

    public ReturnCodes.Codex logout() {
        try {
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
            writer.write("List " + userNickname);
            writer.newLine();
            writer.flush();

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
            writer.write("Rank " + userNickname);
            writer.newLine();
            writer.flush();

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
}
