import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class OptionsPanel extends JPanel {
    private SoundManager soundManager;

    public OptionsPanel() {
        this.soundManager = SoundManager.getInstance();

        setLayout(null);
        setPreferredSize(new Dimension(800, 600));

        initUI();
    }

    private void initUI() {
        // Tiêu đề


        // === PANEL NHẠC NỀN ===
        JPanel musicPanel = createSettingPanel("BACK GROUND MUSIC VOLUME", 150);

        // Slider âm lượng nhạc
        JSlider musicSlider = createCustomSlider();
        musicSlider.setValue((int)(soundManager.getMusicVolume() * 100));
        musicSlider.setBounds(50, 80, 500, 50);

        // Label hiển thị % nhạc
        JLabel musicValueLabel = new JLabel(musicSlider.getValue() + "%");
        musicValueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        musicValueLabel.setForeground(Color.WHITE);
        musicValueLabel.setBounds(570, 80, 80, 50);

        musicSlider.addChangeListener(e -> {
            int value = musicSlider.getValue();
            musicValueLabel.setText(value + "%");
            soundManager.setMusicVolume(value / 100f);
            if (!musicSlider.getValueIsAdjusting()) {
                soundManager.playSound("click");
            }
        });

        // Nút bật/tắt nhạc
        JToggleButton musicToggle = createToggleButton("ON", "OFF", soundManager.isMusicEnabled());
        musicToggle.setBounds(650, 80, 100, 50);
        musicToggle.addActionListener(e -> {
            soundManager.playSound("click");
            soundManager.setMusicEnabled(musicToggle.isSelected());
            musicSlider.setEnabled(musicToggle.isSelected());
            if (musicToggle.isSelected()) {
                soundManager.playBackgroundMusic("menu_music"); // hoặc "game_music" tùy bạn đang ở đâu
            } else {
                soundManager.stopBackgroundMusic();
            }
        });

        musicPanel.add(musicSlider);
        musicPanel.add(musicValueLabel);
        musicPanel.add(musicToggle);
        add(musicPanel);

        // === PANEL HIỆU ỨNG ÂM THANH ===
        JPanel soundPanel = createSettingPanel("SOUND EFFECTS VOLUME", 310);

        // Slider âm lượng sound
        JSlider soundSlider = createCustomSlider();
        soundSlider.setValue((int)(soundManager.getSoundVolume() * 100));
        soundSlider.setBounds(50, 80, 500, 50);

        // Label hiển thị % sound
        JLabel soundValueLabel = new JLabel(soundSlider.getValue() + "%");
        soundValueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        soundValueLabel.setForeground(Color.WHITE);
        soundValueLabel.setBounds(570, 80, 80, 50);

        soundSlider.addChangeListener(e -> {
            int value = soundSlider.getValue();
            soundValueLabel.setText(value + "%");
            soundManager.setSoundVolume(value / 100f);
            if (!soundSlider.getValueIsAdjusting()) {
                soundManager.playSound("click");
            }
        });

        // Nút test sound

        // Nút bật/tắt sound
        JToggleButton soundToggle = createToggleButton("ON", "OFF", soundManager.isSoundEnabled());
        soundToggle.setBounds(650, 80, 100, 50);
        soundToggle.addActionListener(e -> {
            boolean wasEnabled = soundManager.isSoundEnabled();

            if (soundToggle.isSelected()) {
                // Khi bật
                soundManager.setSoundEnabled(true);
                if (wasEnabled) soundManager.playSound("click"); // chỉ phát nếu trước đó bật
            } else {
                // Khi tắt
                if (wasEnabled) soundManager.playSound("click"); // phát trước khi tắt
                soundManager.setSoundEnabled(false);
            }

            soundSlider.setEnabled(soundToggle.isSelected());
        });

        soundPanel.add(soundSlider);
        soundPanel.add(soundValueLabel);

        soundPanel.add(soundToggle);
        add(soundPanel);

        // Nút QUAY LẠI
        JButton backButton = createStyledButton("BACK TO MENU");
        backButton.setBounds(300, 500, 200, 50);
        backButton.addActionListener(e -> {
            soundManager.playSound("click");
            Main.showMenu();
        });
        add(backButton);
    }

    private JPanel createSettingPanel(String title, int y) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(40, 40, 80, 200),
                        0, getHeight(), new Color(20, 20, 50, 200)
                );
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Border
                g2d.setColor(new Color(100, 150, 255));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 20, 20);
            }
        };
        panel.setLayout(null);
        panel.setBounds(25, y, 750, 140);
        panel.setOpaque(false);

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(20, 10, 400, 30);
        panel.add(titleLabel);

        return panel;
    }

    private JSlider createCustomSlider() {
        JSlider slider = new JSlider(0, 100, 50);
        slider.setOpaque(false);
        slider.setForeground(new Color(0, 200, 255));
        slider.setMajorTickSpacing(25);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setFont(new Font("Arial", Font.PLAIN, 10));

        // Custom UI
        slider.setUI(new javax.swing.plaf.basic.BasicSliderUI(slider) {
            @Override
            public void paintTrack(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Track background
                g2d.setColor(new Color(50, 50, 80));
                g2d.fillRoundRect(trackRect.x, trackRect.y + 5, trackRect.width, 10, 10, 10);

                // Filled track
                int filledWidth = (int)((slider.getValue() / 100.0) * trackRect.width);
                g2d.setColor(new Color(0, 180, 255));
                g2d.fillRoundRect(trackRect.x, trackRect.y + 5, filledWidth, 10, 10, 10);
            }

            @Override
            public void paintThumb(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Thumb shadow
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.fillOval(thumbRect.x + 2, thumbRect.y + 2, thumbRect.width, thumbRect.height);

                // Thumb
                GradientPaint gp = new GradientPaint(
                        thumbRect.x, thumbRect.y, new Color(255, 255, 255),
                        thumbRect.x, thumbRect.y + thumbRect.height, new Color(200, 200, 200)
                );
                g2d.setPaint(gp);
                g2d.fillOval(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);

                // Thumb border
                g2d.setColor(new Color(0, 150, 255));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawOval(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);
            }
        });
        slider.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int value = (int) ((e.getX() / (double) slider.getWidth()) *
                        (slider.getMaximum() - slider.getMinimum()));
                slider.setValue(value);
            }
        });
        return slider;
    }

    private JToggleButton createToggleButton(String onText, String offText, boolean initialState) {
        JToggleButton button = new JToggleButton(initialState ? onText : offText, initialState) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bgColor = isSelected() ? new Color(0, 200, 80) : new Color(200, 50, 50);
                GradientPaint gp = new GradientPaint(
                        0, 0, bgColor,
                        0, getHeight(), bgColor.darker()
                );
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 15, 15);

                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                String text = isSelected() ? onText : offText;
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(text, x, y);
            }
        };

        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addItemListener(e -> {
            button.setText(button.isSelected() ? onText : offText);
            button.repaint();
        });

        return button;
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Gradient background
        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(20, 20, 40),
                0, getHeight(), new Color(10, 25, 50)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Vẽ stars
        g2d.setColor(new Color(255, 255, 255, 80));
        for (int i = 0; i < 100; i++) {
            int x = (i * 137) % getWidth();
            int y = (i * 241) % getHeight();
            g2d.fillOval(x, y, 2, 2);
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
}
