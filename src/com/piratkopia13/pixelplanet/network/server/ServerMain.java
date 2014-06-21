package com.piratkopia13.pixelplanet.network.server;

import javax.swing.*;

public class ServerMain {

    public static void main(String[] args){

        // Init GUI
        setLookAndFeel();
        GUI gui = new GUI();

        // Init server
        GameServer server = new GameServer();

        // Start the server
        server.start();

    }

    private static void setLookAndFeel(){
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                System.out.println(info.getClassName());
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

}
