import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

class Paddle {
    int x, y;
    int width, height;
    int dx = 20;
    BufferedImage image;

    public Paddle() {
        width = 100;
        height = 10;
    }

    // Vẽ paddle với màu từ skin
    public void draw(Graphics2D g2d, Color paddleColor) {
        // Glow effect
        for (int i = 8; i > 0; i--) {
            float alpha = (float) i / 16;
            Color glowColor = new Color(
                    paddleColor.getRed(),
                    paddleColor.getGreen(),
                    paddleColor.getBlue(),
                    (int) (alpha * 255)
            );
            g2d.setColor(glowColor);
            g2d.fillRoundRect(x - i, y - i, width + i * 2, height + i * 2, 20, 20);
        }

        // Main paddle body
        g2d.setColor(paddleColor);
        g2d.fillRoundRect(x, y, width, height, 15, 15);

        // White border
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y, width, height, 15, 15);

        // Highlight effect
        GradientPaint highlight = new GradientPaint(
                x, y, new Color(255, 255, 255, 100),
                x, y + height/2, new Color(255, 255, 255, 0)
        );
        g2d.setPaint(highlight);
        g2d.fillRoundRect(x, y, width, height/2, 15, 15);
    }

    void moveLeft() {
        x = x - dx;
        if(x < 0) {
            x = 0;
        }
    }

    void moveRight(int panelWidth) {
        x = x + dx;
        if(x > panelWidth - width) {
            x = panelWidth - width;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
