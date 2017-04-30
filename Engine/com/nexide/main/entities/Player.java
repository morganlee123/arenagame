package com.nexide.main.entities;

import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.image.BufferedImage;

import com.nexide.main.Game;

public class Player implements Entity {

	private double mouseX, mouseY;

	private BufferedImage[] playerSprites;
	private BufferedImage currentDirection;
	private int x, y;
	public static boolean up, left, right, down;

	public Player(int x, int y, BufferedImage[] playerSprites){
		this.playerSprites = playerSprites;
		this.x = x;
		this.y = y;
		
		up = false;
		down = false;
		left = false;
		right = false;
	}
	
	public Player(){
		
	}

	private BufferedImage lastDirection;
	
	public void tick(){
		mouseX = MouseInfo.getPointerInfo().getLocation().getX();
		mouseY = MouseInfo.getPointerInfo().getLocation().getY();
		
		/*SET PLAYER SPRITE CORRESPONDING TO DIRECTION THEY ARE MOVING*/
		if(up){
			currentDirection = playerSprites[0];
			lastDirection = playerSprites[0];
		}else if(down){
			currentDirection = playerSprites[6];
			lastDirection = playerSprites[6];
		}else if(left){
			currentDirection = playerSprites[9];
			lastDirection = playerSprites[9];
		}else if(right){
			currentDirection = playerSprites[3];
			lastDirection = playerSprites[3];
		}else{
			currentDirection = lastDirection;
			if(currentDirection==null){
				currentDirection = playerSprites[0];
			}
		}

		
		
		//System.out.println(mouseX + ", " + mouseY);
	}


	public void render(Graphics g, Game game){
		g.drawImage(currentDirection, x, y, game);
	}
	
	public String getCurrentDirection(){
		if(up)
			return "up";
		else if(down)
			return "down";
		else if(right)
			return "right";
		else if(left)
			return "left";
		else{
			return "idle";
		}
	}
	

}

