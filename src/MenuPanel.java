import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

public class MenuPanel extends JPanel implements ActionListener {
    private JButton continueButton;

    // THÊM: Animation system
    private Timer animationTimer;
    private ArrayList<MenuParticle> particles;
    private ArrayList<FloatingOrb> floatingOrbs;
    private Random random;

    // THÊM: Hiệu ứng động
    private float gradientOffset = 0;
    private float waveOffset = 0;
    private float titlePulse = 1.0f;
    private float titlePulseDirection = 0.003f;
    private float glowIntensity = 0;
    private Point mousePos = new Point(400, 300);

    // THÊM: Animation cho title
    private float neonFlicker = 1.0f;
    private long lastFlickerTime = 0;

<<<<<<< Updated upstream
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

=======
>>>>>>> Stashed changes
    public MenuPanel() {
        setLayout(null);
        setPreferredSize(new Dimension(800, 600));

        SoundManager.getInstance().playBackgroundMusic("menu_music");

<<<<<<< Updated upstream
        // Title label với hiệu ứng neon động
=======
        // THÊM: Khởi tạo animation system
        random = new Random();
        particles = new ArrayList<>();
        floatingOrbs = new ArrayList<>();
        initAnimations();
        initMouseTracking();

        // Title label với hiệu ứng neon NÂNG CẤP
>>>>>>> Stashed changes
        JLabel titleLabel = new JLabel("ARKANOID") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

<<<<<<< Updated upstream
                g2d.setFont(new Font("Arial Black", Font.BOLD, 72));

                // Shadow/Glow
                g2d.setColor(new Color(0, 255, 255, (int)(80 + 80 * glowAlpha)));
                for (int i = 0; i < 8; i++) {
                    g2d.drawString("ARKANOID", getWidth()/2 - 220, 80);
                }

                // Main text
                g2d.setColor(new Color(0, 255, 255));
                g2d.drawString("ARKANOID", getWidth()/2 - 220, 80);
=======
                // THÊM: Outer glow với nhiều layers
                for (int radius = 20; radius > 0; radius -= 2) {
                    g2d.setColor(new Color(0, 255, 255, (int)(5 * neonFlicker)));
                    g2d.setFont(new Font("Arial Black", Font.BOLD, 72 + radius/2));
                    FontMetrics fm = g2d.getFontMetrics();
                    int textWidth = fm.stringWidth("ARKANOID");
                    g2d.drawString("ARKANOID", (getWidth() - textWidth)/2, 80);
                }

                // Enhanced shadow/Glow effect
                g2d.setColor(new Color(0, 255, 255, (int)(120 * neonFlicker)));
                g2d.setFont(new Font("Arial Black", Font.BOLD, 72));
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth("ARKANOID");
                int textX = (getWidth() - textWidth) / 2;

                for (int i = 0; i < 15; i++) {
                    int offsetX = (int)(Math.sin(i * 0.5 + glowIntensity) * 3);
                    int offsetY = (int)(Math.cos(i * 0.5 + glowIntensity) * 3);
                    g2d.drawString("ARKANOID", textX + offsetX, 80 + offsetY);
                }

                // THÊM: Inner bright core
                g2d.setColor(new Color(200, 255, 255, (int)(200 * neonFlicker)));
                g2d.drawString("ARKANOID", textX, 80);

                // Main text với gradient
                GradientPaint gradient = new GradientPaint(
                        textX, 50, new Color(0, 255, 255),
                        textX + textWidth, 100, new Color(100, 200, 255)
                );
                g2d.setPaint(gradient);
                g2d.drawString("ARKANOID", textX, 80);

                // THÊM: Highlight effect
                g2d.setColor(new Color(255, 255, 255, (int)(150 * neonFlicker)));
                g2d.setStroke(new BasicStroke(2f));
                g2d.drawString("ARKANOID", textX - 1, 79);
>>>>>>> Stashed changes
            }
        };
        titleLabel.setBounds(0, 50, 800, 100);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel);

<<<<<<< Updated upstream
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
=======
        // Subtitle NÂNG CẤP với animation
        JLabel subtitleLabel = new JLabel("BRICK BREAKER") {
            private float letterSpacing = 0;
            private float letterDirection = 0.1f;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // THÊM: Animated letter spacing
                letterSpacing += letterDirection;
                if (letterSpacing > 5 || letterSpacing < 0) letterDirection *= -1;

                String text = "BRICK BREAKER";
                g2d.setFont(new Font("Arial", Font.BOLD, 20));
                FontMetrics fm = g2d.getFontMetrics();

                int totalWidth = fm.stringWidth(text) + (int)(text.length() * letterSpacing);
                int startX = (getWidth() - totalWidth) / 2;

                // Shadow
                g2d.setColor(new Color(0, 0, 0, 100));
                for (int i = 0; i < text.length(); i++) {
                    String letter = String.valueOf(text.charAt(i));
                    g2d.drawString(letter, startX + 2, 22);
                    startX += fm.stringWidth(letter) + letterSpacing;
                }

                // Main text với gradient
                startX = (getWidth() - totalWidth) / 2;
                for (int i = 0; i < text.length(); i++) {
                    String letter = String.valueOf(text.charAt(i));
                    float hue = (i / (float)text.length() + glowIntensity * 0.1f) % 1.0f;
                    Color color = Color.getHSBColor(hue, 0.8f, 1.0f);
                    g2d.setColor(color);
                    g2d.drawString(letter, startX, 20);
                    startX += fm.stringWidth(letter) + letterSpacing;
                }
            }
        };
>>>>>>> Stashed changes
        subtitleLabel.setBounds(0, 140, 800, 30);
        subtitleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(subtitleLabel);

<<<<<<< Updated upstream
        // Buttons
        String[] buttonTexts = {"START GAME", "HIGH SCORES", "SHOP", "OPTIONS", "EXIT"};
        buttons = new JButton[buttonTexts.length];
        int startY = 220;

        for (int i = 0; i < buttonTexts.length; i++) {
            JButton button = createStyledButton(buttonTexts[i]);
            button.setBounds(250, startY + (i * 70) + buttonOffsetY, 300, 40);
=======
        // Buttons NÂNG CẤP
        String[] buttonTexts = {"START GAME","CONTINUE", "HIGH SCORES", "SHOP", "OPTIONS", "EXIT"};
        int startY = 220;

        for (int i = 0; i < buttonTexts.length; i++) {
            JButton button = createEnhancedStyledButton(buttonTexts[i], i);
            button.setBounds(250, startY + (i * 60), 300, 40);
>>>>>>> Stashed changes

            final int index = i;
            button.addActionListener(e -> {
                SoundManager.getInstance().playSound("click");
<<<<<<< Updated upstream
                if (index == 0) Main.startGame();
                else if (index == 2) Main.showShop();
                else if (index == 3) Main.showOptions();
                else if (index == 4) System.exit(0);
                else JOptionPane.showMessageDialog(this, buttonTexts[index] + " - Đang phát triển!");
            });

            add(button);
            buttons[i] = button;
=======
                if (index == 0) {
                    System.out.println("START GAME clicked!");
                    stopAnimations(); // THÊM
                    Main.startGame();
                } else if (index == 1) {
                    stopAnimations(); // THÊM
                    Main.continueGame();
                } else if (index == 2) {
                    System.out.println("HIGH SCORES clicked!");
                    Main.showHighScores();
                } else if (index == 3) {
                    System.out.println("SHOP clicked!");
                    stopAnimations(); // THÊM
                    Main.showShop();
                } else if (index == 4) {
                    System.out.println("OPTION clicked!");
                    Main.showOptions();
                } else if (index == 5) {
                    System.exit(0);
                }
            });

            if (index == 1) {
                continueButton = button;
            }

            if (!SaveManager.hasSave() && index == 1) {
                button.setEnabled(false);
                button.setText("CONTINUE GAME (No Save)");
            }

            add(button);
>>>>>>> Stashed changes
        }

        // Version label
        JLabel versionLabel = new JLabel("v1.0.0");
        versionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        versionLabel.setForeground(new Color(150, 150, 150));
        versionLabel.setBounds(10, 540, 100, 20);
        add(versionLabel);

        // Copyright label NÂNG CẤP
        JLabel copyrightLabel = new JLabel("© 2025 - Nhấn SPACE để bắt đầu") {
            private float alpha = 0.5f;
            private float alphaDirection = 0.02f;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                alpha += alphaDirection;
                if (alpha > 1.0f || alpha < 0.3f) alphaDirection *= -1;

                g2d.setColor(new Color(150, 150, 150, (int)(alpha * 255)));
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                g2d.drawString(getText(), x, 15);
            }
        };
        copyrightLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        copyrightLabel.setBounds(0, 540, 800, 20);
        copyrightLabel.setHorizontalAlignment(JLabel.CENTER);
        add(copyrightLabel);

<<<<<<< Updated upstream
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
=======
        // THÊM: Start animation timer
        animationTimer = new Timer(16, this); // ~60 FPS
        animationTimer.start();
    }

    // THÊM: Khởi tạo particles và orbs
    private void initAnimations() {
        // Tạo 80 particles nhỏ
        for (int i = 0; i < 80; i++) {
            particles.add(new MenuParticle(
                    random.nextInt(800),
                    random.nextInt(600),
                    random.nextFloat() * 2 + 1,
                    random.nextFloat() * 0.5f + 0.2f
            ));
        }

        // Tạo 5 orbs lớn phát sáng
        for (int i = 0; i < 5; i++) {
            floatingOrbs.add(new FloatingOrb(
                    random.nextInt(800),
                    random.nextInt(600),
                    random.nextInt(30) + 20,
                    new Color(random.nextInt(100) + 155, random.nextInt(100) + 155, 255)
            ));
        }
    }

    // THÊM: Track mouse cho interactive effects
    private void initMouseTracking() {
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mousePos = e.getPoint();
            }
        });
>>>>>>> Stashed changes
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Gradient background NÂNG CẤP với animation
        GradientPaint gradient = new GradientPaint(
                0, gradientOffset, new Color(25, 25, 60),
                0, getHeight() + gradientOffset, new Color(15, 32, 70)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

<<<<<<< Updated upstream
        // Sao lấp lánh di chuyển
        g2d.setColor(new Color(255, 255, 255, 100));
        for (int i = 0; i < 60; i++) {
            int x = (i * 131 + starOffset) % getWidth();
=======
        // THÊM: Secondary animated gradient overlay
        GradientPaint overlay = new GradientPaint(
                gradientOffset, 0, new Color(30, 60, 100, 50),
                getWidth() + gradientOffset, getHeight(), new Color(60, 30, 100, 50)
        );
        g2d.setPaint(overlay);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // THÊM: Vẽ floating orbs trước
        drawFloatingOrbs(g2d);

        // Vẽ các "ngôi sao" background NÂNG CẤP
        drawEnhancedStars(g2d);

        // Vẽ grid pattern NÂNG CẤP với fade effect
        drawEnhancedGrid(g2d);

        // THÊM: Vẽ particles
        drawParticles(g2d);

        // THÊM: Vẽ mouse glow effect
        drawMouseGlow(g2d);

        // THÊM: Vẽ animated wave lines
        drawWaveLines(g2d);
    }

    // THÊM: Vẽ floating orbs phát sáng
    private void drawFloatingOrbs(Graphics2D g2d) {
        for (FloatingOrb orb : floatingOrbs) {
            // Glow effect
            RadialGradientPaint glow = new RadialGradientPaint(
                    orb.x, orb.y, orb.size * 2,
                    new float[]{0f, 0.5f, 1f},
                    new Color[]{
                            new Color(orb.color.getRed(), orb.color.getGreen(), orb.color.getBlue(), 80),
                            new Color(orb.color.getRed(), orb.color.getGreen(), orb.color.getBlue(), 30),
                            new Color(orb.color.getRed(), orb.color.getGreen(), orb.color.getBlue(), 0)
                    }
            );
            g2d.setPaint(glow);
            g2d.fillOval((int)(orb.x - orb.size * 2), (int)(orb.y - orb.size * 2),
                    (int)(orb.size * 4), (int)(orb.size * 4));

            // Core
            RadialGradientPaint core = new RadialGradientPaint(
                    orb.x, orb.y, orb.size,
                    new float[]{0f, 1f},
                    new Color[]{
                            new Color(255, 255, 255, 150),
                            orb.color
                    }
            );
            g2d.setPaint(core);
            g2d.fillOval((int)(orb.x - orb.size), (int)(orb.y - orb.size),
                    (int)(orb.size * 2), (int)(orb.size * 2));
        }
    }

    // THÊM: Enhanced stars với twinkle effect
    private void drawEnhancedStars(Graphics2D g2d) {
        for (int i = 0; i < 50; i++) {
            int x = (i * 137) % getWidth();
>>>>>>> Stashed changes
            int y = (i * 241) % getHeight();
            float twinkle = (float)Math.sin(System.currentTimeMillis() * 0.001 + i) * 0.5f + 0.5f;
            g2d.setColor(new Color(255, 255, 255, (int)(150 * twinkle)));
            int size = (int)(2 + twinkle * 2);
            g2d.fillOval(x, y, size, size);

<<<<<<< Updated upstream
        // Grid pattern nhẹ
        g2d.setColor(new Color(100, 150, 255, 25));
        for (int i = 0; i < getWidth(); i += 40)
            g2d.drawLine(i, 0, i, getHeight());
        for (int i = 0; i < getHeight(); i += 40)
=======
            // Star glow
            g2d.setColor(new Color(255, 255, 255, (int)(30 * twinkle)));
            g2d.fillOval(x - 2, y - 2, size + 4, size + 4);
        }
    }

    // THÊM: Enhanced grid với fade và glow
    private void drawEnhancedGrid(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(1f));
        for (int i = 0; i < getWidth(); i += 40) {
            float alpha = (float)Math.sin(i * 0.02 + waveOffset * 0.1) * 0.3f + 0.5f;
            g2d.setColor(new Color(100, 150, 255, (int)(30 * alpha)));
            g2d.drawLine(i, 0, i, getHeight());
        }
        for (int i = 0; i < getHeight(); i += 40) {
            float alpha = (float)Math.sin(i * 0.02 + waveOffset * 0.1) * 0.3f + 0.5f;
            g2d.setColor(new Color(100, 150, 255, (int)(30 * alpha)));
>>>>>>> Stashed changes
            g2d.drawLine(0, i, getWidth(), i);
    }

    // THÊM: Vẽ particles
    private void drawParticles(Graphics2D g2d) {
        for (MenuParticle p : particles) {
            g2d.setColor(new Color(255, 255, 255, (int)(p.alpha * 200)));
            g2d.fillOval((int)p.x, (int)p.y, (int)p.size, (int)p.size);

            // Particle glow
            RadialGradientPaint glow = new RadialGradientPaint(
                    p.x + p.size/2, p.y + p.size/2, p.size * 2,
                    new float[]{0f, 1f},
                    new Color[]{
                            new Color(200, 220, 255, (int)(p.alpha * 100)),
                            new Color(200, 220, 255, 0)
                    }
            );
            g2d.setPaint(glow);
            g2d.fillOval((int)(p.x - p.size), (int)(p.y - p.size),
                    (int)(p.size * 4), (int)(p.size * 4));
        }
    }

    // THÊM: Mouse glow effect
    private void drawMouseGlow(Graphics2D g2d) {
        RadialGradientPaint mouseGlow = new RadialGradientPaint(
                mousePos.x, mousePos.y, 150,
                new float[]{0f, 0.7f, 1f},
                new Color[]{
                        new Color(100, 200, 255, 40),
                        new Color(100, 200, 255, 15),
                        new Color(100, 200, 255, 0)
                }
        );
        g2d.setPaint(mouseGlow);
        g2d.fillOval(mousePos.x - 150, mousePos.y - 150, 300, 300);
    }

    // THÊM: Animated wave lines
    private void drawWaveLines(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(2f));
        for (int i = 0; i < 3; i++) {
            g2d.setColor(new Color(100, 150, 255, 40 - i * 10));
            Path2D wave = new Path2D.Float();
            wave.moveTo(0, 150 + i * 150);

            for (int x = 0; x <= 800; x += 10) {
                double y = 150 + i * 150 + Math.sin((x + waveOffset) * 0.01 + i) * 20;
                wave.lineTo(x, y);
            }
            g2d.draw(wave);
        }
    }

    // NÂNG CẤP: Enhanced button với nhiều hiệu ứng hơn
    private JButton createEnhancedStyledButton(String text, int index) {
        JButton button = new JButton(text) {
            private float hoverProgress = 0f;
            private float pulsePhase = index * 0.5f;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean hover = Boolean.TRUE.equals(getClientProperty("hover"));

<<<<<<< Updated upstream
                GradientPaint gp = hover
                        ? new GradientPaint(0, 0, new Color(0, 150, 255), 0, getHeight(), new Color(0, 100, 200))
                        : new GradientPaint(0, 0, new Color(50, 50, 100), 0, getHeight(), new Color(30, 30, 70));
=======
                // Update hover animation
                if (hover) {
                    hoverProgress = Math.min(1f, hoverProgress + 0.1f);
                } else {
                    hoverProgress = Math.max(0f, hoverProgress - 0.1f);
                }

                pulsePhase += 0.05f;
                float pulse = (float)Math.sin(pulsePhase) * 0.1f + 0.9f;

                // THÊM: Outer glow khi hover
                if (hoverProgress > 0) {
                    g2d.setColor(new Color(0, 200, 255, (int)(hoverProgress * 100)));
                    g2d.fillRoundRect(-5, -5, getWidth() + 10, getHeight() + 10, 20, 20);

                    g2d.setColor(new Color(0, 150, 255, (int)(hoverProgress * 50)));
                    g2d.fillRoundRect(-10, -10, getWidth() + 20, getHeight() + 20, 25, 25);
                }

                // Button gradient với animation
                Color baseColor = hover ? new Color(0, 150, 255) : new Color(50, 50, 100);
                Color endColor = hover ? new Color(0, 100, 200) : new Color(30, 30, 70);

                GradientPaint gp = new GradientPaint(
                        0, 0, baseColor,
                        0, getHeight(), endColor
                );
>>>>>>> Stashed changes
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // THÊM: Inner shine effect
                if (hover) {
                    GradientPaint shine = new GradientPaint(
                            0, 0, new Color(255, 255, 255, (int)(hoverProgress * 60)),
                            0, getHeight() / 2, new Color(255, 255, 255, 0)
                    );
                    g2d.setPaint(shine);
                    g2d.fillRoundRect(5, 5, getWidth() - 10, getHeight() / 2, 10, 10);
                }

                // Border với pulse effect
                Color borderColor = hover ? new Color(0, 200, 255) : new Color(100, 100, 150);
                g2d.setColor(new Color(
                        borderColor.getRed(),
                        borderColor.getGreen(),
                        borderColor.getBlue(),
                        (int)(255 * pulse)
                ));
                g2d.setStroke(new BasicStroke(2f + hoverProgress * 2f));
                g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 15, 15);

<<<<<<< Updated upstream
                g2d.setColor(Color.WHITE);
=======
                // Text shadow
                g2d.setColor(new Color(0, 0, 0, 150));
                g2d.setFont(getFont());
>>>>>>> Stashed changes
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(getText(), x + 2, y + 2);

                // Main text với glow
                if (hover) {
                    g2d.setColor(new Color(200, 240, 255, (int)(hoverProgress * 150)));
                    for (int i = 0; i < 3; i++) {
                        g2d.drawString(getText(), x, y);
                    }
                }

                g2d.setColor(Color.WHITE);
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
                SoundManager.getInstance().playSound("paddle_hit"); // THÊM: hover sound
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.putClientProperty("hover", false);
            }
        });

        return button;
    }
<<<<<<< Updated upstream
}
=======

    // THÊM: Animation update loop
    @Override
    public void actionPerformed(ActionEvent e) {
        // Update animations
        gradientOffset += 0.5f;
        if (gradientOffset > 600) gradientOffset = 0;

        waveOffset += 2f;
        if (waveOffset > 1000) waveOffset = 0;

        glowIntensity += 0.05f;
        if (glowIntensity > Math.PI * 2) glowIntensity = 0;

        // Neon flicker effect (occasional)
        long now = System.currentTimeMillis();
        if (now - lastFlickerTime > 3000 && random.nextFloat() < 0.01f) {
            neonFlicker = random.nextFloat() * 0.3f + 0.7f;
            lastFlickerTime = now;
        } else if (neonFlicker < 1.0f) {
            neonFlicker = Math.min(1.0f, neonFlicker + 0.05f);
        }

        // Update particles
        for (MenuParticle p : particles) {
            p.update();
            if (p.y < -10) {
                p.y = 610;
                p.x = random.nextInt(800);
            }
        }

        // Update floating orbs
        for (FloatingOrb orb : floatingOrbs) {
            orb.update();
        }

        repaint();
    }

    // THÊM: Stop animations khi chuyển scene
    public void stopAnimations() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
    }

    public void updateContinueButton() {
        boolean hasSave = SaveManager.hasSave();
        if (hasSave) {
            continueButton.setEnabled(true);
            continueButton.setText("CONTINUE GAME");
        } else {
            continueButton.setEnabled(false);
            continueButton.setText("CONTINUE GAME (No Save)");
            continueButton.setVisible(false);
        }
    }

    // THÊM: Inner class cho particles
    private class MenuParticle {
        float x, y, size, speed, alpha;

        MenuParticle(float x, float y, float size, float speed) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.speed = speed;
            this.alpha = random.nextFloat() * 0.5f + 0.3f;
        }

        void update() {
            y -= speed;
            alpha = (float)(0.3f + Math.sin(y * 0.01f) * 0.3f);
        }
    }

    // THÊM: Inner class cho floating orbs
    private class FloatingOrb {
        float x, y, size;
        float vx, vy;
        Color color;

        FloatingOrb(float x, float y, float size, Color color) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.color = color;
            this.vx = (random.nextFloat() - 0.5f) * 0.5f;
            this.vy = (random.nextFloat() - 0.5f) * 0.5f;
        }

        void update() {
            x += vx;
            y += vy;

            // Bounce off edges
            if (x < -size || x > 800 + size) vx *= -1;
            if (y < -size || y > 600 + size) vy *= -1;

            // Keep in bounds
            x = Math.max(-size, Math.min(800 + size, x));
            y = Math.max(-size, Math.min(600 + size, y));
        }
    }
}
>>>>>>> Stashed changes
