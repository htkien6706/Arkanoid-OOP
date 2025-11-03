import javax.sound.sampled.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private static SoundManager instance;
    private Map<String, Clip> soundClips = new HashMap<>();
    private Clip backgroundMusic;
    private boolean musicEnabled = true, soundEnabled = true;
    private float musicVolume = 0.7f, soundVolume = 0.8f;

    private SoundManager() {
        loadSound("menu_music", "sound/menu_music.wav");
        loadSound("game_music", "sound/game_music.wav");
        loadSound("click", "sound/click.wav");
        loadSound("paddle_hit", "sound/paddle_hit.wav");
        loadSound("brick_break", "sound/paddle_hit.wav");
        loadSound("game_over", "sound/gameover.wav");
        loadSound("wall_hit", "sound/paddle_hit.wav");
    }

    public static SoundManager getInstance() {
        if (instance == null) instance = new SoundManager();
        return instance;
    }

    public void loadSound(String name, String path) {
        try {
            File f = new File(path);
            if (!f.exists()) return;
            AudioInputStream audio = AudioSystem.getAudioInputStream(f);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            soundClips.put(name, clip);
        } catch (Exception e) {
            System.out.println("Không thể load âm thanh " + name + ": " + e.getMessage());
        }
    }

    private void setVolume(Clip clip, float volume) {
        if (clip == null) return;
        try {
            FloatControl ctrl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            ctrl.setValue((float) (20 * Math.log10(Math.max(volume, 0.0001f))));
        } catch (Exception ignored) {}
    }

    public void playSound(String name) {
        if (!soundEnabled) return;
        Clip c = soundClips.get(name);
        if (c != null) {
            if (c.isRunning()) c.stop();
            c.setFramePosition(0);
            setVolume(c, soundVolume);
            c.start();
        }
    }

    public void playBackgroundMusic(String name) {
        if (!musicEnabled) return;
        stopBackgroundMusic();
        backgroundMusic = soundClips.get(name);
        if (backgroundMusic != null) {
            backgroundMusic.setFramePosition(0);
            setVolume(backgroundMusic, musicVolume);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) backgroundMusic.stop();
    }

    public void setMusicEnabled(boolean enabled) {
        musicEnabled = enabled;
        if (!enabled) stopBackgroundMusic();
    }

    public void setSoundEnabled(boolean enabled) { soundEnabled = enabled; }
    public void setMusicVolume(float v) { musicVolume = clamp(v); setVolume(backgroundMusic, musicVolume); }
    public void setSoundVolume(float v) { soundVolume = clamp(v); }
    public boolean isMusicEnabled() { return musicEnabled; }
    public boolean isSoundEnabled() { return soundEnabled; }
    public float getMusicVolume() { return musicVolume; }
    public float getSoundVolume() { return soundVolume; }

    private float clamp(float v) { return Math.max(0, Math.min(1, v)); }
}
