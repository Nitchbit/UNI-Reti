import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Vector;

public class MainView {
    private JFrame frame;

    private Client clientInstance;
    private Notify notifyInstance;
    private String username;

    private ArrayList<Notification> notifications = new ArrayList<>();

    private JPanel mainPanel;

    private JButton signOutButton;
    private JButton fListButton;
    private JButton ranking;
    private JButton addFriend;
    private JButton challengeButton;
    private JButton scoreButton;

    private JScrollPane listPane;
    private JTextArea listArea;
    private JTextField nameField;

    private JLabel scoreLabel;
    private JScrollPane notePane;
    private JList<String> noteList;
    private JLabel nameLabel;
    private JLabel errorLabel;

    private JLabel noteLabel;

    private Vector<String> list = new Vector<>();

    //settings the client reference
    public void setInstance(Client user, Notify notify, String username) {
        this.clientInstance = user;
        this.notifyInstance = notify;
        this.username = username;

        nameLabel.setText(username);
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
        mainPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(8, 5, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.setBackground(new Color(-16449406));
        mainPanel.setPreferredSize(new Dimension(575, 350));
        mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        addFriend = new JButton();
        addFriend.setBackground(new Color(-10855073));
        Font addFriendFont = this.$$$getFont$$$("Michroma", Font.BOLD, 12, addFriend.getFont());
        if (addFriendFont != null) addFriend.setFont(addFriendFont);
        addFriend.setForeground(new Color(-16777216));
        addFriend.setText("Add  ");
        mainPanel.add(addFriend, new com.intellij.uiDesigner.core.GridConstraints(1, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(45, 25), null, 0, false));
        nameField = new JTextField();
        nameField.setBackground(new Color(-10855073));
        nameField.setCaretColor(new Color(-16777216));
        Font nameFieldFont = this.$$$getFont$$$("Michroma", Font.BOLD, 12, nameField.getFont());
        if (nameFieldFont != null) nameField.setFont(nameFieldFont);
        nameField.setForeground(new Color(-16777216));
        nameField.setMargin(new Insets(2, 6, 2, 6));
        mainPanel.add(nameField, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(120, 30), null, 0, false));
        challengeButton = new JButton();
        challengeButton.setBackground(new Color(-16734973));
        Font challengeButtonFont = this.$$$getFont$$$("Michroma", Font.BOLD, 12, challengeButton.getFont());
        if (challengeButtonFont != null) challengeButton.setFont(challengeButtonFont);
        challengeButton.setForeground(new Color(-16777216));
        challengeButton.setText("Challange");
        mainPanel.add(challengeButton, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(45, 25), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        mainPanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(3, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        signOutButton = new JButton();
        signOutButton.setBackground(new Color(-5296384));
        Font signOutButtonFont = this.$$$getFont$$$("Michroma", Font.BOLD, 12, signOutButton.getFont());
        if (signOutButtonFont != null) signOutButton.setFont(signOutButtonFont);
        signOutButton.setForeground(new Color(-16777216));
        signOutButton.setText("Sign Out");
        mainPanel.add(signOutButton, new com.intellij.uiDesigner.core.GridConstraints(7, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, 30), null, 0, false));
        scoreButton = new JButton();
        scoreButton.setBackground(new Color(-10855073));
        Font scoreButtonFont = this.$$$getFont$$$("Michroma", Font.BOLD, 12, scoreButton.getFont());
        if (scoreButtonFont != null) scoreButton.setFont(scoreButtonFont);
        scoreButton.setForeground(new Color(-16777216));
        scoreButton.setText("Score");
        mainPanel.add(scoreButton, new com.intellij.uiDesigner.core.GridConstraints(6, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, 30), null, 0, false));
        scoreLabel = new JLabel();
        Font scoreLabelFont = this.$$$getFont$$$("Inter Extra Bold", Font.BOLD, 20, scoreLabel.getFont());
        if (scoreLabelFont != null) scoreLabel.setFont(scoreLabelFont);
        scoreLabel.setForeground(new Color(-16777216));
        scoreLabel.setText("");
        mainPanel.add(scoreLabel, new com.intellij.uiDesigner.core.GridConstraints(5, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(40, 25), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        mainPanel.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 4, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        notePane = new JScrollPane();
        notePane.setBackground(new Color(-10855073));
        notePane.setName("");
        notePane.setVerifyInputWhenFocusTarget(true);
        notePane.setVerticalScrollBarPolicy(20);
        mainPanel.add(notePane, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 4, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        noteList = new JList<String>();
        noteList.setBackground(new Color(-10855073));
        noteList.setName("");
        noteList.setSelectionMode(0);
        notePane.setViewportView(noteList);
        listPane = new JScrollPane();
        listPane.setBackground(new Color(-10855073));
        listPane.setVerticalScrollBarPolicy(20);
        mainPanel.add(listPane, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 6, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        listArea = new JTextArea();
        listArea.setBackground(new Color(-10855073));
        listArea.setEditable(false);
        Font listAreaFont = this.$$$getFont$$$("Michroma", Font.BOLD, 14, listArea.getFont());
        if (listAreaFont != null) listArea.setFont(listAreaFont);
        listArea.setForeground(new Color(-16777216));
        listPane.setViewportView(listArea);
        fListButton = new JButton();
        fListButton.setBackground(new Color(-10855073));
        Font fListButtonFont = this.$$$getFont$$$("Michroma", Font.BOLD, 12, fListButton.getFont());
        if (fListButtonFont != null) fListButton.setFont(fListButtonFont);
        fListButton.setForeground(new Color(-16777216));
        fListButton.setText("Friends  ");
        mainPanel.add(fListButton, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(45, 30), null, 0, false));
        ranking = new JButton();
        ranking.setBackground(new Color(-10855073));
        Font rankingFont = this.$$$getFont$$$("Michroma", Font.BOLD, 12, ranking.getFont());
        if (rankingFont != null) ranking.setFont(rankingFont);
        ranking.setForeground(new Color(-16777216));
        ranking.setText("Ranking");
        mainPanel.add(ranking, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(45, 30), null, 0, false));
        nameLabel = new JLabel();
        nameLabel.setBackground(new Color(-10855073));
        Font nameLabelFont = this.$$$getFont$$$("OpenDyslexic", Font.BOLD, 18, nameLabel.getFont());
        if (nameLabelFont != null) nameLabel.setFont(nameLabelFont);
        nameLabel.setForeground(new Color(-16777216));
        nameLabel.setHorizontalAlignment(0);
        nameLabel.setHorizontalTextPosition(0);
        nameLabel.setOpaque(true);
        nameLabel.setText("");
        mainPanel.add(nameLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        errorLabel = new JLabel();
        Font errorLabelFont = this.$$$getFont$$$("Inter Extra Bold", Font.BOLD, 14, errorLabel.getFont());
        if (errorLabelFont != null) errorLabel.setFont(errorLabelFont);
        errorLabel.setForeground(new Color(-6619134));
        errorLabel.setText("");
        mainPanel.add(errorLabel, new com.intellij.uiDesigner.core.GridConstraints(2, 3, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        mainPanel.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(4, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
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

    public class Notification {
        InetAddress address;
        int TCPPort;
        int UDPPort;
        String username;

        public Notification(String username, InetAddress address, int TCPPort, int UDPPort) {
            this.address = address;
            this.TCPPort = TCPPort;
            this.UDPPort = UDPPort;
            this.username = username;
        }
    }

    public void addNote(String username, InetAddress address, int TCPPort, int UDPPort) {
        notifications.add(new MainView.Notification(username, address, TCPPort, UDPPort));
        list.add("Challenged by " + username);
        noteList.setListData(list);
    }

    public Notification removeNote(String username) {
        Notification removed = null;
        for (int i = 0; i < notifications.size(); i++) {
            if (notifications.get(i).username.equals(username)) {
                removed = notifications.remove(i);
                list.removeElement("Challenged by " + username);
                noteList.setListData(list);
            }
        }
        return removed;
    }

    public MainView() {
        frame = new JFrame();
        frame.setSize(575, 350);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle("WORD QUIZZLE");

        frame.add(mainPanel);

        signOutButton.addActionListener(new SignOutAction());
        fListButton.addActionListener(new FriendsAction());
        ranking.addActionListener(new RankingAction());
        addFriend.addActionListener(new AddAction());
        challengeButton.addActionListener(new ChallengeAction());
        scoreButton.addActionListener(new ScoreAction());

        noteList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String line = noteList.getSelectedValue();
                Object[] option = {"Accept", "Decline"};
                Object selection = JOptionPane.showOptionDialog(frame, null, null, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[0]);
                if (selection.equals(option[0])) {
                    String[] token = line.split(" ");
                    Notification rem = removeNote(token[2]);
                    notifyInstance.accept(rem.address, rem.UDPPort);
                    clientInstance.challengePort = rem.TCPPort;
                    //go to game
                }
                if (selection.equals(option[1])) {
                    String[] token = line.split(" ");
                    Notification rem = removeNote(token[2]);
                    notifyInstance.decline(rem.address, rem.UDPPort);
                }
            }
        });

        frame.setVisible(true);
    }

    public class SignOutAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            clientInstance.logout();
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
    }

    public class FriendsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<String> result = clientInstance.listFriend();
            listArea.setText("");
            String tmpLine = "";
            for (String item : result) {
                item = item + "\n";
                tmpLine = tmpLine + item;
            }
            listArea.setText(tmpLine.replace("[", "").replace("]", ""));
        }
    }

    public class RankingAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<String> result = clientInstance.rank();
            listArea.setText("");
            String tmpLine = "";
            for (String item : result) {
                item = item + "\n";
                tmpLine = tmpLine + item;
            }
            listArea.setText(tmpLine.replaceAll("\"", "").replace("[", "").replace("]", ""));

        }
    }

    public class AddAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ReturnCodes.Codex result = clientInstance.addFriend(nameField.getText());
            if (!result.equals(ReturnCodes.Codex.SUCCESS)) {
                errorLabel.setText(ReturnCodes.toMessage(result));
            }
        }
    }

    public class ChallengeAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ReturnCodes.Codex result = clientInstance.challenge(nameField.getText());
            errorLabel.setText(ReturnCodes.toMessage(result));
        }
    }

    public class ScoreAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int result = clientInstance.score();
            scoreLabel.setText(String.valueOf(result).replace("[", "").replace("]", ""));
        }
    }
}
