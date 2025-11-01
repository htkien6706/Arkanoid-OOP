import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements KeyListener, ActionListener {
    private Ball ball;
    private Paddle paddle;
    private Brick brick;
    private Timer timer;
    private BrickManagement manage;
    private ShopManager shopManager;
    private CoinManager coinManager;


    int PANEL_HEIGHT = 636;
    int PANEL_WIDTH = 800;

    public GamePanel(ShopManager shopManager, CoinManager coinManager) {
        this.shopManager = shopManager;
        this.coinManager = coinManager;


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
        manage.draw(g2d);

        // Hiển thị điểm và coins
        drawUI(g2d);
    }

    private void drawUI(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Hiển thị coins
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("" + coinManager.getCoins(), 20, 30);


    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            paddle.moveLeft();
        } else if (key == KeyEvent.VK_RIGHT) {
            paddle.moveRight(PANEL_WIDTH);
        } else if (key == KeyEvent.VK_ESCAPE) {
            Main.showMenu();
        }

        repaint();
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    public void initGameObjects() {
        paddle = new Paddle();
        paddle.x = (PANEL_WIDTH - paddle.width) / 2;
        paddle.y = PANEL_HEIGHT - paddle.height - 50;

        ball = new Ball(300, 300);

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
        ball.checkWallColiision(PANEL_HEIGHT, PANEL_WIDTH);

        Rectangle ball_bounds = ball.getBounds();
        Rectangle paddle_bounds = paddle.getBounds();

        if(hasCollision(ball_bounds, paddle_bounds)) {
            ball.reverseY();
            ball.randomizeMove();
            SoundManager.getInstance().playSound("paddle_hit");
            double paddleCenter = paddle.x + paddle.width / 2.0;
            double hitPos = (ball.x + ball.diameter / 2.0 - paddleCenter) / (paddle.width / 2.0);

            hitPos = Math.max(-1, Math.min(1, hitPos));

            double angle = hitPos * Math.toRadians(60);
            ball.dX = ball.SPEED * Math.sin(angle);
            ball.dY = -ball.SPEED * Math.cos(angle);
        }

        ArrayList<Brick> bricks = manage.getBricks();
        for(int i = 0; i < bricks.size(); i++) {
            Brick b = bricks.get(i);
            Rectangle brick_bounds = b.getBounds();

            if(!b.isDestroyed()) {
                if(hasCollision(ball_bounds, brick_bounds)) {
                    b.setDestroyed(true);
                    SoundManager.getInstance().playSound("brick_break");
                    // Thêm coins và điểm khi phá gạch
                    coinManager.earnCoinsFromBrick();


                    handleBallBounce(ball, b, ball_bounds);
                }
            }
        }

        // Kiểm tra game over
        if(ball.isFallingToGround(PANEL_HEIGHT)) {
            timer.stop();
            SoundManager.getInstance().playSound("game_over");
            int option = JOptionPane.showConfirmDialog(
                    this,
                    "Game Over!\nĐiểm: " + "\nCoins: " + coinManager.getCoins() + "\n\nChơi lại?",
                    "Game Over",
                    JOptionPane.YES_NO_OPTION
            );

            if(option == JOptionPane.YES_OPTION) {
                resetGame();
            } else {
                Main.showMenu();
            }
        }

        // Kiểm tra thắng
        if(manage.allBricksDestroyed()) {
            timer.stop();
            coinManager.addCoins(100); // Thưởng khi hoàn thành màn
            JOptionPane.showMessageDialog(
                    this,
                    "Chúc mừng! Bạn đã hoàn thành màn!\n+100 coins bonus",
                    "Chiến thắng",
                    JOptionPane.INFORMATION_MESSAGE
            );
            Main.showMenu();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == timer) {
            ball.move();
            updateGame();
            repaint();
        }
    }

    public boolean hasCollision(Rectangle r1, Rectangle r2) {
        return r1.intersects(r2);
    }

    private void handleBallBounce(Ball ball, Brick brick, Rectangle ballBounds) {
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
            ball.reverseY();
        } else {
            ball.reverseX();
        }
    }

    private void resetGame() {

        initGameObjects();
        timer.start();
        repaint();
    }
}
