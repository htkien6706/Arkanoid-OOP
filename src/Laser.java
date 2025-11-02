import java.awt.*;

public class Laser {
    private int x, y;
    private int width = 4;
    private int height = 15;
    private double speed = 8.0;
    private boolean active = true;

    public Laser(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move() {
        y -= speed;
    }

    public boolean isOffScreen() {
        return y + height < 0;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void draw(Graphics2D g2d) {
        if (!active) return;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Glow effect
        for (int i = 3; i > 0; i--) {
            g2d.setColor(new Color(255, 255, 0, 50 * i));
            g2d.fillRect(x - i, y, width + i * 2, height);
        }

        // Laser body với gradient
        GradientPaint gp = new GradientPaint(
                x, y, new Color(255, 255, 100),
                x, y + height, new Color(255, 200, 0)
        );
        g2d.setPaint(gp);
        g2d.fillRect(x, y, width, height);

        // Highlight
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.fillRect(x + 1, y, 1, height);
    }
}