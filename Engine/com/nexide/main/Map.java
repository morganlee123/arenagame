package com.nexide.main;

import java.awt.Dimension;
import java.awt.Graphics;

import com.nexide.main.entities.Tile;
import com.nexide.main.entities.TileManager;
import com.nexide.main.gfx.ImageManager;

/*
 * 
 * REVAMP OF THE MAP CLASS USING TILES NOT SPRITESHEET
 * 
 * AUTHOR -> MORGAN SANDLER
 * 
 * */

public class Map {

	// map is 52x62

	private TileManager tm;

	private Tile[][] tileMap;
	private int[][] intmap = {};

	public Map(int[][] intmap){
		this.intmap = intmap;
	}


	public void render(Graphics g, Game game, TileManager tm){
		tileMap = convertIntMapToTileMap(intmap, tileMap, tm);
		for(int i = 0; i<54; i++)
			for(int z = 0; z<64; z++)
				g.drawImage(tileMap[i][z].getImage(), z*64, i*64, game);

	} 


	public Tile[][] convertIntMapToTileMap(int[][] intmap, Tile[][] emptyTileMap, TileManager tm){
		emptyTileMap = new Tile[54][64];
		for(int i = 0; i<intmap.length; i++){
			for(int z = 0; z<(intmap[i].length); z++){
				switch(intmap[i][z]){
				case 0:
					emptyTileMap[i][z] = tm.getTile("grasstile"); 
					break;
				case 1:
					emptyTileMap[i][z] = tm.getTile("rocktile");
					break;
				case 2:
					emptyTileMap[i][z] = tm.getTile("watertile");
					break;
				case 3:
					emptyTileMap[i][z] = tm.getTile("sandtile");
					break;
				case 4:
					emptyTileMap[i][z] = tm.getTile("cobbletile");
					break;
				case 5:
					emptyTileMap[i][z] = tm.getTile("cobbletile");
					break;
				case 6:
					emptyTileMap[i][z] = tm.getTile("sandtile");
					break;
				case 7:
					emptyTileMap[i][z] = tm.getTile("cobbletile");
					break;
				default:
					System.out.println("error loading tile");
					break;
				}
			}
		}
		return emptyTileMap;
	}

}

