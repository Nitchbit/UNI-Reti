import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionsHandler{
    public static class RegisterHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Registerd");
        }
    }

    public static class LoginHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

}
