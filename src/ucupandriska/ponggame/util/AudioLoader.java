package ucupandriska.ponggame.util;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class AudioLoader {

    /**
     * Loads an audio clip from the resource path.
     * 
     * @param path The resource path, e.g. "/resource/audio/pong.wav"
     * @return A Clip object, or null if loading failed.
     */
    public static Clip loadClip(String path) {
        try {
            InputStream audioSrc = AudioLoader.class.getResourceAsStream(path);
            if (audioSrc == null) {
                System.err.println("Audio resource not found: " + path);
                return null;
            }
            InputStream bufferedIn = new java.io.BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error loading audio: " + path);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Plays the given audio clip from the beginning.
     * 
     * @param clip The Clip to play.
     */
    public static void playClip(Clip clip) {
        if (clip == null)
            return;
        if (clip.isRunning())
            clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }
}