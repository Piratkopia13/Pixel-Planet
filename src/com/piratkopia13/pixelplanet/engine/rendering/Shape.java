package com.piratkopia13.pixelplanet.engine.rendering;

import com.piratkopia13.pixelplanet.engine.core.Vertex;
import static org.lwjgl.opengl.GL11.*;

public class Shape {

    private Mesh mesh;

    public Shape(){
        this.mesh = new Mesh();
    }

    public void setAsSquare(int x1, int y1, int x2, int y2){
        Vertex[] vertices = new Vertex[] { new Vertex(x1, y1),
                                           new Vertex(x1, y2),
                                           new Vertex(x2, y2),
                                           new Vertex(x2, y1) };
        int[] indices = new int[] { 0,1,3,
                                    3,1,2 };
        mesh.addVertices(vertices, indices);
    }

    public void draw(){
        mesh.draw();
    }

    public void dispose(){
        mesh.dispose();
    }

}
