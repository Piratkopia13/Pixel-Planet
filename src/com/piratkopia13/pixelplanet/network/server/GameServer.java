package com.piratkopia13.pixelplanet.network.server;

import com.esotericsoftware.kryonet.Server;
import com.piratkopia13.pixelplanet.network.RegisterClasses;

import java.io.IOException;

public class GameServer {

    public static Server server;

    public GameServer() {
        server = new Server();
        RegisterClasses.registerAll(server.getKryo());
    }

    public void start(){

        GUI.addToLog(GUI.LogLevel.INFO, "Starting server..");

        server.start();
        try {
            server.bind(12345, 12345);
            server.addListener(new ServerListener());

            GUI.addToLog(GUI.LogLevel.INFO, "Server is online");

        } catch (IOException e) {
            GUI.addToLog(GUI.LogLevel.ERROR, e.getMessage());
            e.printStackTrace();
        }
    }


}
