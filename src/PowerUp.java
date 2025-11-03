import java.awt.*;

public class PowerUp {
    private int x, y;
    private int width = 40;
    private int height = 20;
    private double speed = 2.0;
    private PowerUpType type;
    private boolean collected = false;
    private long spawnTime;

    public PowerUp(int x, int y, PowerUpType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.spawnTime = System.currentTimeMillis();
    }

    public void move() {
        y += speed;
    }

    public boolean isOffScreen(int panelHeight) {
        return y > panelHeight;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public PowerUpType getType() {
        return type;
    }

    public void draw(Graphics2D g2d) {
        if (collected) return;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Lấy màu theo loại power-up
        Color baseColor = getColorForType();

        // Shadow
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillRoundRect(x + 2, y + 2, width, height, 10, 10);

        // Background với gradient
        GradientPaint gp = new GradientPaint(
                x, y, baseColor,
                x, y + height, baseColor.darker()
        );
        g2d.setPaint(gp);
        g2d.fillRoundRect(x, y, width, height, 10, 10);

        // Border sáng
        g2d.setColor(baseColor.brighter());
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y, width, height, 10, 10);

        // Hiệu ứng lấp lánh
        long elapsed = System.currentTimeMillis() - spawnTime;
        int alpha = (int)(Math.abs(Math.sin(elapsed / 200.0)) * 100) + 100;
        g2d.setColor(new Color(255, 255, 255, alpha));
        g2d.fillRoundRect(x + 2, y + 2, width / 3, height / 3, 5, 5);

        // Text viết tắt
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 10));
        FontMetrics fm = g2d.getFontMetrics();
        String text = type.getName();
        int textX = x + (width - fm.stringWidth(text)) / 2;
        int textY = y + (height + fm.getHeight()) / 2 - 2;

        // Text shadow
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.drawString(text, textX + 1, textY + 1);

        // Text chính
        g2d.setColor(Color.WHITE);
        g2d.drawString(text, textX, textY);
    }

    private Color getColorForType() {
        switch (type) {
            case EXPAND_PADDLE: return new Color(0, 200, 0);      // Xanh lá
            case SHRINK_PADDLE: return new Color(200, 0, 0);      // Đỏ
            case SLOW_BALL: return new Color(0, 150, 255);        // Xanh dương
            case FAST_BALL: return new Color(255, 100, 0);        // Cam
            case MULTI_BALL: return new Color(255, 0, 255);       // Tím
            case LASER: return new Color(255, 255, 0);            // Vàng
            case EXTRA_LIFE: return new Color(255, 20, 147);      // Hồng
            case BONUS_POINTS: return new Color(0, 255, 255);     // Cyan
            default: return Color.WHITE;
        }
    }
}