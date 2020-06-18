import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server {
    //threadpool
    //...
    //cuncurrent-data-structure
    //...

    private static int port = 6666;
    private static final String address = "localhost";

    public static void main(String[] args){
        System.out.println("Server starting...");
        //initializing data structure with json file
        //creating socket
        try {
            ServerSocket listeningSocket = new ServerSocket(port);
            while (true) {
                //waiting for connection
                Socket activeSocket = listeningSocket.accept();
                //connection established, creating task and executing it
                //...

                /*
                BufferedReader reader = new BufferedReader(new InputStreamReader((activeSocket.getInputStream())));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(activeSocket.getOutputStream()));
                String line = reader.readLine();
                System.out.println(line);
                writer.write(line);
                writer.newLine();
                writer.flush();
                reader.close();
                writer.close();
                activeSocket.close();
                */
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
