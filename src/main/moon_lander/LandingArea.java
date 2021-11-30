package main.moon_lander;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Landing area where rocket will land.
 * 
 * @author www.gametutorial.net
 */

public class LandingArea {
    
    /**
     * X coordinate of the landing area.
     */
    public int x;
    /**
     * Y coordinate of the landing area.
     */
    public int y;
    
    /**
     * Image of landing area.
     */
    private BufferedImage landingAreaImg;
    
    /**
     * Width of landing area.
     */
    public int landingAreaImgWidth;
    
    public boolean isGoingRight;

    public int vecticalPer100ms=5;

    public long waitTime=0;


    public LandingArea(int i)
    {
        Initialize(i);
        LoadContent();
    }
    
    
    private void Initialize(int i)
    {   
        // X coordinate of the landing area is at 46% frame width.
        x = (int)(Framework.frameWidth * 0.46);
        // Y coordinate of the landing area is at 86% frame height.
        y = (int)(Framework.frameHeight * 0.88);

        vecticalPer100ms = 5 + i;//��Ȯ���� mapdata.txt�� i*2�� ������ �ι�° ������
    }
    
    private void LoadContent()
    {
        try
        {
            URL landingAreaImgUrl = this.getClass().getClassLoader().getResource("landing_area.png");
            landingAreaImg = ImageIO.read(landingAreaImgUrl);
            landingAreaImgWidth = landingAreaImg.getWidth();
        }
        catch (IOException ex) {
            Logger.getLogger(LandingArea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Update() {
	    if(System.currentTimeMillis()-waitTime>100) {
	    	waitTime=System.currentTimeMillis();

	    	if(isGoingRight) { x += vecticalPer100ms;}
		    else { x -= vecticalPer100ms;}

		    if(x<=0) {
		    	x=0;
		    	isGoingRight = true;
		    }
		    else if(x+landingAreaImgWidth>Framework.frameWidth) {
		    	x=Framework.frameWidth-landingAreaImgWidth;
		    	isGoingRight = false;
		    }
	    }

    }

    public void ResetArea() {
    	Random random = new Random();
    	x=random.nextInt(Framework.frameWidth-landingAreaImgWidth);
    	isGoingRight=random.nextBoolean();
    }

    public void Draw(Graphics2D g2d)
    {
        g2d.drawImage(landingAreaImg, x, y, null);
    }
    
}
