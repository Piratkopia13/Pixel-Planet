package com.piratkopia13.pixelplanet.engine.network.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.piratkopia13.pixelplanet.Player;
import com.piratkopia13.pixelplanet.engine.core.Vector2f;
import com.piratkopia13.pixelplanet.engine.network.GeneralRequest;
import com.piratkopia13.pixelplanet.engine.network.server.GUI;
import com.piratkopia13.pixelplanet.states.Play;

public class NetworkListener extends Listener{
    @Override
    public void disconnected(Connection connection) {
        System.out.println("Server DC");
    }

    @Override
    public void received(Connection conn, Object object) {
        if (object instanceof GeneralRequest){
            GeneralRequest obj = (GeneralRequest) object;
            Object key = obj.key;
            Object data = obj.data;

            if (key.equals(0x01)){ // Got player position
                Vector2f pos = (Vector2f) data;
                Play.otherPlayers.get(conn.getID()).setPosition(pos);
            }else
            if (key.equals(0x02)){ // Got player rotation
                float rotation = (float) data;
                Play.otherPlayers.get(conn.getID()).setRotation(rotation);
            }else
            if (key.equals(0x05)){ // Player join
                Play.spawnPlayer(conn.getID());
            }else
            if (key.equals(0x08)){ // Player DC
                Play.otherPlayers.remove(conn.getID());
            }else
            if (key.equals(0x01)){
            }else
                System.out.println("UNKNOWN KEY: " + key);
        }
    }
}
