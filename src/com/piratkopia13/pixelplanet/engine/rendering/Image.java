package com.piratkopia13.pixelplanet.engine.rendering;

import com.piratkopia13.pixelplanet.engine.core.GameResourceLoader;
import com.piratkopia13.pixelplanet.engine.core.Vertex;
import org.newdawn.slick.opengl.Texture;

import static org.lwjgl.opengl.GL11.*;

public class Image {

    private Texture tex_;
    private Mesh mesh;

    /**
     * @param filename
     * @param x
     * @param y
     * Initializes the full image starting at x and y
     */
    public Image(String filename, int x, int y){
        loadTexture(filename);

        mesh = new Mesh();
        Vertex[] vertices = new Vertex[] { new Vertex(x, y, 0, 0),
                            new Vertex(x, y+getImageHeight(), 0, getHeight()),
                            new Vertex(x+getImageWidth(), y+getImageHeight(), getWidth(), getHeight()),
                            new Vertex(x+getImageWidth(), y, getWidth(), 0) };
        int[] indices = new int[] { 0,1,3,
                                    3,1,2 };
        mesh.addVertices(vertices, indices);

    }

    /**
     * @param filename
     * @param x2
     * @param y1
     * @param x2
     * @param y2
     * Initializes the image starting at x,y and ending at x2,y2
     */
    public Image(String filename, int x1, int y1, int x2, int y2){
        loadTexture(filename);

        mesh = new Mesh();
        Vertex[] vertices = new Vertex[] { new Vertex(x1, y1, 0, 0),
                            new Vertex(x1, y2, 0, getHeight()),
                            new Vertex(x2, y2, getWidth(), getHeight()),
                            new Vertex(x2, y1, getWidth(), 0) };
        int[] indices = new int[] { 0,1,3,
                                    3,1,2 };
        mesh.addVertices(vertices, indices);
    }

    /**
     * @param filename
     * Initializes the image, coords needs to be specified later
     */
    public Image(String filename){
        loadTexture(filename);
    }

    public void setLocation(int x, int y){
        mesh = new Mesh();
        Vertex[] vertices = new Vertex[] { new Vertex(x, y, 0, 0),
                            new Vertex(x, y+getImageHeight(), 0, getHeight()),
                            new Vertex(x+getImageWidth(), y+getImageHeight(), getWidth(), getHeight()),
                            new Vertex(x+getImageWidth(), y, getWidth(), 0) };
        int[] indices = new int[] { 0,1,3,
                                    3,1,2 };
        mesh.addVertices(vertices, indices);
    }

    public void setLocation(int x1, int y1, int x2, int y2){
        mesh = new Mesh();
        Vertex[] vertices = new Vertex[] { new Vertex(x1, y1, 0, 0),
                            new Vertex(x1, y2, 0, getHeight()),
                            new Vertex(x2, y2, getWidth(), getHeight()),
                            new Vertex(x2, y1, getWidth(), 0) };
        int[] indices = new int[] { 0,1,3,
                                    3,1,2 };
        mesh.addVertices(vertices, indices);
    }

    private void loadTexture(String filename){
        this.tex_ = GameResourceLoader.loadTexture(filename);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    }

    /**
     * Renders the image
     */
    public void draw(){
        glEnable(GL_BLEND);
        tex_.bind();
        mesh.draw();
        glDisable(GL_BLEND);
    }

    public void dispose(){
        tex_.release();
        mesh.dispose();
    }

    public float getWidth(){
        return tex_.getWidth();
    }
    public float getImageWidth(){
        return tex_.getImageWidth();
    }
    public float getHeight(){
        return tex_.getHeight();
    }
    public float getImageHeight(){
        return tex_.getImageHeight();
    }

}
