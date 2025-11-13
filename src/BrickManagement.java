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
