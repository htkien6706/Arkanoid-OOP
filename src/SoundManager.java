import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundManager {
    private Clip clip;

    // 🔊 Hàm phát âm thanh 1 lần (vd: click, va chạm)
    public void playSound(String filePath) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(filePath));
            Clip soundClip = AudioSystem.getClip();
            soundClip.open(audioIn);
            soundClip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🎵 Hàm bật nhạc nền (loop vô hạn)
    public void playLoop(String filePath) {
        stop(); // tránh phát 2 lần cùng lúc
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(filePath));
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ⏸ Dừng nhạc nền
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
}
