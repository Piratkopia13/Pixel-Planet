package com.piratkopia13.pixelplanet.engine.physics.shape;

import com.piratkopia13.pixelplanet.engine.core.Vector2f;
import com.piratkopia13.pixelplanet.engine.core.Vertex;
import com.piratkopia13.pixelplanet.engine.rendering.Mesh;

import java.util.ArrayList;
import java.util.List;

public class Circle extends Shape {

    private Vector2f position;
    private float radius;
    private int slices;

    public Circle(float x, float y, float radius, int slices) {
        this.position = new Vector2f(x, y);
        this.radius = radius;
        this.slices = slices;
    }

    @Override
    public void setRenderable() {
        super.setRenderable();

        List<Vertex> vertexList = new ArrayList<>();
        for(int i = 1; i <= slices+1; i++){
            double angle = Math.toRadians((360f/slices)*i);
            vertexList.add(new Vertex(position.getX(), position.getY()));
            vertexList.add(new Vertex(position.getX()+(float)Math.cos(angle)*radius, position.getY()+(float)Math.sin(angle)*radius));
            angle = Math.toRadians((360f/slices)*(i+1));
            vertexList.add(new Vertex(position.getX()+(float)Math.cos(angle)*radius, position.getY()+(float)Math.sin(angle)*radius));
        }

        Vertex[] vertices = new Vertex[vertexList.size()];
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = vertexList.get(i);
        }

        int[] indices = new int[vertices.length];
        for (int i = indices.length-1; i >= 0; i--) {
            indices[indices.length-i-1] = i;
        }

        mesh.addVertices(vertices, indices);

    }
}
