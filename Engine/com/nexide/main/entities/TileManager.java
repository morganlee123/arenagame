package com.nexide.main.entities;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import com.nexide.main.Game;
import com.nexide.main.Map;
import com.nexide.main.gfx.Animation;
import com.nexide.main.gfx.ImageManager;

public class TileManager {

	private ImageManager im;
	private Tile grasstile, rocktile, watertile, sandtile, cobbletile;

	private Map map;
	
	private BufferedImage[] grassdirtTileset, grasssandTileset, miscTileset, stoneTileset;

	public TileManager(ImageManager im){
		this.im = im;
		grassdirtTileset = im.getTileset("grassdirt");
		grasssandTileset = im.getTileset("grasssand");
		miscTileset = im.getTileset("misc");
		stoneTileset = im.getTileset("stone");
		
		grasstile = new Tile(grassdirtTileset[4], false); // NORM GRASS
		rocktile = new Tile(miscTileset[3], true); // ROCK
		watertile = new Tile(new Animation(10, miscTileset[0], miscTileset[1], miscTileset[2]), true);
		sandtile = new Tile(grasssandTileset[4], false); // SAND
		cobbletile = new Tile(stoneTileset[4], true); // COBBLE
	}



	public Tile getTile(String tile){
		switch(tile){
		case "grasstile":
			return grasstile;
		case "rocktile":
			return rocktile;
		case "watertile":
			return watertile;
		case "sandtile":
			return sandtile;
		case "cobbletile":
			return cobbletile;
		default:
			return grasstile;
		}
	}


}
