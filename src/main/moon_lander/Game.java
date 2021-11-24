package main.moon_lander;

import Score.Helper.ScoreManagement;
import main.moon_lander.MobileController.MobileControlHelper;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Actual game.
 * 
 * @author www.gametutorial.net
 */

public class Game extends ScoreManagement {

    /**
     * The space rocket with which player will have to land.
     */
    private PlayerRocket playerRocket;
    
    /**
     * Landing area on which rocket will have to land.
     */
    private LandingArea landingArea;
    
    /**
     * Game background image.
     */
    private BufferedImage backgroundImg;

    private BufferedImage backgroundEarthImg;

    private BufferedImage backgroundSpaceImg;
    /**
     * Red border of the frame. It is used when player crash the rocket.
     */
    private BufferedImage redBorderImg;

    private MobileControlHelper controlHelper = new MobileControlHelper();

    private int stage;

    private String background;

    private String[] mapdata;
    private BumperManager bumperManager;
    private Fuel fuel;

    public Game(int stage)
    {
        super();

        this.stage = stage;

        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;
        
        Thread threadForInitGame = new Thread() {
            @Override
            public void run(){
                // Sets variables and objects for the game.
                Initialize();
                // Load game files (images, sounds, ...)
                LoadContent();
                Music.playMusic("src/resources/musics/backgroundmusic.wav",true);
                Framework.gameState = Framework.GameState.PLAYING_EARTH;
                controlHelper.updateGameStatus(Framework.gameState);
            }
        };

        threadForInitGame.start();
    }
    
    
   /**
     * Set variables and objects for the game.
     */
    private void Initialize()
    {
    	try {
			GetFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	int gravity = Integer.parseInt(mapdata[0]);
    	int landingAreaSpeed = Integer.parseInt(mapdata[1]);
        playerRocket = new PlayerRocket(gravity);
        landingArea  = new LandingArea(landingAreaSpeed);
        bumperManager = new BumperManager(stage);
        fuel = new Fuel();
    }

    private void GetFile() throws IOException {
    	InputStream in = this.getClass().getClassLoader().getResourceAsStream("MapData.txt");
    	BufferedReader br = new BufferedReader(new InputStreamReader(in));
    	for(int i=0;i<stage;i++) {
    		br.readLine();
    	}
    	mapdata = br.readLine().split(" ");
    	br.close();
    }
    
    /**
     * Load game files - images, sounds, ...
     */
    private void LoadContent()
    {
        try
        {
            URL backgroundImgUrl = this.getClass().getClassLoader().getResource("background.jpg");
            backgroundImg = ImageIO.read(backgroundImgUrl);
            URL backgroundEarthImgUrl = this.getClass().getClassLoader().getResource("background_earth.jpg");
            backgroundEarthImg = ImageIO.read(backgroundEarthImgUrl);
            URL backgroundSpaceImgUrl = this.getClass().getClassLoader().getResource("background_space.jpg");
            backgroundSpaceImg = ImageIO.read(backgroundSpaceImgUrl);

            URL redBorderImgUrl = this.getClass().getClassLoader().getResource("red_border.png");
            redBorderImg = ImageIO.read(redBorderImgUrl);
        }
        catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * Restart game - reset some variables.
     */
    public void RestartGame()
    {
        playerRocket.ResetPlayer();
        landingArea.ResetArea();
        fuel.ResetFuel();
        controlHelper.updateGameStatus(Framework.GameState.PLAYING_EARTH);
    }

    public void UpdateGame(long gameTime, Point mousePosition, long pauseTime)
    {
        // Move the rocket
        playerRocket.Update();
        landingArea.Update();
        fuel.Update(playerRocket);
        fuel.crashCheck(playerRocket);
        if(fuel.isFuelEmpty){
            playerRocket.crashed = true;
            Framework.gameState = Framework.GameState.GAMEOVER;
        }

        controlHelper.updateCoordinates(playerRocket.x, playerRocket.y);
        // Checks where the player rocket is. Is it still in the space or is it landed or crashed?
        // First we check bottom y coordinate of the rocket if is it near the landing area.
        if (Framework.gameState == Framework.GameState.PLAYING_MOON) {
            if (playerRocket.y + playerRocket.rocketImgHeight - 10 > landingArea.y) {
                // Here we check if the rocket is over landing area.
                if ((playerRocket.x > landingArea.x) && (playerRocket.x < landingArea.x + landingArea.landingAreaImgWidth - playerRocket.rocketImgWidth)) {
                    // Here we check if the rocket speed isn't too high.
                    if (playerRocket.speedY <= playerRocket.topLandingSpeed) {
                        playerRocket.landed = true;

                        updateScore((stage + 1) * 10000 * ((gameTime-pauseTime) / Framework.secInNanosec), Integer.toString(stage + 1));
                    } else
                        playerRocket.crashed = true;
                } else
                    playerRocket.crashed = true;

                Framework.gameState = Framework.GameState.GAMEOVER;
                controlHelper.updateGameStatus(Framework.gameState);
            }
            background = "Moon";
            bumperManager.checkCollision(playerRocket.x, playerRocket.y);
        }
        changeBackground();
    }
    private void changeBackground(){
        if(Framework.gameState==Framework.GameState.PLAYING_EARTH) {
            if(playerRocket.y<-70) {
                playerRocket.y=(int)(Framework.frameHeight * 0.9);
                Framework.gameState = Framework.GameState.PLAYING_SPACE;
            }
            background = "Earth";
        }
        if(Framework.gameState==Framework.GameState.PLAYING_SPACE) {
            if(playerRocket.y<-70) {
                Framework.gameState = Framework.GameState.PLAYING_MOON;
                playerRocket.y=0;
                playerRocket.speedY=0;
            }
            background = "Space";
        }
    }
    
    /**
     * Draw the game to the screen.
     * 
     * @param g2d Graphics2D
     * @param mousePosition current mouse position.
     */
    public void Draw(Graphics2D g2d, Point mousePosition)
    {
        if(background == "Moon"){
            g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
            landingArea.Draw(g2d);
            bumperManager.Draw(g2d);
        }
        else if(background == "Earth") {
            g2d.drawImage(backgroundEarthImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        }
        else if(background == "Space") {
            g2d.drawImage(backgroundSpaceImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        }
        playerRocket.Draw(g2d);
        fuel.Draw(g2d);
    }

    public void DrawPause(Graphics2D g2d, Point mousePosition)
    {
        g2d.drawString("PAUSE", Framework.frameWidth / 2 - 10, Framework.frameHeight / 2);

        playerRocket.Draw(g2d);
    }
    
    /**
     * Draw the game over screen.
     * 
     * @param g2d Graphics2D
     * @param mousePosition Current mouse position.
     * @param gameTime Game time in nanoseconds.
     */
    public void DrawGameOver(Graphics2D g2d, Point mousePosition, long gameTime, long pauseTime)
    {
        Draw(g2d, mousePosition);
        
        g2d.drawString("스페이스바 또는 엔터키를 누르면 다시 시작합니다.", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3 + 70);
        
        if(playerRocket.landed)
        {
            g2d.drawString("잘했어요!", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3);
            g2d.drawString((gameTime-pauseTime) / Framework.secInNanosec + "초만에 성공했습니다.", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3 + 20);
        }
        else
        {
            g2d.setColor(Color.red);
            g2d.drawString("게임 오버!", Framework.frameWidth / 2 - 95, Framework.frameHeight / 3);
            g2d.drawImage(redBorderImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        }
    }
}
