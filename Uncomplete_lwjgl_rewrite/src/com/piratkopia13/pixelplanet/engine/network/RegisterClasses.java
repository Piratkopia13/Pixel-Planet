package com.piratkopia13.pixelplanet.engine.network;

import com.esotericsoftware.kryo.Kryo;
import com.piratkopia13.pixelplanet.engine.core.Vector2f;

public class RegisterClasses {

    public static void registerAll(Kryo kryo){
        kryo.register(GeneralRequest.class);
        kryo.register(Vector2f.class);
        kryo.register(Float.class);
        kryo.register(Integer.class);
        kryo.register(Object.class);
    }

}
