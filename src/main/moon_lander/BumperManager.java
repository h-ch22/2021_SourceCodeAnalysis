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

    private int rocketW=PlayerRocket.rocketImgWidth-30;
    private int rocketH=PlayerRocket.rocketImgHeight-8;

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
            bumperRange = bumperImg.getWidth()/2 - 7;

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

    	int speed = (int) Math.sqrt((Math.pow(PlayerRocket.speedX, 2)+Math.pow(PlayerRocket.speedY, 2)));
    	if(speed<15) speed = 15;
    	int temp;

    	x += PlayerRocket.speedX+15;	//다음 프레임의 로켓 위치
    	y += PlayerRocket.speedY+4;

    	int dx = (int) ((Math.abs(PlayerRocket.speedX)<10) ? Math.signum(PlayerRocket.speedX)*10 : PlayerRocket.speedX);	//속도 보정
    	int dy = (int) ((Math.abs(PlayerRocket.speedY)<10) ? Math.signum(PlayerRocket.speedY)*10 : PlayerRocket.speedY);

    	int vx;		//범퍼에 의한 속도 변환값
    	int vy;

        for (int i = 0; i < amountOfBumper; i++) {

        	vx=0;
        	vy=0;

    		int tx = bumperXY[i][0];	//범퍼 위치
    		int ty = bumperXY[i][1];
    		if(!bumpers[i].active) {
    			if((y-bumperRange<=ty&&ty<y)&&(x<tx&&tx<x+rocketW)) { //위에 있을 때
	    			vy += (dy!=0) ? -dy : -10;
	    		}
    			else if((y+rocketH<ty&&ty<=y+rocketH+bumperRange)&&(x<tx&&tx<x+rocketW)) { //아래에 있을 때
	    			vy += (dy!=0) ? -dy : 10;
	    		}
	    		else if((x-bumperRange<=tx&&tx<x)&&(y<ty&&ty<y+rocketH)) {//왼쪽에 있을 때
	    			vx += (dx!=0) ? -dx : -10;
	    		}
	    		else if((x+rocketW<tx&&tx<=x+rocketW+bumperRange)&&(y<ty&&ty<y+rocketH)) {//오른쪽에 있을 때
	    			vx += (dx!=0) ? -dx : 10;
	    		}
	    		else if(checkBumperInside(x,y,tx,ty)) { //왼쪽 위
	    			temp=calculateSpeed(x-tx,y-ty, speed);
	    			vx += temp;
	    			vy += Math.sqrt(Math.pow(speed, 2)-Math.pow(temp, 2));
	    		}
	    		else if(checkBumperInside(x+rocketW,y,tx,ty)) { //오른쪽 위
	    			temp=calculateSpeed(x+rocketW-tx,y-ty, speed);
	    			vx -= temp;
	    			vy += Math.sqrt(Math.pow(speed, 2)-Math.pow(temp, 2));
	    		}
	    		else if(checkBumperInside(x,y+rocketH,tx,ty)) { //왼쪽 아래
	    			temp=calculateSpeed(x-tx,(y+rocketH)-ty, speed);
	    			vx += temp;
	    			vy -= Math.sqrt(Math.pow(speed, 2)-Math.pow(temp, 2));
	    		}
	    		else if(checkBumperInside(x+rocketW,y+rocketH,tx,ty)) { //오른쪽 아래
	    			temp=calculateSpeed((x+rocketW)-tx,(y+rocketH)-ty, speed);
	    			vx -= temp;
	    			vy -= Math.sqrt(Math.pow(speed, 2)-Math.pow(temp, 2));
	    		}

	    		else if((x<=tx&&tx<=x+rocketW)&&(y<=ty&&ty<=y+rocketH)){
	    			temp=calculateSpeed((x+rocketW)/2-tx, (y+rocketH/2)-ty, speed);
	    			vx += temp;
	    			vy += Math.sqrt(Math.pow(speed, 2)-Math.pow(temp, 2));
	    		}
    		}

	    	else {
	    		if(System.currentTimeMillis()-bumpers[i].activeTime>100) {
	    			bumpers[i].active=false;
	    			bumpers[i].activeTime = System.currentTimeMillis();
	    		}
	    	}

    		if(vx!=0||vy!=0) {
    			bumpers[i].active = true;
    			PlayerRocket.speedX = (vx!=0) ? vx : PlayerRocket.speedX;
    			PlayerRocket.speedY = (vy!=0) ? vy : PlayerRocket.speedY;
    		}
    	}

    }
    private boolean checkBumperInside(int x, int y, int tx, int ty) {
    	if(Math.sqrt((Math.pow(x-tx, 2)+Math.pow(y-ty, 2)))<=1.5*bumperRange) return true;
    	return false;
    }

    private int calculateSpeed(int x, int y, int v) {

    	int t=(int)Math.sqrt(v);
    	if(x!=0) {
    		t = (int) (v / Math.sqrt(Math.pow((double)y/x, 2)+1));
    	}
    	else if(y!=0) {
    		t = (int) (v / Math.sqrt(Math.pow((double)x/y, 2)+1));
    	}
    	return t;
	}

    public void Draw(Graphics2D g2d)
    {
        for (Bumper i : bumpers) {
			i.Draw(g2d);
		}
    }

}
