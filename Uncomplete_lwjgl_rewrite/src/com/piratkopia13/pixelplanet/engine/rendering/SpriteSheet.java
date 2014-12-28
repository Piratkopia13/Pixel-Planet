package com.piratkopia13.pixelplanet.engine.rendering;

import com.piratkopia13.pixelplanet.engine.core.GameResourceLoader;
import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.ARBTextureRectangle.GL_TEXTURE_RECTANGLE_ARB;
import static org.lwjgl.opengl.GL11.*;

public class SpriteSheet {

    private Map<String, Sprite> sprites = new HashMap<>();
    private int sheetImg;

    public SpriteSheet(String location) {
        Document document = GameResourceLoader.loadXML("res/textures/"+location+".xml");
        sheetImg = GameResourceLoader.loadTextureLinear(location+".png");
        Element root = document.getRootElement();
        try {
            for (Object spriteObject : root.getChildren()){
                Element spriteElement = (Element) spriteObject;
                String name = spriteElement.getAttributeValue("n");

                int x = spriteElement.getAttribute("x").getIntValue();
                int y = spriteElement.getAttribute("y").getIntValue();
                int w = spriteElement.getAttribute("w").getIntValue();
                int h = spriteElement.getAttribute("h").getIntValue();

                sprites.put(name, new Sprite(x, y, w, h));

            }
        } catch (DataConversionException e) {
            e.printStackTrace();
        }
    }

    public void bind(){
        glEnable(GL_TEXTURE_RECTANGLE_ARB);
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_RECTANGLE_ARB, sheetImg);
//        sheet.bind();
    }
    public void unBind(){
//        glBindTexture(GL_TEXTURE_2D, 0);
        glBindTexture(GL_TEXTURE_RECTANGLE_ARB, 0);
        glDisable(GL_TEXTURE_RECTANGLE_ARB);
        glDisable(GL_TEXTURE_2D);
    }

    public Sprite get(String name){
        return sprites.get(name);
    }

    public class Sprite {
        public int x;
        public int y;
        public int w;
        public int h;
//        public int imageW;
//        public int imageH;

        private Sprite(int x, int y, int w, int h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
//            this.imageW = iW;
//            this.imageH = iH;
        }
    }
}
