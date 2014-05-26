package com.piratkopia13.pixelplanet.engine;

import com.piratkopia13.pixelplanet.Version;
import org.newdawn.slick.Color;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtil {

    public static void clearScreen(){
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public static void setTextures(boolean enabled){
        if (enabled)
            glEnable(GL_TEXTURE_2D);
        else
            glDisable(GL_TEXTURE_2D);
    }

    public static void initGraphics(){
        glClearColor(0, 0, 0, 1);

        glMatrixMode(GL_PROJECTION);
        glOrtho(0, Window.getWidth(), Window.getHeight(), 0, 1, -1);

        glCullFace(GL_BACK);
        glEnable(GL_CULL_FACE);
        glEnable(GL_TEXTURE_2D);
    }

    public static String getOpenGLVersion(){
        return glGetString(GL_VERSION);
    }

    public static void drawGameVersion(GameFont font){
        font.draw("v" + Version.VERSION, 0, Window.getHeight()-font.getHeight(), Color.white);
    }

}
