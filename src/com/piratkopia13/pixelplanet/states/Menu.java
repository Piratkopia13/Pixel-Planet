package com.piratkopia13.pixelplanet.states;

import com.piratkopia13.pixelplanet.engine.core.*;
import com.piratkopia13.pixelplanet.engine.rendering.*;
import com.piratkopia13.pixelplanet.shaders.BasicShader;
import org.newdawn.slick.Color;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class Menu implements GameState {

    private Shader shader;
    private GameFont font;
    private Image backgroundImage,
                  logoImage;
    List<Button> buttons;
    private Color defaultColor;
    private Color hoverColor;

    @Override
    public void init() {
        shader = BasicShader.getInstance();
        font = new GameFont(30, true);
        backgroundImage = new Image("bg.png", 0, 0, Window.getWidth(), Window.getHeight());
        logoImage = new Image("logo.png");
        logoImage.setLocation((int)(Window.getWidth()/2-logoImage.getImageWidth()/2), 100);
        buttons = new ArrayList<>();
        defaultColor = new Color(39, 242, 242);
        hoverColor = new Color(27, 133, 133);

        String[] buttonNames = { "Play", "Settings", "Exit" };
        int startY = 370;
        for (int i = 0; i < buttonNames.length; i++) {
            String name = buttonNames[i];
            buttons.add( new Button(name, font, defaultColor,
                         new Vector2f(Window.getWidth()/2-font.getWidth(name)/2, startY + (font.getHeight()+20)*i )) );
        }
        for (Button button : buttons){
            button.addEventListener(buttonListener);
        }
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
        for (Button button : buttons){
            button.draw();
        }

        // Draw version
        RenderUtil.drawGameVersion(font);
    }

    @Override
    public void cleanUp() {
        backgroundImage.dispose();
        logoImage.dispose();
        shader.dispose();
        for (Button button : buttons){
            button.remove();
        }
    }

    private ButtonListener buttonListener = new ButtonListener() {
        @Override
        public void onMouseDown(EventObject e) {
            Button src = (Button)e.getSource();
            System.out.println("mouse down");
        }

        @Override
        public void onMouseUp(EventObject e) {
            Button src = (Button)e.getSource();
            switch (src.getText()) {
                case "Exit":
                    cleanUp();
                    CoreEngine.stop();
                    break;
                case "Settings":
                    cleanUp();
                    CoreEngine.setState(State.SETTINGS);
                    break;
                case "Play":
                    cleanUp();
                    CoreEngine.setState(State.PLAY);
                    break;
            }
            System.out.println("mouse up");
        }

        @Override
        public void onHover(EventObject e) {
            Button src = (Button)e.getSource();
            src.setColor(hoverColor);
            System.out.println("hover");
        }

        @Override
        public void onUnHover(EventObject e) {
            Button src = (Button)e.getSource();
            src.setColor(defaultColor);
            System.out.println("unhover");
        }
    };

    @Override
    public State getState() {
        return State.MENU;
    }

}
