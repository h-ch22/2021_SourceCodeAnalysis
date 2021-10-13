package main.moon_lander;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;


public class BumperManager {

	private int w=Framework.frameWidth;
	private int h=Framework.frameHeight;

	private Bumper[] bumpers;

	private int[][] bumperXY;

	private int rocketW=PlayerRocket.rocketImgWidth-16;
	private int rocketH=PlayerRocket.rocketImgHeight-4;

	private int bumperRange;
	private int amountOfBumper;


	public BumperManager(int m)
	{
		try {
			GetData(m);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Initialize();
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
			bumperRange = bumperImg.getWidth()/2 - 5;

		}
		catch (IOException ex) {
			Logger.getLogger(LandingArea.class.getName()).log(Level.SEVERE, null, ex);
		}
	}


	private void Initialize()
	{
		bumpers = new Bumper[amountOfBumper];
		for (int i = 0; i < amountOfBumper; i++) {
			bumpers[i]=new Bumper(bumperXY[i][0], bumperXY[i][1]);
		}
	}

	public void checkCollision(int x, int y)
	{
		x += 10;
		y += 2;
		int speed = (int) Math.sqrt((Math.pow(PlayerRocket.speedX, 2)+Math.pow(PlayerRocket.speedY, 2)));

		for (int i = 0; i < amountOfBumper; i++) {

			int tx = bumperXY[i][0];
			int ty = bumperXY[i][1];

			if(!bumpers[i].active) {
				if(((y-bumperRange<=ty&&ty<y)
						||(y+rocketH<ty&&ty<=y+rocketH+bumperRange))&&(x<tx&&tx<x+rocketH)) { //위 또는 아래에 있을 때
					PlayerRocket.speedX *= -1;
					if(Math.abs(PlayerRocket.speedY)<3) {
						PlayerRocket.speedY = (y+rocketH/2<ty) ? -5 : 5;
					}
				}
				else if(((x-bumperRange<=tx&&tx<x)
						||(x+rocketW<tx&&tx<=x+rocketW+bumperRange))&&(y<ty&&ty<y+rocketW)) {//왼쪽 또는 오른쪽에 있을 때
					PlayerRocket.speedY *= -1;
					if(Math.abs(PlayerRocket.speedX)<3) {
						PlayerRocket.speedX = (x+rocketW/2<tx) ? -5 : 5;
					}
				}
				else if(checkBumperInside(x,y,tx,ty)) { //왼쪽 위
					calculateSpeed(x-tx,y-ty, speed);
					if(x<tx) PlayerRocket.speedX *=-1;
					if(y<ty) PlayerRocket.speedY *=-1;
				}
				else if(checkBumperInside(x+rocketW,y,tx,ty)) { //오른쪽 위
					calculateSpeed(x+rocketW-tx,y-ty, speed);
					if(tx<x+rocketW) PlayerRocket.speedX *=-1;
					if(y<ty) PlayerRocket.speedY *=-1;
				}
				else if(checkBumperInside(x,y+rocketH,tx,ty)) { //왼쪽 아래
					calculateSpeed(x-tx,y+rocketH-ty, speed);
					if(x<tx) PlayerRocket.speedX *=-1;
					if(ty<y+rocketH) PlayerRocket.speedY *=-1;
				}
				else if(checkBumperInside(x+rocketW,y+rocketH,tx,ty)) { //오른쪽 아래
					calculateSpeed(x+rocketW-tx,y+rocketH-ty, speed);
					if(tx<x+rocketW) PlayerRocket.speedX *=-1;
					if(ty<y+rocketH) PlayerRocket.speedY *=-1;
				}

				//    		else if((x<=tx&&tx<=x+rocketH)&&(y<=ty&&ty<=y+rocketW)){
				//    			calculateSpeed(x+rocketW/2-tx, y+rocketH/2-ty, speed);
				//    		}
			}

			else {
				if(System.currentTimeMillis()-bumpers[i].activeTime>50) {
					bumpers[i].active=false;
					bumpers[i].activeTime = System.currentTimeMillis();
				}
			}
			if((x<=tx&&tx<=x+rocketH)&&(y<=ty&&ty<=y+rocketW)) {
				PlayerRocket.x += tx+rocketH/2-x;
				PlayerRocket.y += ty+rocketH/2-y;
			}

		}

	}
	private boolean checkBumperInside(int x, int y, int tx, int ty) {
		if(Math.sqrt((Math.pow(x-tx, 2)+Math.pow(y-ty, 2)))<=1.5*bumperRange) return true;
		return false;
	}

	private void calculateSpeed(int x, int y, int v) {

		if(x!=0) {
			int t = (int) (((x)/Math.abs(x)) * v / Math.sqrt(Math.pow(y/x, 2)+1));
			int q = (int) (((x)/Math.abs(x))  * v / Math.sqrt(Math.pow(y/x, 2)+1) * y/x);
			PlayerRocket.speedX = t;
			PlayerRocket.speedY = q;
		}
		else if(y!=0) {
			int t = (int) (((y)/Math.abs(y)) * v / Math.sqrt(Math.pow(x/y, 2)+1));
			int q = (int) (((y)/Math.abs(y)) * v / Math.sqrt(Math.pow(x/y, 2)+1) * x/y);
			PlayerRocket.speedX = t;
			PlayerRocket.speedY = q;
		}
	}

	public void Draw(Graphics2D g2d)
	{
		for (Bumper i : bumpers) {
			i.Draw(g2d);
		}
	}

}
