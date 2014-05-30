package com.piratkopia13.pixelplanet.states;

import com.piratkopia13.pixelplanet.Bullet;
import com.piratkopia13.pixelplanet.GameMap;
import com.piratkopia13.pixelplanet.Player;
import com.piratkopia13.pixelplanet.engine.core.*;
import com.piratkopia13.pixelplanet.engine.rendering.*;
import com.piratkopia13.pixelplanet.engine.rendering.Shape;
import com.piratkopia13.pixelplanet.shaders.BasicShader;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Play implements GameState {

    private Player player;
    private Shader shader;
    private Camera camera;
    private GameMap map;
    private GameFont font;
    private List<Bullet> bullets;
    private float movementSpeed = 10f;

    @Override
    public void init() {
        camera = new Camera(0, 0);
        CoreEngine.setGameCamera(camera);

        player = new Player();
        player.setShipIcon("axiom.png");
        player.setPosition(275, 175);
        player.setSpeed(movementSpeed);
        player.follow(camera);
        player.pointTowardsMouse(true);

        font = new GameFont("Arial", 20, Font.PLAIN, true);
        map = GameMap.getTestMap(50); // Initiate a new testmap with 30x30px as blocksize
        bullets = new ArrayList<>();
        shader = BasicShader.getInstance();

    }

    @Override
    public void update(){

        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();

            bullet.update();
            if (map.collidesWith(bullet.getPosition())){
                iterator.remove(); // Remove bullet
            }
        }

    }

    @Override
    public void input(){

        player.updateFromInput();

//        while (Keyboard.next()){
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
                bullets.add(new Bullet(player.getCenterPosition(), player.getRotation(), 20f));
            }
//        }

    }

    @Override
    public void render(){
        RenderUtil.clearScreen();
//        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE); // wireframe mode
        shader.bind();

        glPushMatrix();
        camera.applyTransform();

        // Drawing the bullets before the map to remove any overlays
        for (Bullet bullet : bullets){
            bullet.draw();
        }

        map.draw();

        player.draw();

        glPopMatrix();

        // HUD
        shader.unBind();
        font.draw(CoreEngine.getFPS()+"", 0, 0, Color.white);

    }

    public void cleanUp(){
        shader.dispose();
        map.dispose();
    }

    @Override
    public State getState() {
        return State.PLAY;
    }

}
