package main.moon_lander;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;


public class Music {
	
	private static Clip clip;

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


 
}
