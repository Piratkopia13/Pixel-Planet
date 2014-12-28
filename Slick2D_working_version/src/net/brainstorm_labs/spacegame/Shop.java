package net.brainstorm_labs.spacegame;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.TextField;

import java.awt.Font;
import java.awt.font.TextAttribute;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Shop {

    int typeID;
    GUI gui;

    Map<Integer, ShopItems> items = new HashMap<>();

    Shape bg;
    Shape border;
    SpriteSheet sprites;
    TextField amount;

    TrueTypeFont font, fontSmall, fontTitle, fontSmallTitle, fontVerySmall;
    public Shop(){

        try{
            sprites = new SpriteSheet("res/shopsheet.png", 16, 16);
        }catch (SlickException e){
            e.printStackTrace();
        }

        String refills = "Refills";
        String upgrades = "Upgrades";
        String appliance = "Appliance";

        ShopItems si = new ShopItems();
        si.add(refills, "Bullets", 1, 2, "Bullets to shoot", sprites.getSprite(0, 0));
        this.items.put(0, si);

        ShopItems si2 = new ShopItems();
        si2.add(refills, "Bullets", 1, 5, "Bullets to shoot", sprites.getSprite(0, 0)).
            add(refills, "HP", 10, 1, "HP to live", sprites.getSprite(1, 0)).
            add(upgrades, "Weapons", 10, 1, "Enhances your weaponry", sprites.getSprite(2, 0)).
            add(upgrades, "Shield", 10, 1, "Upgrades your shield", sprites.getSprite(3, 0)).
            add(upgrades, "Hull", 10, 1, "Makes your ships hull stronger", sprites.getSprite(4, 0)).
            add(appliance, "Turret", 1000, 1, "Placeable turret that automatically fires at enemies", sprites.getSprite(0, 1)).
            add(appliance, "Block", 10, 1, "Block with 10HP", sprites.getSprite(1, 1)).
            add(appliance, "Block", 10, 1, "Block with 20HP", sprites.getSprite(2, 1)).
            add(appliance, "Block", 10, 1, "Block with 30HP", sprites.getSprite(3, 1));
        this.items.put(1, si2);


        this.font = setFontSize(30f, Game.awtFont);
        this.fontSmall = setFontSize(20f, Game.awtFont);
        this.fontVerySmall = setFontSize(16f, Game.awtFont);
        Map<TextAttribute, Integer> fontAttributes = new HashMap<>();
        fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        fontAttributes.put(TextAttribute.SIZE, 24);
        this.fontTitle = new TrueTypeFont(Game.awtFont.deriveFont(fontAttributes), true);
        fontAttributes.put(TextAttribute.SIZE, 16);
        this.fontSmallTitle = new TrueTypeFont(Game.awtFont.deriveFont(fontAttributes), true);

    }
    int bgWidth,
    bgHeight,
    bgPosX,
    bgPosY;
    ArrayList<String> cats = new ArrayList<>();
    ShopItems si;

    GameContainer gc;
    boolean chsAmntIsOpen = false;
    boolean isSet = false;
    void set(int typeID, GUI gui){
        this.typeID = typeID;
        this.gui = gui;

        gui.clear();

        gc = gui.gc;
        bgWidth = 600;
        bgHeight = 400;
        bgPosX = gc.getWidth()/2-bgWidth/2;
        bgPosY = gc.getHeight()/2-bgHeight/2;
        this.bg = new Rectangle(bgPosX, bgPosY, bgWidth, bgHeight);
        this.border = new Rectangle(gc.getWidth()/2-(bgWidth+5)/2, gc.getHeight()/2-(bgHeight+5)/2, bgWidth, bgHeight);

        si = items.get(typeID);
        cats = si.getCategories();

        int strHeight = fontSmall.getHeight("A"),
            titleHeight = fontTitle.getHeight("A"),
            x = bgPosX+10,
            y = bgPosY+titleHeight+15;
        for (String cat : cats){
            gui.drawText(cat, x, y, fontSmall, Color.white);
            y+=strHeight;
        }
        gui.drawText("X", bgPosX+bgWidth-20, bgPosY+5, fontSmall, Color.white);

        amount = new TextField(gc, fontSmall, 0, 0, 150, 40);
        amount.setBackgroundColor(new Color(0, 0, 0, 50));
        amount.setBorderColor(Color.transparent);
        amount.setTextColor(Color.white);
        amount.setMaxLength(253);
        amount.setFocus(true);
        amount.isAcceptingInput();

        closeChooseAmount();

        isSet = true;
    }
    String renderItems = null;
    int box = -1,
        setBox = 0,
    itemStart = -1;
    boolean btnDown = false,
            clicked = false,
            upAftrClick = false;
    void render(Graphics g){
    if (isSet){
        g.setColor(new Color(0, 0, 0, 200));
        g.fill(bg);
        g.setColor(Color.white);
        g.draw(border);
        String str = "Shop";
        font.drawString(bgPosX, bgPosY-font.getHeight(str), str);

        if (Mouse.isButtonDown(0)){ clicked = true; upAftrClick = false; }
        else{ upAftrClick = true; clicked = false; }

        // CAT MOUSE HANDLER
        for (int i = 0; i < cats.size()+1; i++){
            if (Mouse.isButtonDown(0) && gui.isInside(i)){
                if (i == cats.size()){ // Close btn
                    Play.closeShop();
                }else{
                    itemStart = -1;
                    String cat = cats.get(i);
                    for (int i2 = 0; i2 < cats.size(); i2++)
                        gui.setTextColor(i2, Color.white);
                    gui.setTextColor(i, Color.cyan);

                    renderItems = cat;
                }
            }
        }
        // EO MOUSE HANDLER
        // ITEM MOUSE HANDLER
        box = gui.isInsideBox();
        if (box != -1){
            gui.setBoxColor(box, new Color(70, 136, 227, 50));
            if (Mouse.isButtonDown(0) && !btnDown){
                chsAmntIsOpen = true;
                //  buy(box);
                btnDown = true;
            }else if (!Mouse.isButtonDown(0)){
                btnDown = false;
            }
            setBox = box;
        }
        // EO ITEM MOUSE HANDLER

        if (chsAmntIsOpen){
            amount.setFocus(true);
            chooseAmount(setBox, g);
        }

        // Render clickable gui
        gui.render();

        // Render stuff
        fontTitle.drawString(bgPosX+10, bgPosY+10, "Categories");
        g.setColor(Color.white);
        g.drawLine(bgPosX+160, bgPosY, bgPosX+160, bgPosY+bgHeight);
        g.drawLine(bgPosX+160, bgPosY+bgHeight-100, bgPosX+bgWidth, bgPosY+bgHeight-100);
        fontTitle.drawString(bgPosX+170, bgPosY+10, "Items");

        if (renderItems != null){
            // Remove all item boxes
            gui.removeAllBoxes();
            // Render items
            int i = 0;
            int i2 = 0;
            int x = bgPosX+170,
                y = bgPosY+fontTitle.getHeight("A")+15,
                boxW = 100,
                boxH = 100;
            for (String name : si.names){
                String siCat = si.cats.get(i),
                       desc = si.descs.get(i);
                int price = si.prices.get(i),
                    amount = si.amounts.get(i);
                Image sprite = si.sprites.get(i);
                sprite.setFilter(Image.FILTER_NEAREST);
                sprite = sprite.getScaledCopy(boxW, boxH);
                if (renderItems.equals(siCat)){
                    if (itemStart == -1) itemStart = i;
                    gui.drawBox(x, y, boxW, boxH, new Color(232, 232, 232, 50));
                    sprite.draw(x, y);
                    fontSmall.drawString(x+(boxW/2-fontSmall.getWidth(name)/2), y+boxH-fontSmall.getHeight("A"), name);
                    if (i % 3 == 1){
                        y+=boxH+40;
                        x = bgPosX+170;
                    }else {
                        x+= boxW+40;
                    }

                    // Render item specs
                    if (box != -1 && box == i2){
                        int x2 = bgPosX+170;
                        int y2 = bgPosY+bgHeight-90;
                        fontSmallTitle.drawString(x2, y2, name);
                        fontVerySmall.drawString(x2, y2+fontSmallTitle.getHeight("A"), desc);
                        fontVerySmall.drawString(x2, bgPosY+bgHeight-fontVerySmall.getHeight("A"), amount+" for $"+price);
                    }

                    i2++;
                }
                i++;
            }

        }

    }
    }
    TrueTypeFont setFontSize(Float size, Font awtFont){
        Font aawtFont = awtFont.deriveFont(size);
        return new TrueTypeFont(aawtFont, true);
    }
    Shape amountBg,
    amountBorder;
    private void chooseAmount(int item, Graphics g){
        int w = 200,
            h = 40,
            x = gc.getWidth()/2-w/2,
            y = gc.getHeight()/2-h/2;
        amountBg = new Rectangle(x, y, w, h);
        amountBorder = new Rectangle(gc.getWidth()/2-(w+5)/2, gc.getHeight()/2-(h+5)/2, w, h);
        g.setColor(Color.black);
        g.fill(amountBg);
        g.setColor(Color.white);
        g.draw(amountBorder);
        String str = "How many?";
        fontSmall.drawString(x, y - fontSmall.getHeight(str), str);
        amount.setLocation(x, y);
        amount.render(gc, g);
        gui.drawText("Buy", x+w-50, y, fontSmall, Color.white);

        if (Mouse.isButtonDown(0) && gui.isInside(gui.size()-1)){
            int am;
            try{
                am = Integer.parseInt(amount.getText());
                buy(item, am);
            }catch (NumberFormatException e){
                System.out.println("not a valid number");
            }
            gui.removeText(gui.size()-1);
            closeChooseAmount();
        }else if (Mouse.isButtonDown(0) && upAftrClick && ( Mouse.getX() < x || Mouse.getX() > x+w || gc.getHeight()-Mouse.getY() < y || gc.getHeight()-Mouse.getY() > y+h )){
            closeChooseAmount();
            gui.removeText(gui.size()-1);
        }
    }
    private void closeChooseAmount(){
        if (chsAmntIsOpen){
            chsAmntIsOpen = false;
            amount.setText("");
        }
    }
    private void buy(int item, int amount){
        int i = itemStart+item;
        String name = si.names.get(i),
               siCat = si.cats.get(i),
               desc = si.descs.get(i);
        int price = si.prices.get(i),
            amoun = si.amounts.get(i);

        if (Play.players.get(Network.playerID).money >= price*amount){

            if (Game.gameClient != null)
                Game.gameClient.sendBoughtItem(name, amoun*amount, price*amount);
            else
                Game.gameServer.sendBoughtItem(name, amoun*amount, price*amount);

            Player bplayer = Play.players.get(Network.playerID);
            switch (name){
                case "Bullets":
                    bplayer.bulletCount+=amoun*amount;
                    break;
                case "HP":
                    bplayer.HP+=amoun*amount;
                    break;
            }
            bplayer.money-=price*amount;
        }
    }

    public class ShopItems {
        ArrayList<String> cats = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Integer> prices = new ArrayList<>();
        ArrayList<Integer> amounts = new ArrayList<>();
        ArrayList<String> descs = new ArrayList<>();
        ArrayList<Image> sprites = new ArrayList<>();

        ShopItems(){}

        ShopItems add(String category, String name, int price, int amount, String desc, Image sprite){
            this.cats.add(category);
            this.names.add(name);
            this.prices.add(price);
            this.amounts.add(amount);
            this.descs.add(desc);
            this.sprites.add(sprite);
            return this;
        }
        ArrayList<String> getCategories(){
            ArrayList<String> asd = new ArrayList<>();
            for (String cat : this.cats){
                if (!asd.contains(cat)){
                    asd.add(cat);
                }
            }
            return asd;
        }


    }

    static class Location {
        Point2D.Float pos;
        int typeID;
        Shape shape;
        Location(Point2D.Float pos, int typeID){
            this.pos = pos;
            this.typeID = typeID;
        }
    }

}
