package com.piratkopia13.pixelplanet.engine.network.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.piratkopia13.pixelplanet.engine.core.Vector2f;
import com.piratkopia13.pixelplanet.engine.network.GeneralRequest;

import java.util.ArrayList;
import java.util.List;

public class ServerListener extends Listener {

    private Server server;
    private List<String> players;

    public ServerListener(Server server) {
        this.server = server;
        this.players = new ArrayList<>();
    }

    @Override
    public void received(Connection conn, Object object) {
        if (object instanceof GeneralRequest) {
            GeneralRequest obj = (GeneralRequest) object;
            Object key = obj.key;
            Object data = obj.data;
            GUI.addToLog(GUI.LogLevel.INFO, "Net received: "+ key + " - " + data);

            if (key.equals(0x01)){ // Got player position
                Vector2f pos = (Vector2f) data;
                server.sendToAllExceptUDP(conn.getID(), new GeneralRequest(0x01, pos));
            }else
            if (key.equals(0x02)){ // Got player rotation
                float rotation = (float) data;
                server.sendToAllExceptUDP(conn.getID(), new GeneralRequest(0x02, rotation));
            }else
            if (key.equals(0x01)){
            }else
            if (key.equals(0x01)){
            }else
            if (key.equals(0x01)){
            }else
                GUI.addToLog(GUI.LogLevel.WARNING, "UNKNOWN KEY: "+key);

        }
//        GUI.addToLog(GUI.LogLevel.DEBUG, object.toString());
    }

    @Override
    public void connected(Connection conn) {
        GUI.addToLog(GUI.LogLevel.INFO, "Client id " + conn.getID() + " connected " + conn.getRemoteAddressTCP());
        GUI.addPlayer(conn.getID()+"");


        for (String username : players)
            conn.sendTCP(new GeneralRequest(0x05, username));
        players.add(conn.getID()+"");

        server.sendToAllExceptTCP(conn.getID(), new GeneralRequest(0x05, conn.getID() /*change to username*/ ));
    }

    @Override
    public void disconnected(Connection conn) {
        GUI.addToLog(GUI.LogLevel.INFO, "Client id " + conn.getID() + " disconnected ");
        GUI.removePlayer(conn.getID() + "");

        players.remove(conn.getID()+"");

        server.sendToAllExceptTCP(conn.getID(), new GeneralRequest(0x08, conn.getID() /*change to username*/ ));
    }
}
