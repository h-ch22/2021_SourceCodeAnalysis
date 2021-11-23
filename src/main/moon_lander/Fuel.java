package main.moon_lander;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Fuel {

    public int fuelX;

    public int fuelY;

    private int consumeFuel;

    private PlayerRocket playerRocket;

    private int rocketX = playerRocket.x;

    private int rocketY = playerRocket.y;

    private int rocketWidth = playerRocket.rocketImgWidth;

    private int rocketHeight = playerRocket.rocketImgHeight;

    private int remainingFuel = 100;

    public boolean isFuelEmpty;

    private BufferedImage fuelImg;

    public int fuelImgWidth;

    public int fuelImgHeight;

    private Random random;

    public Fuel(){
        Initiallize();
        LoadContent();
    }

    private void Initiallize() {

        consumeFuel = 1;

        isFuelEmpty = false;

        ResetFuel();
    }

    private void LoadContent() {
        try
        {
            URL fuelImgUrl = this.getClass().getClassLoader().getResource("fuel.png");
            fuelImg = ImageIO.read(fuelImgUrl);
            fuelImgWidth = fuelImg.getWidth();
            fuelImgHeight = fuelImg.getHeight();
        }
        catch (IOException ex) {
            Logger.getLogger(PlayerRocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Update(){

        remainingFuel -= consumeFuel;

        if(remainingFuel <= 0){
            isFuelEmpty = true;
            consumeFuel = 0;
        }


    }

    public void refueling(){
        remainingFuel = 100;
    }

    public void ResetFuel(){

        remainingFuel = 100;

        relocationFuel();
    }

    public void crashCheck(PlayerRocket playerRocket){
        if(playerRocket.x + playerRocket.rocketImgWidth > fuelX && playerRocket.x < fuelX + fuelImgWidth && playerRocket.y + playerRocket.rocketImgHeight > fuelY && playerRocket.y < fuelY + fuelImgHeight){
            refueling();
            relocationFuel();
        }
    }

    public void relocationFuel(){

        random = new Random();

        fuelX = random.nextInt(Framework.frameWidth - fuelImgWidth);
        fuelY = random.nextInt((int) (Framework.frameHeight * 0.75 - fuelImgHeight));
    }

    public void Draw(Graphics2D g2d)
    {
        g2d.setColor(Color.white);
        g2d.drawString("남은 연료량 : " + remainingFuel + " : " , 5, 30);

        g2d.drawImage(fuelImg, fuelX, fuelY, null);

    }
}
