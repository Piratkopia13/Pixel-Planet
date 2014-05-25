package com.piratkopia13.pixelplanet.engine;

import com.piratkopia13.pixelplanet.engine.Vector2f;

public class Vertex {
    public static final int SIZE = 4;

    private Vector2f pos;
    private Vector2f texCoord;

    public Vertex(Vector2f pos) {
        this(pos, new Vector2f(0, 0));
    }
    public Vertex(Vector2f pos, Vector2f texCoord) {
        this.pos = pos;
        this.texCoord = texCoord;
    }

    public Vector2f getPos() {
        return pos;
    }

    public void setPos(Vector2f pos) {
        this.pos = pos;
    }

    public Vector2f getTexCoord() {
        return texCoord;
    }

    public void setTexCoord(Vector2f texCoord) {
        this.texCoord = texCoord;
    }
}
