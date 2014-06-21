package com.piratkopia13.pixelplanet.engine.physics.shape;

import com.piratkopia13.pixelplanet.GameMap;
import com.piratkopia13.pixelplanet.engine.core.Vector2f;
import com.piratkopia13.pixelplanet.engine.core.Vector4f;
import com.piratkopia13.pixelplanet.engine.rendering.Mesh;
import com.piratkopia13.pixelplanet.engine.rendering.RenderUtil;
import com.piratkopia13.pixelplanet.shaders.BasicShader;
import org.newdawn.slick.opengl.Texture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;

public abstract class Shape {

    public Texture texture;
    public Mesh mesh;
    private Vector4f color = new Vector4f(1, 1, 1, 1);

    public void setRenderable(){
        this.mesh = new Mesh();
    }
    public void draw(){
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        BasicShader.setColor(color.getX(), color.getY(), color.getZ(), color.getW());
        mesh.draw();
        BasicShader.resetColor();
        glDisable(GL_BLEND);
    }
    public void dispose(){
        mesh.dispose();
    }

    public void move(Vector2f pos){}
    public void add(float x, float y){};

    public void setTexture(Texture texture){
        this.texture = texture;
    }

    public Vector4f getColor() {
        return color;
    }

    public void setColor(float r, float g, float b, float a) {
        this.color = new Vector4f(r, g, b, a);
    }

    public Vector2f getPosition(){return null;};

    public boolean intersects(Vector2f b){
        return false;
    }

}
