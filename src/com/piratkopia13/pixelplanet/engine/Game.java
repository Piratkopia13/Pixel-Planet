package com.piratkopia13.pixelplanet.engine;

import com.piratkopia13.pixelplanet.states.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Game {

    private int windowWidth;
    private int windowHeight;
    private String windowTitle;

    private static final double FRAME_CAP = 5000.0;

    public State state;

    private boolean isRunning = false;
    private List<GameState> gameStates;

    // A synchronized task is a background task that runs with the game loop
    private List<SynchronizedTask> tasks;
    private List<SynchronizedTask> tasksToRemove;

    public Game(){
        this.gameStates = new ArrayList<>();
        this.tasks = new ArrayList<>();
        this.tasksToRemove = new ArrayList<>();
    }

    public void setWindowTitle(String title){
        this.windowTitle = title;
    }
    public void setWindowSize(int screenWidth, int screenHeight) {
        this.windowWidth = screenWidth;
        this.windowHeight = screenHeight;
    }
    public void addGameState(GameState state){
        this.gameStates.add(state);
    }

    public void start(){
        Window.createWindow(windowWidth, windowHeight, windowTitle);

        // Init state classes
        for (GameState state : gameStates){
            state.init();
        }

        int frames = 0;
        long frameCounter = 0;

        final double frameTime = 1.0 / FRAME_CAP;

        long lastTime = Time.getTime();
        double unprocessedTime = 0;

        RenderUtil.initGraphics();

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

                for (GameState state : gameStates){
                    if (this.state == state.getState()){  // If current state is the same as the class's state update only that gamestate
                        state.input();
                        state.update();
                        break;
                    }
                }

                // Run all background tasks
                Iterator<SynchronizedTask> iterator = tasks.iterator();
                while (iterator.hasNext()) {
                    SynchronizedTask task = iterator.next();
                    if (tasksToRemove.contains(task)){
                        iterator.remove();
                        tasksToRemove.remove(task);
                    }
                    task.update();
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

    public void stop(){
        isRunning = false;
    }

    private void render() {
        for (GameState state : gameStates){
            if (this.state == state.getState()){  // If current state is the same as the class's state draw only that one
                state.render();
                break;
            }
        }
        Window.render();
    }

    public void addSynchronizedTask(SynchronizedTask task) {
        this.tasks.add(task);
    }
    public void removeSynchronizedTask(SynchronizedTask task) {
        this.tasksToRemove.add(task);
    }
}
