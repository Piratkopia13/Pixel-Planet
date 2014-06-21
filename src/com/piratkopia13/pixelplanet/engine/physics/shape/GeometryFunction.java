package com.piratkopia13.pixelplanet.engine.physics.shape;

import com.piratkopia13.pixelplanet.engine.core.Vector2f;

/**
 * A collection of general geometry calculating functions
 */
public class GeometryFunction {

    public static float angleBetweenPoints(Vector2f p1, Vector2f p2){
        float deltaX = p2.x-p1.x;
        float deltaY = p2.y-p1.y;
        return (float)Math.toDegrees(Math.atan2(deltaY, deltaX));
    }

}
