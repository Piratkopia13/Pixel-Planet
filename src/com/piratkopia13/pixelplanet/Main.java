package com.piratkopia13.pixelplanet;

import com.piratkopia13.pixelplanet.engine.core.CoreEngine;
import com.piratkopia13.pixelplanet.states.*;

public class Main {

    public static void main(String[] args){

        // TODO: check opengl version, if it's over x it can run, otherwise give error message

        CoreEngine engine = new CoreEngine();
        engine.setWindowSize(1600, 900);
        engine.setWindowTitle("Pixel Planet");
//        CoreEngine.setFPS(200);
        CoreEngine.setVsyncEnabled(true);

        engine.addGameState( new Menu() );
        engine.addGameState( new Settings() );
        engine.addGameState( new Play() );

        engine.setGameState(State.PLAY);

        engine.start();
    }

}
