package com.nexide.main;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;

import javax.print.attribute.standard.MediaSize.Other;

import com.nexide.main.entities.Player;
import com.nexide.main.entities.Tile;
import com.nexide.main.entities.TileManager;
import com.nexide.main.gfx.ImageManager;
import com.nexide.main.net.ConnectToServer;

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
	private boolean TMisInit = false;

	private Tile[][] tileMap;
	private int[][] intmap = {};

	private int xOffset, yOffset;
	private int xVel, yVel;

	public Map(int[][] intmap){
		this.intmap = intmap;

		xOffset = -1344;
		yOffset = 144;
		xVel = 5;
		yVel = 5;
		
	}
	
	public void tick(Player player, TileManager tman){
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
		
		
		//if (oldOffset != yOffset+xOffset)
			//System.out.println("xOff: " + xOffset + ", yOff: " + yOffset + ", x: " + (0 - (xOffset - 640)) + ", y: " + (0 - (yOffset - 400)));
		
		//System.out.println("xOff: " + xOffset + ", yOff: " + yOffset);
		
		try {
			
			ConnectToServer.send("sendInput/" + Player.direction + "/" + xOffset + "/" + yOffset + "/" + String.valueOf(Player.shooting) + "/");
			ConnectToServer.receive();
			ConnectToServer.send("getHealth");
			Player.health = Integer.parseInt(ConnectToServer.receive());
			if (Player.health <= 0)
				reset(player);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	private void respawn() {
		try {
			ConnectToServer.send("Respawn");
			String coords = ConnectToServer.receive();
			xOffset = Integer.parseInt(coords.split("&")[0]);
			yOffset = Integer.parseInt(coords.split("&")[0]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		
		if(tile == tman.getTiles("grasstile")[9]){
			xVel = 3;
			yVel = 3;
		}else{
			xVel = 5;
			yVel = 5;
		}
		
		return true;
	}
	
	private String debugOld01 = "void like Morgan's soul"; //lol, this will autofix itself
	
	public void render(Graphics g, Game game, TileManager tm, ImageManager im){
		if ((TMisInit = !TMisInit))
			tileMap = convertIntMapToTileMap(intmap, tileMap, tm);
		for(int i = 0; i<54; i++)
			for(int z = 0; z<64; z++){
				/*if(tileMap[i][z].getImage() == null){
					tileMap[i][z].getAnimation().drawAnimation(g, (z*64)+xOffset, (i*64)+yOffset, 0);
					tileMap[i][z].getAnimation().runAnimation();
				}*/
				g.drawImage(tileMap[i][z].getImage(), (z*64)+xOffset, (i*64)+yOffset, game);
			}
		
		//now we get positions of other players
		
				try {
					ConnectToServer.send("getUserPositions");
					String UPs = ConnectToServer.receive();
					ConnectToServer.send("getDirections");
					String DIRs = ConnectToServer.receive();
					for(int i = 0; i < UPs.split("&").length; i++) 
						if (onScreen(UPs.split("&")[i])) {
							int newX = xOffset - Integer.parseInt(UPs.split("&")[i].split(",")[0]) + 640;
							int newY = yOffset - Integer.parseInt(UPs.split("&")[i].split(",")[1]) + 400;
							//if (!(ConnectToServer.ID + ": DRAWING @" + UPs.split("&")[i] + " -> " + newX + "," + newY).equals(debugOld01))
								//System.out.println(ConnectToServer.ID + ": DRAWING @" + UPs.split("&")[i] + " -> " + newX + "," + newY);
							debugOld01 = ConnectToServer.ID + ": DRAWING @" + UPs.split("&")[i] + " -> " + newX + "," + newY;
							if (i < 4) g.setColor(Color.BLUE);
							if (i >= 4) g.setColor(Color.RED);
							g.fillOval(newX, newY , 64, 64);
							int sprite = 0;
							if (Integer.parseInt(DIRs.split("&")[i]) == 0) {
								sprite = 0; //up
							} else if (Integer.parseInt(DIRs.split("&")[i]) == 90) {
								sprite = 3; //right
							} else if (Integer.parseInt(DIRs.split("&")[i]) == 180) {
								sprite = 6; //down
							} else if (Integer.parseInt(DIRs.split("&")[i]) == 270) {
								sprite = 9; //left
							}
							g.drawImage(im.getTileset("playersprites")[sprite],newX,newY,64,64,game);
						}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//now draw health bar...
				int HEALTH = 100;
				//color goes from 00FF00 to FFFF00 to FF0000, or 0,255,0 to 255,255,0 to 255,0,0
				try {
					ConnectToServer.send("getHealth");
					HEALTH = Integer.parseInt(ConnectToServer.receive());
				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int ColorR = 0;
				if (HEALTH < 50)
					ColorR = (int)((double)HEALTH * (255.0/50.0));
				else
					ColorR = (int)(255.0 - (((double)HEALTH - 50.0) * (255.0/50.0)));
				int ColorG = (int)(((double)HEALTH - 50.0) * (255.0/50.0));
				if (ColorR > 255)
					ColorR = 255;
				if (ColorG > 255)
					ColorG = 255;
				if (ColorR < 0)
					ColorR = 0;
				if (ColorG < 0)
					ColorG = 0;
				int ColorB = 0;
				
				g.setColor(Color.BLACK);
				g.fillRect(1100, 50, 120, 50);
				g.setColor(new Color(ColorR, ColorG, ColorB));
				g.fillRect(1110, 60, HEALTH, 30);
				
	} 
	
	public boolean onScreen(String coords) {
		
		if (Integer.parseInt(coords.split(",")[0]) > xOffset - 640 && Integer.parseInt(coords.split(",")[0]) < xOffset + 640)
			if (Integer.parseInt(coords.split(",")[1]) > yOffset - 400 && Integer.parseInt(coords.split(",")[1]) < yOffset + 400)
				return true;
		return false;		
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
				case 42: //
					emptyTileMap[i][z] = tm.getTiles("grasstile")[9];
					break;
				case 43: 
					emptyTileMap[i][z] = tm.getTiles("grasstile")[0];
					break;
				case 44:
					emptyTileMap[i][z] = tm.getTiles("grasstile")[2];
					break;
				case 45:
					emptyTileMap[i][z] = tm.getTiles("grasstile")[6];
					break;
				case 46:
					emptyTileMap[i][z] = tm.getTiles("grasstile")[8];
					break;
				case 47:
					emptyTileMap[i][z] = tm.getTiles("grasstile")[1];
					break;
				case 48:
					emptyTileMap[i][z] = tm.getTiles("grasstile")[7];
					break;
				case 49:
					emptyTileMap[i][z] = tm.getTiles("grasstile")[3];
					break;
				case 50:
					emptyTileMap[i][z] = tm.getTiles("grasstile")[5];
					break;
				case 51:
					emptyTileMap[i][z] = tm.getTiles("grasstile")[10];
					break;
				case 52:
					emptyTileMap[i][z] = tm.getTiles("grasstile")[11];
					break;
				case 53:
					emptyTileMap[i][z] = tm.getTiles("grasstile")[12];
					break;
				case 54:
					emptyTileMap[i][z] = tm.getTiles("grasstile")[13];
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

