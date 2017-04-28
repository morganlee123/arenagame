package com.nexide.main.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.nexide.main.Game;

public class Tile implements Entity{
	
	private BufferedImage tile;
	private int x, y;
	
	private boolean isSolid;
	
	public Tile(BufferedImage tile, boolean isSolid){
		this.tile = tile;
		this.x = x;
		this.y = y;
		this.isSolid = isSolid;
	}

	public void tick() {
		
	}

	public void render(Graphics g, Game game) {
		
	}
	
	public BufferedImage getImage(){
		return tile;
	}
	
	public int getTileX(){
		return x;
	}
	
	public int getTileY(){
		return y;
	}
	
	public boolean isSolid(){
		return isSolid;
	}
	
}
