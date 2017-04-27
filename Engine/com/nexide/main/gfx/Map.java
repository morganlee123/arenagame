package com.nexide.main.gfx;

import java.awt.image.BufferedImage;
import java.awt.image.AffineTransformOp;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.awt.Graphics2D;

/**
 * Holds the MAP of the project, renders submaps for the player.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Map
{
    // in tile terms, VOID_GRASS = vvvvvg or so. to get other corners, simply transform it.
    //                             vvvvgg
    //                             vvvggg
    //                             vvgggg
    //                             vggggg
    
    private final int TILE_WIDTH = 16;
    private final int TILE_HEIGHT = 16;
    
    //-50 to -100 reserved for half tiles soon
    
    private final int ENEMY = -3; //enemy player
    private final int FRIENDLY = -2; //friendly player
    private final int PLAYER = -1; //native player
    private final int VOID = 0;
    private final int VOIDSTONE = 1;
    private final int STONE = 2;
    
    private static final int VIEWING_DISTANCE = 10;
    
    private BufferedImage sprites = null;
    
    private final int[][] MAP_DIAGRAM;
    
    private static final int[][] MAP_DIAGRAM_B = { //for testing puropses only xP
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    };
    
    private static final int[][] MAP_DIAGRAM_A = {
    //                         5                       10                       15                       20                       25
        {  0 ,  0 ,  0 ,  0 ,  0 ,  0 ,  0 ,  0 ,  0 ,  0 ,  0 ,  0 ,  0 ,  0 ,  0 ,  0 ,  0 ,  0 ,  0 ,  0 ,  0 ,  0 ,  0 ,  0 ,  0 ,  0 ,},
        {},
        {},
        {},
        {}, //5
        {},
        {},
        {},
        {},
        {}, //10
        {},
        {},
        {},
        {},
        {}, //15
        {},
        {},
        {},
        {},
        {}, //20
        {},
        {},
        {},
        {},
        {}, //25
        {},
        {},
        {},
        {},
        {}, //30
        {},
        {},
        {},
        {},
        {}, //35
        {},
        {},
        {},
        {},
        {}, //40
        {},
        {},
        {},
        {},
        {}, //45
        {},
        {},
        {},
        {},
        {}, //50
        {},
        {},
        {},
        {}, //54
    };
    public Map(String spriteSheet, int mapNumber) //map number better be 1, ya hear me?
    {
        if (mapNumber==1) {
            MAP_DIAGRAM = MAP_DIAGRAM_A;
        } else {
            MAP_DIAGRAM = MAP_DIAGRAM_B;
        }
        try { sprites = ImageIO.read(new File(spriteSheet)); } catch(IOException e) { e.printStackTrace(); }
        if (sprites==null) return;
    }

    public boolean didProperlyInit() {
        return !(sprites==null);
    }
    
    private BufferedImage getSprite(int spriteID, double degreesRotationFromNorth) {
        int x = 0;
        int y = 0;
        switch(spriteID) {
            case PLAYER:
                x = 5;
                y = 0;
                break;
            case VOID:
                x = 0;
                y = 0;
                break;
        }
        return new AffineTransformOp(AffineTransform.getRotateInstance(Math.toRadians(degreesRotationFromNorth),TILE_WIDTH/2,TILE_HEIGHT/2), AffineTransformOp.TYPE_BILINEAR).filter(sprites.getSubimage(x*TILE_WIDTH,y*TILE_HEIGHT,TILE_WIDTH,TILE_HEIGHT),null);
    }
    
    private BufferedImage getSprite(int spriteID) {
        int x = 0;
        int y = 0;
        switch(spriteID) {
            case PLAYER:
                x = 5;
                y = 0;
                break;
            case VOID:
                x = 0;
                y = 0;
                break;
        }
        return sprites.getSubimage(x*TILE_WIDTH,y*TILE_HEIGHT,TILE_WIDTH,TILE_HEIGHT);
    }
    
    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public BufferedImage renderSubimage(int xPos, int yPos, double degreesRotationFromNorth)
    {
        //1. construct an image 11 tiles long in each direction from the player's center position.
        
        BufferedImage newRender = new BufferedImage((VIEWING_DISTANCE*2 + 3) * TILE_WIDTH, (VIEWING_DISTANCE*2 + 3) * TILE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newRender.createGraphics();
        
        //2. Draw the background
        
        for (int i = -11; i < 12; i++)
            for (int j = -11; j < 12; j++)
                g.drawImage(getSprite(validate(MAP_DIAGRAM,yPos+i,xPos+j,0)),(TILE_WIDTH) * (j + 11),(TILE_HEIGHT) * (i + 11), null);
        
        //3. Draw the native player.
        
        g.drawImage(getSprite(PLAYER,degreesRotationFromNorth),((VIEWING_DISTANCE*2 + 3) * TILE_WIDTH) / 2, ((VIEWING_DISTANCE*2 + 3) * TILE_HEIGHT)/2,null);
        
        //4. Draw the other player(s) if needed, as well as random bullets and other entities.
        
        g.drawImage(getSprite(PLAYER,0),((VIEWING_DISTANCE*2 + 3) * TILE_WIDTH) / 2, ((VIEWING_DISTANCE*2 + 3) * TILE_HEIGHT)/2,null);
        g.drawImage(getSprite(PLAYER,45),((VIEWING_DISTANCE*2 + 3) * TILE_WIDTH) / 2, ((VIEWING_DISTANCE*2 + 3) * TILE_HEIGHT)/2,null);
        g.drawImage(getSprite(PLAYER,90),((VIEWING_DISTANCE*2 + 3) * TILE_WIDTH) / 2, ((VIEWING_DISTANCE*2 + 3) * TILE_HEIGHT)/2,null);
        g.drawImage(getSprite(PLAYER,135),((VIEWING_DISTANCE*2 + 3) * TILE_WIDTH) / 2, ((VIEWING_DISTANCE*2 + 3) * TILE_HEIGHT)/2,null);
        g.drawImage(getSprite(PLAYER,180),((VIEWING_DISTANCE*2 + 3) * TILE_WIDTH) / 2, ((VIEWING_DISTANCE*2 + 3) * TILE_HEIGHT)/2,null);
        g.drawImage(getSprite(PLAYER,225),((VIEWING_DISTANCE*2 + 3) * TILE_WIDTH) / 2, ((VIEWING_DISTANCE*2 + 3) * TILE_HEIGHT)/2,null);
        g.drawImage(getSprite(PLAYER,270),((VIEWING_DISTANCE*2 + 3) * TILE_WIDTH) / 2, ((VIEWING_DISTANCE*2 + 3) * TILE_HEIGHT)/2,null);
        g.drawImage(getSprite(PLAYER,315),((VIEWING_DISTANCE*2 + 3) * TILE_WIDTH) / 2, ((VIEWING_DISTANCE*2 + 3) * TILE_HEIGHT)/2,null);
        g.drawImage(getSprite(PLAYER,347.8),((VIEWING_DISTANCE*2 + 3) * TILE_WIDTH) / 2, ((VIEWING_DISTANCE*2 + 3) * TILE_HEIGHT)/2,null);
        g.drawImage(getSprite(PLAYER,136.8),((VIEWING_DISTANCE*2 + 3) * TILE_WIDTH) / 2, ((VIEWING_DISTANCE*2 + 3) * TILE_HEIGHT)/2,null);
        g.drawImage(getSprite(PLAYER,237.6),((VIEWING_DISTANCE*2 + 3) * TILE_WIDTH) / 2, ((VIEWING_DISTANCE*2 + 3) * TILE_HEIGHT)/2,null);
        
        
            //TODO: THIS. above is simply a "graphically-intense" worst-case scenario to test fps
        
        //5. Return the new render.
        
        return newRender;
    }
    
    private int validate(int[][] data, int L1, int L2, int def) {
        try {
            int i = data[L1][L2];
            return i;
        } catch (Exception e) {
            return def;
        }
    }
}
