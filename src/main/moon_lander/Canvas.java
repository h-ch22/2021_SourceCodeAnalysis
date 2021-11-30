package main.moon_lander;

import main.moon_lander.Controller.CanvasViewController;
import main.moon_lander.Home.Controller.HomeViewController;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.*;

/**
 * Create a JPanel on which we will draw and listen for keyboard and mouse events.
 * 
 * @author www.gametutorial.net
 */

public abstract class Canvas extends JPanel implements KeyListener {
    
    // Keyboard states - Here are stored states for keyboard keys - is it down or not.
    private static boolean[] keyboardState = new boolean[525];
    public JButton btn_myPage, btn_settings;

    public Window gameWindow;

    public Canvas(Window gameWindow) {
        // We use double buffer to draw on the screen.
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.setBackground(Color.black);
        this.gameWindow = gameWindow;
        
        // If you will draw your own mouse cursor or if you just want that mouse cursor disapear, 
        // insert "true" into if condition and mouse cursor will be removed.
        if(false)
        {
            BufferedImage blankCursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(blankCursorImg, new Point(0, 0), null);
            this.setCursor(blankCursor);
        }
        
        // Adds the keyboard listener to JPanel to receive key events from this component.
        this.addKeyListener(this);

        URL iconURL = this.getClass().getClassLoader().getResource("ic_myPage.png");
        ImageIcon ic_myPage = new ImageIcon(iconURL);

        Image iconImg = ic_myPage.getImage();
        Image scaledIcon = iconImg.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);
        ic_myPage = new ImageIcon(scaledIcon);

        btn_myPage = new JButton(ic_myPage);
        btn_myPage.setBorderPainted(false);
        btn_myPage.setContentAreaFilled(false);
        btn_myPage.setBounds(10, 10, 10, 10);
        btn_myPage.setSize(new Dimension(50, 50));

        URL settingsIconURL = this.getClass().getClassLoader().getResource("ic_settings.png");
        ImageIcon ic_settings = new ImageIcon(settingsIconURL);

        Image settingsImg = ic_settings.getImage();
        Image settingsScaledIcon = settingsImg.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ic_settings = new ImageIcon(settingsScaledIcon);

        btn_settings = new JButton(ic_settings);
        btn_settings.setBorderPainted(false);
        btn_settings.setContentAreaFilled(false);
        btn_settings.setBounds(700, 10, 10, 10);
        btn_settings.setSize(new Dimension(50, 50));

        btn_myPage.setName("btn_myPage");
        btn_settings.setName("btn_settings");

        new CanvasViewController(this);
    }

    public void placeUserInteractionButtons(boolean show){
        if(show){
            btn_myPage.setVisible(true);
            btn_settings.setVisible(true);

            this.add(btn_myPage);
            this.add(btn_settings);
        }

        else{
            btn_myPage.setVisible(false);
            btn_settings.setVisible(false);

            this.remove(btn_myPage);
            this.remove(btn_settings);
        }
    }
    
    // This method is overridden in Framework.java and is used for drawing to the screen.
    public abstract void Draw(Graphics2D g2d);
    
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;        
        super.paintComponent(g2d);        
        Draw(g2d);
    }
       
    
    // Keyboard
    /**
     * Is keyboard key "key" down?
     * 
     * @param key Number of key for which you want to check the state.
     * @return true if the key is down, false if the key is not down.
     */
    public static boolean keyboardKeyState(int key)
    {
        return keyboardState[key];
    }
    
    // Methods of the keyboard listener.
    @Override
    public void keyPressed(KeyEvent e) 
    {
        keyboardState[e.getKeyCode()] = true;
    }
    
    @Override
    public void keyReleased(KeyEvent e)
    {
        keyboardState[e.getKeyCode()] = false;
        keyReleasedFramework(e);
    }
    
    @Override
    public void keyTyped(KeyEvent e) { }
    
    public abstract void keyReleasedFramework(KeyEvent e);
    
}
