import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private MenuPanel menuPanel;
    private GamePanel gamePanel;
    private PausePanel pausePanel;
    private GameOverPanel gameOverPanel;

    public MainFrame() {
        setTitle("Arkanoid Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);

        // Khởi tạo các panel
        menuPanel = new MenuPanel(this);
        gamePanel = new GamePanel(this);

        mainPanel.add(menuPanel, "menu");
        mainPanel.add(gamePanel, "game");

        showMenu();
    }

    // --- Chuyển màn hình ---
    public void showMenu() {
        cardLayout.show(mainPanel, "menu");
    }

    public void startGame() {
        gamePanel = new GamePanel(this); // tạo mới mỗi lần chơi
        mainPanel.add(gamePanel, "game");
        cardLayout.show(mainPanel, "game");
    }

    public void showPause() {
        pausePanel = new PausePanel(
            () -> resumeGame(),
            () -> startGame(),
            () -> showMenu()
        );
        setGlassPane(pausePanel);
        pausePanel.setVisible(true);
    }

    public void showGameOver(int score) {
        gameOverPanel = new GameOverPanel(score, 9999,  // tạm highscore = 9999
            () -> startGame(),
            () -> showMenu()
        );
        setGlassPane(gameOverPanel);
        gameOverPanel.setVisible(true);
    }

    public void resumeGame() {
        getGlassPane().setVisible(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
