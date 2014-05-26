package com.piratkopia13.pixelplanet.engine;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

public class Button {

    private String text;
    private GameFont font;
    private Color color;
    private Vector2f position;

    private boolean isClicked = false;

    public Button(String text, GameFont font, Color color, Vector2f position){
        this.text = text;
        this.font = font;
        this.color = color;
        this.position = position;

        CoreEngine.addSynchronizedTask( new ButtonUpdater() );
    }

    public void draw(){
        font.render(text, position.getX(), position.getY(), Color.orange);
    }

    // Everything below handles the button's events
    private class ButtonUpdater implements SynchronizedTask{
        @Override
        public void update() {
            int mx = CoreEngine.getMouseX(),
                my = CoreEngine.getMouseY();

            if ( (mx > position.getX() && mx < position.getX()+font.getWidth(text)) &&
                 (my > position.getY() && my < position.getY()+font.getHeight()) ){
                fireHoverEvent();

                if (Mouse.isButtonDown(0) && !isClicked){
                    fireClickEvent();
                    isClicked = true;
                }
                if (!Mouse.isButtonDown(0) && isClicked)
                    isClicked = false;
            }
        }
    }

    private List _listeners = new ArrayList();
    public synchronized void addEventListener(ButtonListener listener)  {
        _listeners.add(listener);
    }
    public synchronized void removeEventListener(ButtonListener listener)   {
        _listeners.remove(listener);
    }
    private synchronized void fireHoverEvent() {
        ButtonEvent event = new ButtonEvent(this);
        Iterator i = _listeners.iterator();
        while(i.hasNext())  {
            ((ButtonListener) i.next()).onHover(event);
        }
    }
    private synchronized void fireClickEvent() {
        ButtonEvent event = new ButtonEvent(this);
        Iterator i = _listeners.iterator();
        while(i.hasNext())  {
            ((ButtonListener) i.next()).onClick(event);
        }
    }

    public class ButtonEvent extends EventObject{
        public ButtonEvent(Object source){
            super(source);
        }
    }

}
