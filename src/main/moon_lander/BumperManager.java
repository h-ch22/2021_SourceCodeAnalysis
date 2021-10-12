package main.moon_lander;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;


public class BumperManager {
 
    private int w=Framework.frameWidth;
    private int h=Framework.frameHeight;

    private Bumper[] bumpers;
    
    private int[][] bumperXY;
    
    private int rocketW=PlayerRocket.rocketImgWidth;
    private int rocketH=PlayerRocket.rocketImgHeight;
    
    private int bumperRange;
	private int amountOfBumper;
	
	private boolean usableLocate[][] = new boolean[w][h];
    
	private long time=0;
	
    public BumperManager(int m)
    {
    	System.out.println("BumperI");
    	try {
			GetData(m);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println("BumperO");
        Initialize();
    	System.out.println("BumperO");
    }
    
    private void GetData(int m) throws IOException {
    	InputStream in = this.getClass().getClassLoader().getResourceAsStream("BumperData.txt");
    	BufferedReader br = new BufferedReader(new InputStreamReader(in));
    	for(int i=0;i<m*2;i++) {
    		br.readLine();
    	}
    	amountOfBumper=Integer.parseInt(br.readLine());
    	bumperXY = new int[amountOfBumper][2];
    	String[] x = br.readLine().split(" ");
    	for (int i = 0; i < amountOfBumper; i++) {
    		bumperXY[i][0] = (int) (w * Double.parseDouble(x[2*i]));
    		bumperXY[i][1] = (int) (h * Double.parseDouble(x[2*i+1]));
		}
    	br.close();
    	GetRange();
	}
    
    private void GetRange()
    {
        try
        {
            URL BumperImgUrl = this.getClass().getClassLoader().getResource("bumper.png");
            BufferedImage bumperImg = ImageIO.read(BumperImgUrl);
            bumperRange = bumperImg.getWidth()/2;
            
        }
        catch (IOException ex) {
            Logger.getLogger(LandingArea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private void Initialize()
    {
        bumpers = new Bumper[amountOfBumper];
        int setUnit=0;
        while(setUnit<amountOfBumper) {
        	int x = bumperXY[setUnit][0];
        	int y = bumperXY[setUnit][1];
    		for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					if(Math.pow(x-i, 2)+Math.pow(y-j, 2)<=Math.pow(bumperRange,2)) usableLocate[i][j]=true;
				}
			}
    		setUnit++;
        }
        for (int i = 0; i < amountOfBumper; i++) {
			bumpers[i]=new Bumper(bumperXY[i][0], bumperXY[i][1]);
		}
    }
    
   
    
    
    /**
     * Here we move the rocket.
     */
    public void checkCollision(int x, int y)
    {
    	for (int i = 0; i < rocketW; i++) {
			for (int j = 0; j < rocketH; j++) {
				if(i+x>=0&&i+x<w&&j+y>=0&&j+y<h)
					if(usableLocate[i+x][j+y]&&System.currentTimeMillis()-time>50) {
						time=System.currentTimeMillis();
						PlayerRocket.speedX *=-1;
						PlayerRocket.speedY *=-1;
						return;
					}
			}
		}
    }
    
    public void Draw(Graphics2D g2d)
    {
        for (Bumper i : bumpers) {
			i.Draw(g2d);
		}
    }

}
