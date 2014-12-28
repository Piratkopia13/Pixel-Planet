package net.brainstorm_labs.spacegame;
import java.awt.geom.Point2D;

import org.newdawn.slick.geom.Shape;

class Bullet {
		
	public Point2D.Float point,
			trajectory;
	private float speed;
	private boolean alive = true;
	public Shape shape;
	public int owner;
    private int range = 500;
    private int distance = 0;
	
	public Bullet(Point2D.Float StartPoint, Point2D.Float trajectory, float speed, Shape shape, int ownerID){
		this.point = StartPoint;
		this.trajectory = trajectory;
		this.speed = speed;
		this.shape = shape;
		this.owner = ownerID;
	}
	public Bullet(){
		this.alive = false;
	}
	public void update() {
	    this.point.x = this.point.x + (this.trajectory.x * this.speed);
	    this.point.y = this.point.y + (this.trajectory.y * this.speed);
    //    this.distance += this.speed;
    //    if (this.distance > range) markDead();
	}
	public void markDead(){
		this.alive = false;
	}
	public float x(){
		return this.point.x;
	}
	public float y(){
		return this.point.y;
	}
	public boolean isAlive(){
		if (alive) {
			return true;
		}else
			return false;
	}
	
}
