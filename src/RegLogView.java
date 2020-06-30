import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

public class RegLogView {
    private Client clientInstance;

    private JTextField usernameField;
    private JPasswordField passwdField;
    private JLabel errorMessage;
    private JFrame frame;
    private JPanel panel;

    //settings the client reference
    public void setInstance(Client user) {
        this.clientInstance = user;
    }

    public RegLogView() {
        //frame settings
        frame = new JFrame();
        frame.setSize(300, 280);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle("Word Quizzle");

        //panel settings and adding it to the frame
        panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        //text field settings and adding it to the panel
        usernameField = new JTextField("username");
        usernameField.setBorder(BorderFactory.createEmptyBorder());
        usernameField.setBounds(20, 20, 165, 25);
        panel.add(usernameField);

        //password field settings and adding it to the panel
        passwdField = new JPasswordField("password");
        passwdField.setBorder(BorderFactory.createEmptyBorder());
        passwdField.setBounds(20, 50, 165, 25);
        panel.add(passwdField);

        //register button settings and adding it to the panel
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(20, 80, 90, 25);
        registerButton.addActionListener(new Register());
        panel.add(registerButton);

        //login button settings and adding it to the panel
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(115, 80, 70, 25);
        loginButton.addActionListener(new Login());
        panel.add(loginButton);

        //label to display result message
        errorMessage = new JLabel();
        errorMessage.setBounds(20, 110, 300, 25);
        panel.add(errorMessage);

        frame.setVisible(true);
    }

    public class Register implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(usernameField.getText().isEmpty() || passwdField.getText().isEmpty()) {
                errorMessage.setText("Fill all the fields");
            }
            else {
                try {
                    ReturnCodes.Codex result = Client.userReg.userRegistration(usernameField.getText(), passwdField.getText());
                    errorMessage.setText(ReturnCodes.toMessage(result));
                } catch (RemoteException | NullPointerException err) {
                    err.printStackTrace();
                }
            }
        }
    }

    public class Login implements  ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(usernameField.getText().isEmpty() || passwdField.getText().isEmpty()) {
                errorMessage.setText("Fill all the fields");
            }
            else {
                ReturnCodes.Codex result = clientInstance.login(usernameField.getText(), passwdField.getText());
                if(result.equals(ReturnCodes.Codex.SUCCESS))
                    clientInstance.gotoMainView();
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        }
    }
}
