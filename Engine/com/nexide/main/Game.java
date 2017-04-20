package com.nexide.main;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.nexide.main.gfx.ImageLoader;
import com.nexide.main.gfx.ImageManager;
import com.nexide.main.gfx.SpriteSheet;

public class Game extends Canvas implements Runnable {
	
	private static String TITLE = "morgan is the best engine programmer";
	private static int WIDTH = 1024, HEIGHT = 768;

	private boolean running = false;
	private Thread gameThread;
	
	private ImageManager im;
	private SpriteSheet ss;
	
	private ImageLoader loader;
	
	private BufferedImage[] grasstiles;
	
	private void init(){
		loader = new ImageLoader();
		
		ss = new SpriteSheet(loader.loadImage("res/spritesheets/spritesheet.png")); 
		im = new ImageManager(ss);
		
		loadTextures();
		
		this.addKeyListener(new KeyManager());
	}
	
	private void loadTextures() {
		grasstiles = im.getTileset("grass");
		
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

	}
	
	private void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs==null){
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		//
		
			// ALL DRAW GRAPHICS ARE TEMPORARY FOR TESTING PURPOSES
		
			for(int i = 0; i<8; i++){
				for(int z = 0; z<12; z++)
					g.drawImage(grasstiles[0], i*64, z*64, this);
			}
			for(int i = 9; i<16; i++){
				for(int z = 0; z<12; z++)
					g.drawImage(grasstiles[2], i*64, z*64, this);
			}
			for(int z = 0; z<12; z++)
				g.drawImage(grasstiles[1], 8*64, z*64, this);
			
		
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
		
		game.start();
	}

}
