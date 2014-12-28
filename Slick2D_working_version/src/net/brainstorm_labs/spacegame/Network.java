package net.brainstorm_labs.spacegame;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.state.StateBasedGame;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;


public class Network {
	
	// GAME GLOBAL VARIABLES
	public static int playerID;
	
	// CLASS GLOBAL VARIABLES
	public int PORT = 2645;

	private StateBasedGame sbg;
	
	public Network(GameContainer gc, StateBasedGame sbg) {
//		this.gc = gc;
		this.sbg = sbg;
	}
	
	private void registerClasses(Kryo kryo){
		kryo.register(float[].class);
		kryo.register(int[].class);
		kryo.register(HashMap.class);
		kryo.register(String[].class);
		kryo.register(String[][].class);
		kryo.register(ArrayList.class);
		kryo.register(Player.class);
		kryo.register(Image.class);
		kryo.register(byte[].class);
		kryo.register(TextureImpl.class);
		kryo.register(Point2D.Float.class);
		kryo.register(NetworkFormat.class);
		kryo.register(List.class);
        kryo.register(Chat.class);
	}
	
	/*
	 *  CLIENT
	 */
		
	public class GameClient {

		Client client = new Client();
		
		public GameClient(String serverAddress) throws Exception{
			
			client.start();
			client.connect(5000, serverAddress, PORT);
			Kryo kryo = client.getKryo();
			registerClasses(kryo);
			
			client.addListener(listener);
			
			// Try to join
			client.sendTCP(format("JOIN_GAME", Game.username, true));
			
		}
		
		public Client getClient(){
			return this.client;
		}
		
		public void sendData(NetworkFormat data){
			this.client.sendTCP(data);
		}
		public void sendPos(Point2D pos){
			sendData(format("POS", pos, false));
		}
		public void sendAngle(Float angle){
			sendData(format("ANGLE", angle, false));
		}
		public void sendBullet(Bullet bullet){
			float[] data = {
					bullet.point.x,
					bullet.point.y,
					bullet.trajectory.x,
					bullet.trajectory.y
			};
			sendData(format("BULLET", data, false));
		}
        public void sendBoughtItem(String name, int amount, int price){
            HashMap<String, String> data = new HashMap<>();
            data.put("name", name);
            data.put("amount", String.valueOf(amount));
            data.put("price", String.valueOf(price));
            sendData(format("BOUGHT", data, false));
        }
        public void sendChatMsg(String msg){
            sendData(format("ChatMsg", msg, true));
        }
	}
	
	
	
	
	/*
	 *  SERVER
	 */
	
	public class GameServer {
		
		public Map<Integer, String> playerList = new HashMap<Integer, String>();
		private Map<Integer, Player> tmpPlayers = new HashMap<Integer, Player>();
		Server server = new Server();
		
		public GameServer() throws IOException{
			this.server.start();
			this.server.bind(PORT);
			Kryo kryo = server.getKryo();
			registerClasses(kryo);
			
			Network.playerID = 0;
			// Add host to players
			Play.players.add(new Player(new Point2D.Float(0,0), Play.shipImage, 0, Game.prefs.get(Game.pref_playername, Game.default_username)));
			// Add host to lobbylist
			playerList.put(0, "[host] " + Game.username);
						
		}
		
		// Host
		public void host(Graphics g){
			
			server.addListener(listener);	
				
		}
		// Start hosted game
		public void startGame(){
			
			// Set player data
			Connection[] c = server.getConnections();
			int i = 1;
			int j = c.length-1;

			for(Entry<Integer, Player> entry : tmpPlayers.entrySet()) {
			    Player player = entry.getValue();
				player.id = i;
				Play.players.add(player);
				while (!c[j].isConnected()) { j--; }
				c[j].sendTCP(format("ID", i, true));
                i++;
				j--;
			}
			// Send player data
			server.sendToAllTCP(format("PLAYERS", Play.players, true));
			server.sendToAllTCP(format("START_GAME", null, true));
			sbg.enterState(Game.play);
			
		}
		public void sendData(NetworkFormat data){
			this.server.sendToAllTCP(data);
		}
		public void sendPos(Point2D pos){
			sendData(format("POS", pos, false));
		}
		public void sendAngle(Float angle){
			sendData(format("ANGLE", angle, false));
		}
		public void sendBullet(Bullet bullet){
			float[] data = {
					bullet.point.x,
					bullet.point.y,
					bullet.trajectory.x,
					bullet.trajectory.y
			};
			sendData(format("BULLET", data, false));
		}
		public void sendBlockHP(int[] data){
			sendData(format("BLOCKHP", data, false));
		}
		public void removeBlock(int index, String[] render){
			String[][] data = new String[2][render.length];
			data[0][0] = Integer.toString(index);
			data[1] = render;
			sendData(format("REMOVEBLOCK", data, false));
		}
		public void updatePlayerHP(int pid, int hp){
			sendData(format("HP", new int[]{pid, hp}, false));
		}
        public void setPlayerKiller(int pid, String killer){
            sendData(format("SETKILLER", new String[]{pid+"", killer}, false));
        }
        public void sendBoughtItem(String name, int amount, int price){
            HashMap<String, String> data = new HashMap<>();
            data.put("name", name);
            data.put("amount", String.valueOf(amount));
            data.put("price", String.valueOf(price));
            sendData(format("BOUGHT", data, false));
        }
		public void givePlayer(Map<?,?> data){
			sendData(format("GIVEPLAYER", data, false));
		}

	}
	
	private Listener listener = new Listener(){
		int pid = 1;
		@SuppressWarnings("unchecked")
		public void received(Connection c, Object object){
			NetworkFormat netObject;
            String dataName;
			int dataID;
			if (object instanceof NetworkFormat) {
				netObject = (NetworkFormat) object;
				if (Game.gameServer != null && !netObject.privat) { // If server, then send to all clients except who it came from
					Game.gameServer.server.sendToAllExceptTCP(c.getID(), object);
				}
				object = netObject.data;
				dataID = netObject.id;
                dataName = netObject.data_name;

                switch (dataName){
                    case "BULLET":
                        float[] data = (float[]) object;
                        Play.bullets.add(new Bullet(new Point2D.Float(data[0], data[1]),
                                new Point2D.Float(data[2], data[3]), 20f, Play.bulletShape, dataID));
                        // Calculate sound position
                        Point2D.Float offCoord = Game.worldToOffset(new Point2D.Float(data[0], data[1]), Game.pgc);
                        float soundX = offCoord.x-Play.offset.x - Play.shipImage.getWidth(),
                                soundY = offCoord.y-Play.offset.y - Play.shipImage.getHeight();
                        if (soundX > 0) soundX = (float)Math.sqrt(soundX);
                        else soundX = (float)-Math.sqrt(-soundX);
                        if (soundY > 0) soundY = (float)Math.sqrt(soundY);
                        else soundY = (float)-Math.sqrt(-soundY);
                        soundX = -soundX / 10;
                        soundY = -soundY / 10;
                        Game.sounds.laser.stop();
                        Game.sounds.laser.playAt(soundX, soundY, 0);
                        break;
                    case "BLOCKHP":
                        int[] blockdata = (int[]) object;
                        Play.blockList.put(blockdata[0], blockdata[1]);
                        break;
                    case "REMOVEBLOCK":
                        String[][] blockdata2 = (String[][]) object;
                        Play.renderBlocks.set(Integer.parseInt(blockdata2[0][0]), blockdata2[1]);
                        break;
                    case "PLAYERS":
                        System.out.println("Instance of arraylist");
                        Play.players = (ArrayList<Player>) object;
                        break;
                    case "JOIN_GAME":
                        // Player joined server
                        String name = (String) object;
                        //	System.out.println("Add player "+pid);
                        Game.gameServer.tmpPlayers.put(pid, new Player(new Point2D.Float(0, 0), Play.shipImage, 0, name));
                        System.out.println("PID "+pid+" connected");

                        // Add player to playerlist
                        Game.gameServer.playerList.put(pid, name);

                        // Return OK
                        c.sendTCP(format("OK", pid, true));
                        // Send lobby list
                        Game.gameServer.server.sendToAllTCP(format("PLAYERLIST", Game.gameServer.playerList, false));

                        pid++;
                        //	System.out.println("Player connected");
                        break;
                    case "DISCONNECT":
                        Game.gameServer.tmpPlayers.remove(dataID);
                        Game.gameServer.playerList.remove(dataID);
                        // Send lobby list
                        Game.gameServer.server.sendToAllTCP(format("PLAYERLIST", Game.gameServer.playerList, false));
                        break;
                    case "HOSTDC":
                        sbg.enterState(Game.menu);
                        Menu.showHostDC();
                        break;

                    case "POS":
                        Point2D.Float pos = (Point2D.Float) object;
                        Play.players.get(dataID).pos.setLocation(pos);
                        break;
                    case "ANGLE":
                        Play.players.get(dataID).angle = (Float) object;
                        break;
                    case "HP":
                        int[] hp = (int[]) object;
                        Player p = Play.players.get(hp[0]);
                        p.updateHP(hp[1]);
                        break;
                    case "GIVEPLAYER":
                        Map<String, Integer> givedata = (Map) object;
                        if (givedata.get("owner") == Network.playerID) {
                            // Add bullets
                            if (givedata.get("bullets") != null)
                                Play.players.get(Network.playerID).bulletCount+=givedata.get("bullets");
                            if (givedata.get("money") != null)
                                Play.players.get(Network.playerID).money+=givedata.get("money");
                        }
                        break;
                    case "BOUGHT":
                        Map<String, String> Bdata = (Map) object;
                        String Bname = Bdata.get("name");
                        int Bamount = Integer.valueOf(Bdata.get("amount")),
                            Bprice = Integer.valueOf(Bdata.get("price"));
                        Player Bplayer = Play.players.get(dataID);
                        Bplayer.money-=Bprice;
                        if (Bname.equals("Bullets"))
                            Bplayer.bulletCount+=Bamount;
                        else if (Bname.equals("HP"))
                            Bplayer.HP+=Bamount;
                        break;
                    case "ChatMsg":
                        Play.chat.addMsg(dataID, (String) object);
                        break;
                    case "SETKILLER":
                        String[] killerData = (String[]) object;
                        Play.players.get(Integer.valueOf(killerData[0])).killedBy = killerData[1];
                        break;

                    default:
                        System.out.println("Unknown data");
                        System.out.println(object);
                        System.out.println("EO Unknown data");

                        break;

				}
			}
			
		}
	};
	
	public static NetworkFormat format(String data_name, Object data, boolean privat){
		return new NetworkFormat(data_name, data, Network.playerID, privat);
	}
	
	
	
}
