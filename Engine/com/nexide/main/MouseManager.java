package com.nexide.main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import com.nexide.main.entities.Player;
import com.nexide.main.net.ConnectToServer;

public class MouseManager implements MouseListener {

	public void mouseClicked(MouseEvent e) {
		try {
			ConnectToServer.send("Shoot");
			ConnectToServer.receive();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
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
