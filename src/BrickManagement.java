import java.awt.*;
import java.util.ArrayList;

public class BrickManagement {
    ArrayList<Brick> Bricks;

    public BrickManagement(int rows, int cols, int brickWidth, int brickHeight, int padding) {
        Bricks = new ArrayList<>();
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                int x = 30 + j * (brickWidth + padding);
                int y = 40 + i * (brickHeight + padding);
                Color c = new Color(255, 20, 147);
                Bricks.add(new Brick(x, y, brickWidth, brickHeight, c));
            }
        }
    }

    public void draw(Graphics g) {
        for(Brick brick : Bricks) {
            if(!brick.isDestroyed()) {
                Graphics2D g2d = (Graphics2D) g;
                brick.draw(g2d);
            }
        }
    }
}