import javax.swing.*;
import java.util.Random;

public class Level2BrickManagement extends BrickManagement {

    public Level2BrickManagement() {};

    @Override
    public void setBricks(int rows, int cols, int brickWidth, int brickHeight, int padding) {
        Random rand = new Random();
        LevelCreator levelCreator = new Level2Creator();
        allBricks = levelCreator.createLevel(rows, cols, brickWidth, brickHeight, padding);
    }
}
