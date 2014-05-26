package com.piratkopia13.pixelplanet.engine;

import com.piratkopia13.pixelplanet.states.State;

public class CoreEngine {

    private Game game;
    private boolean windowSizeSet = false;

    public CoreEngine() {
        this.game = new Game();
    }

    public void start(){
        if (windowSizeSet)
            game.start();
        else{
            System.err.println("You must set the window size using engine.setWindowSize()");
            System.exit(1);
        }
    }

    public void setWindowTitle(String title){
        game.setWindowTitle(title);
    }

    public void setWindowSize(int screenWidth, int screenHeight){
        game.setWindowSize(screenWidth, screenHeight);
        windowSizeSet = true;
    }

    public CoreEngine addGameState(GameState state){
        game.addGameState(state);
        return this;
    }

    public void setGameState(State state){
        game.state = state;
    }

    public void setFPS(int fps){
        game.setFpsLimit(fps);
    }

}
