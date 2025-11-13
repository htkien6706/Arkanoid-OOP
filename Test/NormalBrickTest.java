import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.*;
import static org.junit.jupiter.api.Assertions.*;

class NormalBrickTest {

    private NormalBrick normalBrick;
    private final int X = 10;
    private final int Y = 20;
    private final int WIDTH = 50;
    private final int HEIGHT = 30;
    private final Color BASE_COLOR = Color.RED;

    @BeforeEach
    void setUp() {
        normalBrick = new NormalBrick(X, Y, WIDTH, HEIGHT, BASE_COLOR);
    }

    @Test
    void isDestroyed() {
        assertFalse(normalBrick.isDestroyed());

        normalBrick.setDestroyed(true);
        assertTrue(normalBrick.isDestroyed());

        normalBrick.setDestroyed(false);
        assertFalse(normalBrick.isDestroyed());
    }

    @Test
    void setDestroyed() {
        normalBrick.setDestroyed(true);
        assertTrue(normalBrick.isDestroyed());

        normalBrick.setDestroyed(false);
        assertFalse(normalBrick.isDestroyed());

        normalBrick.setDestroyed(true);
        assertTrue(normalBrick.isDestroyed());
    }

    @Test
    void getBounds() {
        Rectangle bounds = normalBrick.getBounds();

        assertNotNull(bounds);
        assertEquals(X, bounds.x);
        assertEquals(Y, bounds.y);
        assertEquals(WIDTH, bounds.width);
        assertEquals(HEIGHT, bounds.height);

        Rectangle bounds2 = normalBrick.getBounds();
        assertNotSame(bounds, bounds2);
        assertEquals(bounds, bounds2);
    }

}