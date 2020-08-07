import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
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

    private int numWord;

    private JFrame frame;
    private JPanel mainPanel;

    private JTextField englishField;
    private JTextField ENGLISHTextField;

    private JTextField italianField;
    private JTextField ITALIANTextField;

    private JButton sendButton;
    private JProgressBar progressBar;
    private JLabel errorLabel;
    private JButton exitButton;

    public void setInstance(Client user) {
        this.clientInstance = user;
    }

    public void setWord(int words) {
        this.numWord = words;
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
                errorLabel.setText("End, score: " + tok[1] + " " + tok[2]);
                exitButton.setEnabled(true);
            }
            else if(tok[0].equals("Timeout") && tok.length == 3) {
                errorLabel.setText("Timeout, score: " + tok[1] + " " + tok[2]);
                exitButton.setEnabled(true);
            }
            else {
                italianField.setText(tok[0]);
                progressBar.setValue(100/numWord);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ChallengeView () {
        frame = new JFrame();
        frame.setSize(350, 125);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle("Word Quizzle");

        frame.add(mainPanel);

        sendButton.addActionListener(new SendAction());
        exitButton.addActionListener(new ExitButton());

        frame.setVisible(true);
    }

    public class SendAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(!englishField.getText().isEmpty()) {
                String lineTmp = clientInstance.userNickname + " " + englishField.getText();
                buffer = ByteBuffer.wrap(lineTmp.getBytes());
                buffer.clear();
                buffer.flip();
                italianField.setText("");
                myRead();
            }
            else {
                errorLabel.setText("Fill the field");
            }
        }
    }
    public class ExitButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
    }
}