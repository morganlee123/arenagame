package com.nexide.main.entities;

import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.image.BufferedImage;

import com.nexide.main.Game;

public class Player implements Entity {

	private Game game;
	
	private int xVel, yVel;
	private double mouseX, mouseY;
	private int xPos, yPos;
	
	private BufferedImage[] playerSprites;
	
	public Player(int x, int y, BufferedImage[] playerSprites){
		this.playerSprites = playerSprites;
		this.xPos = x;
		this.yPos = y;
		
		game = new Game();
	}
	
	public void tick(){
		mouseX = MouseInfo.getPointerInfo().getLocation().getX();
		mouseY = MouseInfo.getPointerInfo().getLocation().getY();
	
		System.out.println(mouseX + ", " + mouseY);
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
	
	
}
