import java.awt.*;
import javax.swing.*;

public class GameOverPanel extends JPanel {
    public GameOverPanel(int score, int highScore, Runnable onRestart, Runnable onMenu) {
        setOpaque(false);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel title = new JLabel("GAME OVER");
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.RED);

        JLabel scoreLabel = new JLabel("Điểm: " + score);
        JLabel highLabel = new JLabel("Kỷ lục: " + highScore);
        scoreLabel.setForeground(Color.WHITE);
        highLabel.setForeground(Color.YELLOW);

        JButton restart = new JButton("Chơi lại");
        JButton menu = new JButton("Về Menu");

        restart.addActionListener(e -> onRestart.run());
        menu.addActionListener(e -> onMenu.run());

        JPanel inner = new JPanel(new GridLayout(0, 1, 10, 10));
        inner.setBackground(new Color(0, 0, 0, 160));
        inner.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        inner.add(title);
        inner.add(scoreLabel);
        inner.add(highLabel);
        inner.add(restart);
        inner.add(menu);

        add(inner, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
