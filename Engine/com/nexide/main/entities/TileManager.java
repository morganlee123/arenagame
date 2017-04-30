package com.nexide.main.entities;

import java.awt.image.BufferedImage;

import com.nexide.main.Map;
import com.nexide.main.gfx.Animation;
import com.nexide.main.gfx.ImageManager;

public class TileManager {

	private ImageManager im;
	private Tile[] grasstiles  = new Tile[14], stonetiles= new Tile[13], sandtiles= new Tile[13], darkcobbletiles= new Tile[13], miscTiles= new Tile[12];
	private Tile watertile;
	
	
	private Map map;
	
	private BufferedImage[] grassdirtTileset, grasssandTileset, miscTileset, stoneTileset, darkcobbleTileset;

	public TileManager(ImageManager im){
		this.im = im;
		grassdirtTileset = im.getTileset("grassdirt");
		grasssandTileset = im.getTileset("grasssand");
		miscTileset = im.getTileset("misc");
		stoneTileset = im.getTileset("stone");
		darkcobbleTileset = im.getTileset("darkstone");
		
		grasstiles[0] = new Tile(grassdirtTileset[0], false); // GRASS TOP LEFT
		grasstiles[1] = new Tile(grassdirtTileset[1], false); // GRASS TOP MID
		grasstiles[2] = new Tile(grassdirtTileset[2], false); // GRASS TOP RIGHT
		grasstiles[3] = new Tile(grassdirtTileset[3], false); // GRASS MID LEFT
		grasstiles[4] = new Tile(grassdirtTileset[4], false); // GRASS MID 
		grasstiles[5] = new Tile(grassdirtTileset[5], false); // GRASS MID RIGHT
		grasstiles[6] = new Tile(grassdirtTileset[6], false); // GRASS BOT LEFT
		grasstiles[7] = new Tile(grassdirtTileset[7], false); // GRASS BOT MID
		grasstiles[8] = new Tile(grassdirtTileset[8], false); // GRASS BOT RIGHT
		grasstiles[9] = new Tile(grassdirtTileset[9], false); // DIRT
		
		grasstiles[10] = new Tile(grassdirtTileset[10], false); // TOP LEFT ELBOW
		grasstiles[11] = new Tile(grassdirtTileset[11], false); // TOP RIGHT ELBOW
		grasstiles[12] = new Tile(grassdirtTileset[12], false); // BOT LEFT ELBOW
		grasstiles[13] = new Tile(grassdirtTileset[13], false); // BOT RIGHT ELBOW
		
		stonetiles[0] = new Tile(stoneTileset[0], false); // LIGHT STONE TOP LEFT
		stonetiles[1] = new Tile(stoneTileset[1], false); // LIGHT STONE TOP MID
		stonetiles[2] = new Tile(stoneTileset[2], false); // LIGHT STONE TOP RIGHT
		stonetiles[3] = new Tile(stoneTileset[3], false); // LIGHT STONE MID LEFT
		stonetiles[4] = new Tile(stoneTileset[4], false); // LIGHT STONE MID
		stonetiles[5] = new Tile(stoneTileset[5], false); // LIGHT STONE MID RIGHT
		stonetiles[6] = new Tile(stoneTileset[6], false); // LIGHT STONE BOT LEFT
		stonetiles[7] = new Tile(stoneTileset[7], false); // LIGHT STONE BOT MID
		stonetiles[8] = new Tile(stoneTileset[8], false); // LIGHT STONE BOT RIGHT
		
		stonetiles[9] = new Tile(stoneTileset[9], false); // TOP LEFT ELBOW
		stonetiles[10] = new Tile(stoneTileset[10], false); // TOP RIGHT ELBOW
		stonetiles[11] = new Tile(stoneTileset[11], false); // BOT LEFT ELBOW
		stonetiles[12] = new Tile(stoneTileset[12], false); // BOT RIGHT ELBOW
		
		watertile = new Tile(new Animation(10, miscTileset[0], miscTileset[1], miscTileset[2]), false);
		
		sandtiles[0] = new Tile(grasssandTileset[0], false); // TOP LEFT SAND
		sandtiles[1] = new Tile(grasssandTileset[1], false); // TOP MID SAND
		sandtiles[2] = new Tile(grasssandTileset[2], false); // TOP RIGHT SAND
		sandtiles[3] = new Tile(grasssandTileset[3], false); // MID LEFT SAND
		sandtiles[4] = new Tile(grasssandTileset[4], false); // MID SAND
		sandtiles[5] = new Tile(grasssandTileset[5], false); // MID RIGHT SAND
		sandtiles[6] = new Tile(grasssandTileset[6], false); // BOT LEFT SAND
		sandtiles[7] = new Tile(grasssandTileset[7], false); // BOT MID SAND
		sandtiles[8] = new Tile(grasssandTileset[8], false); // BOT RIGHT SAND
		
		sandtiles[9] = new Tile(grasssandTileset[9], false); // TOP LEFT ELBOW
		sandtiles[10] = new Tile(grasssandTileset[10], false); // TOP RIGHT ELBOW
		sandtiles[11] = new Tile(grasssandTileset[11], false); // BOT LEFT ELBOW
		sandtiles[12] = new Tile(grasssandTileset[12], false); // BOT RIGHT ELBOW
		
		
		darkcobbletiles[0] = new Tile(darkcobbleTileset[0], true); // DARK STONE TOP LEFT 
		darkcobbletiles[1] = new Tile(darkcobbleTileset[1], true); // DARK STONE TOP MID 
		darkcobbletiles[2] = new Tile(darkcobbleTileset[2], true); // DARK STONE TOP RIGHT 
		darkcobbletiles[3] = new Tile(darkcobbleTileset[3], true); // DARK STONE MID LEFT 
		darkcobbletiles[4] = new Tile(darkcobbleTileset[4], true); // DARK STONE MID  
		darkcobbletiles[5] = new Tile(darkcobbleTileset[5], true); // DARK STONE MID RIGHT 
		darkcobbletiles[6] = new Tile(darkcobbleTileset[6], true); // DARK STONE BOT LEFT 
		darkcobbletiles[7] = new Tile(darkcobbleTileset[7], true); // DARK STONE BOT MID 
		darkcobbletiles[8] = new Tile(darkcobbleTileset[8], true); // DARK STONE BOT RIGHT 
		
		darkcobbletiles[9] = new Tile(darkcobbleTileset[9], true); // TOP LEFT ELBOW
		darkcobbletiles[10] = new Tile(darkcobbleTileset[10], true); // TOP RIGHT ELBOW
		darkcobbletiles[11] = new Tile(darkcobbleTileset[11], true); // BOT LEFT ELBOW
		darkcobbletiles[12] = new Tile(darkcobbleTileset[12], true); // BOT RIGHT ELBOW
	
		miscTiles[0] = new Tile(miscTileset[3], true);
		
	}


	public Tile[] getTiles(String tile){
		switch(tile){
		case "grasstile":
			return grasstiles;
		case "stonetile":
			return stonetiles;
		case "sandtile":
			return sandtiles;
		case "darkstone":
			return darkcobbletiles;
		case "misc":
			return miscTiles;
		default:
			System.out.println("DEFAULT TILE RETURNED");
			return grasstiles;
		}
	}

	
	public Tile getTile(String tile){
		switch(tile){
		case "watertile":
			return watertile;
		default:
			System.out.println("DEFAULT TILE RETURNED");
			return grasstiles[4];
		}
	}


}
