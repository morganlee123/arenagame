package com.nexide.main.entities;

import java.awt.Graphics;

import com.nexide.main.Game;

public interface Entity {

	void tick();
	void render(Graphics g, Game game);
		
}
