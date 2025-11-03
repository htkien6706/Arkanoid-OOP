import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ShopPanel extends JPanel {
    private ShopManager shopManager;
    private CoinManager coinManager;
    private JLabel coinsLabel;
    private JScrollPane scrollPane;

    public ShopPanel(ShopManager shopManager, CoinManager coinManager) {
        this.shopManager = shopManager;
        this.coinManager = coinManager;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));
        initUI();
    }

    private void initUI() {
        JPanel contentPanel = createContentPanel();
        addTitle(contentPanel);
        coinsLabel = addCoinsLabel(contentPanel);
        addSkinCards(contentPanel);

        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(20, 20, 40), 0, getHeight(), new Color(10, 25, 50));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.setColor(new Color(255, 255, 255, 80));
                for (int i = 0; i < 100; i++) {
                    int x = (i * 137) % getWidth();
                    int y = (i * 241) % getHeight();
                    g2d.fillOval(x, y, 2, 2);
                }
            }
        };
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(780, 1050));
        return panel;
    }

    private void addTitle(JPanel panel) {
        JLabel titleLabel = new JLabel("SKIN SHOP");
        titleLabel.setFont(new Font("Arial Black", Font.BOLD, 36));
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setBounds(0, 20, 780, 50);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(titleLabel);
    }

    private JLabel addCoinsLabel(JPanel panel) {
        JLabel label = new JLabel("Coins: " + coinManager.getCoins());
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(Color.YELLOW);
        label.setBounds(0, 70, 780, 30);
        label.setHorizontalAlignment(JLabel.CENTER);
        panel.add(label);
        return label;
    }

    private void addSkinCards(JPanel panel) {
        int startX = 50, startY = 130, cardWidth = 220, cardHeight = 280, gap = 20;
        java.util.List<Skin> skins = shopManager.getSkins();
        for (int i = 0; i < skins.size(); i++) {
            int x = startX + (i % 3) * (cardWidth + gap);
            int y = startY + (i / 3) * (cardHeight + gap);
            SkinCard card = new SkinCard(skins.get(i));
            card.setBounds(x, y, cardWidth, cardHeight);
            panel.add(card);
        }
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(15, 15, 30), 0, getHeight(), new Color(10, 10, 25));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        bottomPanel.setPreferredSize(new Dimension(800, 60));

        JButton backButton = createStyledButton("BACK TO MENU");
        backButton.setPreferredSize(new Dimension(200, 40));
        backButton.addActionListener(e -> {
            SoundManager.getInstance().playSound("click");
            Main.showMenu();
        });
        bottomPanel.add(backButton);
        return bottomPanel;
    }

    private void showCustomDialog(String message, String title, boolean isSuccess) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setUndecorated(true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(30, 30, 60), 0, getHeight(), new Color(15, 15, 40));
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                Color borderColor = isSuccess ? new Color(0, 255, 100) : new Color(255, 50, 50);
                g2d.setColor(borderColor);
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 20, 20);
            }
        };
        panel.setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

        JLabel iconLabel = new JLabel(isSuccess ? "✓" : "✗");
        iconLabel.setFont(new Font("Arial", Font.BOLD, 48));
        iconLabel.setForeground(isSuccess ? new Color(0, 255, 100) : new Color(255, 50, 50));
        topPanel.add(iconLabel);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial Black", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel);

        JLabel messageLabel = new JLabel("<html><center>" + message + "</center></html>");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);

        JButton okButton = createDialogButton("OK", isSuccess);
        okButton.addActionListener(e -> {
            SoundManager.getInstance().playSound("click");
            dialog.dispose();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(okButton);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(messageLabel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private JButton createDialogButton(String text, boolean isSuccess) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color buttonColor = isSuccess ? new Color(0, 200, 80) : new Color(200, 50, 50);
                boolean hover = Boolean.TRUE.equals(getClientProperty("hover"));
                if (hover) buttonColor = buttonColor.brighter();

                GradientPaint gp = new GradientPaint(0, 0, buttonColor, 0, getHeight(), buttonColor.darker());
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 15, 15);

                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(getText(), x, y);
            }
        };

        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setPreferredSize(new Dimension(120, 40));
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

    class SkinCard extends JPanel {
        private Skin skin;
        private boolean hover = false;

        public SkinCard(Skin skin) {
            this.skin = skin;
            setLayout(null);
            setOpaque(false);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                @Override
                public void mouseExited(MouseEvent e) { hover = false; repaint(); }
            });
            initComponents();
        }

        private void initComponents() {
            JLabel nameLabel = new JLabel(skin.getName());
            nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setBounds(0, 160, 220, 25);
            nameLabel.setHorizontalAlignment(JLabel.CENTER);
            add(nameLabel);

            JLabel priceLabel;
            if (skin.isUnlocked()) {
                if (shopManager.getSelectedSkin().equals(skin.getId())) {
                    priceLabel = new JLabel("EQUIPPED");
                    priceLabel.setForeground(new Color(0, 255, 0));
                } else {
                    priceLabel = new JLabel("UNLOCKED");
                    priceLabel.setForeground(new Color(100, 200, 255));
                }
            } else {
                priceLabel = new JLabel("" + skin.getPrice() + " coins");
                priceLabel.setForeground(Color.YELLOW);
            }
            priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
            priceLabel.setBounds(0, 190, 220, 20);
            priceLabel.setHorizontalAlignment(JLabel.CENTER);
            add(priceLabel);

            JButton actionButton;
            if (!skin.isUnlocked()) {
                actionButton = createCardButton("BUY");
                actionButton.addActionListener(e -> buySkin());
            } else if (!shopManager.getSelectedSkin().equals(skin.getId())) {
                actionButton = createCardButton("EQUIP");
                actionButton.addActionListener(e -> equipSkin());
            } else {
                actionButton = createCardButton("SELECTED");
                actionButton.setEnabled(false);
            }
            actionButton.setBounds(35, 225, 150, 35);
            add(actionButton);
        }

        private void buySkin() {
            SoundManager.getInstance().playSound("click");
            if (shopManager.buySkin(skin.getId(), coinManager)) {
                refreshShopDisplay();
            } else {
                showCustomDialog("No coins, no skin!", "Not enough coins", false);
            }
        }

        private void equipSkin() {
            SoundManager.getInstance().playSound("click");
            shopManager.equipSkin(skin.getId());
            refreshShopDisplay();
        }

        private void refreshShopDisplay() {
            int currentScrollPosition = scrollPane.getVerticalScrollBar().getValue();
            coinsLabel.setText("Coins: " + coinManager.getCoins());
            ShopPanel.this.removeAll();
            initUI();
            ShopPanel.this.revalidate();
            ShopPanel.this.repaint();
            SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(currentScrollPosition));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(hover ? new Color(60, 60, 100, 200) : new Color(40, 40, 70, 180));
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

            g2d.setColor(hover ? new Color(100, 150, 255) : new Color(70, 70, 120));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 20, 20);

            drawPreview(g2d);
        }

        private void drawPreview(Graphics2D g2d) {
            int centerX = getWidth() / 2;
            int paddleY = 100, ballY = 60;

            Color paddleColor = skin.getPaddleColor();
            Color ballColor = skin.getBallColor();

            if (skin.getId().equals("rainbow")) {
                paddleColor = Color.getHSBColor(0.5f, 1.0f, 1.0f);
                ballColor = Color.getHSBColor(0.7f, 1.0f, 1.0f);
            }

            g2d.setColor(paddleColor);
            g2d.fillRoundRect(centerX - 50, paddleY, 100, 15, 10, 10);
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(centerX - 50, paddleY, 100, 15, 10, 10);

            g2d.setColor(ballColor);
            g2d.fillOval(centerX - 12, ballY, 24, 24);
            g2d.setColor(Color.WHITE);
            g2d.drawOval(centerX - 12, ballY, 24, 24);
        }
    }

    private JButton createCardButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (isEnabled()) {
                    GradientPaint gp = new GradientPaint(0, 0, new Color(0, 150, 255), 0, getHeight(), new Color(0, 100, 200));
                    g2d.setPaint(gp);
                } else {
                    g2d.setColor(new Color(80, 80, 80));
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(getText(), x, y);
            }
        };

        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                boolean hover = Boolean.TRUE.equals(getClientProperty("hover"));
                GradientPaint gp = hover ?
                        new GradientPaint(0, 0, new Color(0, 150, 255), 0, getHeight(), new Color(0, 100, 200)) :
                        new GradientPaint(0, 0, new Color(50, 50, 100), 0, getHeight(), new Color(30, 30, 70));
                g2d.setPaint(gp);
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
