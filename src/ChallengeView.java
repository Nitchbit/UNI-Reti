import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ChallengeView {
    private Client clientInstance;
    private SocketAddress sockAddr;
    private SocketChannel sockChannel;
    private ByteBuffer buffer;

    JFrame frame;
    JPanel panel;
    JLabel italian;
    JTextField english;
    JButton send;
    JLabel label;

    public void setInstance(Client user) {
        this.clientInstance = user;
    }

    //socket for the challenge
    public void setSock(int portUDP, InetAddress address) {
        sockAddr = new InetSocketAddress(address, portUDP);
        try {
            sockChannel = SocketChannel.open();
            sockChannel.connect(sockAddr);
            myRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void myRead() {
        buffer = ByteBuffer.allocate(1024);
        boolean flag = true;
        String line = "";

        try {
            while(flag) {
                buffer.clear();
                int byteRed = sockChannel.read(buffer);
                buffer.flip();
                line = line + StandardCharsets.UTF_8.decode(buffer).toString();
                buffer.flip();
                if(byteRed < 1024) {
                    flag = false;
                }
            }
            buffer.flip();
            String[] tok = line.split(" ");
            if(tok[0].equals("End") && tok.length == 3) {
                label.setText("End, score: " + tok[1] + " " + tok[2]);
            }
            else if(tok[0].equals("Timeout") && tok.length == 3) {
                label.setText("Timeout, score: " + tok[1] + " " + tok[2]);
            }
            else {
                italian.setText(tok[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ChallengeView() {
        frame = new JFrame();
        frame.setSize(300, 280);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle("Word Quizzle");

        panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        italian = new JLabel();
        italian.setBounds(20, 50, 90, 25);
        panel.add(italian);

        english = new JTextField();
        english.setBorder(BorderFactory.createEmptyBorder());
        english.setBounds(20, 80, 90, 25);
        panel.add(english);

        send = new JButton("Send");
        send.setBounds(20, 120, 90, 23);
        send.addActionListener(new Send());
        panel.add(send);

        label = new JLabel();
        label.setBounds(140, 120, 400, 50);
        panel.add(label);

        frame.setVisible(true);
    }

    public class Send implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(!english.getText().isEmpty()) {
                String lineTmp = clientInstance.userNickname + " " + english.getText();
                buffer = ByteBuffer.wrap(lineTmp.getBytes());
                buffer.clear();
                buffer.flip();
                italian.setText("");
                myRead();
            }
        }
    }
}
