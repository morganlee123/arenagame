package com.nexide.main.gfx;

import java.awt.image.BufferedImage;

public class ImageManager {
	
	private SpriteSheet ss;
	
	private int DEFAULT_TILESIZE = 16;
	private BufferedImage[] grassTileset = new BufferedImage[9];
	
	public ImageManager(SpriteSheet ss){
		this.ss = ss;
		
		loadImages();
	}
	
	public void loadImages(){
		grassTileset[0] = ss.crop(1, 1, DEFAULT_TILESIZE, DEFAULT_TILESIZE); // plain grass tile
		
	}

	public BufferedImage[] getTileset(String requestedTileset){
		if(requestedTileset == "grass"){
			return grassTileset;
		}
		
		System.out.println("Not a valid tileset");
		return null;
	}
	
}
