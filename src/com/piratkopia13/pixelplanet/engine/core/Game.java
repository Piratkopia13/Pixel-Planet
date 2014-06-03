package com.piratkopia13.pixelplanet.engine.core;

import com.piratkopia13.pixelplanet.engine.rendering.RenderUtil;
import com.piratkopia13.pixelplanet.engine.rendering.Window;
import com.piratkopia13.pixelplanet.states.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Game {

    private int windowWidth;
    private int windowHeight;
    private String windowTitle;

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

        RenderUtil.initGraphics();

        double frameTime = 1.0/10000.0;

        int frames = 0;
        long frameCounter = 0;

        double lastTime = Time.getTime();
        double unprocessedTime = 0;

        isRunning = true;
        while (isRunning){

            boolean render = false;

            double startTime = Time.getTime();
            double passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime / Time.SECOND;
            frameCounter += passedTime;

            while(unprocessedTime > frameTime)
            {
                render = true;

                unprocessedTime -= frameTime;

                if(Window.isCloseRequested())
                    stop();

//                Time.setDelta(passedTime*100 / Time.SECOND); // Wierd delta calculation, don't ask me what it does..
                Time.setDelta(100/60f);   // temporary

                // old update location

                if(frameCounter >= Time.SECOND)
                {
                    CoreEngine.setCurrentFPS(frames);
                    frames = 0;
                    frameCounter = 0;
                }
            }

            if (render){
                Time.setFrameNum(frames);
                update();
                render();
                frames++;
            }else{
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        // Clean everything up before closing
        for (GameState state : gameStates){
            state.cleanUp();
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
    private void update() {
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
            if (tasksToRemove.contains(task)){ // Remove task if flagged
                iterator.remove();
                tasksToRemove.remove(task);
            }
            task.update();
        }
    }
    public void init(State state){
        for (GameState st : gameStates){
            if (state == st.getState()){
                st.init();
                break;
            }
        }
    }

    public void addSynchronizedTask(SynchronizedTask task) {
        this.tasks.add(task);
    }
    public void removeSynchronizedTask(SynchronizedTask task) {
        this.tasksToRemove.add(task);
    }
}
