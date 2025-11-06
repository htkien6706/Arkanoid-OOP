import java.awt.*;

import java.util.Random;

public class Ball {
    int x, y;
    double dX, dY;
    final double SPEED = 4;
    int diameter;
    Random rand = new Random();

    Ball(int x, int y) {
        this.x = x;
        this.y = y;
        diameter = 16;

        double angle = Math.toRadians(rand.nextInt(60) + 30);
        dX = SPEED * Math.cos(angle);
        dY = SPEED * Math.sin(angle);
    }

    public boolean isFallingToGround(double PANEL_HEIGHT) {
        return y >= PANEL_HEIGHT;
    }

    public void move() {
        x += dX;
        y += dY;
    }

    public void randomizeMove() {
        dX += (rand.nextDouble() - 0.5) * 0.5;
    }

    public void reverseX() {
        dX = -dX;
    }

    public void reverseY() {
        dY = -dY;
    }

    public void checkWallColiision(int PANEL_HEIGHT, int PANEL_WIDTH) {
        if(x < 0 || x + diameter > PANEL_WIDTH) {
            reverseX();
            SoundManager.getInstance().playSound("wall_hit");
        }
        else if(y < 0) {
            reverseY();
            SoundManager.getInstance().playSound("wall_hit");
        }
    }

    public void resetBall(int x, int y) {
        this.x = x;
        this.y = y;
        dX = SPEED * 0.6;
        dY = SPEED * 0.6;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, diameter, diameter);
    }

    // Vẽ quả bóng với màu từ skin
    public void draw(Graphics2D g2d, Color ballColor) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Shadow
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.fillOval(x + 2, y + 2, diameter, diameter);

        // Tạo màu tối hơn cho gradient
        Color darkerColor = new Color(
                Math.max(0, ballColor.getRed() - 55),
                Math.max(0, ballColor.getGreen() - 100),
                Math.max(0, ballColor.getBlue() - 100)
        );

        // Body với gradient
        GradientPaint bodyGradient = new GradientPaint(
                x, y, ballColor,
                x + diameter, y + diameter, darkerColor
        );
        g2d.setPaint(bodyGradient);
        g2d.fillOval(x, y, diameter, diameter);

        // Highlight effect
        RadialGradientPaint highlight = new RadialGradientPaint(
                x + diameter * 0.3f, y + diameter * 0.3f,
                diameter * 0.4f,
                new float[]{0.0f, 1.0f},
                new Color[]{
                        new Color(255, 255, 255, 180),
                        new Color(255, 255, 255, 0)
                }
        );
        g2d.setPaint(highlight);
        g2d.fillOval(x, y, diameter, diameter);

        // Border
        Color borderColor = new Color(
                Math.max(0, ballColor.getRed() - 100),
                Math.max(0, ballColor.getGreen() - 100),
                Math.max(0, ballColor.getBlue() - 100)
        );
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawOval(x, y, diameter, diameter);

        // Inner highlight border
        Color lightBorderColor = new Color(
                Math.min(255, ballColor.getRed() + 55),
                Math.min(255, ballColor.getGreen() + 50),
                Math.min(255, ballColor.getBlue() + 50),
                100
        );
        g2d.setColor(lightBorderColor);
        g2d.setStroke(new BasicStroke(1f));
        g2d.drawOval(x + 1, y + 1, diameter - 2, diameter - 2);
    }
}
