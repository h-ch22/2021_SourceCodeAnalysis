package main.moon_lander;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

class Bumper {
	/**
     * X coordinate of the landing area.
     */
    private int x;
    /**
     * Y coordinate of the landing area.
     */
    private int y;
    
    /**
     * Image of landing area.
     */
    private BufferedImage bumperImg;
    
    /**
     * Width of landing area.
     */
    
    public Bumper(int x, int y)
    {
        Initialize(x,y);
        LoadContent();
    }
    
    
    private void Initialize(int x, int y)
    {   
        this.x = x;
        this.y = y;
    }
    
    private void LoadContent()
    {
        try
        {
            URL BumperImgUrl = this.getClass().getClassLoader().getResource("bumper.png");
            bumperImg = ImageIO.read(BumperImgUrl);
        }
        catch (IOException ex) {
            Logger.getLogger(LandingArea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void Draw(Graphics2D g2d)
    {
        g2d.drawImage(bumperImg, x-(bumperImg.getWidth()/2), y-(bumperImg.getWidth()/2), null);
    }
}
