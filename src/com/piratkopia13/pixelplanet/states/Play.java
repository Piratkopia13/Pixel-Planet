package com.piratkopia13.pixelplanet.states;

import com.piratkopia13.pixelplanet.engine.GameState;
import com.piratkopia13.pixelplanet.engine.RenderUtil;

public class Play implements GameState {

    @Override
    public void init() {

    }

    @Override
    public void update(){

    }

    @Override
    public void input(){

    }

    @Override
    public void render(){
        RenderUtil.clearScreen();

    }

    @Override
    public State getState() {
        return State.PLAY;
    }

}
