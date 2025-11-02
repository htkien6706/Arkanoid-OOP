import javax.swing.*;

public class Main {
    private static JFrame frame;
    private static ShopManager shopManager;
    private static CoinManager coinManager;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Khởi tạo các manager
            shopManager = new ShopManager();
            coinManager = new CoinManager();
            SoundManager.getInstance();

            frame = new JFrame("ARKANOID");
            frame.setSize(800, 636);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);

            // Hiển thị menu đầu tiên
            showMenu();

            frame.setVisible(true);
        });
    }

    public static void showMenu() {
        SoundManager.getInstance().stopBackgroundMusic();
        frame.getContentPane().removeAll();
        MenuPanel menuPanel = new MenuPanel();
        frame.add(menuPanel);
        frame.revalidate();
        frame.repaint();
    }

    public static void startGame() {
        SoundManager.getInstance().stopBackgroundMusic();
        frame.getContentPane().removeAll();
        GamePanel gamePanel = new GamePanel(shopManager, coinManager);
        frame.add(gamePanel);
        frame.revalidate();
        frame.repaint();
        gamePanel.requestFocusInWindow();
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
    public static ShopManager getShopManager() {
        return shopManager;
    }

    public static CoinManager getCoinManager() {
        return coinManager;
    }
}
