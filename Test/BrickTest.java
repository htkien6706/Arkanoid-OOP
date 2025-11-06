import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.*;
import static org.junit.jupiter.api.Assertions.*;

class BrickTest {

    private Brick brick;
    private final int X = 10;
    private final int Y = 20;
    private final int WIDTH = 50;
    private final int HEIGHT = 30;
    private final Color BASE_COLOR = Color.RED;

    @BeforeEach
    void setUp() {
        brick = new Brick(X, Y, WIDTH, HEIGHT, BASE_COLOR);
    }

    @Test
    void isDestroyed() {
        assertFalse(brick.isDestroyed());

        brick.setDestroyed(true);
        assertTrue(brick.isDestroyed());

        brick.setDestroyed(false);
        assertFalse(brick.isDestroyed());
    }

    @Test
    void setDestroyed() {
        brick.setDestroyed(true);
        assertTrue(brick.isDestroyed());

        brick.setDestroyed(false);
        assertFalse(brick.isDestroyed());

        brick.setDestroyed(true);
        assertTrue(brick.isDestroyed());
    }

    @Test
    void getBounds() {
        Rectangle bounds = brick.getBounds();

        assertNotNull(bounds);
        assertEquals(X, bounds.x);
        assertEquals(Y, bounds.y);
        assertEquals(WIDTH, bounds.width);
        assertEquals(HEIGHT, bounds.height);

        Rectangle bounds2 = brick.getBounds();
        assertNotSame(bounds, bounds2);
        assertEquals(bounds, bounds2);
    }

    @Test
    void getBaseColor() {
        Color color = brick.getBaseColor();
        assertEquals(BASE_COLOR, color);
        assertSame(BASE_COLOR, color);

        Brick blueBrick = new Brick(0, 0, 100, 50, Color.BLUE);
        assertEquals(Color.BLUE, blueBrick.getBaseColor());
    }
}