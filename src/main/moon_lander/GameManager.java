package main.moon_lander;


import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class GameManager {

    private static int speedXPaused;
    private static int speedYPaused;
    public static boolean isPaused;

    private static Clip clip;

    public void ResetGameManager(){
        isPaused = false;
        speedXPaused = 0;
        speedYPaused = 0;
    }

    public static void pause(int speedX, int speedY){
        speedXPaused = speedX;
        speedYPaused = speedY;
        PlayerRocket.setSpeed(0, 0, 0);

    }
    public static void resume(){
        PlayerRocket.setSpeed(speedXPaused, speedYPaused, 1);

    }
    public void changeBackground(PlayerRocket playerRocket){
        if(Game.background == Game.Background.EARTH) {
            if(playerRocket.y<-70) {
                playerRocket.setRocketY((int)(Framework.frameHeight * 0.9));
                Game.background = Game.Background.SPACE;
            }
        }
        if(Game.background == Game.Background.SPACE) {
            if(playerRocket.y<-70) {
                Game.background = Game.Background.MOON;
                playerRocket.setRocketY(0);
                playerRocket.setSpeedY(0);
            }
        }
    }

    public static void stopMusic(){
        if(clip != null){
            clip.stop();
        }
    }
    public static void playMusic(String pathName, boolean isLoop) {
        stopMusic();
        try {
            clip = AudioSystem.getClip();
            File audioFile = new File(pathName);
            AudioInputStream ais = AudioSystem.getAudioInputStream(audioFile);
            clip.open(ais);
            clip.start();
            if(isLoop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        }catch(LineUnavailableException e) {
            e.printStackTrace();
        }
        catch(UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    public GameManager() {
        Initiallize();
    }

    private void Initiallize() {
        ResetGameManager();
    }
}