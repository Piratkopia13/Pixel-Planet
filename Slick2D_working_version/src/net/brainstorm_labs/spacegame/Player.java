package net.brainstorm_labs.spacegame;

import java.awt.geom.Point2D;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Shape;

public class Player {

	Point2D.Float pos = new Point2D.Float();
	int id;
	String name;
	Image icon;
	float angle;
	Shape bounding;
	int HP = 10;
	int bulletCount = 100;
    boolean isAlive = true;
    boolean ignore = false;
    int money = 0;
    String killedBy;
	
	public Player(Point2D.Float startPosition, Image icon, int id, String name){
		this.pos = startPosition;
		this.icon = icon;
		this.id = id;
		this.name = name;
	}
	public Player(){}

    public void updateHP(int newHP){
        if(this.isAlive) this.HP = newHP;
        if (this.HP <= 0){ // Dead
            this.isAlive = false;
        }
    }
	
	
	
	
	
}
