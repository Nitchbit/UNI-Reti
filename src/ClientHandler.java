import com.google.gson.JsonArray;

import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable{
    private Database dataStructure;
    //socket in which server and client communicate
    private Socket clientTCPSock;
    //socket in which the client get the challange request
    private DatagramSocket clientUDPSock;

    private BufferedReader reader;
    private BufferedWriter writer;

    //constructor
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
            //reading client's messages
            String lineTmp = reader.readLine();
            String[] tokenParams = lineTmp.split(" ");
            while(!tokenParams[0].equals("Logout")) {
                //if client wants to login
                if(tokenParams[0].equals("Login") && tokenParams.length == 5) {
                    ReturnCodes.Codex result = dataStructure.userLogin(tokenParams[1], tokenParams[2], InetAddress.getByName(tokenParams[3]), Integer.parseInt(tokenParams[4]));

                    //writing response to client
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
                //if client wants to add a friend
                else if(tokenParams[0].equals("Add") && tokenParams.length == 3) {
                    ReturnCodes.Codex result = dataStructure.userAddFriend(tokenParams[1], tokenParams[2]);

                    //writing response to client
                    writer.write(String.valueOf(result));
                    writer.newLine();
                    writer.flush();
                }
                //if client wants to know all of his friends
                else if(tokenParams[0].equals("List") && tokenParams.length == 2) {
                    JsonArray list = dataStructure.userListFriends(tokenParams[1]);

                    //writing response to client
                    writer.write(String.valueOf(list));
                    writer.newLine();
                    writer.flush();
                }
                //if client wants to challenge a friend
                else if(tokenParams[0].equals("Challenge") && tokenParams.length == 3) {
                    //creating tcp port for the challenge
                    int challengeTCPport = (int) ((Math.random() * ((65535 -1024) +1)) + 1024);
                    //setting thread for the challenge and starting it
                    ChallengeHandler challenge = new ChallengeHandler(dataStructure, challengeTCPport);
                    challenge.start();

                    ReturnCodes.Codex result = dataStructure.userChallenge(tokenParams[1], tokenParams[2], clientUDPSock, challengeTCPport);
                    if(!result.equals(ReturnCodes.Codex.SUCCESS)) {
                        if(challenge.isAlive()) challenge.interrupt();
                        //writing response to client
                        writer.write(ReturnCodes.toMessage(result));
                        writer.newLine();
                        writer.flush();
                    }
                    else {
                        //writing response to client
                        writer.write(ReturnCodes.toMessage(result));
                        writer.newLine();
                        writer.flush();
                        //creating buffer for challenge's response
                        byte[] buffer = new byte[1024];
                        DatagramPacket received = new DatagramPacket(buffer, buffer.length);
                        //setting waiting timeout for a challenge's response
                        clientUDPSock.setSoTimeout(25000);
                        try {
                            clientUDPSock.receive(received);
                        }
                        //if time is over
                        catch (SocketTimeoutException e) {
                            //sending declined to the challenger
                            dataStructure.challengeRequestResult(tokenParams[1], tokenParams[2], "Declined", clientUDPSock, challengeTCPport);
                            //sending timeout to the challenged
                            dataStructure.challengeRequestResult(tokenParams[1], tokenParams[2], "Timeout", clientUDPSock, challengeTCPport);
                            //stop challenge handler
                            if(challenge.isAlive()) challenge.interrupt();
                        }
                        //checking the response
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
                //if client wants to knows his score
                else if(tokenParams[0].equals("Score") && tokenParams.length == 2) {
                    int result = dataStructure.showUserScore(tokenParams[1]);

                    //writing response to client
                    writer.write(String.valueOf(result));
                    writer.newLine();
                    writer.flush();
                }
                //if client wants to knows his position in the rank
                else if(tokenParams[0].equals("Rank") && tokenParams.length == 2) {
                    JsonArray list = dataStructure.showRanking(tokenParams[1]);

                    //writing response to client
                    writer.write(String.valueOf(list));
                    writer.newLine();
                    writer.flush();
                }
                //command not recognized
                else {
                    //writing response to client
                    writer.write(String.valueOf(ReturnCodes.Codex.COMMAND_NOT_FOUND));
                    writer.newLine();
                    writer.flush();
                }
                lineTmp = reader.readLine();
                tokenParams = lineTmp.split(" ");
            }
            //if client wants to logout
            ReturnCodes.Codex result = dataStructure.userLogout(tokenParams[1]);
            writer.write(String.valueOf(result));
            writer.newLine();
            writer.flush();

            //closing sockets
            clientUDPSock.close();
            clientTCPSock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
