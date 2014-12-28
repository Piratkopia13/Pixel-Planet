package net.brainstorm_labs.spacegame;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Inventory {
    private SpriteSheet sprites;
    private Image hotbarLeft,
                  hotbarRight,
                  hotbarSel,
                  hotbarNotsel;
    private final int HOTBAR_SLOTS = 5,
                      HOTBAR_SIZE = 60;
    private int hotbar_selected = 0;
    private Graphics g;

    Inventory(Graphics g){
        this.g = g;
        try{
            sprites = new SpriteSheet("res/guisheet.png", 16, 16);
        }catch (SlickException e){
            e.printStackTrace();
        }
        sprites.setFilter(Image.FILTER_NEAREST);

        hotbarLeft = sprites.getSprite(0,0).getScaledCopy(HOTBAR_SIZE, HOTBAR_SIZE);
        hotbarRight = sprites.getSprite(3,0).getScaledCopy(HOTBAR_SIZE, HOTBAR_SIZE);
        hotbarSel = sprites.getSprite(2,0).getScaledCopy(HOTBAR_SIZE, HOTBAR_SIZE);
        hotbarNotsel = sprites.getSprite(1,0).getScaledCopy(HOTBAR_SIZE, HOTBAR_SIZE);
    }

    public void drawHotbar(int gcWidth, int gcHeight){
        int startX = gcWidth/2-( (HOTBAR_SLOTS+2)*HOTBAR_SIZE ) /2,
            startY = gcHeight-HOTBAR_SIZE;
        hotbarLeft.draw(startX, startY);
        int x = startX+HOTBAR_SIZE;
        for (int i = 0; i < HOTBAR_SLOTS; i++){
            if (i == hotbar_selected){
                hotbarSel.draw(x,startY);
            }else{
                hotbarNotsel.draw(x,startY);
            }
            x+=HOTBAR_SIZE;
        }
        hotbarRight.draw(x, startY);
    }

    public void update(){
        int scroll = Mouse.getDWheel();
        if (scroll > 0){
            hotbar_selected = (hotbar_selected==HOTBAR_SLOTS-1) ? 0 : hotbar_selected+1;
        }else if (scroll < 0){
            hotbar_selected = (hotbar_selected==0) ? HOTBAR_SLOTS-1 : hotbar_selected-1;
        }
    }

}
