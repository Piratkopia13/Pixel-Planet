package com.piratkopia13.pixelplanet.shaders;

import com.piratkopia13.pixelplanet.engine.core.GameResourceLoader;
import com.piratkopia13.pixelplanet.engine.rendering.Shader;

public class BasicShader {

    private static Shader shader;

    public static Shader getInstance(){
        loadShader();
        return shader;
    }

    private static void loadShader(){
        shader = new Shader();
        shader.addVertexShader(GameResourceLoader.loadShader("basicVertex.vs"));
        shader.addFragmentShader(GameResourceLoader.loadShader("basicFragment.fs"));
        shader.compileShader();
    }

}
