import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements KeyListener, ActionListener {
    private Ball ball;
    private Paddle paddle;
    private Timer timer;
    private BrickManagement manage;

    int PANEL_HEIGHT = 636;
    int PANEL_WIDTH = 800;

    // ==================== THÊM MỚI: Game State ====================
    private int score = 0;
    private int lives = 3;
    private boolean gameOver = false;
    private boolean gameWon = false;
    private boolean gamePaused = false;

    // ==================== THÊM MỚI: Skin System ====================
    private Color paddleColor = new Color(70, 130, 180);  // Steel Blue
    private Color ballColor = new Color(255, 69, 0);       // Red Orange
    private Color backgroundColor = Color.WHITE;
    private Color textColor = Color.BLACK;

    private MainFrame parentFrame;

    
    public GamePanel(MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        // phần code khởi tạo game cũ giữ nguyên
    }

    public GamePanel() {
        // setup panel
        initPanel();
        initGameObjects();
        initTimer();
    }

    // có gì cần vẽ thì vẽ ở trong cái paintCOmpoinent này, gọi lại các hàm draw ở các object đó
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // ==================== THÊM MỚI: Anti-aliasing ====================
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                            RenderingHints.VALUE_ANTIALIAS_ON);
        
        // ==================== THÊM MỚI: Apply màu từ Skin System ====================
        g2d.setColor(paddleColor);
        paddle.draw(g2d);
        
        g2d.setColor(ballColor);
        ball.draw(g2d);
        
        manage.draw(g2d);
        
        // ==================== THÊM MỚI: Vẽ UI ====================
        drawUI(g2d);
        
        // ==================== THÊM MỚI: Vẽ các màn hình ====================
        if (gameOver) {
            drawGameOver(g2d);
        } else if (gameWon) {
            drawGameWon(g2d);
        } else if (gamePaused) {
            drawPaused(g2d);
        }
    }

    // ==================== THÊM MỚI: Vẽ Score và Lives ====================
    private void drawUI(Graphics2D g) {
        g.setColor(textColor);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        
        // Score ở góc trái trên
        g.drawString("Score: " + score, 20, 30);
        
        // Lives ở góc phải trên
        g.drawString("Lives: " + lives, PANEL_WIDTH - 120, 30);
        
        // Hướng dẫn phím
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("SPACE: Pause | R: Restart", PANEL_WIDTH/2 - 90, PANEL_HEIGHT - 10);
    }

    // ==================== THÊM MỚI: Màn hình Game Over ====================
    private void drawGameOver(Graphics2D g) {
        // Overlay tối
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        
        // Chữ GAME OVER màu đỏ
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 60));
        String msg = "GAME OVER";
        int msgWidth = g.getFontMetrics().stringWidth(msg);
        g.drawString(msg, (PANEL_WIDTH - msgWidth) / 2, PANEL_HEIGHT / 2 - 50);
        
        // Điểm số
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        String scoreMsg = "Final Score: " + score;
        int scoreMsgWidth = g.getFontMetrics().stringWidth(scoreMsg);
        g.drawString(scoreMsg, (PANEL_WIDTH - scoreMsgWidth) / 2, PANEL_HEIGHT / 2 + 20);
        
        // Hướng dẫn restart
        String restartMsg = "Press R to Restart";
        int restartMsgWidth = g.getFontMetrics().stringWidth(restartMsg);
        g.drawString(restartMsg, (PANEL_WIDTH - restartMsgWidth) / 2, PANEL_HEIGHT / 2 + 60);
    }

    // ==================== THÊM MỚI: Màn hình Win ====================
    private void drawGameWon(Graphics2D g) {
        // Overlay vàng
        g.setColor(new Color(255, 215, 0, 180));
        g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        
        // Chữ YOU WIN màu xanh lá
        g.setColor(new Color(0, 128, 0));
        g.setFont(new Font("Arial", Font.BOLD, 60));
        String msg = "YOU WIN!";
        int msgWidth = g.getFontMetrics().stringWidth(msg);
        g.drawString(msg, (PANEL_WIDTH - msgWidth) / 2, PANEL_HEIGHT / 2 - 50);
        
        // Điểm số
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        String scoreMsg = "Final Score: " + score;
        int scoreMsgWidth = g.getFontMetrics().stringWidth(scoreMsg);
        g.drawString(scoreMsg, (PANEL_WIDTH - scoreMsgWidth) / 2, PANEL_HEIGHT / 2 + 20);
        
        // Hướng dẫn
        String restartMsg = "Press R to Play Again";
        int restartMsgWidth = g.getFontMetrics().stringWidth(restartMsg);
        g.drawString(restartMsg, (PANEL_WIDTH - restartMsgWidth) / 2, PANEL_HEIGHT / 2 + 60);
    }

    // ==================== THÊM MỚI: Màn hình Pause ====================
    private void drawPaused(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 60));
        String msg = "PAUSED";
        int msgWidth = g.getFontMetrics().stringWidth(msg);
        g.drawString(msg, (PANEL_WIDTH - msgWidth) / 2, PANEL_HEIGHT / 2);
    }

    // xử lý phím
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // ==================== THÊM MỚI: Phím R để Restart ====================
        if (key == KeyEvent.VK_R) {
            restartGame();
            return;
        }
        
        // ==================== THÊM MỚI: Phím SPACE để Pause ====================
        if (key == KeyEvent.VK_SPACE) {
            if (!gameOver && !gameWon) {
                gamePaused = !gamePaused;
                repaint();
            }
            return;
        }

        // ==================== SỬA: Chỉ di chuyển khi game đang chạy ====================
        if (!gameOver && !gameWon && !gamePaused) {
            if (key == KeyEvent.VK_LEFT) {
                paddle.moveLeft();
            } else if (key == KeyEvent.VK_RIGHT) {
                paddle.moveRight(PANEL_WIDTH);
            }
            repaint(); // vẽ lại sau khi di chuyển
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    //hàm chuyên để khởi tạo những đối tượng trong game
    public void initGameObjects() {
        paddle = new Paddle();
        paddle.x = (PANEL_WIDTH - paddle.width) / 2;
        paddle.y = PANEL_HEIGHT - paddle.height - 50; // đặt paddle gần đáy

        ball = new Ball(300, 300);

        manage = new BrickManagement(6, 12, 50, 30, 10);
        
        // ==================== THÊM MỚI: Reset game state ====================
        score = 0;
        lives = 3;
        gameOver = false;
        gameWon = false;
        gamePaused = false;
    }

    public void initPanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(backgroundColor); // ==================== SỬA: Dùng backgroundColor từ skin ====================
        setFocusable(true);
        addKeyListener(this);
    }

    public void initTimer() {
        timer = new Timer(10, this);
        timer.start();
    }

    public void updateGame() {
        // ==================== THÊM MỚI: Dừng update khi game kết thúc hoặc pause ====================
        if (gameOver || gameWon || gamePaused) {
            return;
        }
        
        // ==================== THÊM MỚI: Kiểm tra bóng rơi xuống đáy ====================
        if (ball.y + ball.diameter >= PANEL_HEIGHT) {
            lives--;
            if (lives <= 0) {
                gameOver = true;
            } else {
                // Reset bóng về giữa màn hình
                ball.x = PANEL_WIDTH / 2;
                ball.y = PANEL_HEIGHT / 2;
                ball.dY = -ball.SPEED;
                ball.dX = 0;
            }
            return;
        }
        
        ball.checkWallColiision(PANEL_HEIGHT, PANEL_WIDTH);

        Rectangle ball_bounds = ball.getBounds();
        Rectangle paddle_bounds = paddle.getBounds();

        if(hasCollision(ball_bounds, paddle_bounds)) {
            ball.reverseY();
            ball.randomizeMove();

            double paddleCenter = paddle.x + paddle.width / 2.0;
            double hitPos = (ball.x + ball.diameter / 2.0 - paddleCenter) / (paddle.width / 2.0);

            // giới hạn hitPos [-1, 1]
            hitPos = Math.max(-1, Math.min(1, hitPos));

            double angle = hitPos * Math.toRadians(60); // lệch tối đa 60 độ
            ball.dX = ball.SPEED * Math.sin(angle);
            ball.dY = -ball.SPEED * Math.cos(angle);

        }

        ArrayList<Brick> bricks = manage.getBricks();
        
        // ==================== THÊM MỚI: Đếm số gạch còn lại ====================
        int activeBricks = 0;
        
        for(int i = 0; i < bricks.size(); i++) {
            Brick b = bricks.get(i);
            Rectangle brick_bounds = b.getBounds();

            if(!b.isDestroyed()) {
                activeBricks++; // ==================== THÊM MỚI ====================
                
                if(hasCollision(ball_bounds, brick_bounds)) {
                    b.setDestroyed(true);
                    score += 10; // ==================== THÊM MỚI: Cộng điểm ====================
                    handleBallBounce(ball, b, ball_bounds);
                }
            }
        }
        
        // ==================== THÊM MỚI: Kiểm tra thắng (phá hết gạch) ====================
        if (activeBricks == 0) {
            gameWon = true;
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

    // ==================== THÊM MỚI: Hàm Restart Game ====================
    private void restartGame() {
        initGameObjects();
        repaint();
    }

    // ==================== THÊM MỚI: Skin System - Đổi màu sắc ====================
    public void setSkin(Color paddle, Color ball, Color background, Color text) {
        this.paddleColor = paddle;
        this.ballColor = ball;
        this.backgroundColor = background;
        this.textColor = text;
        setBackground(backgroundColor);
        repaint();
    }

    public void initFrame() {
        JFrame frame = new JFrame("Paddle Test");
        GamePanel panel = new GamePanel();

        // đặt kích thước panel TRƯỚC khi add vào frame
        panel.setPreferredSize(new Dimension(panel.PANEL_WIDTH, panel.PANEL_HEIGHT));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
}