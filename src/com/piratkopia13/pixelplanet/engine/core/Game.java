package com.piratkopia13.pixelplanet.engine.core;

import com.piratkopia13.pixelplanet.GameVar;
import com.piratkopia13.pixelplanet.engine.network.GeneralRequest;
import com.piratkopia13.pixelplanet.engine.rendering.Camera;
import com.piratkopia13.pixelplanet.engine.rendering.Window;
import com.piratkopia13.pixelplanet.states.State;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class Game {

    private static CoreEngine engine;
    private boolean windowSizeSet = false;
    private static Camera gameCamera;
    private static int fps;

    public Game() {
        engine = new CoreEngine();
    }

    public void start(){
        if (windowSizeSet)
            engine.start();
        else{
            System.err.println("You must set the window size using engine.setWindowSize()");
            System.exit(1);
        }
    }

    public static void stop(){
        engine.stop();
    }

    public void setWindowTitle(String title){
        engine.setWindowTitle(title);
    }

    public void setWindowSize(int screenWidth, int screenHeight){
        engine.setWindowSize(screenWidth, screenHeight);
        windowSizeSet = true;
    }

    public void addGameState(GameState state){
        engine.addGameState(state);
    }

    public void setGameState(State state){
//        engine.init(state);
        engine.state = state;
    }

    public static void setFPS(int fps){
        Window.setFPSLimit(fps);
    }
    public static void setState(State state){
        engine.state = state;
    }
    public static void setVsyncEnabled(boolean enable){
        Display.setVSyncEnabled(enable);
    }

    public static void addSynchronizedTask(SynchronizedTask task){
        engine.addSynchronizedTask(task);
    }
    public static void removeSynchronizedTask(SynchronizedTask task){
        engine.removeSynchronizedTask(task);
    }

    public static int getMouseX(){
        return Mouse.getX();
    }
    public static int getMouseY(){
        return Window.getHeight()-Mouse.getY();
    }

    public static Camera getGameCamera() {
        return gameCamera;
    }

    public static void setGameCamera(Camera gameCamera) {
        Game.gameCamera = gameCamera;
    }

    public static int getFPS() {
        return fps;
    }
    public static void setCurrentFPS(int fps) {
        Game.fps = fps;
    }

    // Net
    public static void joinServer(String address){
        engine.joinServer(address);
    }
    public static void netSendPosition(){
        engine.getClient().sendUDP( new GeneralRequest(0x01, GameVar.getPlayer().getPosition()) );
    }
    public static void netSendRotation(){
        engine.getClient().sendUDP(new GeneralRequest(0x02, GameVar.getPlayer().getRotation()));
    }
}
