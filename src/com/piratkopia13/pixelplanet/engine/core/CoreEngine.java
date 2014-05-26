package com.piratkopia13.pixelplanet.engine.core;

import com.piratkopia13.pixelplanet.engine.rendering.Window;
import com.piratkopia13.pixelplanet.states.State;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class CoreEngine {

    private static Game game;
    private boolean windowSizeSet = false;

    public CoreEngine() {
        game = new Game();
    }

    public void start(){
        if (windowSizeSet)
            game.start();
        else{
            System.err.println("You must set the window size using engine.setWindowSize()");
            System.exit(1);
        }
    }

    public static void stop(){
        game.stop();
    }

    public void setWindowTitle(String title){
        game.setWindowTitle(title);
    }

    public void setWindowSize(int screenWidth, int screenHeight){
        game.setWindowSize(screenWidth, screenHeight);
        windowSizeSet = true;
    }

    public void addGameState(GameState state){
        game.addGameState(state);
    }

    public void setGameState(State state){
        game.state = state;
    }

    public static void setFPS(int fps){
        Window.setFPSLimit(fps);
    }
    public static void setState(State state){
        game.state = state;
    }
    public static void setVsyncEnabled(boolean enable){
        Display.setVSyncEnabled(enable);
    }

    public static void addSynchronizedTask(SynchronizedTask task){
        game.addSynchronizedTask(task);
    }
    public static void removeSynchronizedTask(SynchronizedTask task){
        game.removeSynchronizedTask(task);
    }

    public static int getMouseX(){
        return Mouse.getX();
    }
    public static int getMouseY(){
        return Window.getHeight()-Mouse.getY();
    }

}
