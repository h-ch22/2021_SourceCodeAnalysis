package main.moon_lander.Bumper;

import main.moon_lander.LandingArea;

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


    private boolean active;

    private long activeTime;


    /**
     * Width of landing area.
     */

    public Bumper(int x, int y)
    {
        initialize(x,y);
    }


    private void initialize(int x, int y)
    {
        this.x = x;
        this.y = y;

        try
        {
            URL bumperImgUrl = this.getClass().getClassLoader().getResource("bumper.png");
            bumperImg = ImageIO.read(bumperImgUrl);
        }
        catch (IOException ex) {
            Logger.getLogger(LandingArea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void draw(Graphics2D g2d)
    {
        g2d.drawImage(bumperImg, x-(bumperImg.getWidth()/2), y-(bumperImg.getWidth()/2), null);
    }


	public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}


	public long getActiveTime() {
		return activeTime;
	}


	public void setActiveTime(long activeTime) {
		this.activeTime = activeTime;
	}


}
