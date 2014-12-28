package net.brainstorm_labs.spacegame;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import net.brainstorm_labs.spacegame.Network.GameClient;
import net.brainstorm_labs.spacegame.Network.GameServer;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.SlickCallable;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ResourceLoader;


public class Game extends StateBasedGame{
	
	public static final String gamename = "Pixel Planet";
	public static final int menu = 0;
	public static final int play = 1;
	public static final int settings = 2;
	
	public static Network network;
	public static GameServer gameServer;
	public static GameClient gameClient;
	
	public static String default_username = "Player";
	public static String pref_playername = "playername";
	public static String pref_lastip = "lastip";
	public static String username;
	
	public static Font awtFontTitle, awtFont;
	
	public static Preferences prefs = Preferences.userNodeForPackage(net.brainstorm_labs.spacegame.Menu.class);
	
	// Sounds
	public static Sounds sounds;

    public static GameContainer pgc;

	public Game(String name) {
		super(name);
		Game.username = prefs.get(pref_playername, default_username);
		this.addState(new Menu(menu));
		this.addState(new Play(play));
		this.addState(new Settings(settings));
		
	}

	@Override
    public void initStatesList(GameContainer gc) throws SlickException {
        pgc = gc;
		this.getState(menu).init(gc, this);
		this.getState(play).init(gc, this);
		this.getState(settings).init(gc, this);
		
		this.enterState(menu);
	}
    @Override
    public boolean closeRequested(){
        if (gameClient != null){ // Client disconnected
            gameClient.client.sendTCP(Network.format("DISCONNECT", null, false));
        }else if (gameServer != null){ // Host disconnected
            gameServer.server.sendToAllTCP(Network.format("HOSTDC", null,false));
        }
        pgc.exit();
        return false;
    }
	
	public static void main(String[] args) {
        sounds = new Sounds();
        sounds.init();
		try {
            awtFontTitle = Font.createFont(Font.TRUETYPE_FONT, ResourceLoader.getResourceAsStream("res/Xolonium-Bold.ttf"));
            awtFont = Font.createFont(Font.TRUETYPE_FONT, ResourceLoader.getResourceAsStream("res/Montserrat-Regular.ttf"));

			AppGameContainer appgc;
			appgc = new AppGameContainer(new Game(gamename));
			appgc.setDisplayMode(1280, 720, false);
        	appgc.setTargetFrameRate(60);
			appgc.setShowFPS(true);
			appgc.setAlwaysRender(true);
			appgc.start();
		} catch (SlickException ex){
			Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static Point2D.Float offsetToWorld(Point2D.Float offset, GameContainer gc){
		return new Point2D.Float(gc.getWidth()/2 - offset.x, gc.getHeight()/2 - offset.y);
	}
	public static Point2D.Float worldToOffset(Point2D.Float world, GameContainer gc){
		return new Point2D.Float(-(world.x - gc.getWidth()/2), -(world.y - gc.getHeight()/2));
	}

}
