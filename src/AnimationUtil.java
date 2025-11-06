import java.awt.*;
import javax.swing.*;

public class AnimationUtil {
    public static void fadeIn(JComponent comp, int durationMs) {
        Timer timer = new Timer(16, null);
        final long start = System.currentTimeMillis();
        timer.addActionListener(e -> {
            float progress = (System.currentTimeMillis() - start) / (float) durationMs;
            comp.setOpaque(true);
            comp.setBackground(new Color(0, 0, 0, Math.min(1f, progress) * 0.5f));
            comp.repaint();
            if (progress >= 1f) timer.stop();
        });
        timer.start();
    }

    public static void pulseEffect(JComponent comp) {
        Timer timer = new Timer(40, null);
        final long start = System.currentTimeMillis();
        timer.addActionListener(e -> {
            float t = (System.currentTimeMillis() - start) / 400f;
            float alpha = 0.7f + (float)Math.sin(t) * 0.3f;
            comp.setForeground(new Color(1f, 1f, 1f, Math.abs(alpha)));
            comp.repaint();
        });
        timer.start();
    }
}
