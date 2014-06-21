package com.piratkopia13.pixelplanet.engine.core;

public class Vector3f {
    public float x;
    public float y;
    public float z;

    public Vector3f(float x, float y, float z){
        this.set(x, y, z);
    }

    public void set(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public String toString(){
        return "( " + x + ", " + y + ", " + z + " )";
    }
}
