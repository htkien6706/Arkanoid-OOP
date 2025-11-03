import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements MouseMotionListener, KeyListener, ActionListener {
    private Ball ball;
    private Paddle paddle;
    private Brick brick;
    private Timer timer;
    private BrickManagement manage;
    private ShopManager shopManager;
    private CoinManager coinManager;

    int PANEL_HEIGHT = 636;
    int PANEL_WIDTH = 800;

    private boolean isPaused = false;
    private boolean isGameOver = false;
    
    // Để lưu các MouseListener tạm thời cho các nút
    private ArrayList<MouseAdapter> tempMouseListeners = new ArrayList<>();
    
    // Để điều khiển bằng phím
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private final int PADDLE_SPEED = 8;

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

        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(15, 15, 30),
                0, getHeight(), new Color(30, 30, 50)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        paddle.draw(g2d, shopManager.getPaddleColor());
        ball.draw(g2d, shopManager.getBallColor());
        manage.draw(g2d);

        drawUI(g2d);

        if (isPaused) drawPauseOverlay(g2d);
        if (isGameOver) drawGameOverOverlay(g2d);
    }

    private void drawUI(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 20));
        g2d.drawString("" + coinManager.getCoins(), 20, 30);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        // Điều khiển paddle bằng phím mũi tên
        if (!isPaused && !isGameOver) {
            if (key == KeyEvent.VK_LEFT) {
                movingLeft = true;
            } else if (key == KeyEvent.VK_RIGHT) {
                movingRight = true;
            }
        }
        
        // Chỉ dùng SPACE để pause/resume
        if (key == KeyEvent.VK_SPACE && !isGameOver) {
            togglePause();
        }
        
        repaint();
    }

    @Override 
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            movingLeft = false;
        } else if (key == KeyEvent.VK_RIGHT) {
            movingRight = false;
        }
    }
    
    @Override 
    public void keyTyped(KeyEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        // Di chuyển paddle theo vị trí chuột khi game đang chạy
        if (!isPaused && !isGameOver) {
            int mouseX = e.getX();
            // Đặt paddle sao cho tâm paddle trùng với vị trí chuột
            paddle.x = mouseX - paddle.width / 2;
            
            // Giới hạn paddle trong màn hình
            if (paddle.x < 0) {
                paddle.x = 0;
            }
            if (paddle.x + paddle.width > PANEL_WIDTH) {
                paddle.x = PANEL_WIDTH - paddle.width;
            }
            
            repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Xử lý giống mouseMoved
        mouseMoved(e);
    }

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
        addMouseMotionListener(this); // Thêm mouse motion listener
    }

    public void initTimer() {
        timer = new Timer(10, this);
        timer.start();
    }

    // Hàm di chuyển paddle bằng phím
    private void updatePaddleMovement() {
        if (!isPaused && !isGameOver) {
            if (movingLeft) {
                paddle.x -= PADDLE_SPEED;
                if (paddle.x < 0) {
                    paddle.x = 0;
                }
            }
            if (movingRight) {
                paddle.x += PADDLE_SPEED;
                if (paddle.x + paddle.width > PANEL_WIDTH) {
                    paddle.x = PANEL_WIDTH - paddle.width;
                }
            }
        }
    }

    public void updateGame() {
        if (isPaused || isGameOver) return;

        ball.checkWallColiision(PANEL_HEIGHT, PANEL_WIDTH);

        Rectangle ball_bounds = ball.getBounds();
        Rectangle paddle_bounds = paddle.getBounds();

        if (hasCollision(ball_bounds, paddle_bounds)) {
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
        for (Brick b : bricks) {
            if (!b.isDestroyed() && hasCollision(ball_bounds, b.getBounds())) {
                b.setDestroyed(true);
                SoundManager.getInstance().playSound("brick_break");
                coinManager.earnCoinsFromBrick();
                handleBallBounce(ball, b, ball_bounds);
            }
        }

        if (ball.isFallingToGround(PANEL_HEIGHT)) {
            SoundManager.getInstance().playSound("game_over");
            isGameOver = true;
            timer.stop();
        }

        if (manage.allBricksDestroyed()) {
            timer.stop();
            coinManager.addCoins(100);
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
        if (e.getSource() == timer) {
            updatePaddleMovement(); // Cập nhật vị trí paddle từ phím
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

        if (minOverlap == overlapTop || minOverlap == overlapBottom)
            ball.reverseY();
        else ball.reverseX();
    }

    private void resetGame() {
        initGameObjects();
        isGameOver = false;
        isPaused = false;
        movingLeft = false;
        movingRight = false;
        clearTempMouseListeners();
        timer.start();
        repaint();
    }

    private void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
            timer.stop();
            movingLeft = false;
            movingRight = false;
        } else {
            timer.start();
            clearTempMouseListeners();
        }
        repaint();
    }

    private void drawPauseOverlay(Graphics2D g2d) {
        clearTempMouseListeners();
        drawDimBackground(g2d);
        drawOverlayBox(g2d, "TẠM DỪNG", new String[]{"Chơi tiếp", "Chơi lại", "Menu"},
                new Runnable[]{this::resumeGame, this::resetGame, Main::showMenu});
    }

    private void drawGameOverOverlay(Graphics2D g2d) {
        clearTempMouseListeners();
        drawDimBackground(g2d);
        drawOverlayBox(g2d, "GAME OVER", new String[]{"Chơi lại", "Menu"},
                new Runnable[]{this::resetGame, Main::showMenu});
    }

    private void drawDimBackground(Graphics2D g2d) {
        Composite old = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setComposite(old);
    }

    private void drawOverlayBox(Graphics2D g2d, String title, String[] buttons, Runnable[] actions) {
        int boxWidth = 300;
        int boxHeight = 80 + buttons.length * 70;
        int x = (getWidth() - boxWidth) / 2;
        int y = (getHeight() - boxHeight) / 2;

        g2d.setColor(new Color(25, 25, 45, 230));
        g2d.fillRoundRect(x, y, boxWidth, boxHeight, 30, 30);

        g2d.setColor(Color.CYAN);
        g2d.setStroke(new BasicStroke(3f));
        g2d.drawRoundRect(x, y, boxWidth, boxHeight, 30, 30);

        g2d.setFont(new Font("Segoe UI", Font.BOLD, 32));
        FontMetrics fm = g2d.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        g2d.setColor(Color.WHITE);
        g2d.drawString(title, x + (boxWidth - titleWidth) / 2, y + 50);

        int btnY = y + 80;
        for (int i = 0; i < buttons.length; i++) {
            drawNeonButton(g2d, buttons[i], x + 30, btnY, boxWidth - 60, 50, actions[i]);
            btnY += 70;
        }
    }

    private void drawNeonButton(Graphics2D g2d, String text, int x, int y, int width, int height, Runnable action) {
        Point mouse = getMousePosition();
        boolean hover = mouse != null && new Rectangle(x, y, width, height).contains(mouse);

        // Hiệu ứng hover đẹp hơn
        if (hover) {
            // Hiệu ứng glow khi hover
            g2d.setColor(new Color(0, 255, 255, 50));
            for (int i = 0; i < 3; i++) {
                g2d.fillRoundRect(x - i * 2, y - i * 2, width + i * 4, height + i * 4, 20 + i * 2, 20 + i * 2);
            }
        }

        Color border = hover ? new Color(0, 255, 255) : new Color(100, 200, 255);
        Color fill = hover ? new Color(0, 150, 200, 220) : new Color(30, 30, 60, 180);
        
        // Gradient fill cho nút khi hover
        if (hover) {
            GradientPaint gp = new GradientPaint(
                x, y, new Color(0, 180, 255, 180),
                x, y + height, new Color(0, 100, 180, 200)
            );
            g2d.setPaint(gp);
        } else {
            g2d.setColor(fill);
        }
        
        g2d.fillRoundRect(x, y, width, height, 20, 20);
        
        // Border sáng hơn khi hover
        g2d.setColor(border);
        g2d.setStroke(new BasicStroke(hover ? 3.5f : 2.5f));
        g2d.drawRoundRect(x, y, width, height, 20, 20);

        // Text sáng hơn và có shadow khi hover
        g2d.setFont(new Font("Segoe UI", Font.BOLD, hover ? 22 : 20));
        FontMetrics fm = g2d.getFontMetrics();
        
        if (hover) {
            // Shadow cho text
            g2d.setColor(new Color(0, 255, 255, 100));
            g2d.drawString(text, x + (width - fm.stringWidth(text)) / 2 + 2, 
                          y + height / 2 + fm.getAscent() / 2 - 3);
        }
        
        g2d.setColor(hover ? new Color(255, 255, 255) : new Color(220, 220, 220));
        g2d.drawString(text, x + (width - fm.stringWidth(text)) / 2, 
                      y + height / 2 + fm.getAscent() / 2 - 5);

        // Tạo MouseAdapter để xử lý click
        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (new Rectangle(x, y, width, height).contains(e.getPoint())) {
                    action.run();
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                repaint();
            }
            
            @Override
            public void mouseMoved(MouseEvent e) {
                repaint();
            }
        };
        
        // Lưu lại để có thể xóa sau
        tempMouseListeners.add(adapter);
        addMouseListener(adapter);
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                repaint();
            }
        });
    }

    // Xóa tất cả các MouseListener tạm thời
    private void clearTempMouseListeners() {
        for (MouseAdapter listener : tempMouseListeners) {
            removeMouseListener(listener);
        }
        tempMouseListeners.clear();
    }

    private void resumeGame() {
        isPaused = false;
        clearTempMouseListeners();
        timer.start();
        repaint();
    }
}