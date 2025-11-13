import java.util.ArrayList;
import java.awt.Color;
public class Level2Creator implements LevelCreator {
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

    public  ArrayList<Brick> createLevel (int rows, int cols, int brickWidth, int brickHeight, int padding){
        ArrayList<Brick> allBricks = new ArrayList<>();
        final int start_x = 50;
        final int start_y = 60;
        int NumberOfMovingBricks = 1;
        final Color stoneColor = new Color(120, 120, 120);

        for(int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                if (i == rows/2 ){

                    if ((j>=0 && j<=2) || (j>=9 && j<=11)){
                        int x = (j==0 || j==9) ? start_x + j*(brickWidth+padding) : start_x + j * brickWidth+(j-1)*padding;
                        int y = start_y + i * (brickHeight + padding);
                        allBricks.add(new StoneBrick(x, y, brickWidth+padding, brickHeight,stoneColor));
                    }else if (NumberOfMovingBricks > 0) {
                        int x = start_x + j * (brickWidth + padding);
                        int y = start_y + i * (brickHeight + padding);
                        int leftBound = x - padding;
                        int rightBound = start_x + brickWidth * 9 + 8 * padding;
                        MovingBrick movingBrick = new MovingBrick(x, y, brickWidth, brickHeight, stoneColor, 2);
                        movingBrick.setMovementBounds(leftBound,rightBound);
                        allBricks.add(movingBrick);
                        NumberOfMovingBricks--;
                    }
                    continue;
                }

                int x = start_x + j * (brickWidth + padding);
                int y = start_y + i * (brickHeight + padding);
                Color brickColor = getColorForRow(i);
                allBricks.add(new NormalBrick(x, y, brickWidth, brickHeight, brickColor));
            }
        }
        return allBricks;

    }
}
