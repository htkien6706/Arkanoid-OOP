import java.awt.*;
import java.util.Random;

public class Level1BrickManagement extends BrickManagement {
    public Level1BrickManagement() {}
    @Override
    public void setBricks(int rows, int cols, int brickWidth, int brickHeight, int padding) {

        final int start_x = 50;
        final int start_y = 60;

        for(int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int x = start_x + j * (brickWidth + padding);
                int y = start_y + i * (brickHeight + padding);
                Color brickColor = getColorForRow(i);
                allBricks.add(new NormalBrick(x, y, brickWidth, brickHeight, brickColor));
            }
        }

    }


}
