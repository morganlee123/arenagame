package com.nexide.main.entities;

import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.image.BufferedImage;

import com.nexide.main.Game;

public class Player implements Entity {

	private int xVel, yVel;
	private double mouseX, mouseY;
	private int xOffset, yOffset;
	
	private BufferedImage[] playerSprites;
	
	public Player(int x, int y, BufferedImage[] playerSprites){
		this.playerSprites = playerSprites;
		this.xOffset = x;
		this.yOffset = y;
		
	}
	
	public void tick(){
		mouseX = MouseInfo.getPointerInfo().getLocation().getX();
		mouseY = MouseInfo.getPointerInfo().getLocation().getY();
	
		
		System.out.println(mouseX + ", " + mouseY);
	}
	
	public void render(Graphics g, Game game){
		g.drawImage(playerSprites[0], 0-xOffset, 0-yOffset, game);
	}
	
	public int getX(){
		return xOffset;
	}
	
	public int getY(){
		return yOffset;
	}
	
	
}
