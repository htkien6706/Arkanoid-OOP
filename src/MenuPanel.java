import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.*;

public class MenuPanel extends JPanel {
    private JButton continueButton;   // THÊM DÒNG NÀY
    public MenuPanel() {
        setLayout(null);
        setPreferredSize(new Dimension(800, 600));
        SoundManager.getInstance().playBackgroundMusic("menu_music");
        // Title label với hiệu ứng neon
        JLabel titleLabel = new JLabel("ARKANOID") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Shadow/Glow effect
                g2d.setColor(new Color(0, 255, 255, 100));
                g2d.setFont(new Font("Arial Black", Font.BOLD, 72));
                for (int i = 0; i < 10; i++) {
                    g2d.drawString("ARKANOID",
                            getWidth()/2 - 220 + (int)(Math.random() * 4),
                            80 + (int)(Math.random() * 4));
                }

                // Main text
                g2d.setColor(new Color(0, 255, 255));
                g2d.drawString("ARKANOID", getWidth()/2 - 220, 80);
            }

        };
        titleLabel.setBounds(0, 50, 800, 100);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel);

        // Subtitle
        JLabel subtitleLabel = new JLabel("BRICK BREAKER");
        subtitleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        subtitleLabel.setForeground(new Color(255, 200, 100));
        subtitleLabel.setBounds(0, 140, 800, 30);
        subtitleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(subtitleLabel);

        // Buttons
        String[] buttonTexts = {"START GAME","CONTINUE", "HIGH SCORES", "SHOP", "OPTIONS", "EXIT"};
        int startY = 220;

        for (int i = 0; i < buttonTexts.length; i++) {
            JButton button = createStyledButton(buttonTexts[i]);
            button.setBounds(250, startY + (i * 60), 300, 40);

            final int index = i;
            button.addActionListener(e -> {
                SoundManager.getInstance().playSound("click");
                if (index == 0) {
                    System.out.println("START GAME clicked!");
                    Main.startGame();
                } else if (index == 1) {
                    Main.continueGame(); // MỚI
                } else if (index == 2) {
                    System.out.println("HIGH SCORES clicked!");
                    Main.showHighScores();
                } else if (index == 3) {
                    System.out.println("SHOP clicked!");
                    Main.showShop();
                } else if (index == 4) {
                    System.out.println("OPTION clicked!");
                    Main.showOptions();
                } else if (index == 5) {
                    System.exit(0);
                }
            });

            // THÊM: Lưu nút CONTINUE để cập nhật sau
            if (index == 1) {
                continueButton = button;
            }

            // Giữ nguyên đoạn kiểm tra save cũ (sẽ được thay bằng updateContinueButton)
            if (!SaveManager.hasSave() && index == 1) {
                button.setEnabled(false);
                button.setText("CONTINUE GAME (No Save)");
            }

            add(button);

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

        // Vẽ các "ngôi sao" background
        g2d.setColor(new Color(255, 255, 255, 100));
        for (int i = 0; i < 50; i++) {
            int x = (i * 137) % getWidth();
            int y = (i * 241) % getHeight();
            g2d.fillOval(x, y, 2, 2);
        }

        // Vẽ grid pattern
        g2d.setColor(new Color(100, 150, 255, 30));
        for (int i = 0; i < getWidth(); i += 40) {
            g2d.drawLine(i, 0, i, getHeight());
        }
        for (int i = 0; i < getHeight(); i += 40) {
            g2d.drawLine(0, i, getWidth(), i);
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                boolean hover = Boolean.TRUE.equals(getClientProperty("hover"));

                if (hover) {
                    GradientPaint gp = new GradientPaint(
                            0, 0, new Color(0, 150, 255),
                            0, getHeight(), new Color(0, 100, 200)
                    );
                    g2d.setPaint(gp);
                } else {
                    GradientPaint gp = new GradientPaint(
                            0, 0, new Color(50, 50, 100),
                            0, getHeight(), new Color(30, 30, 70)
                    );
                    g2d.setPaint(gp);
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                g2d.setColor(hover ? new Color(0, 200, 255) : new Color(100, 100, 150));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 15, 15);

                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(getText(), x, y);
            }
        };

        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
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

    public void updateContinueButton() {
        boolean hasSave = SaveManager.hasSave();
        if (hasSave) {
            continueButton.setEnabled(true);
            continueButton.setText("CONTINUE GAME");
        } else {
            continueButton.setEnabled(false);
            continueButton.setText("CONTINUE GAME (No Save)");
            // TÙY CHỌN: ẨN HOÀN TOÀN
             continueButton.setVisible(false);
        }
    }
}