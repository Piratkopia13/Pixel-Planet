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
    private boolean isHovering = false;

    private SynchronizedTask buttonUpdater;

    public Button(String text, GameFont font, Color color, Vector2f position){
        this.text = text;
        this.font = font;
        this.color = color;
        this.position = position;

        buttonUpdater = new ButtonUpdater();
        CoreEngine.addSynchronizedTask( buttonUpdater );
    }

    public void draw(){
        font.draw(text, position.getX(), position.getY(), color);
    }

    // Everything below handles the button's events
    private class ButtonUpdater implements SynchronizedTask{
        @Override
        public void update() {
            int mx = CoreEngine.getMouseX(),
                my = CoreEngine.getMouseY();

            if ( (mx > position.getX() && mx < position.getX()+font.getWidth(text)) &&
                 (my > position.getY() && my < position.getY()+font.getHeight()) ){
                if (!isHovering)
                    fireEvent("hover");
                isHovering = true;

                if (Mouse.isButtonDown(0) && !isClicked){
                    fireEvent("mouseDown");
                    isClicked = true;
                }
                if (!Mouse.isButtonDown(0) && isClicked) {
                    fireEvent("mouseUp");
                    isClicked = false;
                }
            }else if (isHovering){
                fireEvent("unhover");
                isHovering = false;
            }
        }
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getText() {
        return text;
    }

    /**
     * Removes the button and its background task
     */
    public void remove(){
        CoreEngine.removeSynchronizedTask(buttonUpdater);
    }

    private List _listeners = new ArrayList();
    public synchronized void addEventListener(ButtonListener listener)  {
        _listeners.add(listener);
    }
    public synchronized void removeEventListener(ButtonListener listener)   {
        _listeners.remove(listener);
    }
    private synchronized void fireEvent(String eventName) {
        ButtonEvent event = new ButtonEvent(this);
        Iterator i = _listeners.iterator();
        while(i.hasNext())  {
            ButtonListener bl = ((ButtonListener) i.next());
            switch (eventName){
                case "hover":
                    bl.onHover(event);
                    break;
                case "unhover":
                    bl.onUnHover(event);
                    break;
                case "mouseUp":
                    bl.onMouseUp(event);
                    break;
                case "mouseDown":
                    bl.onMouseDown(event);
                    break;
            }
        }
    }

    public class ButtonEvent extends EventObject{
        public ButtonEvent(Object source){
            super(source);
        }
    }

}
