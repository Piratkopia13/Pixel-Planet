package com.piratkopia13.pixelplanet.engine.physics.shape;

import com.piratkopia13.pixelplanet.engine.core.Vector4f;
import com.piratkopia13.pixelplanet.engine.rendering.Mesh;
import com.piratkopia13.pixelplanet.shaders.BasicShader;
import org.newdawn.slick.opengl.Texture;

public abstract class Shape {

    public Texture texture;
    public Mesh mesh;
    private Vector4f color;

    public void setRenderable(){
        this.mesh = new Mesh();
    }
    public void draw(){
        BasicShader.setColor(color.getX(), color.getY(), color.getZ(), color.getW());
        mesh.draw();
        BasicShader.resetColor();
    }
    public void dispose(){
        mesh.dispose();
    }

    public void setTexture(Texture texture){
        this.texture = texture;
    }

    public Vector4f getColor() {
        return color;
    }

    public void setColor(float r, float g, float b, float a) {
        this.color = new Vector4f(r, g, b, a);
    }
}
