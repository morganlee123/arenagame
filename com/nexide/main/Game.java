package com.team1.main;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {
	
	private static String TITLE = "game best = asdkajslk";
	private static int WIDTH = 1024, HEIGHT = 768;

	private boolean running = false;
	private Thread gameThread;
	
	private void init(){
		
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
	
	int x = 0, y = 10;
	private void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs==null){
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		//
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		
		g.fillRect(x+=4,y, 64, 64);
		
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
		
		game.start();
	}

}