import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView {
    private Client clientInstance;

    JTextField usernameField;

    public void setInstance(Client user) {
        this.clientInstance = user;
    }

    public MainView() {
        JFrame frame = new JFrame();
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Word Quizzle");

        JPanel panel = new JPanel();
        frame.add(panel);

        JButton registerButton = new JButton("Add");
        registerButton.setBounds(20, 80, 90, 25);
        registerButton.addActionListener(new Act());
        panel.add(registerButton);

        usernameField = new JTextField("username");
        usernameField.setBorder(BorderFactory.createEmptyBorder());
        usernameField.setBounds(20, 20, 165, 25);
        panel.add(usernameField);

        frame.setVisible(true);
    }

    public class Act implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            clientInstance.listFriend();
        }
    }
}
