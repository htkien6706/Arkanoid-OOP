import java.awt.*;

public class Particle {
    private double x, y;
    private double dx, dy;
    private int size;
    private Color color;
    private int life;
    private int maxLife; // Lưu để tính alpha

    public Particle(double x, double y, double dx, double dy, int size, Color color, int life) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.size = size;
        this.color = color;
        this.life = life;
        this.maxLife = life;
    }

    public void update() {
        x += dx;
        y += dy;
        dy += 0.15; // Gravity nhẹ
        dx *= 0.96; // Ma sát
        life--;
    }

    public boolean isDead() {
        return life <= 0;
    }

    public void draw(Graphics2D g2d) {
        // Tính alpha: từ 255 → 0 theo life
        int alpha = (int) (255.0 * life / maxLife);
        alpha = Math.max(0, Math.min(255, alpha)); // Giới hạn 0–255

        Color fadeColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
        g2d.setColor(fadeColor);
        g2d.fillRoundRect((int) x, (int) y, size, size, 2, 2); // Bo góc nhẹ
    }
}
