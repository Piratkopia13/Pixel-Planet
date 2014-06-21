package com.piratkopia13.pixelplanet.engine.core;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.lwjgl.BufferUtils;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.*;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.ARBTextureRectangle.GL_TEXTURE_RECTANGLE_ARB;


public class GameResourceLoader {

    public static Texture loadTexture(String filename){
        String[] splat = filename.split("\\.");
        String format = splat[splat.length-1];
        try {
            return TextureLoader.getTexture(format, new FileInputStream(new File("res/textures/"+filename)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static int loadTextureLinear(String filename){
        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_RECTANGLE_ARB, texture);
        InputStream in;
        try {
            in = new FileInputStream("res/textures/"+filename);
            PNGDecoder decoder = new PNGDecoder(in);
            ByteBuffer buffer = BufferUtils.createByteBuffer(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            buffer.flip();
            glTexParameteri(GL_TEXTURE_RECTANGLE_ARB, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_RECTANGLE_ARB, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexImage2D(GL_TEXTURE_RECTANGLE_ARB, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        glBindTexture(GL_TEXTURE_RECTANGLE_ARB, 0);

        return texture;
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

    public static Document loadXML(String filename){
        SAXBuilder builder =  new SAXBuilder();
        try {
            return builder.build(new File(filename));
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
