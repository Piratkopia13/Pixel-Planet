package com.piratkopia13.pixelplanet.engine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Window {

    private static int fpsLimit = 0;

    public static void createWindow(int width, int height, String title){
        Display.setTitle(title);
        try {
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    public static void render(){
        Display.update();
        if (fpsLimit != 0)
            Display.sync(fpsLimit);
    }

    public static void dispose(){
        Display.destroy();
    }

    public static boolean isCloseRequested(){
        return Display.isCloseRequested();
    }

    public static int getWidth(){
        return Display.getWidth();
    }

    public static int getHeight(){
        return Display.getHeight();
    }

    public static void setFPSLimit(int fpsLimit) {
        Window.fpsLimit = fpsLimit;
    }
}
