import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class Client {
    public static void main(String[] args) throws IOException {
        String hostname = "localhost";
        int port = 10000;
        Socket activeSocket = new Socket(hostname, port);
        BufferedReader reader = new BufferedReader(new InputStreamReader(activeSocket.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(activeSocket.getOutputStream()));
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String userInput = input.readLine();
        writer.write(userInput);
        writer.newLine();
        writer.flush();
        String line = reader.readLine();
        System.out.println(line);
        reader.close();
        writer.close();
        activeSocket.close();
    }
}
