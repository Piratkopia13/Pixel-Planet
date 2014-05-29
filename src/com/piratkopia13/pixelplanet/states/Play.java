package com.piratkopia13.pixelplanet.states;

import com.piratkopia13.pixelplanet.Player;
import com.piratkopia13.pixelplanet.engine.core.CoreEngine;
import com.piratkopia13.pixelplanet.engine.core.GameState;
import com.piratkopia13.pixelplanet.engine.core.Vector2f;
import com.piratkopia13.pixelplanet.engine.rendering.Camera;
import com.piratkopia13.pixelplanet.engine.rendering.RenderUtil;
import com.piratkopia13.pixelplanet.engine.rendering.Shader;
import com.piratkopia13.pixelplanet.engine.rendering.Shape;
import com.piratkopia13.pixelplanet.shaders.BasicShader;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import static org.lwjgl.opengl.GL11.*;

public class Play implements GameState {

    private Player player;
    private Shader shader;
    private Camera camera;
    private Shape box;

    private float movementSpeed = 0.2f;

    @Override
    public void init() {
        camera = new Camera(0, 0);
        CoreEngine.setGameCamera(camera);
        player = new Player();
        player.setShipIcon("axiom.png");
        player.setPosition(new Vector2f(-player.getWidth() / 2, -player.getHeight() / 2));  // Set the position to the ship's center
        player.follow(camera);
        player.pointTowardsMouse(true);
        box = new Shape();
        box.setAsSquare(100, 100, 200, 200);
        shader = BasicShader.getInstance();
    }

    @Override
    public void update(){

    }

    @Override
    public void input(){

        if (Keyboard.isKeyDown(Keyboard.KEY_W)){
            player.move(0, -movementSpeed);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)){
            player.move(0, movementSpeed);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)){
            player.move(-movementSpeed, 0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)){
            player.move(movementSpeed, 0);
        }

    }

    @Override
    public void render(){
        RenderUtil.clearScreen();
        shader.bind();

        glPushMatrix();
        camera.applyTransform();
        BasicShader.setColor(1, 0, 0, 1);
        box.draw();
        BasicShader.resetColor();
        player.draw();

        glPopMatrix();

    }

    public void cleanUp(){
        shader.dispose();
        box.dispose();
    }

    @Override
    public State getState() {
        return State.PLAY;
    }

}
