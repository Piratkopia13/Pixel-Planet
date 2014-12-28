package com.piratkopia13.pixelplanet.engine.physics.shape;

import com.piratkopia13.pixelplanet.engine.core.Vector2f;
import com.piratkopia13.pixelplanet.engine.core.Vertex;
import com.piratkopia13.pixelplanet.engine.rendering.Mesh;
import com.piratkopia13.pixelplanet.engine.rendering.SpriteSheet;

public class SpriteRectangle extends Shape {

    private Vector2f position;
    private Vector2f size;
    private SpriteSheet.Sprite sprite;

    public SpriteRectangle(float x, float y, float width, float height, SpriteSheet.Sprite sprite) {
        this.position = new Vector2f(x, y);
        this.size = new Vector2f(width, height);
        this.sprite = sprite;
    }
    public SpriteRectangle(Vector2f pos, Vector2f size, SpriteSheet.Sprite sprite){
        this(pos.getX(), pos.getY(), size.getX(), size.getY(), sprite);
    }

    @Override
    public void setRenderable() {
        mesh = new Mesh();
        Vertex[] vertices = new Vertex[] { new Vertex(position.getX(), position.getY(), sprite.x, sprite.y),
                                           new Vertex(position.getX(), position.getY()+size.getY(), sprite.x, sprite.y+sprite.h),
                                           new Vertex(position.getX()+size.getX(), position.getY()+size.getY(), sprite.x+sprite.w, sprite.y+sprite.h),
                                           new Vertex(position.getX()+size.getX(), position.getY(), sprite.x+sprite.w, sprite.y) };
//        Vertex[] vertices = new Vertex[] { new Vertex(position.getX(), position.getY(), 0, 0),
//                new Vertex(position.getX(), position.getY()+size.getY(), 0, 1),
//                new Vertex(position.getX()+size.getX(), position.getY()+size.getY(), 1, 1),
//                new Vertex(position.getX()+size.getX(), position.getY(), 1, 0) };
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

    public boolean intersects(Vector2f point){
        return ( ( point.getX() > position.getX() && point.getX() < position.getX()+size.getX()) &&
                ( point.getY() > position.getY() && point.getY() < position.getY()+size.getY() ) );
    }

    @Override
    public Vector2f getPosition() {
        return position;
    }

}
