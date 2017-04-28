package com.nexide.main.entities;

import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.image.BufferedImage;

import com.nexide.main.Game;

public class Player implements Entity {

	private static int xVel;
	private static int yVel;
	private double mouseX, mouseY;
	private static int xOffset;
	private static int yOffset;
	
	private BufferedImage[] playerSprites;
	
	public Player(int x, int y, BufferedImage[] playerSprites){
		this.playerSprites = playerSprites;
		this.xOffset = x;
		this.yOffset = y;
		
		
		xVel = 5;
		yVel = 5;
	}
	
	public void tick(){
		mouseX = MouseInfo.getPointerInfo().getLocation().getX();
		mouseY = MouseInfo.getPointerInfo().getLocation().getY();
	
		
		System.out.println(mouseX + ", " + mouseY);
	}
	
	
	// MAKE MAP MOVE NOT THE PLAYER, WILL BE CLIENT SIDE NOT SERVER SIDE OF COURSE
	public void render(Graphics g, Game game){
		g.drawImage(playerSprites[0], xOffset, yOffset, game);
	}
	
	public int getX(){
		return xOffset;
	}
	
	public int getY(){
		return yOffset;
	}
	
	public static int getXVel(){
		return xVel;
	}
	
	public static int getYVel(){
		return yVel;
	}
	
	public static void setX(int x){
		xOffset+=x;
	}
	
	public static void setY(int y){
		yOffset+=y;
	}
	
	
}
