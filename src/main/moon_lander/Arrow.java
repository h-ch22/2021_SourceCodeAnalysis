package main.moon_lander;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class Arrow {
	
	private boolean isInFrame;
	
	private BufferedImage arrowImg;
	
	private int x, y, w, h;
	
	private double angle;
	
	private int rocketW, rocketH, frameW;
	
	private boolean overLeft, overRight, overTop;
	
	public Arrow(PlayerRocket p)
    {
        LoadContent(p);
    }
    
    private void LoadContent(PlayerRocket p)
    {
        try
        {
            URL ArrowImgUrl = this.getClass().getClassLoader().getResource("arrow.png");
            arrowImg = ImageIO.read(ArrowImgUrl);
        }
        catch (IOException ex) {
            Logger.getLogger(LandingArea.class.getName()).log(Level.SEVERE, null, ex);
        }
        rocketH = p.getRocketImgHeight();
        rocketW = p.getRocketImgWidth();
        frameW = Framework.frameWidth;

        w=arrowImg.getWidth();
        h=arrowImg.getHeight();
    }
    
    public void Update(int rocketX, int rocketY) {
    	if(rocketX + rocketW < 0) overLeft = true;
    	else overLeft = false;
    	
    	if(rocketX > frameW) overRight = true;
    	else overRight = false;
    	
    	if(rocketY + rocketH < 0) overTop = true;
    	else overTop = false;
    	
    	isInFrame = !(overLeft||overRight||overTop);
    	
    	if(!isInFrame) {
    		if(!overTop) {
				y = rocketY + rocketH/2 - w/2;
    			if(overLeft) {
    				x = 0;
    				angle=-Math.PI/2;
    			}
    			else {
    				x = frameW - h;
    				angle=Math.PI/2;
    			}
    		}
    		else {
    			if(!overLeft&&!overRight) {
    				x = rocketX + rocketW/2 - w/2;
    				y = -h / 2;
    				angle=0;
    			}
    			else {
    				double temp;
    				if(overLeft) {
    					x = 0;
    					y = 0;
    					temp = Math.atan((double)(rocketX+rocketW/2)/(-rocketY+rocketH/2));
    				}
    				else {
    					x = frameW - h;
    					y = 0;
    					temp = Math.atan((double)(rocketX+rocketW/2-frameW)/(-rocketY+rocketH/2));
    				}
					angle=temp;
    			}
    		}
    	}
    	
    }
    
    
    public void Draw(Graphics2D g2d)
    {
        // If the rocket in frame
        if(isInFrame)
        {
            
        }
        // If the rocket out frame.
        else
        {
        	BufferedImage newImageFromBuffer = new BufferedImage(w, h, arrowImg.getType());


            Graphics2D graphics2D = newImageFromBuffer.createGraphics();

            graphics2D.rotate(angle, w / 2, h / 2);
            graphics2D.drawImage(arrowImg, null, 0, 0);
            
            g2d.drawImage(newImageFromBuffer, x, y, null);
        }
    }

}
