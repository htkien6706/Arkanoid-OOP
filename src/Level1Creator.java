import java.awt.*;
import java.util.ArrayList;

public class Level1Creator implements LevelCreator {
    private Color getColorForRow(int row) {
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

    public ArrayList<Brick> createLevel (int rows, int cols, int brickWidth, int brickHeight, int padding){
        ArrayList<Brick> normalBricks = new ArrayList<>();
        final int start_x = 50;
        final int start_y = 60;

        for(int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int x = start_x + j * (brickWidth + padding);
                int y = start_y + i * (brickHeight + padding);
                Color brickColor = getColorForRow(i);
                normalBricks.add(new NormalBrick(x, y, brickWidth, brickHeight, brickColor));
            }
        }
        return normalBricks;

    }
}
