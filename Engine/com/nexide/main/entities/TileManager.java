package com.nexide.main.entities;

import java.awt.image.BufferedImage;

import com.nexide.main.Map;
import com.nexide.main.gfx.Animation;
import com.nexide.main.gfx.ImageManager;

public class TileManager {

	private ImageManager im;
	private Tile grasstile, stonetile, watertile, sandtile, darkcobbletile;

	private Map map;
	
	private BufferedImage[] grassdirtTileset, grasssandTileset, miscTileset, stoneTileset, darkcobbleTileset;

	public TileManager(ImageManager im){
		this.im = im;
		grassdirtTileset = im.getTileset("grassdirt");
		grasssandTileset = im.getTileset("grasssand");
		miscTileset = im.getTileset("misc");
		stoneTileset = im.getTileset("stone");
		darkcobbleTileset = im.getTileset("darkstone");
		
		grasstile = new Tile(grassdirtTileset[4], false); // NORM GRASS
		stonetile = new Tile(stoneTileset[4], false); // walkable stone
		watertile = new Tile(new Animation(10, miscTileset[0], miscTileset[1], miscTileset[2]), true);
		sandtile = new Tile(grasssandTileset[4], false); // SAND
		darkcobbletile = new Tile(darkcobbleTileset[4], true); // DARK BORDER stone 
	}



	public Tile getTile(String tile){
		switch(tile){
		case "grasstile":
			return grasstile;
		case "stonetile":
			return stonetile;
		case "watertile":
			return watertile;
		case "sandtile":
			return sandtile;
		case "darkstone":
			return darkcobbletile;
		default:
			System.out.println("DEFAULT TILE RETURNED");
			return grasstile;
		}
	}


}
