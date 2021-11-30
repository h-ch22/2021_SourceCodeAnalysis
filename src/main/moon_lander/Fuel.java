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

    public static int fuelX;

    public static int fuelY;

    private int consumeFuel;

    private int remainingFuel = 100;

    public boolean isFuelEmpty;

    private BufferedImage fuelImg;

    public static int fuelImgWidth;

    public static int fuelImgHeight;

    private static Random random;

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
        }
        if(GameManager.isPaused){
            consumeFuel = 0;
        }
        else{
            consumeFuel = 1;
        }
    }

    public void ResetFuel(){

        remainingFuel = 100;

        relocationFuel();
    }

    public void crashCheck(){
        if(PlayerRocket.x + PlayerRocket.rocketImgWidth > fuelX && PlayerRocket.x < fuelX + fuelImgWidth
                && PlayerRocket.y + PlayerRocket.rocketImgHeight > fuelY && PlayerRocket.y < fuelY + fuelImgHeight){
            remainingFuel = 100;
            relocationFuel();
        }
    }

    public static void relocationFuel(){

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
