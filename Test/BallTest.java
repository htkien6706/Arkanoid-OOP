import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class BallTest {

    Ball ball;

    @BeforeEach
    void setUp() {
        ball = new Ball(10, 20);
    }

    @Test
    void isFallingToGround() {
        ball.y = 500;
        assertTrue(ball.isFallingToGround(400.0));
        assertTrue(ball.isFallingToGround(500.0));

        ball.y = 100;
        assertFalse(ball.isFallingToGround(150.0));
    }

    @Test
    void move() {
        ball.x = 10;
        ball.y = 20;
        ball.dX = 2.7;
        ball.dY = 3.3;
        ball.move();
        assertEquals(12, ball.x);
        assertEquals(23, ball.y);
    }

    @Test
    void randomizeMove() {
        ball.dX = 1.0;
        double before = ball.dX;
        ball.randomizeMove();
        double after = ball.dX;
        double diff = Math.abs(after - before);
        assertTrue(diff <= 0.25);
    }

    @Test
    void reverseX() {
        ball.dX = 3.5;
        ball.reverseX();
        assertEquals(-3.5, ball.dX, 1e-9);
        ball.reverseX();
        assertEquals(3.5, ball.dX, 1e-9);
    }

    @Test
    void reverseY() {
        ball.dY = -2.25;
        ball.reverseY();
        assertEquals(2.25, ball.dY, 1e-9);
        ball.reverseY();
        assertEquals(-2.25, ball.dY, 1e-9);
    }

    @Test
    void checkWallCollision() {
        // left wall
        ball.x = -5;
        ball.dX = 2.0;
        ball.checkWallCollision(600, 800);
        assertTrue(ball.dX < 0, "dX should be reversed when hitting left wall");

        // right wall
        ball.x = 800;
        ball.dX = -3.0;
        ball.checkWallCollision(600, 800);
        assertTrue(ball.dX > 0, "dX should be reversed when hitting right wall");

        // top wall
        ball.y = -2;
        ball.dY = 2.5;
        ball.checkWallCollision(600, 800);
        assertTrue(ball.dY < 0, "dY should be reversed when hitting top wall");
    }

    @Test
    void resetBall() {
        ball.resetBall(50, 60);
        assertEquals(50, ball.x);
        assertEquals(60, ball.y);
        double expected = ball.SPEED * 0.6;
        assertEquals(expected, ball.dX, 1e-9);
        assertEquals(expected, ball.dY, 1e-9);
    }

    




}