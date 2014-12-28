package com.piratkopia13.pixelplanet;

import com.piratkopia13.pixelplanet.engine.core.*;
import com.piratkopia13.pixelplanet.engine.physics.shape.AABB;
import com.piratkopia13.pixelplanet.engine.physics.shape.Rectangle;
import com.piratkopia13.pixelplanet.engine.physics.shape.Shape;
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

//    private Vector2f[] collisionPoints = new Vector2f[4];

    private Shape testBox;
    public boolean renderCollisionTests = false;

    private GameMap map;

    private Vector2f velocity;

    private float tmpSpeed;

    public Player(){
        this.position = new Vector2f(0, 0);
        this.centerPos = new Vector2f(0, 0);
        this.size = new Vector2f(80, 80);
        this.rotation = 0f;
        this.movementSpeed = 1;

        setShipIcon("axiom.png");
        setPosition(0, 0);
        setSpeed(movementSpeed);

//        updateCollisionPoints();

        this.velocity = new Vector2f(0, 0);
    }

//    private void updateCollisionPoints(){
//        collisionPoints[0] = new Vector2f(position.getX(), position.getY());
//        collisionPoints[1] = new Vector2f(position.getX(), position.getY()+size.getY());
//        collisionPoints[2] = new Vector2f(position.getX()+size.getX(), position.getY()+size.getY());
//        collisionPoints[3] = new Vector2f(position.getX()+size.getX(), position.getY());
//    }
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
        if (renderCollisionTests && testBox != null) testBox.draw();
        glPushMatrix();
        glTranslatef(centerPos.getX(), centerPos.getY(), 0); // Translate to the center position of the ship for rotation
        glRotatef(rotation + 90, 0, 0, 1);
        glTranslatef(-size.getX() / 2, -size.getY() / 2, 0); // Translate to the rendering position of the ship
        shipImage.draw();
        glPopMatrix();
    }

    public void move(float dx, float dy){
        this.position.addTo(new Vector2f(dx, dy));
        this.centerPos.addTo(new Vector2f(dx, dy));

        this.velocity = new Vector2f(dx, dy);

//        updateCollisionPoints();

        if (camera != null)
            camera.setPosition(centerPos.toCamera());

        Game.netSendPosition();
    }
    public void moveTo(Vector2f pos){
        Vector2f d = pos.add(position.inverted());
        move(d.getX(), d.getY());
    }

    public void follow(Camera camera){
        this.camera = camera;
        camera.setPosition(centerPos.toCamera());
    }

    public void pointTowardsMouse(boolean enabled){
        SynchronizedTask task = new SynchronizedTask() {
            @Override
            public void update() {
                int mX = Game.getMouseX();
                int mY = Game.getMouseY();
                Vector2f mVec = centerPos.add(Game.getGameCamera().getPosition()).sub(mX, mY);
                rotation = (float)Math.toDegrees( Math.atan2(mVec.getY(), mVec.getX()) ) - 180;
            }
        };
        if (enabled)
            Game.addSynchronizedTask(task);
        else
            Game.removeSynchronizedTask(task);
    }

    public void updateFromInput() {

        // Player movement and collision detection - response
        {
            boolean keyUp = Keyboard.isKeyDown(Keyboard.KEY_UP) | Keyboard.isKeyDown(Keyboard.KEY_W),
                    keyDown = Keyboard.isKeyDown(Keyboard.KEY_DOWN) | Keyboard.isKeyDown(Keyboard.KEY_S),
                    keyLeft = Keyboard.isKeyDown(Keyboard.KEY_LEFT) | Keyboard.isKeyDown(Keyboard.KEY_A),
                    keyRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT) | Keyboard.isKeyDown(Keyboard.KEY_D),
                    keySlow = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
            float x = 0, y = 0, dimul = (float) Math.sqrt(2);

            if (keySlow) {
    //            if (movementSpeed != 1f) tmpSpeed = movementSpeed;
                movementSpeed = 1f;
            } else {
                movementSpeed = 10f;
            }

            movementSpeed = movementSpeed * (float) Time.getDelta();
            int blockSize = map.getBlockSize();
            Vector2f collisionDiff;

    //        boundingBox.move(position);
            if (keyUp && !keyDown) {
                y = -movementSpeed;
                collisionDiff = collisionDifference(x, y);
                if (collisionDiff != null)
                    y = collisionDiff.getY() + blockSize;
            }
            if (keyDown && !keyUp) {
                y = movementSpeed;
                collisionDiff = collisionDifference(x, y);
                if (collisionDiff != null)
                    y = collisionDiff.getY() - size.getY();
            }
            if (keyLeft && !keyRight) {
                x = -movementSpeed;
                collisionDiff = collisionDifference(x, y);
                if (collisionDiff != null)
                    x = collisionDiff.getX() + blockSize;
            }
            if (keyRight && !keyLeft) {
                x = movementSpeed;
                collisionDiff = collisionDifference(x, y);
                if (collisionDiff != null)
                    x = collisionDiff.getX() - size.getX();
            }
            if (keyLeft && keyUp && !keyDown && !keyRight) {
                x = -movementSpeed / dimul;
                y = -movementSpeed / dimul;
                collisionDiff = collisionDifference(x, 0);
                if (collisionDiff != null) {
                    x = collisionDiff.getX() + blockSize;
                }
                collisionDiff = collisionDifference(0, y);
                if (collisionDiff != null) {
                    y = collisionDiff.getY() + blockSize;
                }
            }
            if (keyRight && keyUp && !keyDown && !keyLeft) {
                x = movementSpeed / dimul;
                y = -movementSpeed / dimul;
                collisionDiff = collisionDifference(x, 0);
                if (collisionDiff != null) {
                    x = collisionDiff.getX() - size.getX();
                }
                collisionDiff = collisionDifference(0, y);
                if (collisionDiff != null) {
                    y = collisionDiff.getY() + blockSize;
                }
            }
            if (keyLeft && keyDown && !keyUp && !keyRight) {
                x = -movementSpeed / dimul;
                y = movementSpeed / dimul;
                collisionDiff = collisionDifference(x, 0);
                if (collisionDiff != null) {
                    x = collisionDiff.getX() + blockSize;
                }
                collisionDiff = collisionDifference(0, y);
                if (collisionDiff != null) {
                    y = collisionDiff.getY() - size.getY();
                }
            }
            if (keyRight && keyDown && !keyUp && !keyLeft) {
                x = movementSpeed / dimul;
                y = movementSpeed / dimul;
                collisionDiff = collisionDifference(x, 0);
                if (collisionDiff != null) {
                    x = collisionDiff.getX() - size.getX();
                }
                collisionDiff = collisionDifference(0, y);
                if (collisionDiff != null) {
                    y = collisionDiff.getY() - size.getY();
                }
            }
            if (x != 0 || y != 0)
                move(x, y);
            else
                this.velocity.set(0, 0);
        }

    }

    public Vector2f collisionDifference(float x, float y){
        x += position.getX();
        y += position.getY();

//        collisions.clear();

        int blockSize = map.getBlockSize();

        int x1 = (int)Math.ceil(position.getX());
        int y1 = (int)Math.ceil(position.getY());
        int x2 = (int)Math.ceil(x+blockSize+size.getX());
        int y2 = (int)Math.ceil(y+blockSize+size.getY());

        if (position.getY() > y || position.getX() > x) { // to make sure x1 and y1 is the smallest coordinate
            x1 = (int)Math.ceil(x);
            y1 = (int)Math.ceil(y);
            x2 = (int)Math.ceil(position.getX()+blockSize+size.getX());
            y2 = (int)Math.ceil(position.getY()+blockSize+size.getY());
        }
        y1 /= blockSize;
        y2 /= blockSize;
        x1 /= blockSize;
        x2 /= blockSize;

        testBox = new Rectangle(x1*blockSize, y1*blockSize, (x2-x1)*blockSize, (y2-y1)*blockSize);
        testBox.setRenderable();
        testBox.setColor(1, 1, 0, 0.1f);

        for (int iy = y1; iy < y2; iy++) {
            for (int ix = x1; ix < x2; ix++) {
                GameMap.MapBlock block = map.getBlockAt(ix, iy);
                if (block != null && (block.type.equals(GameMap.BlockType.WALL) || block.type.equals(GameMap.BlockType.STONE))) {
                    AABB a = new AABB(block.x, block.y, blockSize, blockSize);
                    AABB b = new AABB(x, y, size.getX(), size.getY());
                    if (a.intersects(b))
                        return new Vector2f(block.x - position.getX(), block.y - position.getY());
                }
            }
        }

        return null;
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
//        this.boundingBox.move(position);
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
