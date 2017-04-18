package com.team1.main.gfx;

import java.awt.image.BufferedImage;

public class ImageManager {
	
	private SpriteSheet ss;
	
	
	private int DEFAULT_TILESIZE = 16;
	private BufferedImage image1;
	
	public ImageManager(SpriteSheet ss){
		this.ss = ss;
		
		loadImages();
	}
	
	public void loadImages(){
		image1 = ss.crop(0, 0, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
	}
	
}
