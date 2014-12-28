package com.piratkopia13.pixelplanet.engine.rendering;

import org.newdawn.slick.*;
import org.newdawn.slick.util.ResourceLoader;

import java.awt.Font;
import java.io.InputStream;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;

public class GameFont {

    private TrueTypeFont font;

    public GameFont(String font, int size, int style, boolean antiAlias){

        Font awtFont = new Font(font, style, size);
        this.font = new TrueTypeFont(awtFont, antiAlias);

    }
    public GameFont(float size, boolean antiAlias){
        if (size > 46) System.err.println("Warning: Using a fontsize larger then 46 might cause artifacts");
        try {
            InputStream inputStream	= ResourceLoader.getResourceAsStream("res/fonts/Bonveno.ttf");

            Font awtFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            awtFont = awtFont.deriveFont(size);
            this.font = new TrueTypeFont(awtFont, antiAlias);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(String text, float x, float y, Color color){
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        this.font.drawString(x, y, text, color);
        glDisable(GL_BLEND);
    }

    public int getWidth(String text){
        return font.getWidth(text);
    }
    public int getHeight(){
        return font.getHeight();
    }

}
