package com.piratkopia13.pixelplanet.engine.rendering;

import com.piratkopia13.pixelplanet.engine.core.Vector2f;
import static org.lwjgl.opengl.GL11.*;

public class Camera {
    private Vector2f position;
    private float rotation;

    public Camera(float x, float y){
        this.position = new Vector2f(x+Window.getWidth()/2, y+Window.getHeight()/2);
    }
    public Camera(Vector2f position){
        this(position.getX(), position.getY());
    }

    public void applyTransform(){
        glTranslatef(position.getX(), position.getY(), 0);
        glRotatef(rotation, 0, 0, 1);
    }


    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}
