package com.piratkopia13.pixelplanet.engine.rendering;

import com.piratkopia13.pixelplanet.engine.core.Vector2f;
import com.piratkopia13.pixelplanet.engine.core.Vertex;
import org.newdawn.slick.opengl.Texture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Shape {

    public static Mesh rectangle(float x, float y, float width, float height, Texture texture){
        float texHeight = 1;
        float texWidth = 1;
        if (texture != null){
            texHeight = texture.getHeight();
            texWidth = texture.getWidth();
        }
        Mesh mesh = new Mesh();
        Vertex[] vertices = new Vertex[] { new Vertex(x, y, 0, 0),
                                           new Vertex(x, y+height, 0, texHeight),
                                           new Vertex(x+width, y+height, texWidth, texHeight),
                                           new Vertex(x+width, y, texWidth, 0) };
        int[] indices = new int[] { 0,1,3,
                                    3,1,2 };
        mesh.addVertices(vertices, indices);
        return mesh;
    }
    public static Mesh rectangle(float x, float y, float width, float height){
        return rectangle(x, y, width, height, null);
    }


    public static Mesh circle(float x, float y, float radius, int slices){
        Mesh mesh = new Mesh();

        List<Vertex> vertexList = new ArrayList<>();
        for(int i = 1; i <= slices+1; i++){
            double angle = Math.toRadians((360f/slices)*i);
            vertexList.add(new Vertex(x, y));
            vertexList.add(new Vertex(x+(float)Math.cos(angle)*radius, y+(float)Math.sin(angle)*radius));
            angle = Math.toRadians((360f/slices)*(i+1));
            vertexList.add(new Vertex(x+(float)Math.cos(angle)*radius, y+(float)Math.sin(angle)*radius));
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
        return mesh;
    }
    public static boolean circleCollision(Vector2f circlePos, float radius, Vector2f collisionPoint){
        return (Math.sqrt( Math.pow(collisionPoint.getX()-circlePos.getX(),2) + Math.pow(collisionPoint.getY()-circlePos.getY(), 2)) < radius );
    }
    public static boolean rectangleCollision(Vector2f rectPos, Vector2f rectSize, Vector2f collisionPoint){
        return ( ( collisionPoint.getX() > rectPos.getX() && collisionPoint.getX() < rectPos.getX()+rectSize.getX()) &&
                 ( collisionPoint.getY() > rectPos.getY() && collisionPoint.getY() < rectPos.getY()+rectSize.getY() ) );
    }

}
