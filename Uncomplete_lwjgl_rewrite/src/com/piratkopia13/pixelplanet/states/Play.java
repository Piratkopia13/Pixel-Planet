package com.piratkopia13.pixelplanet.states;

import com.piratkopia13.pixelplanet.Bullet;
import com.piratkopia13.pixelplanet.GameMap;
import com.piratkopia13.pixelplanet.GameVar;
import com.piratkopia13.pixelplanet.Player;
import com.piratkopia13.pixelplanet.engine.core.*;
import com.piratkopia13.pixelplanet.engine.physics.particle.ParticleEmitter;
import com.piratkopia13.pixelplanet.engine.rendering.*;
import com.piratkopia13.pixelplanet.shaders.BasicShader;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import java.awt.*;
import java.util.*;
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

    public static Map<Integer, Player> otherPlayers;
    private static List<Integer> joiningPlayers = new ArrayList<>();

    @Override
    public void init() {
        camera = new Camera(0, 0);
//        camera.setScale(0.5f);
        Game.setGameCamera(camera);
        otherPlayers = new HashMap<>();

        player = new Player();
        player.setShipIcon("axiom.png");
        player.setPosition(275, 175);
        player.setSpeed(movementSpeed);
        player.follow(camera);
        player.pointTowardsMouse(true);

        font = new GameFont("Arial", 20, Font.PLAIN, true);
        map = GameMap.getTestMap(50); // Initiate a new testmap with 50x50px as blocksize
        player.setMap(map);
        GameVar.setMap(map);
        GameVar.setPlayer(player);
        bullets = new ArrayList<>();
        shader = BasicShader.getInstance();

    }

    @Override
    public void update(){

        for (int pid : joiningPlayers){
            System.out.println("added player");
            otherPlayers.put(pid, new Player());
        }
        joiningPlayers.clear();

        System.out.println(otherPlayers.size());

        GameVar.setMap(map);

    }

    @Override
    public void input(){

        player.updateFromInput();

        while (Keyboard.next()){
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
                bullets.add(new Bullet(player.getCenterPosition(), player.getRotation(), 20f, player.getVelocity()));
            }
        }

    }

    @Override
    public void render(){
        RenderUtil.clearScreen();
//        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE); // wireframe mode
        shader.bind();

        glPushMatrix();
        camera.applyTransform();

//        shader.unBind();
        map.draw();
//        shader.bind();

        // Drawing the bullets before the map to remove any overlays
        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            if (bullet.isDead()){
                bullet.destroy();
                iterator.remove();
            }else
                bullet.draw();
        }

//        glPointSize(5);
//        BasicShader.setColor(1, 0, 0, 1);
//        glBegin(GL_POINTS);
//        glVertex2f(GameVar.bltCol.x, GameVar.bltCol.y);
//        glEnd();
//        BasicShader.resetColor();

        // Render all enemies
        for (Player p : otherPlayers.values())
            p.draw();

        player.draw();
        ParticleEmitter.updateAndRenderSmallExplosions();

        glPopMatrix();

        // HUD
        shader.unBind();
        font.draw(Game.getFPS()+"", 0, 0, Color.white);

    }

    public void cleanUp(){
        shader.dispose();
        map.dispose();
    }

    public static void spawnPlayer(int id){
        joiningPlayers.add(id);
    }

    @Override
    public State getState() {
        return State.PLAY;
    }

}
