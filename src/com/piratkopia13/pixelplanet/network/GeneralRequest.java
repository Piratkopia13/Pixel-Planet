package com.piratkopia13.pixelplanet.network;

public class GeneralRequest {
    public String text;
    public Object object;

    public GeneralRequest() {}

    public GeneralRequest(String text) {
        this.text = text;
    }

    public GeneralRequest(Object object) {
        this.object = object;
    }

    public static GeneralRequest encode(String data){
        return new GeneralRequest(data);
    }
}
