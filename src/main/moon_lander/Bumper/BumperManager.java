package main.moon_lander.Bumper;

import main.moon_lander.Framework;
import main.moon_lander.LandingArea;
import main.moon_lander.PlayerRocket;

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

	private int w= Framework.frameWidth;
	private int h=Framework.frameHeight;

	private Bumper[] bumpers;

	private int[][] bumperXY;

	private int rocketW;
	private int rocketH;

	private int bumperRange;
	private int amountOfBumper;



	public BumperManager(int m, PlayerRocket p)
	{
		try {
			getData(m);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initialize(p);
	}

	private void getData(int m) throws IOException {
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
		getRange();
	}

	private void getRange()
	{
		try
		{
			URL BumperImgUrl = this.getClass().getClassLoader().getResource("bumper.png");
			BufferedImage bumperImg = ImageIO.read(BumperImgUrl);
			bumperRange = bumperImg.getWidth()/2-2;

		}
		catch (IOException ex) {
			Logger.getLogger(LandingArea.class.getName()).log(Level.SEVERE, null, ex);
		}
	}


	private void initialize(PlayerRocket p)
	{
		bumpers = new Bumper[amountOfBumper];
		for (int i = 0; i < amountOfBumper; i++) {
			bumpers[i]=new Bumper(bumperXY[i][0], bumperXY[i][1]);
		}
		rocketW=p.getRocketImgWidth();
		rocketH=p.getRocketImgHeight();
	}

	public void checkCollision(PlayerRocket p, int x, int y)
	{

		int speedX = p.getSpeedX();
		int speedY = p.getSpeedY();
		int rocketR = rocketH / 2 - 7;

		x+=rocketW/2;
		y+=rocketH/2;

		double speed = Math.sqrt(Math.pow(speedX,2)+Math.pow(speedY,2));
		if(speed<15) speed = 15;

		double dx=0, dy=0;

		for (int i = 0; i < amountOfBumper; i++) {

			int tx = Math.abs(bumperXY[i][0]-x);	//범퍼-로켓 거리
			int ty = Math.abs(bumperXY[i][1]-y);

			if(!bumpers[i].isActive()) {		//중복 적용 방지
				if(Math.pow(tx, 2)+Math.pow(ty, 2) <= Math.pow(bumperRange+rocketR,2)) {
					bumpers[i].setActive(true);;
					dx += x-bumperXY[i][0];
					dy += y-bumperXY[i][1];
				}
			}

			else {
				if(System.currentTimeMillis()-bumpers[i].getActiveTime()>100) {
					bumpers[i].setActive(false);;
					bumpers[i].setActiveTime(System.currentTimeMillis());
				}
			}
		}

		if(dx!=0||dy!=0) {
			double d = Math.sqrt(Math.pow(dx, 2)+Math.pow(dy, 2));

			dx = (dx/d*speed-speedX)/2;	//기존 속도 반전과 범퍼 탄성 반전의 평균치
			dy = (dy/d*speed-speedY)/2;


			p.setSpeedX((int)dx);
			p.setSpeedY((int)dy);
		}

	}


	public void draw(Graphics2D g2d)
	{
		for (Bumper i : bumpers) {
			i.draw(g2d);
		}
	}

}
