package net.brainstorm_labs.spacegame;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class Sounds {

    public Sound laser;

    public Sound[] explosion = new Sound[4];
    public Sounds(){}

    public void init(){
        // Set up sounds
        try{
            this.laser = new Sound("res/sound/laser.ogg");
            explosion[0] = new Sound("res/sound/explosion1.ogg");
            explosion[1] = new Sound("res/sound/explosion2.ogg");
            explosion[2] = new Sound("res/sound/explosion3.ogg");
            explosion[3] = new Sound("res/sound/explosion4.ogg");
        }catch (SlickException e){
        	Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public Sound randomExplosion(){
        int rand = (int)(Math.random() * explosion.length);
        return explosion[rand];
    }
}
