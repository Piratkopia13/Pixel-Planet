package com.piratkopia13.pixelplanet;

import com.piratkopia13.pixelplanet.engine.core.Game;
import com.piratkopia13.pixelplanet.states.*;

public class Main {

    public static void main(String[] args){

        // TODO: check opengl version, if it's over x it can run, otherwise give error message

        Game game = new Game();
        game.setWindowSize(1600, 900);
        game.setWindowTitle("Pixel Planet");
//        CoreEngine.setFPS(200);
        Game.setVsyncEnabled(true);

        Game.joinServer("127.0.0.1");

        game.addGameState( new Menu() );
        game.addGameState( new Settings() );
        game.addGameState( new Play() );

        game.setGameState(State.PLAY);

        game.start();


    }

}
