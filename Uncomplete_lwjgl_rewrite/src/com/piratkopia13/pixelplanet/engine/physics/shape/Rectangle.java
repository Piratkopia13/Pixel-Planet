package com.piratkopia13.pixelplanet.engine.physics.shape;

import com.piratkopia13.pixelplanet.engine.core.Vector2f;
import com.piratkopia13.pixelplanet.engine.core.Vertex;
import com.piratkopia13.pixelplanet.engine.rendering.Mesh;

public class Rectangle extends Shape {

    private Vector2f position;
    private Vector2f size;

    public Rectangle(float x, float y, float width, float height) {
        this.position = new Vector2f(x, y);
        this.size = new Vector2f(width, height);
    }
    public Rectangle(Vector2f pos, Vector2f size){
        this(pos.getX(), pos.getY(), size.getX(), size.getY());
    }

    @Override
    public void setRenderable() {
        mesh = new Mesh();
        float texHeight = 1;
        float texWidth = 1;
        if (texture != null){
            System.out.println(" wat ");
            texHeight = texture.getHeight();
            texWidth = texture.getWidth();
        }
        Vertex[] vertices = new Vertex[] { new Vertex(position.getX(), position.getY(), 0, 0),
                                           new Vertex(position.getX(), position.getY()+size.getY(), 0, texHeight),
                                           new Vertex(position.getX()+size.getX(), position.getY()+size.getY(), texWidth, texHeight),
                                           new Vertex(position.getX()+size.getX(), position.getY(), texWidth, 0) };
        int[] indices = new int[] { 0,1,3,
                                    3,1,2 };
        mesh.addVertices(vertices, indices);
    }

    @Override
    public void move(Vector2f pos) {
        this.position.set(pos);
    }

    @Override
    public void add(float x, float y){
        this.position.addToX(x);
        this.position.addToY(y);
    }

    public boolean intersects(Vector2f pos, Vector2f size){
        return (intersects(pos) || intersects(new Vector2f(pos.getX()+size.getY(), pos.getY())) ||
                intersects(new Vector2f(pos.getX(), pos.getY()+size.getY())) ||
                intersects(pos.add(size)));
    }
    public boolean intersects(Vector2f point){
        return ( ( point.getX() > position.getX() && point.getX() < position.getX()+size.getX()) &&
                ( point.getY() > position.getY() && point.getY() < position.getY()+size.getY() ) );
    }

    @Override
    public Vector2f getPosition() {
        return position;
    }
}
