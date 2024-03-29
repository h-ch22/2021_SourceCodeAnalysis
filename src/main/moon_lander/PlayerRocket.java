package main.moon_lander;

import main.moon_lander.MobileController.MobileControlHelper;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * The space rocket with which player will have to land.
 * 
 * @author www.gametutorial.net
 */

public class PlayerRocket extends MobileControlHelper {
    
    /**
     * We use this to generate a random number for starting x coordinate of the rocket.
     */
    private Random random;
 
    /**
     * X coordinate of the rocket.
     */
    public static int x;
    /**
     * Y coordinate of the rocket.
     */
    public static int y, yAxis;
    
    /**
     * Is rocket landed?
     */
    private boolean landed;
    
    /**
     * Has rocket crashed?
     */
    private boolean crashed;
    /**
     * Accelerating speed of the rocket.
     */
    private int speedAccelerating;
    /**
     * Stopping/Falling speed of the rocket. Falling speed because, the gravity pulls the rocket down to the moon.
     */
    private static int speedStopping;
    
    /**
     * Maximum speed that rocket can have without having a crash when landing.
     */
    private int topLandingSpeed;
    
    /**
     * How fast and to which direction rocket is moving on x coordinate?
     */
    public static int speedX;
    /**
     * How fast and to which direction rocket is moving on y coordinate?
     */
    public static int speedY;

    /**
     * Image of the rocket in air.
     */
    private BufferedImage rocketImg;
    /**
     * Image of the rocket when landed.
     */
    private BufferedImage rocketLandedImg;
    /**
     * Image of the rocket when crashed.
     */
    private BufferedImage rocketCrashedImg;
    /**
     * Image of the rocket fire.
     */
    private BufferedImage rocketFireImg;
    

    public static int rocketImgWidth;
    /**
     * Height of rocket.
     */
    public static int rocketImgHeight;
    
    private int gravity;


    public PlayerRocket(int i)
    {
        Initialize(i);
        LoadContent();
        
        // Now that we have rocketImgWidth we set starting x coordinate.
        x = random.nextInt(Framework.frameWidth - rocketImgWidth);
    }
    
    
    private void Initialize(int i)
    {
        random = new Random();
        
        ResetPlayer();
        
        speedAccelerating = 2;
        speedStopping = 1;

        gravity=i;
        
        topLandingSpeed = 5;
    }
    
    private void LoadContent()
    {
        try
        {
            URL rocketImgUrl = this.getClass().getClassLoader().getResource("rocket.png");
            rocketImg = ImageIO.read(rocketImgUrl);
            rocketImgWidth = rocketImg.getWidth();
            rocketImgHeight = rocketImg.getHeight();
            
            URL rocketLandedImgUrl = this.getClass().getClassLoader().getResource("rocket_landed.png");
            rocketLandedImg = ImageIO.read(rocketLandedImgUrl);
            
            URL rocketCrashedImgUrl = this.getClass().getClassLoader().getResource("rocket_crashed.png");
            rocketCrashedImg = ImageIO.read(rocketCrashedImgUrl);
            
            URL rocketFireImgUrl = this.getClass().getClassLoader().getResource("rocket_fire.png");
            rocketFireImg = ImageIO.read(rocketFireImgUrl);
        }
        catch (IOException ex) {
            Logger.getLogger(PlayerRocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Here we set up the rocket when we starting a new game.
     */
    public void ResetPlayer()
    {
        landed = false;
        crashed = false;
        x = random.nextInt(Framework.frameWidth - rocketImgWidth);
        y = 10;
        
        speedX = 0;
        speedY = 0;

    }
    
    
    /**
     * Here we move the rocket.
     */
    public void Update()
    {
        getUserControl();

        // Calculating speed for moving up or down.
        if(!GameManager.isPaused) {
            if (Canvas.keyboardKeyState(KeyEvent.VK_W))
                speedY -= speedAccelerating;
            else
                speedY += speedStopping+gravity;

            // Calculating speed for moving or stopping to the left.
            if (Canvas.keyboardKeyState(KeyEvent.VK_A))
                speedX -= speedAccelerating;
            else if (speedX < 0)
                speedX += speedStopping;

            // Calculating speed for moving or stopping to the right.
            if (Canvas.keyboardKeyState(KeyEvent.VK_D))
                speedX += speedAccelerating;
            else if (speedX > 0)
                speedX -= speedStopping;
            if (Canvas.keyboardKeyState(KeyEvent.VK_P)) {
                GameManager.isPaused = true;
                GameManager.pause(speedX, speedY);
            }
        }
        else {
                if(Canvas.keyboardKeyState(KeyEvent.VK_R)) {
                    GameManager.isPaused = false;
                    GameManager.resume();
                }
            }

        speedX += axisX;
        speedY -= axisY;

        x += speedX;
        y += speedY;
        }
    public static void setSpeed(int i, int j, int k){
        speedX = i;
        speedY = j;
        speedStopping = k;
    }

    public static void setRocketY(int i){
        y = i;
    }

    public void Draw(Graphics2D g2d)
    {
        g2d.setColor(Color.white);
        g2d.drawString("로켓 좌표 : " + x + " : " + y, 5, 15);
        
        // If the rocket is landed.
        if(landed)
        {
            g2d.drawImage(rocketLandedImg, x, y, null);
        }
        // If the rocket is crashed.
        else if(crashed)
        {
            g2d.drawImage(rocketCrashedImg, x, y + rocketImgHeight - rocketCrashedImg.getHeight(), null);
        }
        // If the rocket is still in the space.
        else
        {
            // If player hold down a W key we draw rocket fire.
            if(Canvas.keyboardKeyState(KeyEvent.VK_W) || axisY > 0)
                g2d.drawImage(rocketFireImg, x + 12, y + 66, null);
            g2d.drawImage(rocketImg, x, y, null);
        }
    }

    public int getX() {
		return x;
	}


	public int getY() {
		return y;
	}


	public int getyAxis() {
		return yAxis;
	}

	public int getSpeedAccelerating() {
		return speedAccelerating;
	}

	public int getSpeedStopping() {
		return speedStopping;
	}


	public int getTopLandingSpeed() {
		return topLandingSpeed;
	}


	public int getSpeedX() {
		return speedX;
	}


	public int getSpeedY() {
		return speedY;
	}


	public int getRocketImgWidth() {
		return rocketImgWidth;
	}


	public int getRocketImgHeight() {
		return rocketImgHeight;
	}


	public void setX(int x) {
		this.x = x;
	}


	public void setY(int y) {
		this.y = y;
	}


	public void setyAxis(int yAxis) {
		this.yAxis = yAxis;
	}


	public void setLanded(boolean landed) {
		this.landed = landed;
	}


	public void setCrashed(boolean crashed) {
		this.crashed = crashed;
	}


	public void setSpeedAccelerating(int speedAccelerating) {
		this.speedAccelerating = speedAccelerating;
	}


	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}


	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}

	public boolean isLanded() {
		return landed;
	}


}
