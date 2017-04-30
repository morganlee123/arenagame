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
	
	public void tick(Player player){
		if(player.getCurrentDirection() == "up"){
			yOffset+=yVel;
		}else if(player.getCurrentDirection() == "down"){
			yOffset-=yVel;
		}

		if(player.getCurrentDirection() == "right"){
			xOffset-=xVel;
		}else if(player.getCurrentDirection() == "left"){
			xOffset+=xVel;
		}
		
		//System.out.println("xOff: " + xOffset + ", yOff: " + yOffset);
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
					emptyTileMap[i][z] = tm.getTiles("grasstile")[4]; 
					break;
				case 1:
					emptyTileMap[i][z] = tm.getTiles("misc")[0];
					break;
				case 2:
					emptyTileMap[i][z] = tm.getTile("watertile");
					break;
				case 3:
					emptyTileMap[i][z] = tm.getTiles("sandtile")[4];
					break;
				case 4:
					emptyTileMap[i][z] = tm.getTiles("darkstone")[4];
					break;
				case 5:
					emptyTileMap[i][z] = tm.getTiles("stonetile")[4];
					break;
				case 6:
					emptyTileMap[i][z] = tm.getTiles("sandtile")[0];
					break;
				case 7:
					emptyTileMap[i][z] = tm.getTiles("sandtile")[2];
					break;
				case 8:
					emptyTileMap[i][z] = tm.getTiles("sandtile")[6];
					break;
				case 9:
					emptyTileMap[i][z] = tm.getTiles("sandtile")[8];
					break;
				case 10:
					emptyTileMap[i][z] = tm.getTiles("sandtile")[1];
					break;
				case 11:
					emptyTileMap[i][z] = tm.getTiles("sandtile")[7];
					break;
				case 12:
					emptyTileMap[i][z] = tm.getTiles("sandtile")[3];
					break;
				case 13:
					emptyTileMap[i][z] = tm.getTiles("sandtile")[5];
					break;
				case 14:
					emptyTileMap[i][z] = tm.getTiles("sandtile")[9];
					break;
				case 15:
					emptyTileMap[i][z] = tm.getTiles("sandtile")[10];
					break;
				case 16:
					emptyTileMap[i][z] = tm.getTiles("sandtile")[11];
					break;
				case 17:
					emptyTileMap[i][z] = tm.getTiles("sandtile")[12];
					break;
				case 18: //
 					emptyTileMap[i][z] = tm.getTiles("stonetile")[0];
					break;
				case 19:
					emptyTileMap[i][z] = tm.getTiles("stonetile")[2];
					break;
				case 20:
					emptyTileMap[i][z] = tm.getTiles("stonetile")[6];
					break;
				case 21:
					emptyTileMap[i][z] = tm.getTiles("stonetile")[8];
					break;
				case 22:
					emptyTileMap[i][z] = tm.getTiles("stonetile")[1];
					break;
				case 23:
					emptyTileMap[i][z] = tm.getTiles("stonetile")[7];
					break;
				case 24:
					emptyTileMap[i][z] = tm.getTiles("stonetile")[3];
					break;
				case 25:
					emptyTileMap[i][z] = tm.getTiles("stonetile")[5];
					break;
				case 26:
					emptyTileMap[i][z] = tm.getTiles("stonetile")[9];
					break;
				case 27:
					emptyTileMap[i][z] = tm.getTiles("stonetile")[10];
					break;
				case 28:
					emptyTileMap[i][z] = tm.getTiles("stonetile")[11];
					break;
				case 29:
					emptyTileMap[i][z] = tm.getTiles("stonetile")[12];
					break;
				case 30: //
 					emptyTileMap[i][z] = tm.getTiles("darkstone")[0];
					break;
				case 31:
					emptyTileMap[i][z] = tm.getTiles("darkstone")[2];
					break;
				case 32:
					emptyTileMap[i][z] = tm.getTiles("darkstone")[6];
					break;
				case 33:
					emptyTileMap[i][z] = tm.getTiles("darkstone")[8];
					break;
				case 34:
					emptyTileMap[i][z] = tm.getTiles("darkstone")[1];
					break;
				case 35:
					emptyTileMap[i][z] = tm.getTiles("darkstone")[7];
					break;
				case 36:
					emptyTileMap[i][z] = tm.getTiles("darkstone")[3];
					break;
				case 37:
					emptyTileMap[i][z] = tm.getTiles("darkstone")[5];
					break;
				case 38:
					emptyTileMap[i][z] = tm.getTiles("darkstone")[9];
					break;
				case 39:
					emptyTileMap[i][z] = tm.getTiles("darkstone")[10];
					break;
				case 40:
					emptyTileMap[i][z] = tm.getTiles("darkstone")[11];
					break;
				case 41:
					emptyTileMap[i][z] = tm.getTiles("darkstone")[12];
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

