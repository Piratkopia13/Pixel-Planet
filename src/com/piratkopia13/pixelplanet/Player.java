package com.piratkopia13.pixelplanet;

import com.piratkopia13.pixelplanet.engine.core.CoreEngine;
import com.piratkopia13.pixelplanet.engine.core.SynchronizedTask;
import com.piratkopia13.pixelplanet.engine.core.Time;
import com.piratkopia13.pixelplanet.engine.core.Vector2f;
import com.piratkopia13.pixelplanet.engine.rendering.Camera;
import com.piratkopia13.pixelplanet.engine.rendering.Image;
import org.lwjgl.input.Keyboard;

import static org.lwjgl.opengl.GL11.*;

public class Player {

    private Image shipImage;

    private float rotation;
    private Vector2f position;
    private Vector2f centerPos;
    private Vector2f size;
    private Camera camera;
    private float movementSpeed;

    private float tmpSpeed;

    public Player(){
        this.position = new Vector2f(0, 0);
        this.centerPos = new Vector2f(0, 0);
        this.size = new Vector2f(80, 80);
        this.rotation = 0f;
        this.movementSpeed = 1;

        this.position.add(size.divide(2));
    }

    public void setShipIcon(String filename){
        shipImage = new Image("ships/"+filename, 0, 0, (int)size.getX(), (int)size.getY());
    }

    public void draw(){
        glPushMatrix();
        glTranslatef(centerPos.getX(), centerPos.getY(), 0); // Translate to the center position of the ship for rotation
        glRotatef(rotation+90, 0, 0, 1);
        glTranslatef(-size.getX() / 2, -size.getY() / 2, 0); // Translate to the rendering position of the ship
        shipImage.draw();
        glPopMatrix();
    }

    public void move(float x, float y){
        this.position.addTo(new Vector2f(x, y));
        this.centerPos.addTo(new Vector2f(x, y));
        if (camera != null)
            camera.setPosition(centerPos.toCamera());
    }

    public void follow(Camera camera){
        this.camera = camera;
        camera.setPosition(centerPos.toCamera());
    }

    public void pointTowardsMouse(boolean enabled){
        SynchronizedTask task = new SynchronizedTask() {
            @Override
            public void update() {
                int mX = CoreEngine.getMouseX();
                int mY = CoreEngine.getMouseY();
                Vector2f mVec = centerPos.add(CoreEngine.getGameCamera().getPosition()).sub(mX, mY);
                rotation = (float)Math.toDegrees( Math.atan2(mVec.getY(), mVec.getX()) ) - 180;
            }
        };
        if (enabled)
            CoreEngine.addSynchronizedTask(task);
        else
            CoreEngine.removeSynchronizedTask(task);
    }

    public void updateFromInput(){

        boolean keyUp = Keyboard.isKeyDown(Keyboard.KEY_UP) | Keyboard.isKeyDown(Keyboard.KEY_W),
                keyDown = Keyboard.isKeyDown(Keyboard.KEY_DOWN) | Keyboard.isKeyDown(Keyboard.KEY_S),
                keyLeft = Keyboard.isKeyDown(Keyboard.KEY_LEFT) | Keyboard.isKeyDown(Keyboard.KEY_A),
                keyRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT) | Keyboard.isKeyDown(Keyboard.KEY_D),
                keySlow = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
        float x = 0, y = 0, dimul = (float)Math.sqrt(2);

        if (keySlow){
            if (movementSpeed != 1f) tmpSpeed = movementSpeed;
            movementSpeed = 1f;
        }else{
            movementSpeed = tmpSpeed;
        }

        movementSpeed = movementSpeed * (float)Time.getDelta();

        if (keyUp && ! keyDown){
            x = 0; y = -movementSpeed;
        }
        if (keyDown && !keyUp){
            x = 0;  y = movementSpeed;
        }
        if (keyLeft && !keyRight){
            x = -movementSpeed; y = 0;
        }
        if (keyRight && !keyLeft){
            x = movementSpeed; y = 0;
        }
        if (keyLeft && keyUp && !keyDown && !keyRight){
            x = -movementSpeed / dimul; y = -movementSpeed / dimul;
        }
        if (keyRight && keyUp && !keyDown && !keyLeft){
            x = movementSpeed / dimul; y = -movementSpeed / dimul;
        }
        if (keyLeft && keyDown && !keyUp && !keyRight){
            x = -movementSpeed / dimul; y = movementSpeed / dimul;
        }
        if (keyRight && keyDown && !keyUp && !keyLeft){
            x = movementSpeed / dimul; y = movementSpeed / dimul;
        }

        move(x, y);
    }

    public Vector2f getPosition() {
        return position;
    }
    public Vector2f getCenterPosition() {
        return centerPos;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
        this.centerPos = position;
    }
    public void setPosition(float x, float y) {
        setPosition(new Vector2f(x, y));
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

    public float getSpeed() {
        return movementSpeed;
    }
    public void setSpeed(float movementSpeed) {
        this.movementSpeed = movementSpeed;
        this.tmpSpeed = movementSpeed;
    }
}
