package com.nexide.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.nexide.main.entities.Player;

public class KeyManager implements KeyListener {
	
	private Player player;
	
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		
		if(keyCode == e.VK_W){
			Player.up = true;
		}else if(keyCode == e.VK_S){
			Player.down = true;
		}
		
		if(keyCode == e.VK_A){
			Player.left = true;
		}else if(keyCode == e.VK_D){
			Player.right = true;
		}
		
		if(keyCode == e.VK_ESCAPE)
			System.exit(0);
	}

	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		
		if(keyCode == e.VK_W){
			Player.up = false;
		}else if(keyCode == e.VK_S){
			Player.down = false;
		}
		
		if(keyCode == e.VK_A){
			Player.left = false;
		}else if(keyCode == e.VK_D){
			Player.right = false;
		}
	}

	public void keyTyped(KeyEvent e) {
		
	}

}
