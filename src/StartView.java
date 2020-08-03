import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class StartView {
    private JFrame frame;

    private JPanel panelView;
    private JLabel mainMSG;
    private JLabel mainMSG1;
    private JButton mainButton;

    public StartView() {
        frame = new JFrame();
        frame.setSize(575, 350);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(panelView);

        mainButton.addActionListener(new Action());

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
