package com.piratkopia13.pixelplanet.states;

import com.piratkopia13.pixelplanet.engine.*;
import com.piratkopia13.pixelplanet.engine.Window;
import com.piratkopia13.pixelplanet.shaders.BasicShader;
import org.newdawn.slick.Color;

import static org.lwjgl.opengl.GL11.*;

public class Menu implements GameState{

    private Shader shader;
    private GameFont font;
    private Image backgroundImage,
                  logoImage;

    @Override
    public void init() {
        shader = BasicShader.getInstance();
        font = new GameFont(30, true);
        backgroundImage = new Image("bg.png", 0, 0, Window.getWidth(), Window.getHeight());
        logoImage = new Image("logo.png");
        logoImage.setLocation((int)(Window.getWidth()/2-logoImage.getImageWidth()/2), 30);

    }

    @Override
    public void input(){

    }

    @Override
    public void update() {

    }

    @Override
    public void render(){
        RenderUtil.clearScreen();

        shader.bind();
        backgroundImage.draw();
        logoImage.draw();

        // HUD
        shader.unBind();
        font.render(100, 50, "Pixel Planet!", Color.yellow);
    }

    @Override
    public State getState() {
        return State.MENU;
    }

}
