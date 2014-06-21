package com.piratkopia13.pixelplanet.network.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.piratkopia13.pixelplanet.network.GeneralRequest;

public class ServerListener extends Listener {
    @Override
    public void received(Connection conn, Object object) {
        if (object instanceof GeneralRequest) {
            String data = ((GeneralRequest) object).text;
            GUI.addToLog(GUI.LogLevel.INFO, "Net received: " + data);
        }
    }

    @Override
    public void connected(Connection connection) {
        GUI.addToLog(GUI.LogLevel.INFO, "Client id "+connection.getID()+" connected "+connection.getRemoteAddressTCP());
        GUI.addPlayer(connection.getID()+"");
    }

    @Override
    public void disconnected(Connection connection) {
        GUI.addToLog(GUI.LogLevel.INFO, "Client id " + connection.getID() + " disconnected ");
        GUI.removePlayer(connection.getID() + "");
    }
}
