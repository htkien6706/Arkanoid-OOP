import java.awt.*;

public abstract class Brick {
    protected int x, y;
    protected int width, height;
    protected Color baseColor;

    public Brick(int x, int y, int width, int height, Color baseColor) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.baseColor = baseColor;
    }

    public void updateBrick() {
        // Default implementation does nothing
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public Color getBaseColor() {
        return baseColor;
    }

    public abstract void setDestroyed(boolean destroyed);

    public abstract boolean isDestroyed ();

    public abstract void draw (Graphics2D g2d);

}
