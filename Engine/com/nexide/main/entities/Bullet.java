package com.nexide.main.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.nexide.main.MouseManager;

public class Bullet {

	private Rectangle hitbox;
	private int x, y;
	private double velY, velX;

	private Player player;
	private MouseManager mm;

	private double lastPX, lastPY, lastMX, lastMY;
	
	public Bullet(int x, int y, Player player, MouseManager mm){
		this.x = x;
		this.y = y;

		this.player = player;
		this.mm = mm;

		hitbox = new Rectangle();
		
		lastPX = player.getX();
		lastPY = player.getY();
		lastMX = mm.getX();
		lastMY = mm.getY();
	}

	private int velZ = 2;

	public void tick(){

		/*
		System.out.println(velX + " " + velY);

		
		
		double slope = (mm.getMouseY() - lastPY) / (mm.getMouseX() - lastPX);

		if((mm.getMouseX() - lastPX != 0) && (lastPX != mm.getMouseX()) && (lastPY != mm.getMouseY())){
			velX = Math.sqrt((velZ * velZ)/(1+(slope*slope)));
			velY = Math.sqrt((velZ*velZ) - ((velZ*velZ)/(1+(slope*slope))));
		}

		
		
		if(mm.getMouseX() > lastPX){
			x+=velX;
		}
		else{
			x-=velX;
		}

		if(mm.getMouseY() > lastPY){
			y+=velY;
		}
		else{
			y-=velY;
		}*/

		int speed = 10;
	    float dx, dy;
	    
		float distance = (float) Math.sqrt(Math.pow(lastMX - lastPX, 2) + Math.pow(lastMY - lastPY, 2));
	    dx = (float) (((lastMX - lastPX) / distance) * speed);
	    dy = (float) (((lastMY - lastPY) / distance) * speed);
		
	    x+=dx;
	    y+=dy;
	    
		hitbox = new Rectangle(x, y, 5, 10);
	}

	public void render(Graphics g){
		g.setColor(Color.GREEN);
		g.fillRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
	}

	public void setVelY(int y){
		this.y = y;
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public Rectangle getHitbox(){
		return hitbox;
	}


}
