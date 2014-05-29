package com.piratkopia13.pixelplanet;

import com.piratkopia13.pixelplanet.engine.core.CoreEngine;
import com.piratkopia13.pixelplanet.engine.core.SynchronizedTask;
import com.piratkopia13.pixelplanet.engine.core.Vector2f;
import com.piratkopia13.pixelplanet.engine.rendering.Camera;
import com.piratkopia13.pixelplanet.engine.rendering.Image;

import static org.lwjgl.opengl.GL11.*;

public class Player {

    private Image shipImage;

    private float rotation;
    private Vector2f position;
    private Vector2f centerPos;
    private Vector2f size;
    private Camera camera;

    public Player(){
        this.position = new Vector2f(0, 0);
        this.centerPos = new Vector2f(0, 0);
        this.size = new Vector2f(100, 100);
        this.rotation = 0f;
    }

    public void setShipIcon(String filename){
        shipImage = new Image("ships/"+filename, 0, 0, (int)size.getX(), (int)size.getY());
    }

    public void draw(){
        glPushMatrix();
        glTranslatef(centerPos.getX(), centerPos.getY(), 0); // Translate to the center position of the ship for rotation
        glRotatef(rotation, 0, 0, 1);
        glTranslatef(-size.getX()/2, -size.getY()/2, 0); // Translate to the rendering position of the ship
        shipImage.draw();
        glPopMatrix();
    }

    public void move(float x, float y){
        this.position.addToX(x);
        this.position.addToY(y);
        this.centerPos = position.add(size.divide(2));
        if (camera != null)
            camera.setPosition(position.toCamera().add(size.divide(2).inverted()));
    }

    public void follow(Camera camera){
        this.camera = camera;
    }

    public void pointTowardsMouse(boolean enabled){
        CoreEngine.addSynchronizedTask(new SynchronizedTask() {
            @Override
            public void update() {
                int mX = CoreEngine.getMouseX();
                int mY = CoreEngine.getMouseY();
                Vector2f mVec = centerPos.add(CoreEngine.getGameCamera().getPosition()).sub(mX, mY);
                rotation = (float)Math.toDegrees( Math.atan2(mVec.getY(), mVec.getX()) ) - 90f;
                System.out.println(rotation);
            }
        });
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public float getWidth(){
        return size.getX();
    }
    public float getHeight(){
        return size.getX();
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}
