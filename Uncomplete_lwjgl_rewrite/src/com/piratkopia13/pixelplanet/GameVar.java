package com.piratkopia13.pixelplanet;

import com.piratkopia13.pixelplanet.engine.core.Vector2f;
import com.piratkopia13.pixelplanet.engine.core.Vector3f;

public class GameVar {
    private static GameMap map;
    private static Player player;
    public static Vector2f bltCol = new Vector2f(0,0);
    public static Vector3f tColor = new Vector3f(0,0,0);

    public static GameMap getMap() {
        return map;
    }
    public static void setMap(GameMap map) {
        GameVar.map = map;
    }

    public static Player getPlayer() {
        return player;
    }
    public static void setPlayer(Player player) {
        GameVar.player = player;
    }
}
