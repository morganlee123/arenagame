package com.nexide.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JFrame;

import com.nexide.main.entities.Player;
import com.nexide.main.entities.TileManager;
import com.nexide.main.gfx.ImageLoader;
import com.nexide.main.gfx.ImageManager;
import com.nexide.main.gfx.SpriteSheet;
import com.nexide.main.net.ConnectToServer;

public class Game extends Canvas implements Runnable {
	
	private static String TITLE = "joey literally hardcodes everything";
	private static int WIDTH = 1280, HEIGHT = 800;

	private boolean running = false;
	private Thread gameThread;
	
	private ImageManager im;
	private SpriteSheet ss;
	
	private ImageLoader loader;
	
	private BufferedImage[] grasssandtiles, grassdirttiles, stonetiles, misctiles;
	
	private TileManager tm;
	
	private Map testMap;
	
	private Player player;
	
	private void init(){
		loader = new ImageLoader();
		
		ss = new SpriteSheet(loader.loadImage("/spritesheets/spritesheet.png")); 
		im = new ImageManager(ss);
		tm = new TileManager(im);
		
		loadTextures();
		
		// LOAD INTMAP
		
		int[][] intmap = {
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,6  ,14 ,30 ,38 ,41 ,35 ,35 ,35 ,35 ,35 ,35 ,35 ,35 ,40 ,39 ,31 ,15 ,7  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  },
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,6  ,14 ,30 ,38 ,41 ,33 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,32 ,40 ,39 ,31 ,15 ,7  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  },
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,6  ,14 ,30 ,38 ,41 ,33 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,32 ,40 ,39 ,31 ,15 ,7  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  },
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,12 ,30 ,38 ,41 ,33 ,0  ,0  ,17 ,11 ,11 ,11 ,11 ,11 ,11 ,16 ,0  ,0  ,32 ,40 ,39 ,31 ,13 ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  },
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,12 ,36 ,41 ,33 ,0  ,0  ,0  ,13 ,3  ,3  ,3  ,3  ,3  ,3  ,12 ,0  ,0  ,0  ,32 ,40 ,37 ,13 ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  },
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,12 ,36 ,37 ,0  ,0  ,0  ,0  ,13 ,3  ,3  ,3  ,3  ,3  ,3  ,12 ,0  ,0  ,0  ,0  ,36 ,37 ,13 ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  },
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,6  ,14 ,36 ,37 ,0  ,0  ,0  ,0  ,13 ,3  ,3  ,3  ,3  ,3  ,3  ,12 ,0  ,0  ,0  ,0  ,36 ,37 ,15 ,7  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  },
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,6  ,14 ,30 ,38 ,37 ,0  ,0  ,0  ,0  ,13 ,3  ,3  ,3  ,3  ,3  ,3  ,12 ,0  ,0  ,0  ,0  ,36 ,39 ,31 ,15 ,7  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  },
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,6  ,14 ,30 ,38 ,41 ,33 ,0  ,0  ,0  ,0  ,13 ,3  ,3  ,3  ,3  ,3  ,3  ,12 ,0  ,0  ,0  ,0  ,32 ,40 ,39 ,31 ,15 ,7  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  },
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,6  ,14 ,30 ,38 ,41 ,33 ,0  ,0  ,0  ,0  ,0  ,15 ,10 ,10 ,10 ,10 ,10 ,10 ,14 ,0  ,0  ,0  ,0  ,0  ,32 ,40 ,39 ,31 ,15 ,7  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  },
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,6  ,14 ,30 ,38 ,41 ,33 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,32 ,40 ,39 ,31 ,15 ,7  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  },
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,6  ,14 ,30 ,38 ,41 ,33 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,32 ,40 ,39 ,31 ,15 ,7  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  },
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,6  ,14 ,30 ,38 ,41 ,33 ,0  ,0  ,0  ,0  ,0  ,0  ,30 ,31 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,30 ,31 ,0  ,0  ,0  ,0  ,0  ,0  ,32 ,40 ,39 ,31 ,15 ,7  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  },
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,6  ,14 ,30 ,38 ,41 ,33 ,0  ,0  ,0  ,0  ,0  ,0  ,30 ,38 ,39 ,34 ,31 ,0  ,0  ,0  ,0  ,30 ,34 ,38 ,39 ,31 ,0  ,0  ,0  ,0  ,0  ,0  ,32 ,40 ,39 ,31 ,15 ,7  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  },
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,6  ,14 ,30 ,38 ,41 ,33 ,0  ,0  ,0  ,0  ,0  ,30 ,34 ,38 ,4  ,4  ,4  ,37 ,0  ,0  ,0  ,0  ,36 ,4  ,4  ,4  ,39 ,34 ,31 ,0  ,0  ,0  ,0  ,0  ,32 ,40 ,39 ,31 ,15 ,7  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  },
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,6  ,14 ,30 ,38 ,41 ,33 ,0  ,0  ,0  ,0  ,0  ,0  ,32 ,40 ,4  ,4  ,4  ,4  ,37 ,0  ,0  ,0  ,0  ,36 ,4  ,4  ,4  ,4  ,41 ,33 ,0  ,0  ,0  ,0  ,0  ,0  ,32 ,40 ,39 ,31 ,15 ,7  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  },
				{3  ,3  ,3  ,6  ,10 ,10 ,10 ,10 ,10 ,10 ,10 ,14 ,30 ,38 ,41 ,33 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,32 ,35 ,40 ,4  ,4  ,37 ,0  ,0  ,0  ,0  ,36 ,4  ,4  ,41 ,35 ,33 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,32 ,40 ,39 ,31 ,15 ,10 ,10 ,10 ,10 ,10 ,10 ,10 ,7  ,3  ,3  ,3  },
				{3  ,6  ,10 ,14 ,30 ,34 ,34 ,34 ,34 ,34 ,34 ,34 ,38 ,41 ,33 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,36 ,41 ,35 ,33 ,0  ,0  ,0  ,0  ,32 ,35 ,40 ,37 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,32 ,40 ,39 ,34 ,34 ,34 ,34 ,34 ,34 ,34 ,31 ,15 ,10 ,7  ,3  },
				{6  ,14 ,30 ,34 ,38 ,41 ,35 ,35 ,35 ,35 ,35 ,35 ,35 ,33 ,0  ,0  ,0  ,0  ,0  ,0  ,30 ,31 ,0  ,0  ,0  ,0  ,32 ,33 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,32 ,33 ,0  ,0  ,0  ,0  ,30 ,31 ,0  ,0  ,0  ,0  ,0  ,0  ,32 ,35 ,35 ,35 ,35 ,35 ,35 ,35 ,40 ,39 ,34 ,31 ,15 ,7  },
				{14 ,30 ,38 ,41 ,35 ,33 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,30 ,34 ,38 ,37 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,36 ,39 ,34 ,31 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,32 ,35 ,40 ,39 ,31 ,15 },
				{30 ,38 ,41 ,33 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,36 ,4  ,4  ,37 ,0  ,0  ,0  ,0  ,0  ,0  ,17 ,11 ,11 ,11 ,11 ,11 ,11 ,16 ,0  ,0  ,0  ,0  ,0  ,0  ,36 ,4  ,4  ,37 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,32 ,40 ,39 ,31 },
				{36 ,41 ,33 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,30 ,38 ,4  ,4  ,39 ,34 ,34 ,31 ,0  ,0  ,0  ,13 ,18 ,22 ,22 ,22 ,22 ,19 ,12 ,0  ,0  ,0  ,30 ,34 ,34 ,38 ,4  ,4  ,39 ,31 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,32 ,40 ,37 },
				{38 ,37 ,0  ,17 ,11 ,11 ,11 ,11 ,11 ,11 ,11 ,11 ,11 ,11 ,16 ,0  ,30 ,38 ,4  ,4  ,4  ,4  ,4  ,41 ,33 ,0  ,0  ,0  ,13 ,24 ,5  ,5  ,5  ,5  ,25 ,12 ,0  ,0  ,0  ,32 ,40 ,4  ,4  ,4  ,4  ,4  ,39 ,31 ,0  ,17 ,11 ,11 ,11 ,11 ,11 ,11 ,11 ,11 ,11 ,11 ,16 ,0  ,36 ,39 },
				{41 ,33 ,0  ,13 ,18 ,22 ,22 ,22 ,22 ,22 ,22 ,22 ,22 ,19 ,12 ,0  ,32 ,40 ,4  ,4  ,4  ,4  ,4  ,37 ,0  ,0  ,0  ,0  ,13 ,24 ,5  ,5  ,5  ,5  ,25 ,12 ,0  ,0  ,0  ,0  ,36 ,4  ,4  ,4  ,4  ,4  ,41 ,33 ,0  ,13 ,18 ,22 ,22 ,22 ,22 ,22 ,22 ,22 ,22 ,19 ,12 ,0  ,32 ,40 },
				{37 ,0  ,0  ,13 ,24 ,5  ,5  ,5  ,5  ,5  ,5  ,5  ,5  ,25 ,12 ,0  ,54 ,32 ,35 ,35 ,35 ,35 ,35 ,33 ,53 ,0  ,0  ,0  ,13 ,24 ,5  ,5  ,5  ,5  ,25 ,12 ,0  ,0  ,0  ,54 ,32 ,35 ,35 ,35 ,35 ,35 ,33 ,53 ,0  ,13 ,24 ,5  ,5  ,5  ,5  ,5  ,5  ,5  ,5  ,25 ,12 ,0  ,0  ,36 },
				{37 ,0  ,0  ,13 ,24 ,5  ,5  ,5  ,5  ,5  ,5  ,5  ,5  ,25 ,12 ,0  ,50 ,42 ,42 ,42 ,42 ,42 ,42 ,42 ,49 ,0  ,0  ,0  ,13 ,24 ,5  ,5  ,5  ,5  ,25 ,12 ,0  ,0  ,0  ,50 ,42 ,42 ,42 ,42 ,42 ,42 ,42 ,49 ,0  ,13 ,24 ,5  ,5  ,5  ,5  ,5  ,5  ,5  ,5  ,25 ,12 ,0  ,0  ,36 },
				{37 ,0  ,0  ,13 ,24 ,5  ,5  ,5  ,5  ,5  ,5  ,5  ,5  ,25 ,12 ,0  ,50 ,42 ,42 ,42 ,42 ,42 ,42 ,42 ,49 ,0  ,0  ,0  ,13 ,24 ,5  ,5  ,5  ,5  ,25 ,12 ,0  ,0  ,0  ,50 ,42 ,42 ,42 ,42 ,42 ,42 ,42 ,49 ,0  ,13 ,24 ,5  ,5  ,5  ,5  ,5  ,5  ,5  ,5  ,25 ,12 ,0  ,0  ,36 },
				{37 ,0  ,0  ,13 ,24 ,5  ,5  ,5  ,5  ,5  ,5  ,5  ,5  ,25 ,12 ,0  ,50 ,42 ,42 ,42 ,42 ,42 ,42 ,42 ,49 ,0  ,0  ,0  ,13 ,24 ,5  ,5  ,5  ,5  ,25 ,12 ,0  ,0  ,0  ,50 ,42 ,42 ,42 ,42 ,42 ,42 ,42 ,49 ,0  ,13 ,24 ,5  ,5  ,5  ,5  ,5  ,5  ,5  ,5  ,25 ,12 ,0  ,0  ,36 },
				{37 ,0  ,0  ,13 ,24 ,5  ,5  ,5  ,5  ,5  ,5  ,5  ,5  ,25 ,12 ,0  ,50 ,42 ,42 ,42 ,42 ,42 ,42 ,42 ,49 ,0  ,0  ,0  ,13 ,24 ,5  ,5  ,5  ,5  ,25 ,12 ,0  ,0  ,0  ,50 ,42 ,42 ,42 ,42 ,42 ,42 ,42 ,49 ,0  ,13 ,24 ,5  ,5  ,5  ,5  ,5  ,5  ,5  ,5  ,25 ,12 ,0  ,0  ,36 },
				{37 ,0  ,0  ,13 ,24 ,5  ,5  ,5  ,5  ,5  ,5  ,5  ,5  ,25 ,12 ,0  ,52 ,30 ,34 ,34 ,34 ,34 ,34 ,31 ,51 ,0  ,0  ,0  ,13 ,24 ,5  ,5  ,5  ,5  ,25 ,12 ,0  ,0  ,0  ,52 ,30 ,34 ,34 ,34 ,34 ,34 ,31 ,51 ,0  ,13 ,24 ,5  ,5  ,5  ,5  ,5  ,5  ,5  ,5  ,25 ,12 ,0  ,0  ,36 },
				{39 ,31 ,0  ,13 ,20 ,23 ,23 ,23 ,23 ,23 ,23 ,23 ,23 ,21 ,12 ,0  ,30 ,38 ,4  ,4  ,4  ,4  ,4  ,37 ,0  ,0  ,0  ,0  ,13 ,24 ,5  ,5  ,5  ,5  ,25 ,12 ,0  ,0  ,0  ,0  ,36 ,4  ,4  ,4  ,4  ,4  ,39 ,31 ,0  ,13 ,20 ,23 ,23 ,23 ,23 ,23 ,23 ,23 ,23 ,21 ,12 ,0  ,30 ,38 },
				{40 ,37 ,0  ,15 ,10 ,10 ,10 ,10 ,10 ,10 ,10 ,10 ,10 ,10 ,14 ,0  ,32 ,40 ,4  ,4  ,4  ,4  ,4  ,39 ,31 ,0  ,0  ,0  ,13 ,24 ,5  ,5  ,5  ,5  ,25 ,12 ,0  ,0  ,0  ,30 ,38 ,4  ,4  ,4  ,4  ,4  ,41 ,33 ,0  ,15 ,10 ,10 ,10 ,10 ,10 ,10 ,10 ,10 ,10 ,10 ,14 ,0  ,36 ,41 },
				{36 ,39 ,31 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,32 ,40 ,4  ,4  ,41 ,35 ,35 ,33 ,0  ,0  ,0  ,13 ,20 ,23 ,23 ,23 ,23 ,21 ,12 ,0  ,0  ,0  ,32 ,35 ,35 ,40 ,4  ,4  ,41 ,33 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,30 ,38 ,37 },
				{32 ,40 ,39 ,31 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,36 ,4  ,4  ,37 ,0  ,0  ,0  ,0  ,0  ,0  ,15 ,10 ,10 ,10 ,10 ,10 ,10 ,14 ,0  ,0  ,0  ,0  ,0  ,0  ,36 ,4  ,4  ,37 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,30 ,38 ,41 ,33 },
				{16 ,32 ,40 ,39 ,34 ,31 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,32 ,35 ,40 ,37 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,36 ,41 ,35 ,33 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,30 ,34 ,38 ,41 ,33 ,17 },
				{8  ,16 ,32 ,35 ,40 ,39 ,34 ,34 ,34 ,34 ,34 ,34 ,34 ,31 ,0  ,0  ,0  ,0  ,0  ,0  ,32 ,33 ,0  ,0  ,0  ,0  ,30 ,31 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,30 ,31 ,0  ,0  ,0  ,0  ,32 ,33 ,0  ,0  ,0  ,0  ,0  ,0  ,30 ,34 ,34 ,34 ,34 ,34 ,34 ,34 ,38 ,41 ,35 ,33 ,17 ,9  },
				{3  ,8  ,11 ,16 ,32 ,35 ,35 ,35 ,35 ,35 ,35 ,35 ,40 ,39 ,31 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,36 ,39 ,34 ,31 ,0  ,0  ,0  ,0  ,30 ,34 ,38 ,37 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,30 ,38 ,41 ,35 ,35 ,35 ,35 ,35 ,35 ,35 ,33 ,17 ,11 ,9  ,3  },
				{3  ,3  ,3  ,8  ,11 ,11 ,11 ,11 ,11 ,11 ,11 ,16 ,32 ,40 ,39 ,31 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,30 ,34 ,38 ,4  ,4  ,37 ,0  ,0  ,0  ,0  ,36 ,4  ,4  ,39 ,34 ,31 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,30 ,38 ,41 ,33 ,17 ,11 ,11 ,11 ,11 ,11 ,11 ,11 ,9  ,3  ,3  ,3  }, 
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,8  ,16 ,32 ,40 ,39 ,31 ,0  ,0  ,0  ,0  ,0  ,0  ,30 ,38 ,4  ,4  ,4  ,4  ,37 ,0  ,0  ,0  ,0  ,36 ,4  ,4  ,4  ,4  ,39 ,31 ,0  ,0  ,0  ,0  ,0  ,0  ,30 ,38 ,41 ,33 ,17 ,9  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  }, 
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,8  ,16 ,32 ,40 ,39 ,31 ,0  ,0  ,0  ,0  ,0  ,32 ,35 ,40 ,4  ,4  ,4  ,37 ,0  ,0  ,0  ,0  ,36 ,4  ,4  ,4  ,41 ,35 ,33 ,0  ,0  ,0  ,0  ,0  ,30 ,38 ,41 ,33 ,17 ,9  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  }, 
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,8  ,16 ,32 ,40 ,39 ,31 ,0  ,0  ,0  ,0  ,0  ,0  ,32 ,40 ,41 ,35 ,33 ,0  ,0  ,0  ,0  ,32 ,35 ,40 ,41 ,33 ,0  ,0  ,0  ,0  ,0  ,0  ,30 ,38 ,41 ,33 ,17 ,9  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  }, 
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,8  ,16 ,32 ,40 ,39 ,31 ,0  ,0  ,0  ,0  ,0  ,0  ,32 ,33 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,32 ,33 ,0  ,0  ,0  ,0  ,0  ,0  ,30 ,38 ,41 ,33 ,17 ,9  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  }, 
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,8  ,16 ,32 ,40 ,39 ,31 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,30 ,38 ,41 ,33 ,17 ,9  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  }, 
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,8  ,16 ,32 ,40 ,39 ,31 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,30 ,38 ,41 ,33 ,17 ,9  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  }, 
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,8  ,16 ,32 ,40 ,39 ,31 ,0  ,0  ,0  ,0  ,0  ,17 ,11 ,11 ,11 ,11 ,11 ,11 ,16 ,0  ,0  ,0  ,0  ,0  ,30 ,38 ,41 ,33 ,17 ,9  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  }, 
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,8  ,16 ,32 ,40 ,39 ,31 ,0  ,0  ,0  ,0  ,13 ,3  ,3  ,3  ,3  ,3  ,3  ,12 ,0  ,0  ,0  ,0  ,30 ,38 ,41 ,33 ,17 ,9  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  }, 
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,8  ,16 ,32 ,40 ,37 ,0  ,0  ,0  ,0  ,13 ,3  ,3  ,3  ,3  ,3  ,3  ,12 ,0  ,0  ,0  ,0  ,36 ,41 ,33 ,17 ,9  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  }, 
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,8  ,16 ,36 ,37 ,0  ,0  ,0  ,0  ,13 ,3  ,3  ,3  ,3  ,3  ,3  ,12 ,0  ,0  ,0  ,0  ,36 ,37 ,17 ,9  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  }, 
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,12 ,36 ,37 ,0  ,0  ,0  ,0  ,13 ,3  ,3  ,3  ,3  ,3  ,3  ,12 ,0  ,0  ,0  ,0  ,36 ,37 ,13 ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  }, 
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,12 ,36 ,39 ,31 ,0  ,0  ,0  ,13 ,3  ,3  ,3  ,3  ,3  ,3  ,12 ,0  ,0  ,0  ,30 ,38 ,37 ,13 ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  }, 
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,12 ,32 ,40 ,39 ,31 ,0  ,0  ,15 ,10 ,10 ,10 ,10 ,10 ,10 ,14 ,0  ,0  ,30 ,38 ,41 ,33 ,13 ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  }, 
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,8  ,16 ,32 ,40 ,39 ,31 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,30 ,38 ,41 ,33 ,17 ,9  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  }, 
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,8  ,16 ,32 ,40 ,39 ,31 ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,30 ,38 ,41 ,33 ,17 ,9  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  }, 
				{3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,8  ,16 ,32 ,40 ,39 ,34 ,34 ,34 ,34 ,34 ,34 ,34 ,34 ,38 ,41 ,33 ,17 ,9  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  ,3  }
				};
		
		testMap = new Map(intmap);
		this.addKeyListener(new KeyManager());
		this.addMouseListener(new MouseManager());
		
		ConnectToServer.initialize();
		
		player = new Player(this.getWidth() / 2, this.getHeight() / 2, im.getTileset("playersprites"));
		
	}
	
	private void loadTextures() {
		grasssandtiles = im.getTileset("grasssand");
		grassdirttiles = im.getTileset("grassdirt");
		stonetiles = im.getTileset("stone");
		misctiles = im.getTileset("misc");
		
	}
	
	
	
	private synchronized void start(){
		if(running)return;
		
		running = true;
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	private synchronized void stop(){
		if(!running)return;
		
		running = false;
		try {
			gameThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		int FPS = 30;
		long startTime;
		init();
		
		while(running){
			startTime = System.currentTimeMillis();
			tick();
			render();
			long totalTime = System.currentTimeMillis() - startTime;
			if (totalTime < 17)
				try {
					gameThread.sleep((17)-totalTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			
		}
	}
	
	private void tick(){
		player.tick();
		testMap.tick(player,tm);
	}
	
	private void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs==null){
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		//
		
		// to remove double buffer flickering on the outskirts of the map
		g.setColor(Color.GRAY);
		g.fillRect(-1500, 0, 4200, 4200);
		
		testMap.render(g, this, tm, im);
		player.render(g, this);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, 20));
		try {
			ConnectToServer.send("getScore");ConnectToServer.send("getScore");
			g.drawString("Red Team: " + ConnectToServer.receive().split("&")[1] + " | Blue Team: " + ConnectToServer.receive().split("&")[0], 30, 30);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//
		g.dispose();
		bs.show();
	}
	
	public static void main(String args[]){
		Game game = new Game();
		JFrame frame = new JFrame(TITLE);
	
		game.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		game.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		game.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		
		frame.add(game);
		frame.pack();
		
		frame.setLocationRelativeTo(null);
		frame.setFocusable(true);
		frame.toFront();
		frame.requestFocus();
		
		game.start();
	}

}
