import com.google.gson.JsonArray;

import java.io.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Database dataStructure;
    private Socket clientTCPSock;
    private DatagramSocket clientUDPSock;

    private BufferedReader reader;
    private BufferedWriter writer;

    public ClientHandler(Database dataStructure, Socket socket) {
        this.dataStructure = dataStructure;
        this.clientTCPSock = socket;
        try {
            this.reader = new BufferedReader(new InputStreamReader(clientTCPSock.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(clientTCPSock.getOutputStream()));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String lineTmp = reader.readLine();
            String[] tokenParams = lineTmp.split(" ");
            while(!tokenParams[0].equals("Logout")) {
                if(tokenParams[0].equals("Login") && tokenParams.length == 4) {
                    ReturnCodes.Codex result = dataStructure.userLogin(tokenParams[1], tokenParams[2], InetAddress.getByName(tokenParams[3]));

                    writer.write(String.valueOf(result));
                    writer.newLine();
                    writer.flush();

                    if(result != ReturnCodes.Codex.SUCCESS) {
                        System.out.println(Thread.currentThread().getId() + "Shutting down");
                        return;
                    }
                    else {
                        clientUDPSock = new DatagramSocket();
                    }
                }
                else if(tokenParams[0].equals("Add") && tokenParams.length == 3) {
                    ReturnCodes.Codex result = dataStructure.userAddFriend(tokenParams[1], tokenParams[2]);

                    writer.write(String.valueOf(result));
                    writer.newLine();
                    writer.flush();
                }
                else if(tokenParams[0].equals("List") && tokenParams.length == 2) {
                    JsonArray list = dataStructure.userListFriends(tokenParams[1]);

                    writer.write(list.getAsString());
                    writer.newLine();
                    writer.flush();
                }
                /*else if(tokenParams[0].equals("Challenge") && tokenParams.length == 5) {

                }*/
                else if(tokenParams[0].equals("Score") && tokenParams.length == 2) {
                    int result = dataStructure.showUserScore(tokenParams[1]);

                    writer.write(result);
                    writer.newLine();
                    writer.flush();
                }
                else if(tokenParams[0].equals("Rank") && tokenParams.length == 2) {
                    JsonArray list = dataStructure.showRanking(tokenParams[1]);

                    writer.write(list.getAsString());
                    writer.newLine();
                    writer.flush();
                }
                else {
                    writer.write(String.valueOf(ReturnCodes.Codex.COMMAND_NOT_FOUND));
                    writer.newLine();
                    writer.flush();
                }
                lineTmp = reader.readLine();
                tokenParams = lineTmp.split(" ");
            }
            ReturnCodes.Codex result = dataStructure.userLogout(tokenParams[1]);
            writer.write(String.valueOf(result));
            writer.newLine();
            writer.flush();

            clientUDPSock.close();
            clientTCPSock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
