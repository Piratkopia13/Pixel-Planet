package com.piratkopia13.pixelplanet.engine.core;

public class Time {

    public static final long SECOND = 1000000000L;

    private static double delta;
    private static double frameNum;

    public static double getTime(){
        return (double)System.nanoTime();
    }

    public static double getDelta(){
        return delta;
    }

    public static void setDelta(double delta){
        Time.delta = delta;
    }

    public static double getFrameNum() {
        return frameNum;
    }

    public static void setFrameNum(double frameNum) {
        Time.frameNum = frameNum;
    }
}
