package com.nexide.main.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.nexide.main.Game;

public class Player implements Entity {

	private Game game;
	
	private int xVel, yVel;
	private double weaponRotation;
	private int xPos, yPos;
	
	private BufferedImage[] playerSprites;
	
	public Player(int x, int y, BufferedImage[] playerSprites){
		this.playerSprites = playerSprites;
		this.xPos = x;
		this.yPos = y;
		
		game = new Game();
	}
	
	public void tick(){
		
	}
	
	public void render(Graphics g){
		g.drawImage(playerSprites[0], xPos, yPos, game);
	}
	
	public int getX(){
		return xPos;
	}
	
	public int getY(){
		return yPos;
	}
	
	public double getWeaponRotation(){
		return weaponRotation;
	}
}
