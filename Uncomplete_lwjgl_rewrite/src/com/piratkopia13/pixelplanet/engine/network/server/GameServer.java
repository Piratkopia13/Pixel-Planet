package com.piratkopia13.pixelplanet.engine.network.server;

import com.esotericsoftware.kryonet.Server;
import com.piratkopia13.pixelplanet.engine.network.RegisterClasses;

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
            server.addListener(new ServerListener(server));

            GUI.addToLog(GUI.LogLevel.INFO, "Server is online");

        } catch (IOException e) {
            GUI.addToLog(GUI.LogLevel.ERROR, e.getMessage());
            e.printStackTrace();
        }
    }


}
