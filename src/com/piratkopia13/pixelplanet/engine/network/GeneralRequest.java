package com.piratkopia13.pixelplanet.engine.network;

public class GeneralRequest {
    public Object key;
    public Object data;

    public GeneralRequest() {}

    public GeneralRequest(Object key, Object data) {
        this.key = key;
        this.data = data;
    }

    public static GeneralRequest encode(String key, String data){
        return new GeneralRequest(key, data);
    }
}
