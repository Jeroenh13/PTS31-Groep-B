/*
 * Plays audio
 */
package Music;

import java.io.File;
import java.io.IOException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author Bas
 */
public class MusicPlayer {

    private Media mediaE;
    private Media mediaM;
    private MediaPlayer music;
    private MediaPlayer effect;

    /**
     * Creates a new music player
     */
    public MusicPlayer() {
    }

    /**
     * Plays a sound
     *
     * @param sound sound to be played
     * @throws IOException
     */
    public void playSound(String sound) throws IOException {
        String path;
        switch (sound) {
//            case "BackgroundMusic":
//                path = new File(".").getCanonicalPath() + "\\src\\Music\\bg.mp3";
//                mediaM = new Media(new File(path).toURI().toString());
//                music = new MediaPlayer(mediaM);
//                music.play();
//                break;
            case "wait":
                path = new File(".").getCanonicalPath() + "\\src\\Music\\wait.mp3";
                mediaE = new Media(new File(path).toURI().toString());
                effect = new MediaPlayer(mediaE);
                effect.play();
                break;
            default:
                break;
        }
    }

    /**
     * Sets volume for effect
     *
     * @param level volumelevel
     */
    public void setVolumeEffecten(double level) {
        if (effect != null) {
            effect.setVolume(level/100);
        }
    }

    /**
     * Sets volume for music
     *
     * @param level volumelevel
     */
    public void setVolumeMusic(double level) {
        if (music != null) {
            music.setVolume(level/100);
        }
    }

    /**
     * stops all audio.
     */
    public void stop() {
        if (music != null) {
            music.stop();
            music.dispose();
        }
        if (effect != null) {
            effect.stop();
            effect.dispose();
        }
    }
}
