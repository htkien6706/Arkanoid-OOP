import java.util.Random;

public class Level1BrickManagement extends BrickManagement {
    public Level1BrickManagement() {}
    @Override
    public void setBricks(int rows, int cols, int brickWidth, int brickHeight, int padding) {

        Random rand = new Random();
        int number = rand.nextInt(2) + 1;
        LevelCreator levelCreator = new Level1Creator();
        allBricks = levelCreator.createLevel(rows, cols, brickWidth, brickHeight, padding);

    }

}
