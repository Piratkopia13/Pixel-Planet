package com.piratkopia13.pixelplanet.network;

import com.esotericsoftware.kryo.Kryo;

public class RegisterClasses {

    public static void registerAll(Kryo kryo){
        kryo.register(GeneralRequest.class);
    }

}
