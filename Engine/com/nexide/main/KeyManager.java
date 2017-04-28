package com.nexide.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.nexide.main.entities.Player;

public class KeyManager implements KeyListener {
	
	private Player player;
	
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		
		if(keyCode == e.VK_W){
			Player.setY(Player.getYVel());
		}
		
		
		if(keyCode == e.VK_ESCAPE)
			System.exit(0);
	}

	public void keyReleased(KeyEvent e) {
		
	}

	public void keyTyped(KeyEvent e) {
		
	}

}
