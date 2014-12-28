package com.piratkopia13.pixelplanet.engine.network.server;

import com.piratkopia13.pixelplanet.engine.network.GeneralRequest;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame{
    private JTextField input;
    private JPanel rootPanel;
    private JList<String> playerList;
    private static DefaultListModel<String> playerListModel;
    private JTextPane log;
    private JLabel serverName;
    private JLabel maxPlayers;
    private JLabel tickrate;
    private JLabel port;
    private JLabel ip;
    private static JTextPane log1;
    private static StyledDocument logDoc;
    private static SimpleAttributeSet attrSet;

    public GUI(){
        super("Pixel Planet server");

        log1 = log;
        logDoc = log.getStyledDocument();
        attrSet = new SimpleAttributeSet();
        playerListModel = new DefaultListModel<>();

        setContentPane(rootPanel);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        serverName.setText("Test server");
        ip.setText("127.0.0.1");
        port.setText("12345");
        tickrate.setText("60");
        maxPlayers.setText("2");

        setVisible(true);

        playerList.setModel(playerListModel);

        input.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField input = (JTextField) e.getSource();
                GameServer.server.sendToAllUDP( new GeneralRequest( "", input.getText() ) );
                addToLog(LogLevel.INFO, input.getText());
                input.setText("");
            }
        });
    }

    public static enum LogLevel{
        DEBUG, INFO, WARNING, ERROR

    }

    public static void addPlayer(String player){
        playerListModel.addElement(player);
    }
    public static void removePlayer(String player){
        playerListModel.removeElement(player);
    }

    private static Color yellow = new Color(201, 194, 67);
    private static Color red = new Color(184, 80, 75);
    public static void addToLog(LogLevel level, String toAppend){

        StyleConstants.setBold(attrSet, true);
        if (level.equals(LogLevel.WARNING))
            StyleConstants.setForeground(attrSet, yellow);
        else if (level.equals(LogLevel.ERROR))
            StyleConstants.setForeground(attrSet, red);

        try {
            logDoc.insertString(logDoc.getLength(), "["+level.toString()+"] ", attrSet);
            StyleConstants.setBold(attrSet, false);
            StyleConstants.setForeground(attrSet, Color.BLACK);

            logDoc.insertString(logDoc.getLength(), toAppend+"\n", attrSet);
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }

        log1.setCaretPosition(logDoc.getLength());

    }

}
