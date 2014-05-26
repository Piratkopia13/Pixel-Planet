package com.piratkopia13.pixelplanet;

import com.piratkopia13.pixelplanet.engine.core.Vector2f;
import com.piratkopia13.pixelplanet.engine.rendering.Camera;
import com.piratkopia13.pixelplanet.engine.rendering.Image;

import static org.lwjgl.opengl.GL11.*;

public class Player {

    private Image shipImage;

    private Vector2f position;
    private Vector2f size;
    private Camera camera;

    public Player(){
        this.position = new Vector2f(0, 0);
        this.size = new Vector2f(100, 100);
    }

    public void setShipIcon(String filename){
        shipImage = new Image("ships/"+filename, 0, 0, (int)size.getX(), (int)size.getY());
    }

    public void draw(){
        glPushMatrix();
        glTranslatef(position.getX(), position.getY(), 0);
        shipImage.draw();
        glPopMatrix();
    }

    public void move(float x, float y){
        this.position.addToX(x);
        this.position.addToY(y);
        if (camera != null)
            camera.setPosition(position.toCamera().add(size.divide(2).inverted()));
    }

    public void follow(Camera camera){
        this.camera = camera;
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
}
