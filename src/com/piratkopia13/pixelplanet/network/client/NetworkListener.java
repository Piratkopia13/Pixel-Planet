package com.piratkopia13.pixelplanet.network.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.piratkopia13.pixelplanet.network.GeneralRequest;

public class NetworkListener extends Listener{
    @Override
    public void disconnected(Connection connection) {
        System.out.println("Server DC");
    }

    @Override
    public void received(Connection conn, Object object) {
        if (object instanceof GeneralRequest){
            String data = ((GeneralRequest) object).text;
            System.out.println("Net received: "+data);
        }
    }
}
