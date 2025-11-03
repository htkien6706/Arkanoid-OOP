import java.awt.*;

public class Brick {
    private final int x, y, width, height;
    private Color baseColor;
    private boolean destroyed;

    public Brick(int x, int y, int width, int height, Color baseColor) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.baseColor = baseColor;
        this.destroyed = false;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics2D g2) {
        if (destroyed) return;

        g2.setColor(new Color(0, 0, 0, 70)); // đen mờ 70/255
        g2.fillRoundRect(x + 5, y + 5, width, height, 12, 12);


        GradientPaint gp = new GradientPaint(
                x, y, brighten(baseColor, 0.4f),
                x + width, y + height, darken(baseColor, 0.4f)
        );
        g2.setPaint(gp);
        g2.fillRoundRect(x, y, width, height, 12, 12);

        // ✨ Viền sáng để viên gạch nổi hơn
        g2.setColor(new Color(255, 255, 255, 100));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, width, height, 12, 12);
    }

    // Tăng độ sáng màu (để tạo hiệu ứng ánh sáng)
    private Color brighten(Color color, float amount) {
        int r = Math.min(255, (int)(color.getRed() * (1 + amount)));
        int g = Math.min(255, (int)(color.getGreen() * (1 + amount)));
        int b = Math.min(255, (int)(color.getBlue() * (1 + amount)));
        return new Color(r, g, b);
    }

    // Giảm độ sáng màu (để tạo hiệu ứng bóng)
    private Color darken(Color color, float amount) {
        int r = Math.max(0, (int)(color.getRed() * (1 - amount)));
        int g = Math.max(0, (int)(color.getGreen() * (1 - amount)));
        int b = Math.max(0, (int)(color.getBlue() * (1 - amount)));
        return new Color(r, g, b);
    }

    public Color getBaseColor() {
        return baseColor;
    }

}
