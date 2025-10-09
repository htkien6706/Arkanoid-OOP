import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements KeyListener {
    private Ball ball;
    private Paddle paddle;
    private Brick brick;
    private Timer timer;
    private BrickManagement manage;

    int panelWidth = 800;
    int panelHeight = 600;

    public GamePanel() {
        // setup panel
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setBackground(Color.white);
        setFocusable(true);
        addKeyListener(this);

        initGameObjects();

        timer = new Timer(16, e -> {
            ball.move();
            repaint();
        });

        timer.start();

    }

    // vẽ mọi thứ
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
            paddle.moveRight(panelWidth);
        }

        repaint(); // vẽ lại sau khi di chuyển
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}


    //hàm chuyên để khởi tạo những đối tượng trong game
    public void initGameObjects() {
        paddle = new Paddle();
        paddle.x = (panelWidth - paddle.width) / 2;
        paddle.y = panelHeight - paddle.height - 10; // đặt paddle gần đáy

        ball = new Ball(300, 300);

        manage = new BrickManagement(6, 14, 50, 30, 10);
    }

    // main test
    public static void main(String[] args) {
        JFrame frame = new JFrame("Paddle Test");
        GamePanel panel = new GamePanel();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

