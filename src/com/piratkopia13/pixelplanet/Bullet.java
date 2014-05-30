package com.piratkopia13.pixelplanet;

import com.piratkopia13.pixelplanet.engine.core.Vector2f;
import com.piratkopia13.pixelplanet.engine.core.Vertex;
import com.piratkopia13.pixelplanet.engine.rendering.Mesh;
import com.piratkopia13.pixelplanet.shaders.BasicShader;

import static org.lwjgl.opengl.GL11.*;

public class Bullet {

    private Vector2f position;
    private Vector2f trajectory;
    private float rotation;
    private float speed;
    private Mesh mesh;
    private Vector2f size;
    private boolean isDead = false;

    public Bullet(Vector2f startPos, float angle, float speed){
        this.position = startPos;
        this.trajectory = new Vector2f( (float) Math.cos(Math.toRadians(angle)), (float) Math.sin(Math.toRadians(angle)) );
        this.rotation = angle;
        this.speed = speed;
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
        position = position.add(trajectory.mul(speed));
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
