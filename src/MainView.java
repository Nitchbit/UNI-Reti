import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class MainView {
    private Client clientInstance;

    JFrame frame;
    JPanel panel;
    JTextField friendField;
    JLabel label;

    public void setInstance(Client user) {
        this.clientInstance = user;
    }

    public MainView() {
        frame = new JFrame();
        frame.setSize(300, 280);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle("Word Quizzle");

        panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        friendField = new JTextField();
        friendField.setBorder(BorderFactory.createEmptyBorder());
        friendField.setBounds(120, 30, 90, 25);
        panel.add(friendField);

        JButton addFriend = new JButton("Add friend");
        addFriend.setBounds(20, 30, 90, 25);
        addFriend.addActionListener(new AddFriend());
        panel.add(addFriend);

        JButton listFriend = new JButton("My friends");
        listFriend.setBounds(20, 60, 90, 25);
        listFriend.addActionListener(new ListFriend());
        panel.add(listFriend);

        JButton logout = new JButton("Logout");
        logout.setBounds(20, 90, 90, 25);
        logout.addActionListener(new Logout());
        panel.add(logout);

        label = new JLabel();
        label.setBounds(140, 120, 400, 50);
        panel.add(label);

        JButton score = new JButton("Score");
        score.setBounds(20, 120, 90, 25);
        score.addActionListener(new Score());
        panel.add(score);

        JButton rank = new JButton("Rank");
        rank.setBounds(20, 150, 90, 25);
        rank.addActionListener(new Rank());
        panel.add(rank);

        JButton challenge = new JButton("Challenge");
        challenge.setBounds(20, 180, 120, 25);
        challenge.addActionListener(new Challenge());
        panel.add(challenge);

        frame.setVisible(true);
    }

    public class AddFriend implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            clientInstance.addFriend(friendField.getText());
        }
    }

    public class ListFriend implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<String> result = clientInstance.listFriend();
            label.setText(String.valueOf(result).replace("[", "").replace("]", ""));
        }
    }

    public class Logout implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            clientInstance.logout();
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
    }

    public class Score implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int result = clientInstance.score();
            label.setText(String.valueOf(result).replace("[", "").replace("]", ""));
        }
    }

    public class Rank implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<String> result = clientInstance.rank();
            label.setText(String.valueOf(result).replaceAll("\"", ""));
        }
    }

    public class Challenge implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ReturnCodes.Codex result = clientInstance.challenge(friendField.getText());
            if(result.equals(ReturnCodes.Codex.SUCCESS)) {
                clientInstance.gotoChallenge();
            }
        }
    }
}
