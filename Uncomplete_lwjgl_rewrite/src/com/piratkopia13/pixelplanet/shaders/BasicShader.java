package com.piratkopia13.pixelplanet.shaders;

import com.piratkopia13.pixelplanet.engine.core.GameResourceLoader;
import com.piratkopia13.pixelplanet.engine.rendering.Shader;

import static org.lwjgl.opengl.GL20.*;

public class BasicShader {

    private static Shader shader;
    private static int colorUniformLoc;

    public static Shader getInstance(){
        loadShader();
        colorUniformLoc = glGetUniformLocation(shader.getProgramID(), "color");
        return shader;
    }

    private static void loadShader(){
        shader = new Shader();
        shader.addVertexShader(GameResourceLoader.loadShader("basicVertex.vs"));
        shader.addFragmentShader(GameResourceLoader.loadShader("basicFragment.fs"));
        shader.compileShader();
    }

    public static void setColor(float r, float g, float b, float a){
        glUniform4f(colorUniformLoc, r, g, b, a);

    }
    public static void resetColor(){
        glUniform4f(colorUniformLoc, 0, 0, 0, 0);
    }

}
