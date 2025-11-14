import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Level2BrickManagement extends BrickManagement {

    public Level2BrickManagement() {};

    @Override
    public void setBricks(int rows, int cols, int brickWidth, int brickHeight, int padding) {
        final int start_x = 50;
        final int start_y = 60;
        int NumberOfMovingBricks = 1;
        final Color stoneColor = new Color(120, 120, 120);

        for(int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                if (i == rows/2 ){

                    if ((j>=0 && j<=2) || (j>=9 && j<=11)) {
                        int x = (j==0 || j==9) ? start_x + j*(brickWidth+padding) : start_x + j * brickWidth+(j-1)*padding;
                        int y = start_y + i * (brickHeight + padding);
                        allBricks.add(new StoneBrick(x, y, brickWidth+padding, brickHeight,stoneColor));

                    }
                    else if (NumberOfMovingBricks > 0) {
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
    }
}
