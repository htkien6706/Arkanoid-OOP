import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameSave implements Serializable {
    private static final long serialVersionUID = 1L;

    // Trạng thái chính
    public int score;
    public int lives;
    public int coins;

    // Paddle
    public int paddleX, paddleY, paddleWidth;

    // Ball
    public double ballX, ballY, ballDX, ballDY;

    // Extra balls
    public List<BallSave> extraBalls;

    // Bricks
    public List<BrickSave> bricks;

    // Power-ups
    public long powerUpEndTime;
    public long laserEndTime;
    public boolean laserActive;

    // Constructor
    public GameSave() {
        extraBalls = new ArrayList<>();
        bricks = new ArrayList<>();
    }

    // Lớp con cho Ball
    public static class BallSave implements Serializable {
        public double x, y, dx, dy;
        public BallSave(double x, double y, double dx, double dy) {
            this.x = x; this.y = y; this.dx = dx; this.dy = dy;
        }
    }

    // Lớp con cho Brick
    public static class BrickSave implements Serializable {
        public int x, y, width, height;
        public boolean destroyed;
        public BrickSave(int x, int y, int width, int height, boolean destroyed) {
            this.x = x; this.y = y; this.width = width; this.height = height;
            this.destroyed = destroyed;
        }
    }
}