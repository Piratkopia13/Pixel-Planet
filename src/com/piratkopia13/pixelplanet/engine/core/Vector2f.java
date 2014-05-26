package com.piratkopia13.pixelplanet.engine.core;

import com.piratkopia13.pixelplanet.engine.rendering.Window;

public class Vector2f {

    private float x;
    private float y;

    public Vector2f(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vector2f add(Vector2f toAdd){
        return new Vector2f(x+toAdd.x, y+toAdd.y);
    }
    public Vector2f divide(int denominator){
        return new Vector2f(x/denominator, y/denominator);
    }

    public String toString(){
        return "( " + x + ", " + y + " )";
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void addToX(float x){
        this.x += x;
    }

    public void addToY(float y){
        this.y += y ;
    }

    public Vector2f inverted(){
        return new Vector2f(-x, -y);
    }

    public Vector2f toCamera(){
        Vector2f inverted = inverted();
        return new Vector2f( inverted.x + Window.getWidth()/2, inverted.y + Window.getHeight()/2 );
    }
}
