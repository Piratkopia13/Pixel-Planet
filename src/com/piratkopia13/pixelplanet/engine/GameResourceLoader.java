package com.piratkopia13.pixelplanet.engine;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.*;
import static org.lwjgl.opengl.GL11.*;

public class GameResourceLoader {

    public static Texture loadTexture(String filename){
        String[] splat = filename.split("\\.");
        String format = splat[splat.length-1];
        try {
            return TextureLoader.getTexture("JPG", new FileInputStream(new File("res/textures/"+filename)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String loadShader(String filename){
        StringBuilder shaderSource = new StringBuilder();
        BufferedReader shaderReader = null;

        try {
            shaderReader = new BufferedReader(new FileReader("./res/shaders/" + filename));
            String line;
            while ((line = shaderReader.readLine()) != null){
                shaderSource.append(line).append("\n");
            }

            shaderReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return shaderSource.toString();
    }

}
