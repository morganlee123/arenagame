package com.nexide.main.gfx;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class ImageManager {
	
	private SpriteSheet ss;
	
	private int DEFAULT_TILESIZE = 16;
	private BufferedImage[] grasssandTileset = new BufferedImage[9];
	private BufferedImage[] grassdirtTileset = new BufferedImage[9];
	
	public ImageManager(SpriteSheet ss){
		this.ss = ss;
		
		loadImages();
	}
	
	public void loadImages(){
		
		/* ADD NEW TILESETS */
		
		// GRASS DIRT TILESET
		
		grassdirtTileset[0] = ss.crop(0,0, DEFAULT_TILESIZE, DEFAULT_TILESIZE); //top left
		grassdirtTileset[0] = createResizedCopy(grassdirtTileset[0], 64 ,64);
		
		grassdirtTileset[1] = ss.crop(1,0, DEFAULT_TILESIZE, DEFAULT_TILESIZE); // top mid
		grassdirtTileset[1] = createResizedCopy(grassdirtTileset[1], 64 ,64);
		
		grassdirtTileset[2] = ss.crop(2,0, DEFAULT_TILESIZE, DEFAULT_TILESIZE); // top right
		grassdirtTileset[2] = createResizedCopy(grassdirtTileset[2], 64 ,64);
		
		grassdirtTileset[3] = ss.crop(0,1, DEFAULT_TILESIZE, DEFAULT_TILESIZE); // mid left
		grassdirtTileset[3] = createResizedCopy(grassdirtTileset[3], 64 ,64);
		
		grassdirtTileset[4] = ss.crop(1,1, DEFAULT_TILESIZE, DEFAULT_TILESIZE); // center
		grassdirtTileset[4] = createResizedCopy(grassdirtTileset[4], 64 ,64);
		
		grassdirtTileset[5] = ss.crop(2,1, DEFAULT_TILESIZE, DEFAULT_TILESIZE); // mid right
		grassdirtTileset[5] = createResizedCopy(grassdirtTileset[5], 64 ,64);
		
		grassdirtTileset[6] = ss.crop(0,2, DEFAULT_TILESIZE, DEFAULT_TILESIZE); // bot left
		grassdirtTileset[6] = createResizedCopy(grassdirtTileset[6], 64 ,64);
		
		grassdirtTileset[7] = ss.crop(1,2, DEFAULT_TILESIZE, DEFAULT_TILESIZE); // bot mid
		grassdirtTileset[7] = createResizedCopy(grassdirtTileset[7], 64 ,64);
		
		grassdirtTileset[8] = ss.crop(2,2, DEFAULT_TILESIZE, DEFAULT_TILESIZE); // bot right
		grassdirtTileset[8] = createResizedCopy(grassdirtTileset[8], 64 ,64);
		
		
		
		// GRASS SAND TILESET
		grasssandTileset[0] = ss.crop(5, 0, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		grasssandTileset[0] = createResizedCopy(grasssandTileset[0], 64, 64); //top left
		
		grasssandTileset[1] = ss.crop(6,0, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		grasssandTileset[1] = createResizedCopy(grasssandTileset[1], 64, 64); //top middle
	
		grasssandTileset[2] = ss.crop(7,0, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		grasssandTileset[2] = createResizedCopy(grasssandTileset[2], 64, 64); //top right
		
		grasssandTileset[3] = ss.crop(5,1, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		grasssandTileset[3] = createResizedCopy(grasssandTileset[3], 64, 64); // mid left
		
		grasssandTileset[4] = ss.crop(6,1, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		grasssandTileset[4] = createResizedCopy(grasssandTileset[4], 64, 64); // center
		
		grasssandTileset[5] = ss.crop(7,1, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		grasssandTileset[5] = createResizedCopy(grasssandTileset[5], 64, 64); // mid right
		
		grasssandTileset[6] = ss.crop(5,2, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		grasssandTileset[6] = createResizedCopy(grasssandTileset[6], 64, 64); // bot left
	
		grasssandTileset[7] = ss.crop(6,2, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		grasssandTileset[7] = createResizedCopy(grasssandTileset[7], 64, 64); // bot mid
	 
		grasssandTileset[8] = ss.crop(7,2, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		grasssandTileset[8] = createResizedCopy(grasssandTileset[8], 64, 64); // bot right
		
		/****************************************************/
	}

	public BufferedImage[] getTileset(String requestedTileset){
		switch(requestedTileset){
		case "grasssand":
			return grasssandTileset;
		case "grassdirt":
			return grassdirtTileset;
		default:
			System.out.println("Not a valid tileset");
		}
		
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
