package com.piratkopia13.pixelplanet;

import com.piratkopia13.pixelplanet.engine.CoreEngine;
import com.piratkopia13.pixelplanet.states.*;

public class Main {

    public static void main(String[] args){

        // TODO: check opengl version, if it's over x it can run, otherwise give error message

        CoreEngine engine = new CoreEngine();
        engine.setWindowSize(1600, 900);
        engine.setWindowTitle("Pixel Planet");
        engine.setFPS(60);

        engine.addGameState(new Menu()).
               addGameState(new Settings()).
               addGameState(new Play());

        engine.setGameState(State.MENU);

        engine.start();
    }

}
