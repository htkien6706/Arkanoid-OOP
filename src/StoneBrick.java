import java.awt.*;

public class StoneBrick extends Brick {
    private final boolean destroyed = false;

    public StoneBrick(int x, int y, int width, int height, Color baseColor) {
        super(x, y, width, height, baseColor);
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public void setDestroyed(boolean destroyed){
        // StoneBrick không thể bị phá hủy
    }

    @Override
    public void draw (Graphics2D g2d) {
        if (destroyed) return;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Shadow
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillRoundRect(x + 2, y + 2, width, height, 10, 10);

        // Background với gradient đá
        GradientPaint gp = new GradientPaint(
                x, y, baseColor,
                x, y + height, baseColor.darker().darker()
        );
        g2d.setPaint(gp);
        g2d.fillRoundRect(x, y, width, height, 10, 10);

        // Highlight
        g2d.setColor(new Color(255, 255, 255, 150));
        g2d.fillRoundRect(x + 1, y + 1, width - 2, height / 4, 10, 10);
    }




}
