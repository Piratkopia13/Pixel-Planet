package com.piratkopia13.pixelplanet;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

public class ClientTest {

    public static void main(String[] args){

        Client client = new Client();
        client.start();
        try {
            InetAddress address = client.discoverHost(12345, 5000);
            client.connect(5000, address, 12345, 12345);
        } catch (IOException e) {
            e.printStackTrace();
        }

        client.addListener(new Listener(){
            @Override
            public void connected(Connection connection) {
                System.out.println("connect");
            }

            @Override
            public void disconnected(Connection connection) {
                System.out.println("disconnect");
            }

            @Override
            public void received(Connection connection, Object o) {
                System.out.println("rec");
            }
        });

        Scanner s = new Scanner(System.in);
        String text;

        while (true){
            text = s.nextLine();
            client.sendUDP(text);
        }


    }

}
