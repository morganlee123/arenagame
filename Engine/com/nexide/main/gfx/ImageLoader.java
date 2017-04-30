package com.nexide.main.gfx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.nexide.main.Game;

public class ImageLoader {

	public ImageLoader(){
		
	}
	
	public BufferedImage loadImage(String path){
		try {
			return ImageIO.read(Game.class.getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
