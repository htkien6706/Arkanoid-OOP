import java.awt.*;
import java.util.ArrayList;

public class BrickManagement {
    private ArrayList<Brick> Bricks;

    public BrickManagement(int rows, int cols, int brickWidth, int brickHeight, int padding) {
        Bricks = new ArrayList<>();
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                int x = 30 + j * (brickWidth + padding);
                int y = 100 + i * (brickHeight + padding);
                Color c = getColorForRow(i, rows);

                Bricks.add(new Brick(x, y, brickWidth, brickHeight, c));
            }
        }
    }

    private Color getColorForRow(int row, int totalRows) {
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

    public void draw(Graphics g) {
        for(Brick brick : Bricks) {
            if(!brick.isDestroyed()) {
                Graphics2D g2d = (Graphics2D) g;
                brick.draw(g2d);
            }
        }
    }

    public ArrayList<Brick> getBricks() {
        return Bricks;
    }

    // Kiểm tra tất cả gạch đã bị phá chưa
    public boolean allBricksDestroyed() {
        for(Brick brick : Bricks) {
            if(!brick.isDestroyed()) {
                return false;
            }
        }
        return true;
    }
}
