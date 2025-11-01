import java.awt.*;

public class Particle {
    float x, y, vx, vy, life;
    Color color;

    public Particle(float x, float y, Color color) {
        this.x = x; this.y = y;
        this.vx = (float)(Math.random() * 4 - 2);
        this.vy = (float)(Math.random() * -3 - 1);
        this.life = 1.0f;
        this.color = color;
    }

    public boolean update() {
        x += vx;
        y += vy;
        vy += 0.1f;
        life -= 0.02f;
        return life > 0;
    }

    public void draw(Graphics2D g) {
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(life * 255)));
        g.fillOval((int)x, (int)y, 5, 5);
    }
}
