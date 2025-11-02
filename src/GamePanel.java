import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements KeyListener, ActionListener {
    private Ball ball;
    private ArrayList<Ball> extraBalls; // Cho multi-ball
    private Paddle paddle;
    private Timer timer;
    private BrickManagement manage;
    private ShopManager shopManager;
    private CoinManager coinManager;
    private ScoreManager scoreManager;
    private PowerUpManager powerUpManager; // THÊM MỚI
    private int score = 0;
    private int lives = 3; // THÊM MỚI

    // Power-up states
    private boolean laserActive = false;
    private long laserEndTime = 0;
    private long powerUpEndTime = 0;
    private int originalPaddleWidth;

    int PANEL_HEIGHT = 636;
    int PANEL_WIDTH = 800;

    public GamePanel(ShopManager shopManager, CoinManager coinManager) {
        this.shopManager = shopManager;
        this.coinManager = coinManager;
        this.scoreManager = Main.getScoreManager();
        this.powerUpManager = new PowerUpManager(); // THÊM MỚI
        this.extraBalls = new ArrayList<>(); // THÊM MỚI

        initPanel();
        initGameObjects();
        initTimer();
        SoundManager.getInstance().playBackgroundMusic("game_music");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Vẽ background gradient
        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(15, 15, 30),
                0, getHeight(), new Color(30, 30, 50)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Vẽ các đối tượng game
        paddle.draw(g2d, shopManager.getPaddleColor());
        ball.draw(g2d, shopManager.getBallColor());

        // Vẽ extra balls
        for (Ball extraBall : extraBalls) {
            extraBall.draw(g2d, shopManager.getBallColor());
        }

        manage.draw(g2d);
        powerUpManager.draw(g2d); // THÊM MỚI

        // Hiển thị UI
        drawUI(g2d);
    }

    private void drawUI(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Score
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("Score: " + score, 20, 30);

        // Lives
        g2d.setColor(new Color(255, 100, 100));
        g2d.drawString("Lives: " + lives, 20, 55);

        // Coins
        g2d.setColor(Color.YELLOW);
        g2d.drawString("💰 " + coinManager.getCoins(), PANEL_WIDTH - 120, 30);

        // Active power-up indicator
        if (laserActive) {
            g2d.setColor(Color.YELLOW);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.drawString("⚡ LASER ACTIVE", PANEL_WIDTH / 2 - 60, 30);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            paddle.moveLeft();
        } else if (key == KeyEvent.VK_RIGHT) {
            paddle.moveRight(PANEL_WIDTH);
        } else if (key == KeyEvent.VK_SPACE && laserActive) {
            // Bắn laser
            fireLaser();
        } else if (key == KeyEvent.VK_ESCAPE) {
            Main.showMenu();
        }

        repaint();
    }

    private void fireLaser() {
        SoundManager.getInstance().playSound("paddle_hit");
        powerUpManager.addLaser(paddle.x + 20, paddle.y - 15);
        powerUpManager.addLaser(paddle.x + paddle.width - 24, paddle.y - 15);
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    public void initGameObjects() {
        paddle = new Paddle();
        paddle.x = (PANEL_WIDTH - paddle.width) / 2;
        paddle.y = PANEL_HEIGHT - paddle.height - 50;
        originalPaddleWidth = paddle.width;

        ball = new Ball(300, 300);
        extraBalls.clear();

        manage = new BrickManagement(6, 12, 50, 30, 10);
    }

    public void initPanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
    }

    public void initTimer() {
        timer = new Timer(10, this);
        timer.start();
    }

    public void updateGame() {
        // Cập nhật power-ups
        powerUpManager.update(PANEL_HEIGHT);

        // Kiểm tra hết hiệu lực power-up
        checkPowerUpExpiration();

        // Cập nhật bóng chính
        updateBall(ball);

        // Cập nhật extra balls
        for (int i = extraBalls.size() - 1; i >= 0; i--) {
            Ball extraBall = extraBalls.get(i);
            updateBall(extraBall);

            if (extraBall.isFallingToGround(PANEL_HEIGHT)) {
                extraBalls.remove(i);
            }
        }

        // Kiểm tra va chạm power-up với paddle
        checkPowerUpCollision();

        // Kiểm tra laser bắn trúng gạch
        checkLaserCollision();

        ArrayList<Brick> bricks = manage.getBricks();
        for(int i = 0; i < bricks.size(); i++) {
            Brick b = bricks.get(i);
            Rectangle brick_bounds = b.getBounds();

            if(!b.isDestroyed()) {
                if(hasCollision(ball.getBounds(), brick_bounds)) {
                    destroyBrick(b, ball);
                }

                // Kiểm tra extra balls
                for (Ball extraBall : extraBalls) {
                    if(hasCollision(extraBall.getBounds(), brick_bounds)) {
                        destroyBrick(b, extraBall);
                        break;
                    }
                }
            }
        }

        // Kiểm tra game over
        if(ball.isFallingToGround(PANEL_HEIGHT) && extraBalls.isEmpty()) {
            lives--;

            if (lives <= 0) {
                gameOver();
            } else {
                // Reset ball nhưng giữ game tiếp tục
                ball.resetBall(PANEL_WIDTH / 2, PANEL_HEIGHT / 2);
                SoundManager.getInstance().playSound("paddle_hit");
            }
        }

        // Kiểm tra thắng
        if(manage.allBricksDestroyed()) {
            winGame();
        }
    }

    private void updateBall(Ball currentBall) {
        currentBall.checkWallColiision(PANEL_HEIGHT, PANEL_WIDTH);

        Rectangle ball_bounds = currentBall.getBounds();
        Rectangle paddle_bounds = paddle.getBounds();

        if(hasCollision(ball_bounds, paddle_bounds)) {
            currentBall.reverseY();
            currentBall.randomizeMove();
            SoundManager.getInstance().playSound("paddle_hit");

            double paddleCenter = paddle.x + paddle.width / 2.0;
            double hitPos = (currentBall.x + currentBall.diameter / 2.0 - paddleCenter) / (paddle.width / 2.0);
            hitPos = Math.max(-1, Math.min(1, hitPos));

            double angle = hitPos * Math.toRadians(60);
            currentBall.dX = currentBall.SPEED * Math.sin(angle);
            currentBall.dY = -currentBall.SPEED * Math.cos(angle);
        }
    }

    private void destroyBrick(Brick b, Ball currentBall) {
        b.setDestroyed(true);
        SoundManager.getInstance().playSound("brick_break");
        coinManager.earnCoinsFromBrick();
        score += 100;

        // Tạo power-up ngẫu nhiên
        Rectangle bounds = b.getBounds();
        powerUpManager.trySpawnPowerUp(bounds.x, bounds.y, bounds.width);

        handleBallBounce(currentBall, b, currentBall.getBounds());
    }

    private void checkPowerUpCollision() {
        Rectangle paddle_bounds = paddle.getBounds();

        for (PowerUp powerUp : powerUpManager.getPowerUps()) {
            if (!powerUp.isCollected() && hasCollision(powerUp.getBounds(), paddle_bounds)) {
                powerUp.setCollected(true);
                applyPowerUp(powerUp.getType());
                SoundManager.getInstance().playSound("click");
            }
        }
    }

    private void applyPowerUp(PowerUpType type) {
        powerUpEndTime = System.currentTimeMillis() + 10000; // 10 giây

        switch (type) {
            case EXPAND_PADDLE:
                paddle.width = (int)(originalPaddleWidth * 1.5);
                showPowerUpMessage("Paddle Expanded!");
                break;

            case SHRINK_PADDLE:
                paddle.width = (int)(originalPaddleWidth * 0.7);
                showPowerUpMessage("Paddle Shrinked!");
                break;

            case SLOW_BALL:
                ball.dX *= 0.5;
                ball.dY *= 0.5;
                for (Ball extraBall : extraBalls) {
                    extraBall.dX *= 0.5;
                    extraBall.dY *= 0.5;
                }
                showPowerUpMessage("Ball Slowed!");
                break;

            case FAST_BALL:
                ball.dX *= 1.5;
                ball.dY *= 1.5;
                for (Ball extraBall : extraBalls) {
                    extraBall.dX *= 1.5;
                    extraBall.dY *= 1.5;
                }
                showPowerUpMessage("Ball Faster!");
                break;

            case MULTI_BALL:
                // Tạo 2 bóng mới
                Ball ball1 = new Ball(ball.x, ball.y);
                Ball ball2 = new Ball(ball.x, ball.y);
                ball1.dX = ball.dX * 0.8;
                ball1.dY = -ball.dY;
                ball2.dX = -ball.dX * 0.8;
                ball2.dY = -ball.dY;
                extraBalls.add(ball1);
                extraBalls.add(ball2);
                showPowerUpMessage("Multi Ball!");
                break;

            case LASER:
                laserActive = true;
                laserEndTime = System.currentTimeMillis() + 15000; // 15 giây
                showPowerUpMessage("Laser Active! (Press SPACE)");
                break;

            case EXTRA_LIFE:
                lives++;
                showPowerUpMessage("+1 Life!");
                break;

            case BONUS_POINTS:
                score += 500;
                showPowerUpMessage("+500 Points!");
                break;
        }
    }

    private void checkPowerUpExpiration() {
        long currentTime = System.currentTimeMillis();

        // Hết hiệu lực paddle size
        if (currentTime > powerUpEndTime && paddle.width != originalPaddleWidth) {
            paddle.width = originalPaddleWidth;
        }

        // Hết hiệu lực laser
        if (laserActive && currentTime > laserEndTime) {
            laserActive = false;
        }
    }

    private void checkLaserCollision() {
        ArrayList<Brick> bricks = manage.getBricks();

        for (Laser laser : powerUpManager.getLasers()) {
            if (!laser.isActive()) continue;

            for (Brick brick : bricks) {
                if (!brick.isDestroyed() && hasCollision(laser.getBounds(), brick.getBounds())) {
                    brick.setDestroyed(true);
                    laser.setActive(false);
                    score += 100;
                    coinManager.earnCoinsFromBrick();
                    SoundManager.getInstance().playSound("brick_break");
                    break;
                }
            }
        }
    }

    private void showPowerUpMessage(String message) {
        // Có thể thêm hiệu ứng hiển thị message ở đây
        System.out.println("Power-up: " + message);
    }

    private void gameOver() {
        timer.stop();
        SoundManager.getInstance().playSound("game_over");

        if(scoreManager.isHighScore(score)) {
            String name = JOptionPane.showInputDialog(
                    this,
                    "🎉 NEW HIGH SCORE! 🎉\n\nScore: " + score + "\n\nEnter your name:",
                    "High Score!",
                    JOptionPane.PLAIN_MESSAGE
            );

            if(name != null && !name.trim().isEmpty()) {
                scoreManager.addScore(name.trim().toUpperCase(), score);
            }
        }

        int option = JOptionPane.showConfirmDialog(
                this,
                "Game Over!\n\nScore: " + score + "\nCoins: " + coinManager.getCoins() + "\n\nChơi lại?",
                "Game Over",
                JOptionPane.YES_NO_OPTION
        );

        if(option == JOptionPane.YES_OPTION) {
            resetGame();
        } else {
            Main.showMenu();
        }
    }

    private void winGame() {
        timer.stop();
        score += 500;
        coinManager.addCoins(100);
        JOptionPane.showMessageDialog(
                this,
                "Chúc mừng! Bạn đã hoàn thành màn!\n\nScore: " + score + "\n+100 coins bonus",
                "Chiến thắng",
                JOptionPane.INFORMATION_MESSAGE
        );
        Main.showMenu();
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == timer) {
            ball.move();
            for (Ball extraBall : extraBalls) {
                extraBall.move();
            }
            updateGame();
            repaint();
        }
    }

    public boolean hasCollision(Rectangle r1, Rectangle r2) {
        return r1.intersects(r2);
    }

    private void handleBallBounce(Ball currentBall, Brick brick, Rectangle ballBounds) {
        Rectangle brickBounds = brick.getBounds();

        double overlapLeft = ballBounds.getMaxX() - brickBounds.getMinX();
        double overlapRight = brickBounds.getMaxX() - ballBounds.getMinX();
        double overlapTop = ballBounds.getMaxY() - brickBounds.getMinY();
        double overlapBottom = brickBounds.getMaxY() - ballBounds.getMinY();

        double minOverlap = Math.min(
                Math.min(overlapLeft, overlapRight),
                Math.min(overlapTop, overlapBottom)
        );

        if(minOverlap == overlapTop || minOverlap == overlapBottom) {
            currentBall.reverseY();
        } else {
            currentBall.reverseX();
        }
    }

    private void resetGame() {
        score = 0;
        lives = 3;
        laserActive = false;
        extraBalls.clear();
        powerUpManager.clear();
        initGameObjects();
        timer.start();
        repaint();
    }
}