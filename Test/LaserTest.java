import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class LaserTest {

    private Laser laser;
    private final int TEST_X = 100;
    private final int TEST_Y = 200;

    @BeforeEach
    void setUp() {
        laser = new Laser(TEST_X, TEST_Y);
    }

    @Test
    void move() {
        // Lưu vị trí Y ban đầu
        int initialY = laser.getBounds().y;

        // Gọi method move
        laser.move();

        // Kiểm tra vị trí Y đã giảm (di chuyển lên trên)
        assertTrue(laser.getBounds().y < initialY);

        // Kiểm tra tốc độ di chuyển chính xác
        assertEquals(initialY - 8, laser.getBounds().y);
    }

    @Test
    void isOffScreen() {
        // Laser mới tạo không nên off-screen
        assertFalse(laser.isOffScreen());

        // Di chuyển laser nhiều lần để ra khỏi màn hình
        for (int i = 0; i < 30; i++) {
            laser.move();
        }

        // Bây giờ laser nên off-screen
        assertTrue(laser.isOffScreen());
    }


}