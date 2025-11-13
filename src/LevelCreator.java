import java.awt.*;
import java.util.ArrayList;
interface LevelCreator {
    ArrayList<Brick> createLevel(int rows, int cols, int brickWidth, int brickHeight, int padding);
}
