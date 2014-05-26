package com.piratkopia13.pixelplanet.engine;

import com.piratkopia13.pixelplanet.states.State;

public interface GameState {

    void init();
    void input();
    void update();
    void render();

    State getState();

}
