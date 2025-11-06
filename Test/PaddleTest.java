import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PaddleTest {

    private Paddle paddle;
    private final int PANEL_WIDTH = 800;

    @BeforeEach
    void setUp() {
        paddle = new Paddle();
        paddle.x = 100;
        paddle.width = 100;
    }

    @Test
    void moveLeft() {
        int initialX = paddle.x;
        paddle.moveLeft();
        assertEquals(initialX - 20, paddle.x);
    }
}