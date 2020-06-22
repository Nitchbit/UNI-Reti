import javax.swing.*;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    public static RegRemoteInterface userReg;

    public Client() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JTextField username = new JTextField("Username");
        JPasswordField passwd = new JPasswordField("Password");
        JButton registerButton = new JButton("Register");
        JButton loginButton = new JButton("Login");

        //frame settings
        frame.setSize(205, 180);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Word Quizzle");

        //panel settings and adding it to the frame
        panel.setLayout(null);
        frame.add(panel);

        //text field settings and adding it to the panel
        username.setBorder(BorderFactory.createEmptyBorder());
        username.setBounds(20, 20, 165, 25);
        panel.add(username);

        //password field settings and adding it to the panel
        passwd.setBorder(BorderFactory.createEmptyBorder());
        passwd.setBounds(20, 50, 165, 25);
        panel.add(passwd);

        //register button settings and adding it to the panel
        registerButton.setBounds(20, 80, 90, 25);
        registerButton.addActionListener(new ActionsHandler.RegisterHandler());
        panel.add(registerButton);

        //login button settings and adding it to the panel
        loginButton.setBounds(115, 80, 70, 25);
        loginButton.addActionListener(new ActionsHandler.LoginHandler());
        panel.add(loginButton);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Registry reg;
        Remote remObject;

       try {
            reg = LocateRegistry.getRegistry(7000);
            remObject = reg.lookup("RegistrationService");
            userReg = (RegRemoteInterface) remObject;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }

        //Graphic Interface Starting
        new Client();
    }
}
