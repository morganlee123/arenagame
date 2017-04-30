package com.nexide.main.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.nexide.main.Game;
import com.nexide.main.gfx.Animation;

public class Tile implements Entity{
	
	private BufferedImage tile;
	private Animation anim;
	
	private boolean isSolid;
	
	public Tile(BufferedImage tile, boolean isSolid){
		this.tile = tile;
		this.isSolid = isSolid;
	}
	
	public Tile(Animation anim, boolean isSolid){
		this.anim = anim;
		this.isSolid = isSolid;
	}

	public void tick() {
		
	}

	public void render(Graphics g, Game game) {
		
	}
	
	public BufferedImage getImage(){
		return tile;
	}
	
	public Animation getAnimation(){
		return anim;
	}
	
	public boolean isSolid(){
		return isSolid;
	}
	
	
}
