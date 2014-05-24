package com.piratkopia13.pixelplanet.engine;

import com.piratkopia13.pixelplanet.states.*;

public class Game {

    public static final int    WINDOW_WIDTH = 1600;
    public static final int    WINDOW_HEIGHT = 900;
    public static final String WINDOW_TITLE = "Pixel Planet";
    public static final double FRAME_CAP = 5000.0;

    public enum State{
        MENU, PLAY, SETTINGS;
    }
    public static State state = State.MENU;

    private boolean isRunning = false;

    private Play playState = new Play();
    private Menu menuState = new Menu();
    private Settings settingsState = new Settings();

    public static void main(String[] args){
        Game game = new Game();
        game.start();
    }

    private void start(){
        Window.createWindow(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE);
        Window.setFPSLimit(60);

        int frames = 0;
        long frameCounter = 0;

        final double frameTime = 1.0 / FRAME_CAP;

        long lastTime = Time.getTime();
        double unprocessedTime = 0;


        isRunning = true;
        while (isRunning){
            boolean render = false;

            long startTime = Time.getTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime / (double)Time.SECOND;
            frameCounter += passedTime;

            while (unprocessedTime > frameTime){
                render = true;

                unprocessedTime -= frameTime;

                if (Window.isCloseRequested())
                    stop();

                Time.setDelta(frameTime);

                switch (state){
                    case MENU:
                        menuState.input();
                        menuState.update();
                        break;
                    case SETTINGS:
                        settingsState.input();
                        settingsState.update();
                        break;
                    case PLAY:
                        playState.input();
                        playState.update();
                        break;
                }

                if (frameCounter >= Time.SECOND){
                    System.out.println(frames);
                    frames = 0;
                    frameCounter = 0;
                }
            }

            if (render){
                render();
                frames++;
            }
            else{
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        cleanUp();
    }

    private void cleanUp() {
        Window.dispose();
    }

    private void stop(){
        isRunning = false;
    }

    private void render() {
        switch (state){
            case MENU:
                menuState.render();
                break;
            case SETTINGS:
                settingsState.render();
                break;
            case PLAY:
                playState.render();
                break;
        }
        Window.render();
    }


}
