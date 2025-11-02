import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.*;

public class MenuPanel extends JPanel {

    // --- Biến animation ---
    private float glowAlpha = 0f;
    private boolean glowIncreasing = true;
    private int starOffset = 0;
    private float subtitleAlpha = 1f;
    private boolean subtitleFadeOut = true;
    private int buttonOffsetY = 100; // Hiệu ứng trượt vào cho nút
    private Timer animationTimer;

    private JLabel subtitleLabel;
    private JButton[] buttons;

    public MenuPanel() {
        setLayout(null);
        setPreferredSize(new Dimension(800, 600));

        SoundManager.getInstance().playBackgroundMusic("menu_music");

        // Title label với hiệu ứng neon động
        JLabel titleLabel = new JLabel("ARKANOID") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setFont(new Font("Arial Black", Font.BOLD, 72));

                // Shadow/Glow
                g2d.setColor(new Color(0, 255, 255, (int)(80 + 80 * glowAlpha)));
                for (int i = 0; i < 8; i++) {
                    g2d.drawString("ARKANOID", getWidth()/2 - 220, 80);
                }

                // Main text
                g2d.setColor(new Color(0, 255, 255));
                g2d.drawString("ARKANOID", getWidth()/2 - 220, 80);
            }
        };
        titleLabel.setBounds(0, 50, 800, 100);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel);

        // Subtitle nhấp nháy
        subtitleLabel = new JLabel("BRICK BREAKER") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, subtitleAlpha));
                super.paintComponent(g);
            }
        };
        subtitleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        subtitleLabel.setForeground(new Color(255, 200, 100));
        subtitleLabel.setBounds(0, 140, 800, 30);
        subtitleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(subtitleLabel);

        // Buttons
        String[] buttonTexts = {"START GAME", "HIGH SCORES", "SHOP", "OPTIONS", "EXIT"};
        buttons = new JButton[buttonTexts.length];
        int startY = 220;

        for (int i = 0; i < buttonTexts.length; i++) {
            JButton button = createStyledButton(buttonTexts[i]);
            button.setBounds(250, startY + (i * 70) + buttonOffsetY, 300, 40);

            final int index = i;
            button.addActionListener(e -> {
                SoundManager.getInstance().playSound("click");
                if (index == 0) Main.startGame();
                else if (index == 2) Main.showShop();
                else if (index == 3) Main.showOptions();
                else if (index == 4) System.exit(0);
                else JOptionPane.showMessageDialog(this, buttonTexts[index] + " - Đang phát triển!");
            });

            add(button);
            buttons[i] = button;
        }

        // Version label
        JLabel versionLabel = new JLabel("v1.0.0");
        versionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        versionLabel.setForeground(new Color(150, 150, 150));
        versionLabel.setBounds(10, 540, 100, 20);
        add(versionLabel);

        // Copyright label
        JLabel copyrightLabel = new JLabel("© 2025 - Nhấn SPACE để bắt đầu");
        copyrightLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        copyrightLabel.setForeground(new Color(150, 150, 150));
        copyrightLabel.setBounds(0, 540, 800, 20);
        copyrightLabel.setHorizontalAlignment(JLabel.CENTER);
        add(copyrightLabel);

        // --- Timer animation ---
        animationTimer = new Timer(40, e -> {
            // Glow tiêu đề
            glowAlpha += glowIncreasing ? 0.05f : -0.05f;
            if (glowAlpha > 1f) glowIncreasing = false;
            if (glowAlpha < 0f) glowIncreasing = true;

            // Nền sao trôi
            starOffset = (starOffset + 1) % 800;

            // Subtitle mờ dần
            subtitleAlpha += subtitleFadeOut ? -0.03f : 0.03f;
            if (subtitleAlpha < 0.3f) subtitleFadeOut = false;
            if (subtitleAlpha > 1f) subtitleFadeOut = true;
            subtitleLabel.repaint();

            // Hiệu ứng trượt cho nút (vào chậm dần)
            if (buttonOffsetY > 0) {
                buttonOffsetY -= 5;
                for (int i = 0; i < buttons.length; i++) {
                    buttons[i].setLocation(250, 220 + i * 70 + buttonOffsetY);
                }
            }

            repaint();
        });
        animationTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Gradient background
        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(25, 25, 60),
                0, getHeight(), new Color(15, 32, 70)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Sao lấp lánh di chuyển
        g2d.setColor(new Color(255, 255, 255, 100));
        for (int i = 0; i < 60; i++) {
            int x = (i * 131 + starOffset) % getWidth();
            int y = (i * 241) % getHeight();
            g2d.fillOval(x, y, 2, 2);
        }

        // Grid pattern nhẹ
        g2d.setColor(new Color(100, 150, 255, 25));
        for (int i = 0; i < getWidth(); i += 40)
            g2d.drawLine(i, 0, i, getHeight());
        for (int i = 0; i < getHeight(); i += 40)
            g2d.drawLine(0, i, getWidth(), i);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean hover = Boolean.TRUE.equals(getClientProperty("hover"));

                GradientPaint gp = hover
                        ? new GradientPaint(0, 0, new Color(0, 150, 255), 0, getHeight(), new Color(0, 100, 200))
                        : new GradientPaint(0, 0, new Color(50, 50, 100), 0, getHeight(), new Color(30, 30, 70));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                g2d.setColor(hover ? new Color(0, 200, 255) : new Color(100, 100, 150));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 15, 15);

                g2d.setColor(Color.WHITE);
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(getText(), x, y);
            }
        };

        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.putClientProperty("hover", true);
                button.repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.putClientProperty("hover", false);
                button.repaint();
            }
        });

        return button;
    }
}
