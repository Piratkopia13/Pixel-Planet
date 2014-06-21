package com.piratkopia13.pixelplanet.engine.physics.shape;

import com.piratkopia13.pixelplanet.engine.core.Vector2f;

public class AABB {
    /**
     * Axis Aligned Bounding box
     * Only used for non rotating boundingboxes
     * for collision detection
     */

    float x, y, w, h;
    public AABB(float x, float y, float width, float height){
        this.x = x;
        this.y = y;
        this.w = width;
        this.h = height;
    }

    public Vector2f getMajorAxis(AABB a){
        if (intersects(a)) {

            // Using Minkowskis Difference technique to get closest collision normal
            Vector2f pointA = new Vector2f(a.x+a.w/2, a.y+a.h/2); // Shrink A down to a point
            Vector2f pointB = new Vector2f(x+w/2, y+h/2); // Extend B by the extents of A
            Vector2f between = new Vector2f(pointB.getX()-pointA.getX(), pointB.getY()-pointA.getY()); // The vector between the two points
            return majorAxis(between); // Get the majox axis of between for the direction of the normal

        }

        return null;
    }
    private Vector2f majorAxis(Vector2f vec){
        if ( Math.abs(vec.getX()) > Math.abs(vec.getY()) )
            return new Vector2f(vec.getX(), 0);
        else
            return new Vector2f(0, vec.getY());
    }

    public boolean intersects(AABB a){
        return ( a.x + a.w > x && a.x < x + w &&
                 a.y + a.h > y && a.y < y + h);
    }

    public enum Side{
        LEFT,RIGHT,UP,DOWN
    }
    public float overlapDistance(AABB a, Side side){
        float d = 0;
        if(intersects(a)){
            switch (side){
                case DOWN:
                    d = Math.abs(y+h - a.y);
                    break;
                case UP:
                    d = Math.abs(y - a.y+a.h);
                    break;
                case LEFT:
                    d = Math.abs(x - a.x+a.w);
                    break;
                case RIGHT:
                    d = Math.abs(x+w - a.x);
                    break;
            }
        }

        return d;
    }
}
