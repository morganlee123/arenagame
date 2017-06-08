package com.nexide.main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import com.nexide.main.entities.Player;
import com.nexide.main.net.ConnectToServer;

public class MouseManager implements MouseListener {

	private int mouseX, mouseY;
	
	public void mouseClicked(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		
	}

	public void mouseReleased(MouseEvent e) {
		
	}
	
	public int getX(){
		return mouseX;
	}
	
	public int getY(){
		return mouseY;
	}

	
}
