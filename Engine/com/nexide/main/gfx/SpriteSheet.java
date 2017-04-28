package com.nexide.main.gfx;

import java.awt.image.BufferedImage;

public class SpriteSheet {

	private BufferedImage image;
	
	public SpriteSheet(BufferedImage image){
		this.image = image;
		
	}
	
	public BufferedImage crop(int a, int b, int width, int height){
		return image.getSubimage(a*16, b*16, width, height);
	}
}
