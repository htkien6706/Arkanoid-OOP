import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements KeyListener, ActionListener {
    private Ball ball;
    private ArrayList<Ball> extraBalls;
    private Paddle paddle;
    private Timer timer;
    private BrickManagement manage;
    private ParticleManager particleManager;
    private ShopManager shopManager;
    private CoinManager coinManager;
    private ScoreManager scoreManager;
    private PowerUpManager powerUpManager;
    private boolean isResumed = false;
    private int score = 0;
    private int lives = 3;

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
        this.powerUpManager = new PowerUpManager();
        this.extraBalls = new ArrayList<>();
        this.particleManager = new ParticleManager();

        initPanel();
        initGameObjects();
        initTimer();
        SoundManager.getInstance().playBackgroundMusic("game_music");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(15, 15, 30),
                0, getHeight(), new Color(30, 30, 50)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        paddle.draw(g2d, shopManager.getPaddleColor());
        ball.draw(g2d, shopManager.getBallColor());

        for (Ball extraBall : extraBalls) {
            extraBall.draw(g2d, shopManager.getBallColor());
        }

        manage.draw(g2d);
        powerUpManager.draw(g2d);  // CHỈ GỌI 1 LẦN
        particleManager.draw(g2d);

        drawUI(g2d);
    }

    private void drawUI(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("Score: " + score, 20, 30);

        g2d.setColor(new Color(255, 100, 100));
        g2d.drawString("Lives: " + lives, 20, 55);

        g2d.setColor(Color.YELLOW);
        g2d.drawString("Coin: " + coinManager.getCoins(), PANEL_WIDTH - 120, 30);

        if (laserActive) {
            g2d.setColor(Color.YELLOW);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.drawString("LASER ACTIVE", PANEL_WIDTH / 2 - 60, 30);
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
            fireLaser();
        } else if (key == KeyEvent.VK_ESCAPE) {
            saveCurrentGame();   // Lưu game
            stopGame();          // DỪNG TIMER HOÀN TOÀN
            Main.showMenu();     // Về menu
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
        powerUpManager.update(PANEL_HEIGHT);
        checkPowerUpExpiration();
        updateBall(ball);
        particleManager.update();

        for (int i = extraBalls.size() - 1; i >= 0; i--) {
            Ball extraBall = extraBalls.get(i);
            updateBall(extraBall);
            if (extraBall.isFallingToGround(PANEL_HEIGHT)) {
                extraBalls.remove(i);
            }
        }

        checkPowerUpCollision();
        checkLaserCollision();

        ArrayList<Brick> bricks = manage.getBricks();
        for (int i = 0; i < bricks.size(); i++) {
            Brick b = bricks.get(i);
            Rectangle brick_bounds = b.getBounds();

            if (!b.isDestroyed()) {
                if (hasCollision(ball.getBounds(), brick_bounds)) {
                    destroyBrick(b, ball);
                }
                for (Ball extraBall : extraBalls) {
                    if (hasCollision(extraBall.getBounds(), brick_bounds)) {
                        destroyBrick(b, extraBall);
                        break;
                    }
                }
            }
        }

        if (ball.isFallingToGround(PANEL_HEIGHT) && extraBalls.isEmpty()) {
            lives--;
            if (lives <= 0) {
                gameOver();
            } else {
                ball.resetBall(PANEL_WIDTH / 2, PANEL_HEIGHT / 2);
                SoundManager.getInstance().playSound("paddle_hit");
            }
        }

        if (manage.allBricksDestroyed()) {
            winGame();
        }
    }

    private void updateBall(Ball currentBall) {
        currentBall.checkWallCollision(PANEL_HEIGHT, PANEL_WIDTH);

        Rectangle ball_bounds = currentBall.getBounds();
        Rectangle paddle_bounds = paddle.getBounds();

        if (hasCollision(ball_bounds, paddle_bounds)) {
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

        particleManager.createBrickParticles(b);

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
        powerUpEndTime = System.currentTimeMillis() + 10000;

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
                ball.dX *= 0.5; ball.dY *= 0.5;
                for (Ball eb : extraBalls) { eb.dX *= 0.5; eb.dY *= 0.5; }
                showPowerUpMessage("Ball Slowed!");
                break;
            case FAST_BALL:
                ball.dX *= 1.5; ball.dY *= 1.5;
                for (Ball eb : extraBalls) { eb.dX *= 1.5; eb.dY *= 1.5; }
                showPowerUpMessage("Ball Faster!");
                break;
            case MULTI_BALL:
                Ball b1 = new Ball(ball.x, ball.y);
                Ball b2 = new Ball(ball.x, ball.y);
                b1.dX = ball.dX * 0.8; b1.dY = -ball.dY;
                b2.dX = -ball.dX * 0.8; b2.dY = -ball.dY;
                extraBalls.add(b1); extraBalls.add(b2);
                showPowerUpMessage("Multi Ball!");
                break;
            case LASER:
                laserActive = true;
                laserEndTime = System.currentTimeMillis() + 15000;
                showPowerUpMessage("Laser Active! (SPACE)");
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
        long now = System.currentTimeMillis();
        if (now > powerUpEndTime && paddle.width != originalPaddleWidth) {
            paddle.width = originalPaddleWidth;
        }
        if (laserActive && now > laserEndTime) {
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

    private void showPowerUpMessage(String msg) {
        System.out.println("Power-up: " + msg);
    }

    private void gameOver() {
        stopGame();  // DỪNG TIMER
        SoundManager.getInstance().playSound("game_over");

        if (scoreManager.isHighScore(score)) {
            String name = JOptionPane.showInputDialog(this,
                    "NEW HIGH SCORE!\nScore: " + score + "\nEnter name:", "High Score!", JOptionPane.PLAIN_MESSAGE);
            if (name != null && !name.trim().isEmpty()) {
                scoreManager.addScore(name.trim().toUpperCase(), score);
            }
        }

        int opt = JOptionPane.showConfirmDialog(this,
                "Game Over!\nScore: " + score + "\nCoins: " + coinManager.getCoins() + "\n\nPlay again?",
                "Game Over", JOptionPane.YES_NO_OPTION);

        if (opt == JOptionPane.YES_OPTION) {
            resetGame();
        } else {
            Main.showMenu();
        }
    }

    private void winGame() {
        stopGame();  // DỪNG TIMER
        score += 500;
        coinManager.addCoins(100);
        JOptionPane.showMessageDialog(this,
                "Congrats! You completed the level!\nScore: " + score + "\n+100 coins bonus",
                "Victory", JOptionPane.INFORMATION_MESSAGE);
        Main.showMenu();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == timer) {
            ball.move();
            for (Ball b : extraBalls) b.move();
            updateGame();
            repaint();
        }
    }

    public boolean hasCollision(Rectangle r1, Rectangle r2) {
        return r1.intersects(r2);
    }

    private void handleBallBounce(Ball currentBall, Brick brick, Rectangle ballBounds) {
        Rectangle brickBounds = brick.getBounds();
        double left = ballBounds.getMaxX() - brickBounds.getMinX();
        double right = brickBounds.getMaxX() - ballBounds.getMinX();
        double top = ballBounds.getMaxY() - brickBounds.getMinY();
        double bottom = brickBounds.getMaxY() - ballBounds.getMinY();
        double min = Math.min(Math.min(left, right), Math.min(top, bottom));

        if (min == top || min == bottom) {
            currentBall.reverseY();
        } else {
            currentBall.reverseX();
        }
    }

    private void saveCurrentGame() {
        GameSave save = new GameSave();
        save.score = score;
        save.lives = lives;
        save.coins = coinManager.getCoins();
        save.paddleX = paddle.x;
        save.paddleY = paddle.y;
        save.paddleWidth = paddle.width;
        save.ballX = ball.x;
        save.ballY = ball.y;
        save.ballDX = ball.dX;
        save.ballDY = ball.dY;

        for (Ball b : extraBalls) {
            save.extraBalls.add(new GameSave.BallSave(b.x, b.y, b.dX, b.dY));
        }

        for (Brick b : manage.getBricks()) {
            save.bricks.add(new GameSave.BrickSave(b.getBounds().x, b.getBounds().y, b.getBounds().width, b.getBounds().height, b.isDestroyed()));
        }

        save.powerUpEndTime = powerUpEndTime;
        save.laserEndTime = laserEndTime;
        save.laserActive = laserActive;

        SaveManager.saveGame(save);
    }

    public void loadGame(GameSave save) {
        if (save == null) return;
        score = save.score;
        lives = save.lives;
        coinManager.setCoins(save.coins);
        paddle.x = save.paddleX;
        paddle.y = save.paddleY;
        paddle.width = save.paddleWidth;
        ball.x = (int) save.ballX;
        ball.y = (int) save.ballY;
        ball.dX = save.ballDX;
        ball.dY = save.ballDY;
        extraBalls.clear();
        for (GameSave.BallSave bs : save.extraBalls) {
            Ball b = new Ball((int)bs.x, (int)bs.y);
            b.dX = bs.dx; b.dY = bs.dy;
            extraBalls.add(b);
        }
        ArrayList<Brick> bricks = manage.getBricks();
        for (int i = 0; i < bricks.size() && i < save.bricks.size(); i++) {
            bricks.get(i).setDestroyed(save.bricks.get(i).destroyed);
        }
        powerUpEndTime = save.powerUpEndTime;
        laserEndTime = save.laserEndTime;
        laserActive = save.laserActive;
        isResumed = true;
        repaint();
    }

    private void resetGame() {
        score = 0; lives = 3; laserActive = false;
        extraBalls.clear(); powerUpManager.clear();
        initGameObjects(); particleManager.clear();
        timer.start(); repaint();
    }

    public void stopGame() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }
}