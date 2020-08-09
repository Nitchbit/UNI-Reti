import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
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
    private MainView parent;

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

    private int index = 0;

    ActionListener start = new StartAction();

    public void setInstance(Client user, int portCh, InetAddress address, MainView parent) {
        this.parent = parent;
        this.clientInstance = user;
        sockAddr = new InetSocketAddress(address, portCh);
        try {
            sockChannel = SocketChannel.open();
            sockChannel.connect(sockAddr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void myRead() {
        int valueBar = 0;
        buffer = ByteBuffer.allocate(1024);
        boolean flag = true;
        String line = "";
        try {
            while (flag) {
                buffer.clear();
                int byteRed = sockChannel.read(buffer);
                buffer.flip();
                line = line + StandardCharsets.UTF_8.decode(buffer).toString();
                buffer.flip();
                if (byteRed < 1024) {
                    flag = false;
                }
            }
            buffer.flip();
            String[] tok = line.split("\\s+");
            if (tok[0].equals("End") && tok.length == 3) {
                errorLabel.setText("Challenge ended! Your score: " + tok[1] + " " + tok[2]);
                exitButton.setEnabled(true);
                exitButton.setVisible(true);
                sendButton.setEnabled(false);
                progressBar.setValue(100);
                progressBar.setString("100%");
            } else if (tok[0].equals("Timeout") && tok.length == 3) {
                errorLabel.setText("Timeout! Your score: " + tok[1] + " " + tok[2]);
                exitButton.setEnabled(true);
                exitButton.setVisible(true);
                sendButton.setEnabled(false);
            } else {
                italianField.setText(tok[0]);
                valueBar = index * (100 / 8);
                progressBar.setValue(valueBar);
                progressBar.setString(String.valueOf(valueBar) + "%");
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ChallengeView(String name) {
        frame = new JFrame();
        frame.setSize(400, 175);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle(name);

        frame.add(mainPanel);

        sendButton.addActionListener(start);
        exitButton.addActionListener(new ExitAction());
        exitButton.setVisible(false);

        frame.setVisible(true);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 6, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.setBackground(new Color(-16449406));
        mainPanel.setEnabled(false);
        mainPanel.setForeground(new Color(-16777216));
        mainPanel.setPreferredSize(new Dimension(400, 150));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        mainPanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(2, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        sendButton = new JButton();
        sendButton.setBackground(new Color(-16734973));
        Font sendButtonFont = this.$$$getFont$$$("Michroma", Font.BOLD, 14, sendButton.getFont());
        if (sendButtonFont != null) sendButton.setFont(sendButtonFont);
        sendButton.setForeground(new Color(-16777216));
        sendButton.setSelected(false);
        sendButton.setText("Start");
        mainPanel.add(sendButton, new com.intellij.uiDesigner.core.GridConstraints(2, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        englishField = new JTextField();
        englishField.setBackground(new Color(-3683123));
        mainPanel.add(englishField, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, 30), null, 0, false));
        italianField = new JTextField();
        italianField.setBackground(new Color(-3617587));
        italianField.setEditable(false);
        mainPanel.add(italianField, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, 30), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        mainPanel.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        progressBar = new JProgressBar();
        progressBar.setForeground(new Color(-16734973));
        progressBar.setStringPainted(true);
        mainPanel.add(progressBar, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 6, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ITALIANTextField = new JTextField();
        ITALIANTextField.setBackground(new Color(-10855073));
        ITALIANTextField.setEditable(false);
        Font ITALIANTextFieldFont = this.$$$getFont$$$("OpenDyslexic", Font.BOLD, 12, ITALIANTextField.getFont());
        if (ITALIANTextFieldFont != null) ITALIANTextField.setFont(ITALIANTextFieldFont);
        ITALIANTextField.setForeground(new Color(-16777216));
        ITALIANTextField.setMargin(new Insets(2, 6, 2, 6));
        ITALIANTextField.setSelectionEnd(7);
        ITALIANTextField.setSelectionStart(7);
        ITALIANTextField.setText("ITALIAN");
        mainPanel.add(ITALIANTextField, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        ENGLISHTextField = new JTextField();
        ENGLISHTextField.setBackground(new Color(-10855073));
        ENGLISHTextField.setCaretColor(new Color(-16777216));
        ENGLISHTextField.setEditable(false);
        Font ENGLISHTextFieldFont = this.$$$getFont$$$("OpenDyslexic", Font.BOLD, 12, ENGLISHTextField.getFont());
        if (ENGLISHTextFieldFont != null) ENGLISHTextField.setFont(ENGLISHTextFieldFont);
        ENGLISHTextField.setForeground(new Color(-16777216));
        ENGLISHTextField.setMargin(new Insets(2, 6, 2, 6));
        ENGLISHTextField.setText("ENGLISH");
        mainPanel.add(ENGLISHTextField, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        exitButton = new JButton();
        exitButton.setBackground(new Color(-3993088));
        exitButton.setEnabled(false);
        Font exitButtonFont = this.$$$getFont$$$("Michroma", Font.BOLD, 14, exitButton.getFont());
        if (exitButtonFont != null) exitButton.setFont(exitButtonFont);
        exitButton.setForeground(new Color(-16777216));
        exitButton.setHideActionText(false);
        exitButton.setText("Exit");
        mainPanel.add(exitButton, new com.intellij.uiDesigner.core.GridConstraints(2, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        errorLabel = new JLabel();
        Font errorLabelFont = this.$$$getFont$$$("Inter Extra Bold", Font.BOLD, 12, errorLabel.getFont());
        if (errorLabelFont != null) errorLabel.setFont(errorLabelFont);
        errorLabel.setForeground(new Color(-3997694));
        errorLabel.setText("");
        mainPanel.add(errorLabel, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

    public class StartAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            sendButton.setText("Send");
            sendButton.removeActionListener(start);
            sendButton.addActionListener(new SendAction());
            myRead();
        }
    }

    public class SendAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!englishField.getText().isEmpty()) {
                String lineTmp = clientInstance.userNickname + " " + englishField.getText();
                buffer = ByteBuffer.wrap(lineTmp.getBytes());
                try {
                    while (buffer.hasRemaining()) {
                        sockChannel.write(buffer);
                    }
                    buffer.clear();
                    buffer.flip();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                englishField.setText("");
                myRead();
            } else {
                errorLabel.setText("Fill the field");
            }
        }
    }

    public class ExitAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String line = "Exit";
            buffer = buffer.wrap(line.getBytes());
            try {
                while (buffer.hasRemaining()) {
                    sockChannel.write(buffer);
                }
                buffer.clear();
                buffer.flip();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            parent.frame.setEnabled(true);
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
    }
}