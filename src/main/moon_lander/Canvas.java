package main.moon_lander;

import main.moon_lander.Controller.CanvasViewController;
import main.moon_lander.MobileController.Observer.mobileControllerObserver;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Create a JPanel on which we will draw and listen for keyboard and mouse events.
 * 
 * @author www.gametutorial.net
 */

public abstract class Canvas extends JPanel implements KeyListener, mobileControllerObserver {
    
    // Keyboard states - Here are stored states for keyboard keys - is it down or not.
    private static boolean[] keyboardState = new boolean[525];
    
    // Mouse states - Here are stored states for mouse keys - is it down or not.
    private static boolean[] mouseState = new boolean[3];

    public JButton btn_myPage;
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

        System.out.println(iconURL);

        Image iconImg = ic_myPage.getImage(); // transform it
        Image scaledIcon = iconImg.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);
        ic_myPage = new ImageIcon(scaledIcon);

        btn_myPage = new JButton(ic_myPage);
        btn_myPage.setBorderPainted(false);
        btn_myPage.setContentAreaFilled(false);
        btn_myPage.setBounds(10, 10, 10, 10);
        btn_myPage.setSize(new Dimension(50, 50));

        new CanvasViewController(this);
    }

    public void placeMyPage(boolean show){
        if(show){
            btn_myPage.setVisible(true);

            this.add(btn_myPage);
        }

        else{
            btn_myPage.setVisible(false);
            this.remove(btn_myPage);
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
