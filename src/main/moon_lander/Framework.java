package main.moon_lander;

import Score.Helper.ScoreManagement;
import UserManagement.Helper.UserManagement;
import main.moon_lander.Home.Controller.HomeViewController;
import main.moon_lander.MobileController.MobileControlHelper;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Framework that controls the game (Game.java) that created it, update it and draw it on the screen.
 * 
 * @author www.gametutorial.net
 */

public class Framework extends Canvas {
    
    /**
     * Width of the frame.
     */
    public static int frameWidth;
    /**
     * Height of the frame.
     */
    public static int frameHeight;

    /**
     * Time of one second in nanoseconds.
     * 1 second = 1 000 000 000 nanoseconds
     */
    public static final long secInNanosec = 1000000000L;
    
    /**
     * Time of one millisecond in nanoseconds.
     * 1 millisecond = 1 000 000 nanoseconds
     */
    public static final long milisecInNanosec = 1000000L;
    
    /**
     * FPS - Frames per second
     * How many times per second the game should update?
     */
    private final int GAME_FPS = 24;
    /**
     * Pause between updates. It is in nanoseconds.
     */
    private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;

    /**
     * Possible states of the game
     */
    public static enum GameState{STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAMEOVER, DESTROYED}
    /**
     * Current state of the game
     */
    public static GameState gameState;
    
    /**
     * Elapsed game time in nanoseconds.
     */
    private long gameTime;
    // It is used for calculating elapsed time.
    private long lastTime;

    private long pauseTime;

    public final JButton[] buttons = {
            new JButton("stage 1"),
            new JButton("stage 2"),
            new JButton("stage 3"),
            new JButton("stage 4"),
            new JButton("stage 5")
    };

    // The actual game
    private Game game;
    private ScoreManagement scoreHelper = new ScoreManagement();
    private MobileControlHelper controlHelper = new MobileControlHelper();
    private UserManagement settings = new UserManagement();
    public static String useStoryMode = "true";
    /**
     * Image for menu.
     */
    private BufferedImage moonLanderMenuImg;
    private final HomeViewController controller = new HomeViewController(this);


    public Framework (Window gameWindow) {
        super(gameWindow);

        setUserPrefs();

        gameState = GameState.VISUALIZING;

        //We start game in new thread.
        Thread gameThread = new Thread() {
            @Override
            public void run(){
                GameLoop();
            }
        };
        gameThread.start();

    }

    public void setButton() {
        btn_myPage.setBorderPainted(false);
        btn_myPage.setContentAreaFilled(false);
        btn_myPage.setBounds(10, 10, 10, 10);
        btn_myPage.setSize(new Dimension(50, 50));
        btn_myPage.setName("btn_myPage");

        for(int i = 0; i < buttons.length; i++){
            switch(i){
                case 0:
                    buttons[i].setBounds(50, 500, 100, 50);

                case 1:
                    buttons[i].setBounds(200, 500, 100, 50);

                case 2:
                    buttons[i].setBounds(350, 500, 100, 50);

                case 3:
                    buttons[i].setBounds(500, 500, 100, 50);

                case 4:
                    buttons[i].setBounds(650, 500, 100, 50);
            }

            add(buttons[i]);
            buttons[i].setVisible(true);
            buttons[i].addActionListener(controller);
            buttons[i].setName("btn_stage_"+i);
        }
    }

    public void startGAME(int stage){
        for(JButton btn : buttons){
            btn.setVisible(false);
        }

        setUserPrefs();

        newGame(stage);
    }
    
   /**
     * Set variables and objects.
     * This method is intended to set the variables and objects for this class, variables and objects for the actual game can be set in Game.java.
     */
    private void Initialize()
    {
        setButton();
    }
    
    /**
     * Load files - images, sounds, ...
     * This method is intended to load files for this class, files for the actual game can be loaded in Game.java.
     */
    private void LoadContent()
    {
        try
        {
            URL moonLanderMenuImgUrl = this.getClass().getClassLoader().getResource("menu.jpg");
            moonLanderMenuImg = ImageIO.read(moonLanderMenuImgUrl);
        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * In specific intervals of time (GAME_UPDATE_PERIOD) the game/logic is updated and then the game is drawn on the screen.
     */
    private void GameLoop()
    {
        // This two variables are used in VISUALIZING state of the game. We used them to wait some time so that we get correct frame/window resolution.
        long visualizingTime = 0, lastVisualizingTime = System.nanoTime();
        // This variables are used for calculating the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
        long beginTime, timeTaken, timeLeft;
        
        while(true)
        {
            beginTime = System.nanoTime();

            switch (gameState)
            {
                case PLAYING:
                    if(!GameManager.isPaused) {
                        gameTime += System.nanoTime() - lastTime;

                        game.UpdateGame(gameTime, mousePosition(), pauseTime);

                        lastTime = System.nanoTime();
                    }

                    else{
                        pauseTime += System.nanoTime() - lastTime;

                        game.UpdateGame(gameTime, mousePosition(), pauseTime);

                        lastTime = System.nanoTime();
                    }
                break;

                case GAMEOVER:
                case MAIN_MENU:
                    controlHelper.receiveGameSTART(this);

                    break;
                case OPTIONS:
                    //...
                break;
                case GAME_CONTENT_LOADING:
                    //...
                break;
                case STARTING:
                    // Sets variables and objects.
                    Initialize();
                    // Load files - images, sounds, ...
                    LoadContent();

                    // When all things that are called above finished, we change game status to main menu.
                    gameState = GameState.MAIN_MENU;
                break;
                case VISUALIZING:
                    // On Ubuntu OS (when I tested on my old computer) this.getWidth() method doesn't return the correct value immediately (eg. for frame that should be 800px width, returns 0 than 790 and at last 798px). 
                    // So we wait one second for the window/frame to be set to its correct size. Just in case we
                    // also insert 'this.getWidth() > 1' condition in case when the window/frame size wasn't set in time,
                    // so that we although get approximately size.
                    if(this.getWidth() > 1 && visualizingTime > secInNanosec)
                    {
                        frameWidth = this.getWidth();
                        frameHeight = this.getHeight();

                        // When we get size of frame we change status.
                        gameState = GameState.STARTING;
                    }

                    else
                    {
                        visualizingTime += System.nanoTime() - lastVisualizingTime;
                        lastVisualizingTime = System.nanoTime();
                    }
                break;

            }
            
            // Repaint the screen.
            repaint();
            
            // Here we calculate the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
            timeTaken = System.nanoTime() - beginTime;
            timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec; // In milliseconds
            // If the time is less than 10 milliseconds, then we will put thread to sleep for 10 millisecond so that some other thread can do some work.
            if (timeLeft < 10) 
                timeLeft = 10; //set a minimum
            try {
                 //Provides the necessary delay and also yields control so that other thread can do work.
                 Thread.sleep(timeLeft);
            } catch (InterruptedException ex) { }
        }
    }
    
    /**
     * Draw the game to the screen. It is called through repaint() method in GameLoop() method.
     */
    @Override
    public void Draw(Graphics2D g2d)
    {
        switch (gameState)
        {
            case PLAYING:
                game.Draw(g2d, mousePosition());
                placeUserInteractionButtons(false);
                if(GameManager.isPaused) {
                    game.DrawPause(g2d, getMousePosition());
                }

                break;

            case GAMEOVER:
                game.DrawGameOver(g2d, mousePosition(), gameTime, pauseTime);
                scoreHelper.getRank();
                scoreHelper.drawRank(g2d);
                placeUserInteractionButtons(true);

                break;
            case MAIN_MENU:
                g2d.drawImage(moonLanderMenuImg, 0, 0, frameWidth, frameHeight, null);
                g2d.setColor(Color.white);

                for(JButton btn : buttons) {
                    btn.setVisible(true);
                }

                placeUserInteractionButtons(true);

                scoreHelper.getRank();
                scoreHelper.drawRank(g2d);
            break;
            case OPTIONS:
                //...
            break;
            case GAME_CONTENT_LOADING:
                g2d.setColor(Color.white);
                g2d.drawString("로드 중...", frameWidth / 2 - 50, frameHeight / 2);
            break;
        }
    }
    
    /**
     * Starts new game.
     */
    public void newGame(int stage)
    {
        // We set gameTime to zero and lastTime to current time for later calculations.
        gameTime = 0;
        lastTime = System.nanoTime();
        pauseTime = 0;
        game = new Game(stage);
    }
    
    /**
     *  Restart game - reset game time and call RestartGame() method of game object so that reset some variables.
     */
    public void restartGame()
    {
        // We set gameTime to zero and lastTime to current time for later calculations.
        gameTime = 0;
        lastTime = System.nanoTime();
        pauseTime = 0;
        game.RestartGame();
        
        // We change game status so that the game can start.
        gameState = GameState.MAIN_MENU;
    }
    
    /**
     * Returns the position of the mouse pointer in game frame/window.
     * If mouse position is null than this method return 0,0 coordinate.
     * 
     * @return Point of mouse coordinates.
     */
    private Point mousePosition()
    {
        try
        {
            Point mp = this.getMousePosition();
            
            if(mp != null)
                return this.getMousePosition();
            else
                return new Point(0, 0);
        }
        catch (Exception e)
        {
            return new Point(0, 0);
        }
    }
    
    /**
     * This method is called when keyboard key is released.
     * 
     * @param e KeyEvent
     */
    @Override
    public void keyReleasedFramework(KeyEvent e)
    {
        switch (gameState)
        {
            case MAIN_MENU:
//                newGame();
            break;

            case GAMEOVER:
                if(e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER)
                    restartGame();
            break;
        }
    }

    public void setUserPrefs(){
        if (settings.getUserPrefs().get("useStoryMode", "true").equals("true")){
            useStoryMode = "true";
        }

        else{
            useStoryMode = "false";
        }
    }
}
