package net.brainstorm_labs.spacegame;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class Menu extends BasicGameState {
	Image bg, logo;
	static org.newdawn.slick.Font titleFont, font;
	static Font awtFont, awtFontTitle;
	float angle = 0.03f;
	
	static Map<Integer, Integer[]> menuButtonPos = new HashMap<Integer, Integer[]>();
	Integer[] pos = new Integer[4];
	
	int buttonHover;
	
	boolean notClicked = true;
	boolean hovering = false, cursorChanged = false;
	
	static int menuToRender = 0;
	TextField IPinput, nameField;
	String[] menuStates = {"main", "selectPlay", "join", "host"};
	
	boolean isHosting = false;
	
	// Networking
	public static Map<Integer, String> playerList;
	Client client;
	
	// Msg box
	public static String messageBox;
	static boolean letGoAfterMsg = false;

	
	public Menu(int state) {
	}
	
	@Override
	public void init(GameContainer gc, final StateBasedGame sbg) throws SlickException {
		bg = new Image("res/bg.png");
		logo = new Image("res/logo.png");
	     
		try {
			awtFontTitle = Game.awtFontTitle.deriveFont(40f); // set font size
			titleFont = new TrueTypeFont(awtFontTitle, true);
			
			awtFont = Game.awtFont.deriveFont(30f); // set font size
			font = new TrueTypeFont(awtFont, true);
	 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		IPinput = new TextField(gc, font, 0, 0, 350, 50);
		IPinput.setBackgroundColor(new Color(0, 0, 0, 50));
		IPinput.setBorderColor(Color.transparent);
		IPinput.setTextColor(Color.white);
		IPinput.setMaxLength(253);
		IPinput.setText(Game.prefs.get(Game.pref_lastip, ""));
		IPinput.setCursorPos(IPinput.getText().length());
		nameField = new TextField(gc, font, 0, 0, 350, 50);
		nameField.setBackgroundColor(new Color(0, 0, 0, 50));
		nameField.setBorderColor(Color.transparent);
		nameField.setTextColor(Color.white);
		nameField.setMaxLength(20);
		nameField.setText(Game.prefs.get(Game.pref_playername, Game.default_username));
		nameField.setCursorPos(nameField.getText().length());
		
		// Net
		Game.network = new Network(gc, sbg);
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		int gcWidth = gc.getWidth()/2;
		
		g.drawImage(bg, -640, -320);
		g.drawImage(logo, gcWidth-logo.getWidth()/2, 100);

		String[] mainButtons = {"Start", "Settings", "Exit"};
		String[] selectButtons = {"Host", "Join", "Back"};
		String[] joinButtons = {"Enter IP", "Connect", "Back"};
		
		String[] lobbyButtons = {"Waiting for players..", "Exit lobby", "Start game"};
		int y = 330;
		int startX;
		String name;
		Color textColor = new Color(0x70cceb);
		Color hoverColor = new Color(0x22687f);
		switch (menuToRender) {
		case 0:
			// Render main menu
			
			for (int i = 0; i < mainButtons.length; i++) {
				name = mainButtons[i];
				startX = gcWidth-font.getWidth(name)/2;
				if (buttonHover == i)
					font.drawString((float)startX, (float)y, name, hoverColor);
				else if(buttonHover != i)
					font.drawString(startX, y, name, textColor);
				pos[0] = gc.getHeight()-y;
				pos[1] = startX;
				pos[2] = gc.getHeight()-y-font.getHeight(name);
				pos[3] = startX+font.getWidth(name);
				y+=70;
				menuButtonPos.put(i, pos);
				pos = new Integer[4];
			}

			break;
			
		case 1:
			// Render play selection
			menuButtonPos.clear();
			for (int i = 0; i < selectButtons.length; i++) {
				name = selectButtons[i];
				startX = gcWidth-font.getWidth(name)/2;
				if (buttonHover == i)
					font.drawString((float)startX, (float)y, name, hoverColor);
				else if(buttonHover != i)
					font.drawString(startX, y, name, textColor);
				pos[0] = gc.getHeight()-y;
				pos[1] = startX;
				pos[2] = gc.getHeight()-y-font.getHeight(name);
				pos[3] = startX+font.getWidth(name);
				y+=70;
				menuButtonPos.put(i, pos);
				pos = new Integer[4];
			}
			
			break;
		case 2:
			// Render lobby
			if (playerList == null) {
				playerList = Game.gameServer.playerList;
			}
			int forI = lobbyButtons.length-1;
			y = 290;
			if (playerList.size() == 1) {
				// Render waiting text
				name = lobbyButtons[0];
				startX = gcWidth-font.getWidth(name)/2;
				font.drawString(startX, y, name, Color.orange);
			}else{
				// Render player list and start btn
				for (Entry<Integer, String> entry : playerList.entrySet()){
					String player = entry.getValue();
					startX = gcWidth-font.getWidth(player)/2;
					font.drawString(startX, y, player, Color.green);
					y+=40;
				}
				forI = lobbyButtons.length;
			}
			
			
			// Render exit btn
			menuButtonPos.clear();
			
			if (Game.gameServer == null) {
				forI = 2;
			}
			for (int i = 1; i < forI; i++) {
				if (i == 1) {
					y = gc.getHeight()-60;
				}else if(i == 2){
					y = gc.getHeight()-120;
					textColor = Color.cyan;
				}
				name = lobbyButtons[i];
				startX = gcWidth-font.getWidth(name)/2;
				if (buttonHover == i)
					font.drawString((float)startX, (float)y, name, hoverColor);
				else if(buttonHover != i)
					font.drawString(startX, y, name, textColor);
				pos[0] = gc.getHeight()-y;
				pos[1] = startX;
				pos[2] = gc.getHeight()-y-font.getHeight(name);
				pos[3] = startX+font.getWidth(name);
				menuButtonPos.put(i, pos);
				pos = new Integer[4];
			}
			
			if (Game.gameServer != null) {
				// Start server if not started already
				if (!isHosting) {
					Game.gameServer.host(g);
					isHosting = true;
				}
			}
			
			
			break;
		case 3:
			// Render join page
			
			name = joinButtons[0];
			startX = gcWidth-font.getWidth(name)/2;
			font.drawString(startX, 290, name, Color.white);
			
			// Render input box
			
			IPinput.setLocation(gc.getWidth()/2-IPinput.getWidth()/2, 340);
			IPinput.render(gc, g);
			
			for (int i = 1; i < joinButtons.length; i++) {
				if (i == 1) {
					y = 400;
				}else if(i == 2){
					y = gc.getHeight()-60;
				}
				name = joinButtons[i];
				startX = gcWidth-font.getWidth(name)/2;
				if (buttonHover == i)
					font.drawString((float)startX, (float)y, name, hoverColor);
				else if(buttonHover != i)
					font.drawString(startX, y, name);
				pos[0] = gc.getHeight()-y;
				pos[1] = startX;
				pos[2] = gc.getHeight()-y-font.getHeight(name);
				pos[3] = startX+font.getWidth(name);
				menuButtonPos.put(i, pos);
				pos = new Integer[4];
			}
			
			break;
		case 4:
			// Render settings
			
			name = "Player name";
			startX = gc.getWidth()/2-font.getWidth(name)/2;
			font.drawString(startX, 290, name, textColor);
			nameField.setLocation(gc.getWidth()/2-nameField.getWidth()/2, 340);
			nameField.render(gc, g);
			name = "Save & back";
			y = gc.getHeight()-60;
			startX = gcWidth-font.getWidth(name)/2;
			int i = 0;
			if (buttonHover == i)
				font.drawString((float)startX, (float)y, name, hoverColor);
			else if(buttonHover != i)
				font.drawString(startX, y, name, textColor);
			pos[0] = gc.getHeight()-y;
			pos[1] = startX;
			pos[2] = gc.getHeight()-y-font.getHeight(name);
			pos[3] = startX+font.getWidth(name);
			menuButtonPos.put(i, pos);
			pos = new Integer[4];
			
			break;
		}
		
		if (messageBox != null) {
			showMessage(messageBox, gc.getGraphics(), gc);
		}
	}
	
	@SuppressWarnings("unused")
	private static TrueTypeFont changeFontSize(Font awtFont, float size){
		awtFont = awtFont.deriveFont(size); // set font size
		return( new TrueTypeFont(awtFont, true) );
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		// Rotate bg
		bg.rotate(angle);

		int mouseX = Mouse.getX();
		int mouseY = Mouse.getY();
				
		if (!cursorChanged) {
			gc.setMouseCursor("res/cursor.png", 0, 0);
			cursorChanged = true;
		}
		
		if (notClicked) {
			for (Entry<Integer, Integer[]> entry : menuButtonPos.entrySet()) {
				Integer[] val = entry.getValue();
				int curK = entry.getKey();
				
				// Check if mouse is inside a btns cords
				if ((mouseX>val[getK("startX")] && mouseX<val[getK("endX")]) && (mouseY<val[getK("startY")] && mouseY>val[getK("endY")])) {

					if (Mouse.isButtonDown(0)) {
						
						// Called when button is clicked
						
						// Do things depending on menuState
						switch (menuToRender) {
						case 0:
							// if on main menu
							if (curK == 0) {
								// Play
								menuToRender = 1;
							}else if(curK == 1){
								// Settings
								menuToRender = 4;
							}else if(curK == 2){
								// Exit
								gc.exit();
							}
							break;
						case 1:
							// if on play select
							if (curK == 0) {
								// Host a game
								//net
								try {
									if (Game.gameServer == null) {
										Game.gameServer = Game.network.new GameServer();
										menuToRender = 2;
									}
								} catch (IOException e) {
									e.printStackTrace();
								}
							}else if(curK == 1){
								// Join a game
								menuToRender = 3;
							}else{
								// Go back
								menuToRender = 0;
							}
							
							break;
						case 2:
							// if on host
							if (curK == 1) {
								// Go back, leave lobby if client								
								if (Game.gameClient != null) {
									Game.gameClient.client.sendTCP(Network.format("DISCONNECT", null, true));
								}else if(Game.gameServer != null){
									Game.gameServer.server.sendToAllTCP(Network.format("HOSTDISCONNECTED", null, true));
								}
								resetNet();
								menuToRender = 0;
							}else if (curK == 2) {
								// Start the game
								Game.gameServer.startGame();
							}
							
							break;
						case 3:
							// if on Join
							if (curK == 1) {
								// Connect
								try {
								//	String[] tmp = IPinput.getText().split(":");
								//	Game.network.PORT = Integer.parseInt(tmp[1]);
									Game.prefs.put(Game.pref_lastip, IPinput.getText());
									Game.gameClient = Game.network.new GameClient(IPinput.getText());
									client = Game.gameClient.getClient();
									startClientListening(sbg);
								} catch (Exception e) {
									e.printStackTrace();
								}
								
							}else if(curK == 2){
								// Go back
								menuToRender = 1;
							}
							
							break;
						case 4:
							// If on settings
							if (curK == 0) {
								// Save settings
								String newName = nameField.getText();
								if (newName.length() > 0) {
									Game.prefs.put(Game.pref_playername, newName);
									Game.username = newName;
								}
								menuToRender = 0;
							}
							break;
						default:
							break;
						}
						
					}
					if (buttonHover != curK) buttonHover = curK;
					if (!hovering){
						gc.setMouseCursor("res/cursor_hover.png", 0, 0);
						hovering = true;
					}
				}else if(buttonHover == curK){
					buttonHover = 5;
					if (hovering){
						gc.setMouseCursor("res/cursor.png", 0, 0);
						hovering = false;
					}
				}
					notClicked = false;
			}
		}
				
		
		if (!Mouse.isButtonDown(0) && !notClicked) {
			// Dont capture normal mouse events when messagebox is active
			if(messageBox != null){
				letGoAfterMsg = true;
			}
			else
				notClicked = true;
		}else if (letGoAfterMsg){ 
			messageBox = null;
			letGoAfterMsg = false;
		} 
	}
	private static void resetNet(){
		playerList = null;
		if (Game.gameClient != null) {
			Game.gameClient.client.stop();
		}else if(Game.gameServer != null){
			Game.gameServer.server.stop();
		}
		Game.gameClient = null;
		Game.gameServer = null;
	}
	public static void setMessage(String msg){
		messageBox = msg;
		letGoAfterMsg = false;
	}
	
	
	
	// Client lobby netcode
	private void startClientListening(final StateBasedGame sbg){
		client.addListener(new Listener() {
			@SuppressWarnings("unchecked")
			public void received (Connection connection, Object object) {

				if (object instanceof NetworkFormat) {
					NetworkFormat netObject = (NetworkFormat) object;
					String dataName = netObject.data_name;
                    Object dataObject = netObject.data;

                    switch (dataName){
                        case "PLAYERLIST":
                            if (netObject.privat) {
                                System.out.println("set players from server");
                                Play.players = (ArrayList<Player>) netObject.data;
                            }else{
                                // Enter lobby
                                System.out.println("enter client lobby");
                                playerList = (HashMap<Integer, String>) netObject.data;
                                //	playerList.add(Game.username);
                                menuToRender = 2;
                            }
                            break;
                        case "HOSTDISCONNECTED":
                            // Exit lobby
                            showHostDC();
                            break;
                        case "OK":
                            // Good to join
                            System.out.println("Good to join");
                            Network.playerID = (int)dataObject;
                            break;
                        case "ID":
                            // Set pid
                            Network.playerID = (int)dataObject;
                            System.out.println("Set id to: "+Network.playerID);
                            break;
                        case "START_GAME":
                            // Host started game
                            sbg.enterState(Game.play);
                            client.removeListener(this);
                            break;


                    }
				}   
			}
		});
	}
    public static void showHostDC(){
        setMessage("Host disconnected");
        playerList = null;
        resetNet();
        menuToRender = 1;
    }
	
	private int getK(String value) {
		return Arrays.asList(new String[]{"startY", "startX", "endY", "endX"}).indexOf(value);
	}

	@Override
	public int getID() {
		return 0;
	}
	
	private boolean showMessage(String msg, Graphics g, GameContainer gc){
		
		int msgWidth = font.getWidth(msg),
			msgHeight = font.getHeight(msg),
			boxWidth = msgWidth+100,
			boxHeight = msgHeight+60;
		g.setColor(new Color(0, 0, 0, 100));
		g.fill(new Rectangle(0, 0, gc.getWidth(), gc.getHeight()));
		g.setColor(new Color(52, 133, 133));
		g.fill(new Rectangle(gc.getWidth()/2-boxWidth/2, gc.getHeight()/2-boxHeight/2, boxWidth, boxHeight));
		font.drawString(gc.getWidth()/2-msgWidth/2, gc.getHeight()/2-msgHeight/2, msg, Color.white);
		
		
		return true;
	}
}