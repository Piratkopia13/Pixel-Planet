package net.brainstorm_labs.spacegame;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.omg.DynamicAny._DynArrayStub;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class DeathMessages {

    private static CopyOnWriteArrayList<String> messages = new CopyOnWriteArrayList<>();
    private static ArrayList<Float> currentFrame = new ArrayList<>();
    private static final int DURATION = 500; // Duration to render msg in frames | something

    public static void addMsg(String msg){
        messages.add(msg);
        currentFrame.add(0f); // Add startframe
    }

    public static void render(Graphics g, Font font, int gcWidth){
        if (!messages.isEmpty()){
            int x,y,
            fH = font.getHeight("");
            float currFrame;
            for (int i = 0; i < messages.size(); i++){
                String msg = messages.get(i);
                currFrame = currentFrame.get(i);
                y = 100+(i*fH)-Math.round(currFrame);
                x = gcWidth-font.getWidth(msg)-10;
                font.drawString(x, y, msg, new Color(0,174,239,Math.round( 255 - 2.55f*(currFrame) )));
                currentFrame.set(i, currFrame+100f/DURATION);
                if (y < 30){
                    messages.remove(i);
                    currentFrame.remove(i);
                }
            }
        }
    }

}
