package com.nexide.main.gfx;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
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
		
		/* ADD NEW TILESETS, EVERYTHING BELOW IS TEMPORARY*/
		grassTileset[0] = ss.crop(1, 1, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		grassTileset[0] = createResizedCopy(grassTileset[0], 64, 64); //grass
		
		grassTileset[1] = ss.crop(7,1, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		grassTileset[1] = createResizedCopy(grassTileset[1], 64, 64); //transfer from grass to sand
	
		grassTileset[2] = ss.crop(6,1, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		grassTileset[2] = createResizedCopy(grassTileset[2], 64, 64); //sand
		
		/****************************************************/
	}

	public BufferedImage[] getTileset(String requestedTileset){
		if(requestedTileset == "grass"){
			return grassTileset;
		}
		
		System.out.println("Not a valid tileset");
		return null;
	}
	
	public BufferedImage createResizedCopy(Image originalImage, 
    		int scaledWidth, int scaledHeight)
    {
    	System.out.println("resizing...");
    	BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
    	Graphics2D g = scaledBI.createGraphics();
    	g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null); 
    	g.dispose();
    	return scaledBI;
    }
	
}
