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
    private String lineTmp;

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
            lineTmp = reader.readLine();
            String[] tokenParams = lineTmp.split(" ");
            while(!tokenParams[0].equals("Logout")) {
                if(tokenParams[0].equals("Login") && tokenParams.length == 4) {
                    if(dataStructure.userLogin(tokenParams[1], tokenParams[2], InetAddress.getByName(tokenParams[3])) < 0) {
                        System.out.println(Thread.currentThread().getId() + "Shutting down");
                        return;
                    }
                    else {
                        clientUDPSock = new DatagramSocket();
                    }
                    //manca roba
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
