import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {
    private static ThreadPoolExecutor exec;
    private static LinkedBlockingQueue<Runnable> queue;

    private static int port = 6666;
    private static final String address = "localhost";

    public static void main(String[] args){
        System.out.println("Starting...");
        Database dataStructure = new Database();

        //building RMI service
        try {
            //exporting dataStructure
            RegRemoteInterface remoteObj = (RegRemoteInterface) UnicastRemoteObject.exportObject(dataStructure, 0);
            //creating registry
            Registry reg = LocateRegistry.createRegistry(6666);
            //rebinding
            reg.rebind("RegistrationService", remoteObj);
        } catch (RemoteException e) {
            System.err.println("Error creating RMI service");
            e.printStackTrace();
        }
        queue = new LinkedBlockingQueue<Runnable>();
        exec = new ThreadPoolExecutor(30, 150, 50000, TimeUnit.MILLISECONDS, queue);
        //creating socket
        ServerSocket listeningSocket;
        try {
            listeningSocket = new ServerSocket(port);
            while (true) {
                //waiting for connection
                Socket activeSocket = listeningSocket.accept();
                //connection established, creating task and executing it
                ClientHandler newTask = new ClientHandler(dataStructure, activeSocket);
                exec.execute(newTask);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
