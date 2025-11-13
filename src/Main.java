import javax.swing.*;

public class Main {
    private static JFrame frame;
    private static ShopManager shopManager;
    private static CoinManager coinManager;
    private static ScoreManager scoreManager; // THÊM MỚI

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Khởi tạo các manager
                shopManager = new ShopManager();
                coinManager = new CoinManager();
                scoreManager = new ScoreManager(); // THÊM MỚI
                SoundManager.getInstance();

                frame = new JFrame("ARKANOID");
                frame.setSize(800, 636);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setResizable(false);

                showMenu();
                frame.setVisible(true);
            }
        });
    }

    public static void showMenu() {
        SoundManager.getInstance().stopBackgroundMusic();
        frame.getContentPane().removeAll();
        MenuPanel menuPanel = new MenuPanel();
        frame.add(menuPanel);
        frame.revalidate();
        frame.repaint();

        // Gọi cập nhật trạng thái nút Continue
        SwingUtilities.invokeLater(() -> menuPanel.updateContinueButton());
    }

    public static void showShop() {
        frame.getContentPane().removeAll();
        ShopPanel shopPanel = new ShopPanel(shopManager, coinManager);
        frame.add(shopPanel);
        frame.revalidate();
        frame.repaint();
    }

    public static void showOptions() {
        frame.getContentPane().removeAll();
        OptionsPanel optionsPanel = new OptionsPanel();
        frame.add(optionsPanel);
        frame.revalidate();
        frame.repaint();
    }

    // THÊM METHOD MỚI
    public static void showHighScores() {
        frame.getContentPane().removeAll();
        HighScorePanel highScorePanel = new HighScorePanel(scoreManager);
        frame.add(highScorePanel);
        frame.revalidate();
        frame.repaint();
    }

    public static void continueGame() {
        GameSave save = SaveManager.loadGame();
        if (save == null) {
            JOptionPane.showMessageDialog(frame, "Không có dữ liệu lưu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SoundManager.getInstance().stopBackgroundMusic();
        frame.getContentPane().removeAll();
        GamePanel gamePanel = new GamePanel(shopManager, coinManager);
        gamePanel.loadGame(save); // Tải save
        frame.add(gamePanel);
        frame.revalidate();
        frame.repaint();
        gamePanel.requestFocusInWindow();
    }

    public static void startGame() {
        SoundManager.getInstance().stopBackgroundMusic();
        frame.getContentPane().removeAll();
        GamePanel gamePanel = new GamePanel(shopManager, coinManager);
        frame.add(gamePanel);
        frame.revalidate();
        frame.repaint();
        gamePanel.requestFocusInWindow();

        // KHỞI ĐỘNG TIMER
        gamePanel.initTimer(); // Đảm bảo timer chạy
    }


    public static ShopManager getShopManager() {
        return shopManager;
    }

    public static CoinManager getCoinManager() {
        return coinManager;
    }

    // THÊM METHOD MỚI
    public static ScoreManager getScoreManager() {
        return scoreManager;
    }

}