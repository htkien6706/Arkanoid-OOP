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

    int PANEL_HEIGHT = 636;
    int PANEL_WIDTH = 800;


    // main test

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
        paddle.draw((Graphics2D) g);
        ball.draw((Graphics2D) g);
        manage.draw((Graphics2D) g);
    }


    // xử lý phím
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            paddle.moveLeft();
        } else if (key == KeyEvent.VK_RIGHT) {
            paddle.moveRight(PANEL_WIDTH);
        }

        repaint(); // vẽ lại sau khi di chuyển
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
    }

    public void initPanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.white);
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

            double paddleCenter = paddle.x + paddle.width / 2.0;
            double hitPos = (ball.x + ball.diameter / 2.0 - paddleCenter) / (paddle.width / 2.0);

            // giới hạn hitPos [-1, 1]
            hitPos = Math.max(-1, Math.min(1, hitPos));

            double angle = hitPos * Math.toRadians(60); // lệch tối đa 60 độ
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

                    handleBallBounce(ball, b, ball_bounds);

                    // dùng cái hàm nẩy quả bóng lên
                }
            }
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

