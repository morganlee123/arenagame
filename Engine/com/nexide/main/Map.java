package com.nexide.main;

import java.awt.Graphics;
import java.util.ArrayList;

import com.nexide.main.entities.Player;
import com.nexide.main.entities.Tile;
import com.nexide.main.entities.TileManager;
import com.nexide.main.gfx.Animation;

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

	private int xOffset, yOffset;
	private int xVel, yVel;

	public Map(int[][] intmap){
		this.intmap = intmap;

		xOffset = 120;
		yOffset = -1275;
		xVel = 5;
		yVel = 5;
		
	}
	
	public void tick(Player player, TileManager tman){
		//System.out.println(xOffset + "," + yOffset);
		int oldOffset = yOffset + xOffset;
		if(player.getCurrentDirection() == "up" && isValidMove(xOffset, yOffset+yVel, tman)){ 
			yOffset+=yVel;
		}else if(player.getCurrentDirection() == "down" && isValidMove(xOffset, yOffset - yVel, tman)){
			yOffset-=yVel;
		}

		if(player.getCurrentDirection() == "right" && isValidMove(xOffset-xVel, yOffset, tman)){
			xOffset-=xVel;
		}else if(player.getCurrentDirection() == "left" && isValidMove(xOffset + xVel, yOffset, tman)){
			xOffset+=xVel;
		}
		
		if (oldOffset != yOffset+xOffset)
			System.out.println("xOff: " + xOffset + ", yOff: " + yOffset + ", x: " + (0 - (xOffset - 640)) + ", y: " + (0 - (yOffset - 400)));
	}

	private boolean isValidMove(int xCoord, int yCoord, TileManager tman) {
		
		//1. Change the offsets into legitimate coordinates...
		
		int trueX = 0 - (xCoord - 640);
		int trueY = 0 - (yCoord - 400);
		int secondX = trueX + 64;
		int secondY = trueY + 64;
		
		//2. Change the LEGITIMATE coordinates to tile numbers
		
		trueX = trueX/64;		//on purpose: rounds down 
		trueY = trueY/64;
		secondX = secondX/64;
		secondY = secondY/64;
		
		
		
		Tile tiles[][] = convertIntMapToTileMap(intmap, tileMap, tman);
		Tile tile = tiles[trueY][trueX];
		if (tile.isSolid())
			return false;
		tile = tiles[trueY][secondX];
		if (tile.isSolid())
			return false;
		tile = tiles[secondY][trueX];
		if (tile.isSolid())
			return false;
		tile = tiles[secondY][secondX];
		if (tile.isSolid())
			return false;
		
		return true;
	}
	
	public void render(Graphics g, Game game, TileManager tm){
		tileMap = convertIntMapToTileMap(intmap, tileMap, tm);
		for(int i = 0; i<54; i++)
			for(int z = 0; z<64; z++){
				if(tileMap[i][z].getImage() == null){
					tileMap[i][z].getAnimation().drawAnimation(g, (z*64)+xOffset, (i*64)+yOffset, 0);
					tileMap[i][z].getAnimation().runAnimation();
				}
				g.drawImage(tileMap[i][z].getImage(), (z*64)+xOffset, (i*64)+yOffset, game);
			}
		
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
					emptyTileMap[i][z] = tm.getTile("darkstone");
					break;
				case 2:
					emptyTileMap[i][z] = tm.getTile("watertile");
					break;
				case 3:
					emptyTileMap[i][z] = tm.getTile("sandtile");
					break;
				case 4:
					emptyTileMap[i][z] = tm.getTile("darkstone");
					break;
				case 5:
					emptyTileMap[i][z] = tm.getTile("sandtile");
					break;
				case 6:
					emptyTileMap[i][z] = tm.getTile("sandtile");
					break;
				case 7:
					emptyTileMap[i][z] = tm.getTile("darkstone");
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
