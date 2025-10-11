import javax.swing.*;

public class Main {
    private static JFrame frame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
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
        frame.getContentPane().removeAll();
        MenuPanel menuPanel = new MenuPanel();
        frame.add(menuPanel);
        frame.revalidate();
        frame.repaint();
    }

    public static void startGame() {
        frame.getContentPane().removeAll();
        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.revalidate();
        frame.repaint();
        gamePanel.requestFocusInWindow(); // Quan trọng để nhận input từ bàn phím
    }
}