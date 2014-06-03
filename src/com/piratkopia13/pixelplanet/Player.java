package com.piratkopia13.pixelplanet;

import com.piratkopia13.pixelplanet.engine.core.*;
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

    private Vector2f[] collisionPoints = new Vector2f[4];
    private Vector2f lastCollision;

    private GameMap map;

    private Vector2f velocity;

    private float tmpSpeed;

    public Player(){
        this.position = new Vector2f(0, 0);
        this.centerPos = new Vector2f(0, 0);
        this.size = new Vector2f(80, 80);
        this.rotation = 0f;
        this.movementSpeed = 1;

        this.lastCollision = new Vector2f(0,0);
        updateCollisionPoints();

        this.velocity = new Vector2f(0, 0);

//        this.position.add(size.divide(2));
    }

    private void updateCollisionPoints(){
        collisionPoints[0] = new Vector2f(position.getX(), position.getY());
        collisionPoints[1] = new Vector2f(position.getX(), position.getY()+size.getY());
        collisionPoints[2] = new Vector2f(position.getX()+size.getX(), position.getY()+size.getY());
        collisionPoints[3] = new Vector2f(position.getX()+size.getX(), position.getY());
    }
    private Vector2f[] getExpectedCollisionPoints(float dx, float dy){
        Vector2f[] points = new Vector2f[4];
        points[0] = new Vector2f(position.getX()+dx, position.getY()+dy);
        points[1] = new Vector2f(position.getX()+dx, position.getY()+size.getY()+dy);
        points[2] = new Vector2f(position.getX()+size.getX()+dx, position.getY()+size.getY()+dy);
        points[3] = new Vector2f(position.getX()+size.getX()+dx, position.getY()+dy);
        return points;
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

    public void move(float dx, float dy){
        this.position.addTo(new Vector2f(dx, dy));
        this.centerPos.addTo(new Vector2f(dx, dy));

        this.velocity = new Vector2f(dx, dy);

        updateCollisionPoints();

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
//            if (movementSpeed != 1f) tmpSpeed = movementSpeed;
            movementSpeed = 1f;
        }else{
            movementSpeed = 10f;
        }

        movementSpeed = movementSpeed * (float)Time.getDelta();

        if (keyUp && ! keyDown){
            y = -movementSpeed;
            if (collidesWithMap(x, y)){
                y = lastCollision.getY() + map.getBlockSize() - position.getY();
            }
        }
        if (keyDown && !keyUp){
            y = movementSpeed;
            if (collidesWithMap(x, y)){
                y = -(position.getY() - lastCollision.getY() + size.getY());
            }
        }
        if (keyLeft && !keyRight){
            x = -movementSpeed;
            if (collidesWithMap(x, y)){
                x = lastCollision.getX() + map.getBlockSize() - position.getX();
            }
        }
        if (keyRight && !keyLeft){
            x = movementSpeed;
            if (collidesWithMap(x, y)){
                x = -(position.getX() - lastCollision.getX() + size.getX());
            }
        }
//        if (keyLeft && keyUp && !keyDown && !keyRight){
//            x = -movementSpeed / dimul; y = -movementSpeed / dimul;
//            if (collidesWithMap(x, y)){
//                x = lastCollision.getX() + map.getBlockSize() - position.getX();
//                y = lastCollision.getY() + map.getBlockSize() - position.getY();
//            }
//        }
//        if (keyRight && keyUp && !keyDown && !keyLeft){
//            x = movementSpeed / dimul; y = -movementSpeed / dimul;
//            if (collidesWithMap(x, y)){
//                x = -(position.getX() - lastCollision.getX() + size.getX());
//                y = lastCollision.getY() + map.getBlockSize() - position.getY();
//            }
//        }
//        if (keyLeft && keyDown && !keyUp && !keyRight){
//            x = -movementSpeed / dimul; y = movementSpeed / dimul;
//            if (collidesWithMap(x, y)){
//                x = lastCollision.getX() + map.getBlockSize() - position.getX();
//                y = -(position.getY() - lastCollision.getY() + size.getY());
//            }
//        }
//        if (keyRight && keyDown && !keyUp && !keyLeft){
//            x = movementSpeed / dimul; y = movementSpeed / dimul;
//            if (collidesWithMap(x, y)){
//                x = -(position.getX() - lastCollision.getX() + size.getX());
//                y = -(position.getY() - lastCollision.getY() + size.getY());
//            }
//        }

        if (x != 0 || y != 0)
            move(x, y);
        else
            this.velocity.set(0, 0);
    }

    public boolean collidesWithMap(float x, float y){
        System.out.println(getExpectedCollisionPoints(x, y)[0]);
        Vector2f collision = this.map.collidesWith( getExpectedCollisionPoints(x, y) );
        if(collision != null){
            this.lastCollision.set(collision);
//            System.out.println("Set to "+collision);
            return true;
        }
        return false;
    }

    public Vector2f getPosition() {
        return position;
    }
    public Vector2f getCenterPosition() {
        return centerPos;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
        this.centerPos = position.add(size.divide(2));
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

    public Vector2f getVelocity() {
        return velocity;
    }

    public GameMap getMap() {
        return map;
    }

    public void setMap(GameMap map) {
        this.map = map;
    }
}
