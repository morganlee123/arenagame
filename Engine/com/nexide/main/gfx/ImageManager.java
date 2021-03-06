package com.nexide.main.gfx;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class ImageManager {
	
	private SpriteSheet ss;
	
	public int DEFAULT_TILESIZE = 16;
	
	private BufferedImage[] grasssandTileset = new BufferedImage[13];
	private BufferedImage[] grassdirtTileset = new BufferedImage[15];
	private BufferedImage[] stoneTileset = new BufferedImage[13];
	private BufferedImage[] miscTileset = new BufferedImage[9];
	private BufferedImage[] playerSprites = new BufferedImage[12];
	private BufferedImage[] darkcobbleTileset = new BufferedImage[13];
	
	public ImageManager(SpriteSheet ss){
		this.ss = ss;
		
		loadImages();
	}
	
	public void loadImages(){
		
		/* ADD NEW TILESETS */
		
		// ALL PLAYER SPRITES
		
		playerSprites[0] = ss.crop(10, 0, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		playerSprites[0] = createResizedCopy(playerSprites[0], 64, 64);
		
		playerSprites[1] = ss.crop(10, 1, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		playerSprites[1] = createResizedCopy(playerSprites[1], 64, 64);
		
		playerSprites[2] = ss.crop(10, 2, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		playerSprites[2] = createResizedCopy(playerSprites[2], 64, 64);
		
		playerSprites[3] = ss.crop(11, 0, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		playerSprites[3] = createResizedCopy(playerSprites[3], 64, 64);
		
		playerSprites[4] = ss.crop(11, 1, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		playerSprites[4] = createResizedCopy(playerSprites[4], 64, 64);
		
		playerSprites[5] = ss.crop(11, 2, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		playerSprites[5] = createResizedCopy(playerSprites[5], 64, 64);
		
		playerSprites[6] = ss.crop(12, 0,  DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		playerSprites[6] = createResizedCopy(playerSprites[6], 64, 64);
	
		playerSprites[7] = ss.crop(12, 1, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		playerSprites[7] = createResizedCopy(playerSprites[7], 64, 64);
		
		playerSprites[8] = ss.crop(12, 2, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		playerSprites[8] = createResizedCopy(playerSprites[8], 64, 64);
	
		playerSprites[9] = ss.crop(13, 0, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		playerSprites[9] = createResizedCopy(playerSprites[9], 64, 64);
		
		playerSprites[10] = ss.crop(13, 1, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		playerSprites[10] = createResizedCopy(playerSprites[10], 64, 64);
		
		playerSprites[11] = ss.crop(13, 2, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		playerSprites[11] = createResizedCopy(playerSprites[11], 64, 64);
		
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
		
		grassdirtTileset[9] = ss.crop(3, 0, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		grassdirtTileset[9] = createResizedCopy(grassdirtTileset[9], 64, 64);
		
		grassdirtTileset[10] = ss.crop(3, 1, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		grassdirtTileset[10] = createResizedCopy(grassdirtTileset[10], 64, 64);
		
		grassdirtTileset[11] = ss.crop(4, 1, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		grassdirtTileset[11] = createResizedCopy(grassdirtTileset[11], 64, 64);
		
		grassdirtTileset[12] = ss.crop(3, 2, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		grassdirtTileset[12] = createResizedCopy(grassdirtTileset[12], 64, 64);
		
		grassdirtTileset[13] = ss.crop(4,2, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		grassdirtTileset[13] = createResizedCopy(grassdirtTileset[13], 64, 64);
		//DARK COBBLE TILESET
		
		darkcobbleTileset[0] = ss.crop(0, 6, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		darkcobbleTileset[0] = createResizedCopy(darkcobbleTileset[0], 64, 64);
		
		darkcobbleTileset[1] = ss.crop(1, 6, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		darkcobbleTileset[1] = createResizedCopy(darkcobbleTileset[1], 64, 64);
		
		darkcobbleTileset[2] = ss.crop(2, 6, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		darkcobbleTileset[2] = createResizedCopy(darkcobbleTileset[2], 64, 64);
		
		darkcobbleTileset[3] = ss.crop(0, 7, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		darkcobbleTileset[3] = createResizedCopy(darkcobbleTileset[3], 64, 64);
		
		darkcobbleTileset[4] = ss.crop(1, 7, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		darkcobbleTileset[4] = createResizedCopy(darkcobbleTileset[4], 64, 64);
		
		darkcobbleTileset[5] = ss.crop(2, 7, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		darkcobbleTileset[5] = createResizedCopy(darkcobbleTileset[5], 64, 64);
		
		darkcobbleTileset[6] = ss.crop(0, 8, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		darkcobbleTileset[6] = createResizedCopy(darkcobbleTileset[6], 64, 64);
		
		darkcobbleTileset[7] = ss.crop(1, 8, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		darkcobbleTileset[7] = createResizedCopy(darkcobbleTileset[7], 64, 64);
		
		darkcobbleTileset[8] = ss.crop(2, 8, DEFAULT_TILESIZE, DEFAULT_TILESIZE); 
		darkcobbleTileset[8] = createResizedCopy(darkcobbleTileset[8], 64, 64);
		
		darkcobbleTileset[9] = ss.crop(3, 7, DEFAULT_TILESIZE, DEFAULT_TILESIZE); // TOP LEFT ELBOW
		darkcobbleTileset[9] = createResizedCopy(darkcobbleTileset[9], 64, 64);
		
		darkcobbleTileset[10] = ss.crop(4, 7, DEFAULT_TILESIZE, DEFAULT_TILESIZE); // TOP RIGHT ELBOW
		darkcobbleTileset[10] = createResizedCopy(darkcobbleTileset[10], 64, 64);
		
		darkcobbleTileset[11] = ss.crop(3, 8, DEFAULT_TILESIZE, DEFAULT_TILESIZE); // BOT LEFT ELBOW
		darkcobbleTileset[11] = createResizedCopy(darkcobbleTileset[11], 64, 64);
		
		darkcobbleTileset[12] = ss.crop(4, 8, DEFAULT_TILESIZE, DEFAULT_TILESIZE); // BOT RIGHT ELBOW
		darkcobbleTileset[12] = createResizedCopy(darkcobbleTileset[12], 64, 64);
		
		// STONE TILESET
		
		stoneTileset[0] = ss.crop(0,3, DEFAULT_TILESIZE, DEFAULT_TILESIZE); //top left
		stoneTileset[0] = createResizedCopy(stoneTileset[0], 64 ,64);
		
		stoneTileset[1] = ss.crop(1,3, DEFAULT_TILESIZE, DEFAULT_TILESIZE); // top mid
		stoneTileset[1] = createResizedCopy(stoneTileset[1], 64 ,64);
		
		stoneTileset[2] = ss.crop(2,3, DEFAULT_TILESIZE, DEFAULT_TILESIZE); // top right
		stoneTileset[2] = createResizedCopy(stoneTileset[2], 64 ,64);
		
		stoneTileset[3] = ss.crop(0,4, DEFAULT_TILESIZE, DEFAULT_TILESIZE); // mid left
		stoneTileset[3] = createResizedCopy(stoneTileset[3], 64 ,64);
		
		stoneTileset[4] = ss.crop(1,4, DEFAULT_TILESIZE, DEFAULT_TILESIZE); // center
		stoneTileset[4] = createResizedCopy(stoneTileset[4], 64 ,64);
		
		stoneTileset[5] = ss.crop(2,4, DEFAULT_TILESIZE, DEFAULT_TILESIZE); // mid right
		stoneTileset[5] = createResizedCopy(stoneTileset[5], 64 ,64);
		
		stoneTileset[6] = ss.crop(0,5, DEFAULT_TILESIZE, DEFAULT_TILESIZE); // bot left
		stoneTileset[6] = createResizedCopy(stoneTileset[6], 64 ,64);
		
		stoneTileset[7] = ss.crop(1,5, DEFAULT_TILESIZE, DEFAULT_TILESIZE); // bot mid
		stoneTileset[7] = createResizedCopy(stoneTileset[7], 64 ,64);
		
		stoneTileset[8] = ss.crop(2,5, DEFAULT_TILESIZE, DEFAULT_TILESIZE); // bot right
		stoneTileset[8] = createResizedCopy(stoneTileset[8], 64 ,64);
		
		stoneTileset[9] = ss.crop(3, 4, DEFAULT_TILESIZE, DEFAULT_TILESIZE); // TOP LEFT ELBOW
		stoneTileset[9] = createResizedCopy(stoneTileset[9], 64 ,64);
		
		stoneTileset[10] = ss.crop(4, 4, DEFAULT_TILESIZE, DEFAULT_TILESIZE); // TOP RIGHT ELBOW
		stoneTileset[10] = createResizedCopy(stoneTileset[10], 64 ,64);
		
		stoneTileset[11] = ss.crop(3, 5, DEFAULT_TILESIZE, DEFAULT_TILESIZE); // BOT LEFT ELBOW
		stoneTileset[11] = createResizedCopy(stoneTileset[11], 64 ,64);
		
		stoneTileset[12] = ss.crop(4, 5, DEFAULT_TILESIZE, DEFAULT_TILESIZE); // BOT RIGHT ELBOW
		stoneTileset[12] = createResizedCopy(stoneTileset[12], 64 ,64);
		
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
		
		grasssandTileset[9] = ss.crop(8,1, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		grasssandTileset[9] = createResizedCopy(grasssandTileset[9], 64, 64); // TOP LEFT ELBOW
		
		grasssandTileset[10] = ss.crop(9,1, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		grasssandTileset[10] = createResizedCopy(grasssandTileset[10], 64, 64); // TOP RIGHT ELBOW
		
		grasssandTileset[11] = ss.crop(8,2, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		grasssandTileset[11] = createResizedCopy(grasssandTileset[11], 64, 64); // BOT LEFT ELBOW
		
		grasssandTileset[12] = ss.crop(9,2, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		grasssandTileset[12] = createResizedCopy(grasssandTileset[12], 64, 64); // BOT RIGHT ELBOW
		
		// MISC TILES
		
		miscTileset[0] = ss.crop(4, 3, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		miscTileset[0] = createResizedCopy(miscTileset[0], 64, 64); //WATER
		
		miscTileset[1] = ss.crop(5,3, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		miscTileset[1] = createResizedCopy(miscTileset[1], 64, 64); // WATER FRAME 2
		
		miscTileset[2] = ss.crop(6,3, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		miscTileset[2] = createResizedCopy(miscTileset[2], 64, 64);
		
		miscTileset[3] = ss.crop(9, 0, DEFAULT_TILESIZE, DEFAULT_TILESIZE);
		miscTileset[3] = createResizedCopy(miscTileset[3], 64, 64); // ROCK
		
		/****************************************************/
	}

	public BufferedImage[] getTileset(String requestedTileset){
		switch(requestedTileset){
		case "grasssand":
			return grasssandTileset;
		case "grassdirt":
			return grassdirtTileset;
		case "stone":
			return stoneTileset;
		case "misc":
			return miscTileset;
		case "playersprites":
			return playerSprites;
		case "darkstone":
			return darkcobbleTileset;
		default:
			System.out.println("Not a valid tileset");
		}
		
		return null;
	}
	
	public BufferedImage createResizedCopy(Image originalImage, 
    		int scaledWidth, int scaledHeight)
    {
    	BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
    	Graphics2D g = scaledBI.createGraphics();
    	g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null); 
    	g.dispose();
    	return scaledBI;
    }
	
}
