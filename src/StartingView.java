import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class StartingView {
    JFrame frame;
    public StartingView() {
        frame = new JFrame();
        frame.setSize(300, 280);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle("Word Quizzle");

        JPanel panel = new JPanel();
        panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        JButton button = new JButton("Enter");
        button.setBounds(115, 80, 70, 25);
        button.addActionListener(new Action());
        panel.add(button);

        frame.setVisible(true);
    }

    public class Action extends Client implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            super.gotoRegLogView();
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
    }

}
