package com.piratkopia13.pixelplanet;

import com.piratkopia13.pixelplanet.engine.core.Vector2f;
import com.piratkopia13.pixelplanet.engine.core.Vertex;
import com.piratkopia13.pixelplanet.engine.rendering.Mesh;
import com.piratkopia13.pixelplanet.shaders.BasicShader;

import static org.lwjgl.opengl.GL11.*;

public class Bullet {

    private final int TICKS_ALIVE = 600; // After these ticks the bullet we be forcedremoved

    private Vector2f position;
    private Vector2f trajectory;
    private float speed;
    private float rotation;
    private Mesh mesh;
    private Vector2f size;
    private Vector2f extraVelocity;
    private boolean isDead = false;
    private int currentTick;

    public Bullet(Vector2f startPos, float angle, float speed, Vector2f extraVelocity){
        this.position = startPos;
        this.trajectory = new Vector2f( (float) Math.cos(Math.toRadians(angle)), (float) Math.sin(Math.toRadians(angle)) );
        this.rotation = angle;
        this.speed = speed;
        this.extraVelocity = extraVelocity;
        this.size = new Vector2f(10,5);

        mesh = new Mesh();
        Vertex[] vertices = new Vertex[] { new Vertex(0, 0, 0, 0),
                                           new Vertex(0, size.getY(), 0, 1),
                                           new Vertex(size.getX(), size.getY(), 1, 1),
                                           new Vertex(size.getX(), 0, 1, 0) };
        int[] indices = new int[] { 0,1,3,
                                    3,1,2 };
        mesh.addVertices(vertices, indices);
    }

    public void update(){
        position = position.add( trajectory.mul(speed*1.0f).add(extraVelocity) );
        if (currentTick >= TICKS_ALIVE)
            markAsDead();
        currentTick++;
    }

    public void draw(){

        glPushMatrix();

        glTranslatef(position.getX(), position.getY(), 0);
        glRotatef(rotation, 0, 0, 1);
        BasicShader.setColor(0, 1, 0, 1);
        mesh.draw();
        BasicShader.resetColor();

        glPopMatrix();

    }

    public void markAsDead(){
        isDead = true;
    }

    public boolean isDead(){
        return isDead;
    }

    public Vector2f getPosition() {
        return position;
    }
}
