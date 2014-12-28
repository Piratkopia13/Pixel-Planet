package com.piratkopia13.pixelplanet.engine.core;

import com.piratkopia13.pixelplanet.engine.rendering.Window;

public class Vector2f {

    public float x;
    public float y;
    private float length = -1;

    public Vector2f(){}

    public Vector2f(float x, float y){
        this.x = x;
        this.y = y;
//        this.length = (float) Math.sqrt(x*x + y*y);
    }
    public float distanceTo(Vector2f p2){
        return (float) Math.sqrt( (p2.x - x) * (p2.x - x) + (p2.y - y) * (p2.y - y) );
    }
    public Vector2f add(Vector2f toAdd){
        return new Vector2f(x+toAdd.x, y+toAdd.y);
    }
    public Vector2f sub(Vector2f toAdd){
        return new Vector2f(x-toAdd.x, y-toAdd.y);
    }
    public Vector2f sub(float x, float y){
        return new Vector2f(this.x-x, this.y-y);
    }
    public Vector2f divide(int denominator){
        return new Vector2f(x/denominator, y/denominator);
    }
    public Vector2f mul(float factor){
        return new Vector2f(x*factor, y*factor);
    }
    public Vector2f mul(Vector2f factor){
        return new Vector2f(x*factor.getX(), y*factor.getY());
    }

    public Vector2f normalize(){
        if (length == -1)
            length = (float) Math.sqrt(x*x + y*y);
        if (length == 0) // return itself if length is zero to avoid division by zero
            return this;
        return new Vector2f(x/length, y/length);
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
    public void set(Vector2f vec){
        this.set(vec.x, vec.y);
    }
    public void set(float x, float y){
        this.x = x;
        this.y = y;
    }
    public Vector2f round(){
        this.x = Math.round(x);
        this.y = Math.round(y);
        return this;
    }

    public void addToX(float x){
        this.x += x;
    }
    public void addToY(float y){
        this.y += y ;
    }
    public void addTo(Vector2f toAdd){
        this.x = x+toAdd.x;
        this.y = y+toAdd.y;
    }

    public Vector2f inverted(){
        return new Vector2f(-x, -y);
    }

    public Vector2f toCamera(){
        Vector2f inverted = inverted();
        return new Vector2f( inverted.x + Window.getWidth()/2, inverted.y + Window.getHeight()/2 );
    }

    @Override
    public boolean equals(Object obj) {
        Vector2f vec = (Vector2f) obj;
        return ( x == vec.x && y == vec.y );
    }

    @Override
    public int hashCode() {
        return ((int)x+1)*((int)y+2);
    }
}
