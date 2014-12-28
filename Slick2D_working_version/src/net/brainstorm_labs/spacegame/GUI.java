package net.brainstorm_labs.spacegame;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class GUI {

    Graphics g;
    GameContainer gc;

    private ArrayList<String> texts = new ArrayList<>();
    private ArrayList<Point2D.Float> coords = new ArrayList<>();
    private ArrayList<Font> fonts = new ArrayList<>();
    private ArrayList<Color> colors = new ArrayList<>();

    private ArrayList<Rectangle> rects = new ArrayList<>();
    private ArrayList<Color> rectColors = new ArrayList<>();

    public GUI(Graphics g, GameContainer gc){
        this.g = g;
        this.gc = gc;
    }

    void drawText(String text, int x, int y, Font font, Color color){

        if (!texts.contains(text)){
            texts.add(text);
            coords.add(new Point2D.Float(x,y));
            fonts.add(font);
            colors.add(color);
        }

    }
    void drawBox(int x, int y, int width, int height, Color color){
        rects.add(new Rectangle(x, y, width, height));
        rectColors.add(color);
    }

    void setTextColor(int index, Color color){
        colors.set(index, color);
    }
    void setBoxColor(int index, Color color){
        rectColors.set(index, color);
    }
    void removeAllBoxes(){
        rects.clear();
        rectColors.clear();
    }
    void removeAllTexts(){
        texts.clear();
        coords.clear();
        fonts.clear();
        colors.clear();
    }
    void removeText(int i){
        texts.remove(i);
        coords.remove(i);
        fonts.remove(i);
        colors.remove(i);
    }
    void clear(){
        removeAllBoxes();
        removeAllTexts();
    }

    void render(){
        Point2D.Float coord;
        Point2D.Float size;
        Font font;
        Color color;
        int i = 0;
        for (String text : texts){
            coord = coords.get(i);
            color = colors.get(i);
            font = fonts.get(i);
            font.drawString(coord.x, coord.y, text, color);
            i++;
        }
        i = 0;
        for (Rectangle rect : rects){
            g.setColor(rectColors.get(i));
            g.fill(rect);
            i++;
        }
    }

    boolean isInside(int index){
        if (texts.size() > 0){
            int mouseX = Mouse.getX(),
                mouseY = gc.getHeight()-Mouse.getY();
            String text = texts.get(index);
            Point2D.Float coord = coords.get(index);
            Font font = fonts.get(index);
            if ((mouseX>coord.x && mouseX<coord.getX()+font.getWidth(text)) && (mouseY>coord.y && mouseY<coord.y+font.getHeight(text))) {
                return true;
            }
        }
        return false;
    }
    int isInsideBox(){
        int mX = Mouse.getX(),
            mY = gc.getHeight()-Mouse.getY(),
            i = 0;
        for(Rectangle rect : rects){
            if (mX > rect.getX() && mX < rect.getX()+rect.getWidth() && mY > rect.getY() && mY < rect.getY()+rect.getHeight()){
                return i;
            }
            i++;
        }
        return -1;
    }
    int size(){
        return texts.size();
    }
}
