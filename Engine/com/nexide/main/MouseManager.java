package com.nexide.main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.nexide.main.entities.Player;

public class MouseManager implements MouseListener {

	public void mouseClicked(MouseEvent e) {
		Player.shooting = true;
		try {
			Thread.sleep(17);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Player.shooting = false;
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		
	}

	public void mouseReleased(MouseEvent e) {
		
	}

	
}
