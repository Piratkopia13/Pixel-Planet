package com.piratkopia13.pixelplanet.states;

import com.piratkopia13.pixelplanet.engine.Vertex;
import com.piratkopia13.pixelplanet.engine.*;
import org.newdawn.slick.opengl.Texture;

public class Menu {

    private Texture bgTex_;
    private Mesh bgMesh;
    private Shader shader;

    public Menu(){
        bgTex_ = ResourceLoader.loadTexture("bg.png");
        bgMesh = new Mesh();
        shader = new Shader();

        Vertex[] data = new Vertex[] { new Vertex(new Vector2f(0, 0), new Vector2f(0,0)),
                                       new Vertex(new Vector2f(0, Window.getHeight()), new Vector2f(0,1)),
                                       new Vertex(new Vector2f(Window.getWidth(), Window.getHeight()), new Vector2f(1,1)),
                                       new Vertex(new Vector2f(Window.getWidth(), 0), new Vector2f(1,0)) };
        int[] indices = new int[] { 0,1,3,
                                    3,1,2 };

        bgMesh.addVertices(data, indices);

        shader.addVertexShader(ResourceLoader.loadShader("basicVertex.vs"));
        shader.addFragmentShader(ResourceLoader.loadShader("basicFragment.fs"));
        shader.compileShader();
    }

    public void update(){

    }

    public void input(){

    }

    public void render(){
        RenderUtil.clearScreen();

        bgTex_.bind();
        shader.bind();
        bgMesh.draw();
    }

}
