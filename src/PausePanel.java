import java.awt.*;
import javax.swing.*;

public class PausePanel extends JPanel {
    public PausePanel(Runnable onResume, Runnable onRestart, Runnable onMenu) {
        setOpaque(false);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JButton resume = new JButton("Tiếp tục");
        JButton restart = new JButton("Chơi lại");
        JButton menu = new JButton("Về Menu");

        resume.addActionListener(e -> onResume.run());
        restart.addActionListener(e -> onRestart.run());
        menu.addActionListener(e -> onMenu.run());

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBackground(new Color(0, 0, 0, 180));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        panel.add(resume);
        panel.add(restart);
        panel.add(menu);

        add(panel, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
