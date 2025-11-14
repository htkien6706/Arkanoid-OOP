import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public abstract class BrickManagement {
    protected ArrayList<Brick> allBricks;

    public BrickManagement() {
        allBricks = new ArrayList<>();
    }

    public abstract void setBricks (int rows, int cols, int brickWidth, int brickHeight, int padding);

    public void draw(Graphics g) {

        for(Brick brick : allBricks) {
            if(!brick.isDestroyed()) {
                brick.updateBrick();
                Graphics2D g2d = (Graphics2D) g;
                brick.draw(g2d);
            }
        }
    }

    protected Color getColorForRow(int row) {
        Color[] colors = {
                new Color(255, 59, 59),   // Đỏ
                new Color(255, 140, 66),  // Cam
                new Color(255, 221, 87),  // Vàng
                new Color(72, 219, 251),  // Xanh dương
                new Color(29, 209, 161),  // Xanh lá
                new Color(162, 155, 254)  // Tím
        };
        return colors[row % colors.length];
    }

    public ArrayList<Brick> getBricks() {
        return allBricks;
    }

    public ArrayList<Brick> getStoneBricks() {
        return allBricks;
    }

    // Kiểm tra tất cả gạch đã bị phá chưa
    public boolean allBricksDestroyed() {
        for(Brick brick : allBricks) {
            if(!(brick instanceof StoneBrick) && !brick.isDestroyed()) {
                return false;
            }
        }
        return true;
    }
}
