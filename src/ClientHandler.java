import com.google.gson.JsonArray;

import java.io.*;
import java.net.*;

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
                if(tokenParams[0].equals("Login") && tokenParams.length == 5) {
                    ReturnCodes.Codex result = dataStructure.userLogin(tokenParams[1], tokenParams[2], InetAddress.getByName(tokenParams[3]), Integer.parseInt(tokenParams[4]));

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

                    writer.write(String.valueOf(list));
                    writer.newLine();
                    writer.flush();
                }
                else if(tokenParams[0].equals("Challenge") && tokenParams.length == 5) {
                    int challengeTCPport = (int) ((Math.random() * ((65535 -1024) +1)) + 1024);
                    ChallengeHandler challenge = new ChallengeHandler(dataStructure, challengeTCPport);
                    challenge.start();

                    ReturnCodes.Codex result = dataStructure.userChallenge(tokenParams[1], tokenParams[2], clientUDPSock, challengeTCPport);
                    if(!result.equals(ReturnCodes.Codex.SUCCESS)) {
                        if(challenge.isAlive()) challenge.interrupt();
                        writer.write(ReturnCodes.toMessage(result));
                        writer.newLine();
                        writer.flush();
                    }
                    else {
                        writer.write(ReturnCodes.toMessage(result));
                        writer.newLine();
                        writer.flush();
                        byte[] buffer = new byte[1024];
                        DatagramPacket received = new DatagramPacket(buffer, buffer.length);
                        clientUDPSock.setSoTimeout(25000);
                        try {
                            clientUDPSock.receive(received);
                        } catch (SocketTimeoutException e) {
                            //sending declined to the challanger
                            dataStructure.challengeRequestResult(tokenParams[1], tokenParams[2], "Declined", clientUDPSock, challengeTCPport);
                            //sending timeout to the challenged
                            dataStructure.challengeRequestResult(tokenParams[1], tokenParams[2], "Timeout", clientUDPSock, challengeTCPport);
                            //stop challenge handler
                            if(challenge.isAlive()) challenge.interrupt();
                        }
                        String line = new String(received.getData(), 0, received.getLength());
                        String[] tok = line.split(" ");
                        if(tok[0].equals("Accepted"))
                            dataStructure.challengeRequestResult(tokenParams[1], tokenParams[2], "Accepted", clientUDPSock, challengeTCPport);
                        if(tok[0].equals("Declined")) {
                            dataStructure.challengeRequestResult(tokenParams[1], tokenParams[2], "Declined", clientUDPSock, challengeTCPport);
                            if(challenge.isAlive()) challenge.interrupt();
                        }
                    }
                }
                else if(tokenParams[0].equals("Score") && tokenParams.length == 2) {
                    int result = dataStructure.showUserScore(tokenParams[1]);

                    writer.write(String.valueOf(result));
                    writer.newLine();
                    writer.flush();
                }
                else if(tokenParams[0].equals("Rank") && tokenParams.length == 2) {
                    JsonArray list = dataStructure.showRanking(tokenParams[1]);

                    writer.write(String.valueOf(list));
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
