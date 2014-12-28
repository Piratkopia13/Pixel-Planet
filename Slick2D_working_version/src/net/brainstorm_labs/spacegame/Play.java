package net.brainstorm_labs.spacegame;
import java.awt.Font;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ResourceLoader;


public class Play extends BasicGameState {

	public static Image shipImage;
	Image stone_tile, border_tile,
	parallax1, parallax2, parallax3,
    shopImage;
	Shape playerBounding;
	int speed;
	boolean mapLoaded = false;
	
	// Collision vars
	ArrayList<Shape> collisionPoints = new ArrayList<Shape>();
	public static HashMap<Integer, Integer> blockList = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> convertIndex = new HashMap<Integer, Integer>();
	int blockHP = 10;
	Point2D.Float lastCollision = new Point2D.Float();
	
	public static Point2D.Float playerStart = new Point2D.Float();
	Point2D.Float playerPos = new Point2D.Float();

	
	// Map vars
    Vector2f mapSize = new Vector2f();
	char tmp;
	int mapBlockX = 0, mapBlockY = 0, blockSize = 50;
	int currMapX, currMapY;
	public static Point2D.Float offset = new Point2D.Float(0, 0);
	String[] tmpMap = new String[3];
	public static ArrayList<String[]> renderBlocks;
	Boolean playerSet = false;
	int spawnNum = 0;
    ArrayList<Shop.Location> shopLocs = new ArrayList<>();
    int insideShop = 0;
    GUI gui;
    Shop shop;
    int textID;
    public static boolean renderShop = false;

    int gcW, gcH;
	
	// Cannon vars
	public static CopyOnWriteArrayList<Bullet> bullets = new CopyOnWriteArrayList<Bullet>();
	
	public static Rectangle bulletShape;
	float bulletHeight = 5,
		  bulletWidth = 5;
	int firingSpeed = 10; // Bullets per second
	int bulletFrame = 0;
    int moneyPerBlock = 5;
    int bulletDamage = 2;
	
	// Player vars
	public static ArrayList<Player> players = new ArrayList<Player>();
	// Random vars
	Float lastAngle = 0f;
	static org.newdawn.slick.Font titleFont, font, HUDfont;
	static Font awtFont, awtFontTitle;
	int lastHP = 100;
    int bltRegen = 1; // Bullets to regen per second while inside shop
    int bltRegenFrame = 0;
    static Chat chat;

    // Inventory vars
    Inventory inventory;

    String[] deathMsgs = {"Yep, that happened", ":/", "Do you see the light?", "Partey time"};

    // Explusion vars
    int expSize;
    Animation explosionAnimation;
    boolean renderExp = false;
    Point2D.Float expPos = new Point2D.Float();

    // rndm vars
    public static boolean shopSet = false;

	public Play(int state) {}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        gc.setVSync(true);
        gc.setClearEachFrame(true);
        // Set up font
		try {
			awtFontTitle = Game.awtFontTitle.deriveFont(20f); // set font size
			titleFont = new TrueTypeFont(awtFontTitle, true);
			
			awtFont = Game.awtFont.deriveFont(15f); // set font size
			font = new TrueTypeFont(awtFont, true);
			awtFont = Game.awtFont.deriveFont(30f);
			HUDfont = new TrueTypeFont(awtFont, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		shipImage = new Image("res/skepp.png");
		stone_tile = new Image("res/tile_stone.png");
		border_tile = new Image("res/tile_border.png");
		parallax1 = new Image("res/Parallax100.png");
		parallax2 = new Image("res/Parallax80.png");
		parallax3 = new Image("res/Parallax60.png");
        shopImage = new Image("res/shop.png");

        // Explosion

        Image img = new Image("res/explosion.png");
        img.setFilter(Image.FILTER_NEAREST);
        expSize = shipImage.getHeight()+50;
        img = img.getScaledCopy(expSize / 12);
        Image[] imgs = { img.getSubImage(0,0,expSize,expSize),
                         img.getSubImage(expSize,0,expSize,expSize),
                         img.getSubImage(expSize*2,0,expSize,expSize),
                         img.getSubImage(expSize*3,0,expSize,expSize) };

        explosionAnimation = new Animation(imgs, 340);
        explosionAnimation.setPingPong(true);
        explosionAnimation.setLooping(false);
		
		// Set startlocation of player
		playerStart.setLocation(gc.getWidth() / 2 - shipImage.getWidth() / 2, gc.getHeight() / 2 - shipImage.getHeight() / 2);
		playerPos.setLocation(playerStart.x, playerStart.y);
		playerBounding = new Rectangle(playerPos.x, playerPos.y, shipImage.getWidth(), shipImage.getHeight());
		
		// Bulltet preffs
		bulletShape = new Rectangle(gc.getWidth()/2, gc.getHeight()/2, bulletWidth, bulletHeight);

        gui = new GUI(gc.getGraphics(), gc);
        shop = new Shop();

        gcW = gc.getWidth();
        gcH = gc.getHeight();

        // set Inventory
        inventory = new Inventory(gc.getGraphics());

		
		if (!mapLoaded) {
			loadMap();
		}

        chat = new Chat();

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		// Render parallax background

		int renderX = 0, renderY;
		while (renderX < gc.getWidth()) {
			renderY = 0;
			while (renderY < gc.getHeight()) {
				parallax1.draw(renderX, renderY);
				renderY+=parallax1.getHeight();
			}
			renderX+=parallax1.getWidth();
		}
		
		renderX = (int)offset.x/8 - parallax2.getWidth();
		while (renderX < gc.getWidth()) {
            renderY = (int)offset.y/8 - parallax2.getHeight();
			while (renderY < gc.getHeight()) {
				parallax2.draw(renderX, renderY);
				renderY+=parallax2.getHeight();
			}
			renderX+=parallax2.getWidth();
		}
		
		renderX = (int)offset.x/4 - parallax3.getWidth();
		while (renderX < gc.getWidth()) {
			renderY = (int)offset.y/4 - parallax3.getHeight();
			while (renderY < gc.getHeight()) {
				parallax3.draw(renderX, renderY);
				renderY+=parallax3.getHeight();
			}
			renderX+=parallax3.getWidth();
		}
		float scale = 1f;
		g.translate(gc.getWidth()/2-gc.getWidth()*scale/2, gc.getHeight()/2-gc.getHeight()*scale/2);
		g.scale(scale, scale);
		
        // Map
		renderMap(g, gc);
		// Bulletz
		g.setColor(Color.green);
        if (!bullets.isEmpty()) {
			for (Bullet bullet : bullets) {
				if (bullet.isAlive()) {
					bullet.update();
                    bullet.shape.setLocation(bullet.x() + offset.x, bullet.y() + offset.y);
                    if (!checkBulletCollide(bullet)) {
                        g.fill(bullet.shape);
                    }
					if (players.get(0).bounding != null) {
						checkPlayerCollide(bullet, gc);
					}
				}else bullets.remove(bullet);
				
			}
		}else{
            bullets.clear();
        }

		// draw players
		for (Player player : players) {
            if (player.isAlive){
                player.icon.setRotation(player.angle);
                if (player.id == Network.playerID){
                    player.bounding = new Rectangle(playerStart.x, playerStart.y, player.icon.getWidth(), player.icon.getHeight());
                    g.drawImage(player.icon, playerStart.x, playerStart.y);
                }
                else{
                    float posX = Game.offsetToWorld(player.pos, gc).x+offset.x-player.icon.getWidth()/2,
                          posY = Game.offsetToWorld(player.pos, gc).y+offset.y-player.icon.getHeight()/2;
                    player.bounding = new Rectangle(posX, posY, player.icon.getWidth(), player.icon.getHeight());
                    player.icon.draw(posX, posY);
                    // Render player name under player
                    g.setColor(Color.cyan);
                    font.drawString(Game.offsetToWorld(player.pos, gc).x+offset.x-font.getWidth(player.name)/2, posY+70, player.name, Color.cyan);
                    // Render HP above player
                    String hp = player.HP+"/100";
                    font.drawString(Game.offsetToWorld(player.pos, gc).x+offset.x-font.getWidth(hp)/2, posY-20, hp, Color.white);
                }
            }else if(!player.ignore){
                handlePlayerDeath(player, gc);
            }
		}
        // Render explosion
        if (renderExp){
            explosionAnimation.draw(expPos.x+offset.x, expPos.y+offset.y);
        }
        if (explosionAnimation.isStopped()) renderExp = false;

        g.resetTransform();
        // Render HUD
		int chp = players.get(Network.playerID).HP;
		Color color = Color.white;
		if (lastHP > chp) {
			color = Color.red;
		}
        String hp;
        int blts = players.get(Network.playerID).bulletCount;
        String bltStr;
        if (chp <= 0){
            hp = "You are dead";
            color = Color.red;
            bltStr = "Spectating";
        }else{
            hp = "HP: "+chp+"/100";
            bltStr = "Bullets: "+blts;
        }
        HUDfont.drawString(gc.getWidth()-HUDfont.getWidth(hp), 0, hp, color);
        lastHP = chp;
        HUDfont.drawString(gc.getWidth()/2-HUDfont.getWidth(bltStr)/2, 0, bltStr);
        // Money
        String str = "$"+players.get(Network.playerID).money;
        HUDfont.drawString(gc.getWidth()-HUDfont.getWidth(str), gc.getHeight()-HUDfont.getHeight(str), str, Color.yellow);
        // Open shop text
        if (insideShop != -1 && !renderShop){
            str = "Press F to enter shop";
            HUDfont.drawString(gc.getWidth()/2-HUDfont.getWidth(str)/2, gc.getHeight()-200, str, Color.white);
        }

        if (renderShop){
            if (!shopSet){
                shop.set(insideShop, gui);
                shopSet = true;
            }
            shop.render(g);
        }

        // Render DeathMessages
        DeathMessages.render(g, font, gc.getWidth());

        // Render inventory
        inventory.drawHotbar(gc.getWidth(), gc.getHeight());

        g.setColor(Color.white);
        // Debug
		g.drawString("X: "+offset.x, 0, 80);
		g.drawString("Y: "+offset.y, 0, 100);
		g.drawString("X2: "+players.get(0).pos.x, 0, 120);
		g.drawString("Y2: "+players.get(0).pos.y, 0, 140);
        g.drawString(Network.playerID+"", 0, gc.getHeight()-20);

		
	}
    public static void closeShop(){
        renderShop = false;
        shopSet = false;
    }
	
	int frame = 1;
	int targetFPS = 60;
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

       runUpdate(gc, sbg, delta);

	/*	int currFPS = gc.getFPS();
		if (currFPS >= targetFPS) {
			if (frame >= currFPS/targetFPS) {
				// Skip this frame
				frame = 1;
			}else {
				runUpdate(gc, sbg, delta);
				frame++;
			}
		}
	/*	}else {
			if (currFPS > 0) {
				for (int i = 0; i < targetFPS/currFPS; i++) {
					runUpdate(gc, sbg, delta);
				}
			}
		}
		*/
		
		
	}
	private void runUpdate(GameContainer gc, StateBasedGame sbg, int delta){

        // Update inventory
        inventory.update();

        explosionAnimation.update(delta); // Sync animation to speed

		// Point ship at mouse
		float xDistance = Mouse.getX() - playerPos.x - players.get(Network.playerID).icon.getWidth()/2;
		float yDistance = Mouse.getY() - playerPos.y - players.get(Network.playerID).icon.getHeight()/2;
		float angleRad = (float) (-Math.atan2(yDistance, xDistance));
		float angle = (float)Math.toDegrees(angleRad)+90;
		players.get(Network.playerID).angle = angle;
		
		if (angle != lastAngle) {
			if(Game.gameClient != null)
				Game.gameClient.sendAngle(angle);
			else
				Game.gameServer.sendAngle(angle);
		}
		lastAngle = angle;
		
		// Rest of stuff

		Input key = gc.getInput();

        if (key.isKeyDown(Input.KEY_F) && insideShop != -1){
            System.out.println("render shop");
            renderShop = true;
        }else if (( insideShop == -1 && renderShop ) || key.isKeyDown(Input.KEY_ESCAPE) ){
            closeShop();
        }

        // Give player 1 blt/s if inside spawn shop
        Player playr = players.get(Network.playerID);
        if (insideShop == 0) {
            if(bltRegenFrame > gc.getFPS()/bltRegen){
                playr.bulletCount+=1;
                bltRegenFrame = 0;
            }else bltRegenFrame++;
        }else{
            if (bltRegenFrame >= gc.getFPS()/bltRegen) {
                bltRegenFrame = gc.getFPS()/bltRegen;
            }else bltRegenFrame++;
        }

			speed = 10;
			
			// Handle input and move el cube
			if (key.isKeyDown(Input.KEY_LSHIFT)) {
				speed = 1;
			}if (key.isKeyDown(Input.KEY_UP) || key.isKeyDown(Input.KEY_W)){
				playerBounding.setY(playerStart.y-speed);
				if (!checkCollide()) {
				//	System.out.println("up we go! " + offset.y);
					offset.y+=speed;
				}else{
					// Move up the remaining pixels
					offset.y+=playerStart.y - (lastCollision.y+blockSize) -1;
				}
				playerBounding.setY(playerStart.y);
				sendPos();
			}else
			 if (key.isKeyDown(Input.KEY_DOWN) || key.isKeyDown(Input.KEY_S)) {
				playerBounding.setY(playerStart.y+speed);
				if (!checkCollide()) {
				//	System.out.println("Down vi go");
					offset.y-=speed;
				}else{
					// Move down the remaining pixels
					offset.y-=(lastCollision.y) - (playerStart.y+players.get(Network.playerID).icon.getHeight()) -1;
				}
				playerBounding.setY(playerStart.y);
				sendPos();
			}if (key.isKeyDown(Input.KEY_LEFT) || key.isKeyDown(Input.KEY_A)) {
				playerBounding.setX(playerStart.x-speed);
				if (!checkCollide()) {
				//	System.out.println("Left we gow");
					offset.x+=speed;
				}else{
					// Move left the remaining pixels
					offset.x-= (lastCollision.x+blockSize) - playerStart.x +1;
				}
				playerBounding.setX(playerStart.x);
				sendPos();
			}else
			 if (key.isKeyDown(Input.KEY_RIGHT) || key.isKeyDown(Input.KEY_D)) {
				playerBounding.setX(playerStart.x+speed);
				if (!checkCollide()) {
				//	System.out.println("URR DURR");
					offset.x-=speed;
				}else{
				// Move right the remaining pixels
					offset.x-= lastCollision.x - (playerStart.x+players.get(Network.playerID).icon.getWidth()) -1;
				}
				playerBounding.setX(playerStart.x);
				sendPos();
			}

			
			// Fire on will!
			if (Mouse.isButtonDown(0) && players.get(Network.playerID).bulletCount > 0 && players.get(Network.playerID).isAlive && !renderShop) {
				if(bulletFrame > gc.getFPS()/firingSpeed){
					// Fire ze missile
					Bullet newBullet = new Bullet(new Point2D.Float((gcW/2-offset.x)-bulletWidth/2, (gcH/2-offset.y)-bulletHeight/2),
							new Point2D.Float((float)Math.cos(angleRad), (float)Math.sin(angleRad)), 20f, bulletShape, Network.playerID);
					bullets.add(newBullet);
                    Game.sounds.laser.stop();
					Game.sounds.laser.play(1, 0.3f);
					if (Game.gameClient != null) {
						Game.gameClient.sendBullet(newBullet);
					}else{
						Game.gameServer.sendBullet(newBullet);
					}
					players.get(Network.playerID).bulletCount--;
					bulletFrame = 0;
				}else bulletFrame++;
			}else{
				if (bulletFrame >= gc.getFPS()/firingSpeed) {
					bulletFrame = gc.getFPS()/firingSpeed;
				}else bulletFrame++;
			}
			
		
	}
	private void sendPos(){
		if (Game.gameClient != null) {
			Game.gameClient.sendPos(offset);
		}else{
			Game.gameServer.sendPos(offset);
		}
	}

	@Override
	public int getID() {
		return 1;
	}
	
	private boolean checkCollide() {
	    if (players.get(Network.playerID).isAlive){
            for(Shop.Location locs : shopLocs){
                if (locs.shape.intersects(playerBounding)){
                    insideShop = locs.typeID;
                    break;
                }else
                    insideShop = -1;
            }
            for(Shape shape : collisionPoints){
                if (shape.intersects(playerBounding)) {
                    lastCollision.setLocation(shape.getX(), shape.getY());

                    return true;
                }
            }
        }
		
		
		return false;
	}
	
	private boolean checkPlayerCollide(Bullet bullet, GameContainer gc) {
		for(Player player : players){
			if(player.bounding.intersects(bullet.shape) && bullet.owner != player.id && player.isAlive){
				bullet.markDead();
				if (Game.gameServer != null) {
					// Remove HP from player
					player.updateHP(player.HP-1);
					Game.gameServer.updatePlayerHP(player.id, player.HP);
                    if (player.HP >= 0){
                        player.killedBy = players.get(bullet.owner).name;
                        Game.gameServer.setPlayerKiller(player.id, player.killedBy);
                    }
				}
				
				return true;
			}
		}
		return false;
	}
	
	private boolean checkBulletCollide(Bullet bullet) {
		int index = 0;
        boolean markBullet = false;
        ArrayList<int[]> markedBlts = new ArrayList<>();
		for (Shape shape : collisionPoints){
			if(shape.intersects(bullet.shape)){
			    markBullet = true;
                if (convertIndex.containsKey(index) && Game.gameServer != null) {
					int convertedIndex = convertIndex.get(index);
					int currHP = blockList.get(convertIndex.get(index));
                    markedBlts.add(new int[]{convertIndex.get(index), currHP});

					// remove block if hp is 0
					if (currHP <= 1) {
						// Replace with nothingness
						String[] currRender = renderBlocks.get(convertedIndex);
						currRender[0] = " ";
						renderBlocks.set(convertedIndex, currRender);
						// Remove on all clients
						Game.gameServer.removeBlock(convertedIndex, currRender);

						if (bullet.owner == 0) {
                            // Give player money
                            players.get(0).money+=moneyPerBlock;
                        }else {
                            // Send data to net
                            Map<String, Integer> data = new HashMap<>();
                            data.put("owner", bullet.owner);
                            data.put("money", moneyPerBlock);
                            Game.gameServer.givePlayer(data);
                        }


					}else{
						System.out.println("HP left: "+ (currHP-2));
					}
				}
			}
			index++;
		}
        if (markBullet){
            // blt dmg is 2hp, 1hp if 2 blocks were hit
            int dmg = (markedBlts.size() > 1) ? bulletDamage/2 : bulletDamage;
            for (int[] marked : markedBlts){
                blockList.put(marked[0], marked[1]-dmg);
                // Send to other clients
                Game.gameServer.sendBlockHP(new int[]{marked[0], marked[1]-dmg});
            }
            bullet.markDead();
            return true;
        }
		return false;
	}

    public void handlePlayerDeath(Player player, GameContainer gc){
        // Calculate and start explosion animation
        float posX = Game.offsetToWorld(player.pos, gc).x+offset.x-expSize/2,
                posY = Game.offsetToWorld(player.pos, gc).y+offset.y-expSize/2;
        expPos.setLocation(posX-offset.x, posY-offset.y);
        renderExp = true;
        explosionAnimation.restart();

        // Show death msg
        DeathMessages.addMsg(player.killedBy+ " zapped " +player.name);

        if (player.id == Network.playerID){ // I'm dead
            Game.sounds.randomExplosion().playAt(0,0,0);
            // Show dead screen naww
            
        }else{ // You're dead
            posX = Game.offsetToWorld(player.pos, gc).x+offset.x;
            posY = Game.offsetToWorld(player.pos, gc).y+offset.y;
            Game.sounds.randomExplosion().playAt((posX-playerStart.x/2-shipImage.getWidth()/2)/300, (posY-playerStart.y-shipImage.getHeight()/2)/300, 0);
        }
        player.ignore = true;
    }
	
	public void renderMap(Graphics g, GameContainer gc){
		String[] asd;
		
		int index  = 0,
			index2 = 0;
		convertIndex.clear();
        collisionPoints.clear();
        Shape rect;

        Iterator<String[]> it = renderBlocks.iterator();
		while (it.hasNext()) {
			asd = it.next();

            currMapX = Integer.parseInt(asd[1]);
            currMapY = Integer.parseInt(asd[2]);

            if (Game.gameServer != null){
                if (asd[0].equals("0") || asd[0].equals("D")){
                    collisionPoints.add(new Rectangle(currMapX+offset.x, currMapY+offset.y, blockSize, blockSize));
                }
                if (asd[0].equals("0")) {
                    convertIndex.put(index2, index);
                    if (!blockList.containsKey(index)) {
                        blockList.put(index, blockHP);
                    }
                    index2++;
                }else if(asd[0].equals("D"))
                    index2++;
                index++;
            }
            if (asd[0].equals("P") && !playerSet) {
                if (players.size() > spawnNum) {
                    // set spawn
                    System.out.println("ran spawn thing "+players.size()+" "+spawnNum);
                    players.get(spawnNum).pos = Game.offsetToWorld(new Point2D.Float(currMapX, currMapY), gc);
                    if (Network.playerID == spawnNum) {
                        offset.setLocation(Game.worldToOffset(new Point2D.Float(currMapX, currMapY), gc));
                    }
                }else
                    playerSet = true;
                spawnNum++;
            }

            rect = new Rectangle(currMapX+offset.x, currMapY+offset.y, blockSize, blockSize);
            if (Game.gameServer == null){
                if (asd[0].equals("0") || asd[0].equals("D"))
                    collisionPoints.add(rect);
            }
            if (currMapX > -offset.x-blockSize && currMapX < -offset.x+gc.getWidth()  &&  currMapY > -offset.y-blockSize && currMapY < -offset.y+gc.getHeight()){ // Only render on screen
                // Dont blur pixels
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
                // Draw the thing
                if (asd[0].equals("0")) {
                    g.setColor(Color.gray);
                    g.texture(rect, stone_tile, true);
                }else if(asd[0].equals("D")){
                    g.setColor(Color.orange);
                    g.texture(rect, border_tile, true);
                }
          }
		}

        // Render shops
        Point2D.Float pos;
        Shape shape = null;
        int typeID;
        g.setColor(new Color(186, 39, 123, 70));
        for (Shop.Location loc : shopLocs){
            pos = loc.pos;
            typeID = loc.typeID;
            if (typeID == 0) {
                shape = new Circle(pos.x+offset.x, pos.y+offset.y, 100, 6);
            }
            else if(typeID == 1) {
                shape = new Circle(pos.x+offset.x, pos.y+offset.y, 200, 6);
            }
            g.fill(shape);
            shopImage.draw(pos.x + offset.x - blockSize / 2, pos.y + offset.y - blockSize / 2, shopImage.getWidth(), shopImage.getHeight());
            loc.shape = shape;
        }

    }
	
	public void loadMap(){
		renderBlocks = new ArrayList<String[]>();
		BufferedReader br = null;
		try {
		br = new BufferedReader(new InputStreamReader(ResourceLoader.getResourceAsStream("maps/benv1")));
	        String line = br.readLine();

	        while (line != null) {
	        	
	        	// add block to render queue
	        	for (int j = 0; j < line.length(); j++) {
	        		tmpMap = new String[3];
					tmp = line.charAt(j);
					tmpMap[0] = String.valueOf(tmp);
					tmpMap[1] = String.valueOf(mapBlockX);
					tmpMap[2] = String.valueOf(mapBlockY);
					renderBlocks.add(tmpMap);
                    if (tmpMap[0].equals("P")){ // Add shop location to spawnpoints
                        shopLocs.add( new Shop.Location(new Point2D.Float(mapBlockX+blockSize/2, mapBlockY+blockSize/2), 0) );
                    }else if (tmpMap[0].equals("S")){ // Add large shops
                        shopLocs.add( new Shop.Location(new Point2D.Float(mapBlockX+blockSize/2, mapBlockY+blockSize/2), 1) );
                    }
					// Increase block x pos
					mapBlockX += blockSize;
				 //	System.out.print(" - " + tmpMap[0]);
				}
				// New row in map, reset x
                mapSize.x = (mapBlockX > mapSize.x) ? mapBlockX : mapSize.x;
				mapBlockX = 0;
				mapBlockY += blockSize;

                mapSize.y = mapBlockY;
				
	            line = br.readLine();
	        }
	        mapLoaded = true;
	    } catch (IOException e) {
			e.printStackTrace();
		} finally {
	        try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	}
}
