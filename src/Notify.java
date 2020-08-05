import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Notify extends Thread {
    private MainView myMainView;
    private DatagramSocket UDPSocket;
    private Client clientFather;
    private InetAddress address;
    private int UDPPort;

    public Notify (Client master, DatagramSocket UDPSock) {
        this.clientFather = master;
        this.UDPSocket = UDPSock;
    }

    public void run () {
        try {
            byte[] buffer = new byte[1024];
            DatagramPacket receivedPack = new DatagramPacket(buffer, buffer.length);

            while (true) {
                UDPSocket.receive(receivedPack);
                String line = new String(receivedPack.getData(), 0, receivedPack.getLength());
                System.out.println(line);
                String[] token = line.split(" ");

                //received messages
                if (token[0].equals("Challenge") && token.length == 3) {
                    address = receivedPack.getAddress();
                    UDPPort = receivedPack.getPort();

                    myMainView.addNote(token[1], address, Integer.parseInt(token[2]), UDPPort);
                }
                if (token[0].equals("Timeout") && token.length == 2) {
                    myMainView.removeNote(token[1]);
                }
                if (token[0].equals("Accepted") && token.length == 2) {
                    //setting challenge's port
                    clientFather.challengePort = Integer.parseInt(token[1]);

                    //start the game view
                }
                if (token[0].equals("Declined")) {

                    //print to the view
                }
            }
        } catch (SocketException e) {
            System.out.println("Socket is closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setView (MainView view) {
        this.myMainView = view;
    }

    public void accept (InetAddress address, int port) {
        String line = "Accept";
        byte[] buffer = line.getBytes();
        DatagramPacket myPack = new DatagramPacket(buffer, buffer.length, address, port);
        try {
            UDPSocket.send(myPack);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void decline (InetAddress address, int port) {
        String line = "Decline";
        byte[] buffer = line.getBytes();
        DatagramPacket myPack = new DatagramPacket(buffer, buffer.length, address, port);
        try {
            UDPSocket.send(myPack);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
