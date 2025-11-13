import java.awt.*;
import java.util.Random;

public class MovingBrick extends StoneBrick {
    private double dX;
    private int leftBound;
    private int rightBound;

    public MovingBrick(int x, int y, int width, int height,Color baseColor, double dX) {
        super(x, y, width, height, baseColor);
        this.dX = dX ; // Thêm biến đổi ngẫu nhiên nhỏ
    }
    @Override
    public void updateBrick () {
        x += dX;
        if (x <= leftBound || x + width >= rightBound) {
            dX = -dX;
        }
    }

    public void move() {
        x += dX + (new Random().nextDouble() - 0.5)*0.5; // Thêm biến đổi ngẫu nhiên nhỏ
    }

    public void setMovementBounds(int leftBound, int rightBound) {
        this.leftBound = leftBound;
        this.rightBound = rightBound;
    }

}
