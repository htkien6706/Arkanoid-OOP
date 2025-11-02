import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HighScorePanel extends JPanel {
    private ScoreManager scoreManager;

    public HighScorePanel(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));
        initUI();
    }

    private void initUI() {
        // Panel chứa nội dung chính
        JPanel contentPanel = createContentPanel();

        // Thêm scroll
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Vẽ background gradient
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(20, 20, 40),
                        0, getHeight(), new Color(10, 25, 50)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Vẽ các ngôi sao
                g2d.setColor(new Color(255, 255, 255, 80));
                for (int i = 0; i < 100; i++) {
                    int x = (i * 137) % getWidth();
                    int y = (i * 241) % getHeight();
                    g2d.fillOval(x, y, 2, 2);
                }
            }
        };

        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(780, 700));

        addTitle(panel);
        addScoreEntries(panel);

        return panel;
    }

    private void addTitle(JPanel panel) {
        // Title với hiệu ứng glow
        JLabel titleLabel = new JLabel("HIGH SCORES") {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Hiệu ứng glow
                g2d.setColor(new Color(255, 215, 0, 100));
                g2d.setFont(new Font("Arial Black", Font.BOLD, 42));
                for (int i = 0; i < 8; i++) {
                    int offsetX = (int)(Math.random() * 3);
                    int offsetY = (int)(Math.random() * 3);
                    int textWidth = g2d.getFontMetrics().stringWidth("HIGH SCORES");
                    g2d.drawString("HIGH SCORES", (getWidth() - textWidth) / 2 + offsetX, 45 + offsetY);
                }

                // Text chính
                g2d.setColor(new Color(255, 215, 0));
                int textWidth = g2d.getFontMetrics().stringWidth("HIGH SCORES");
                g2d.drawString("HIGH SCORES", (getWidth() - textWidth) / 2, 45);
            }
        };
        titleLabel.setBounds(0, 20, 780, 60);
        panel.add(titleLabel);

        // Subtitle
        JLabel subtitleLabel = new JLabel("TOP 10 PLAYERS");
        subtitleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        subtitleLabel.setForeground(new Color(100, 200, 255));
        subtitleLabel.setBounds(0, 80, 780, 25);
        subtitleLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(subtitleLabel);
    }

    private void addScoreEntries(JPanel panel) {
        List<ScoreEntry> scores = scoreManager.getHighScores();

        int startY = 130;
        int entryHeight = 55;
        int gap = 5;

        // Hiển thị các điểm có
        for (int i = 0; i < scores.size(); i++) {
            ScoreEntry entry = scores.get(i);
            int y = startY + i * (entryHeight + gap);

            ScoreCard card = new ScoreCard(i + 1, entry);
            card.setBounds(50, y, 680, entryHeight);
            panel.add(card);
        }

        // Hiển thị các slot trống
        for (int i = scores.size(); i < 10; i++) {
            int y = startY + i * (entryHeight + gap);
            EmptyScoreCard card = new EmptyScoreCard(i + 1);
            card.setBounds(50, y, 680, entryHeight);
            panel.add(card);
        }
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(15, 15, 30),
                        0, getHeight(), new Color(10, 10, 25)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setPreferredSize(new Dimension(800, 70));

        // Nút Back
        JButton backButton = createStyledButton("BACK TO MENU");
        backButton.setPreferredSize(new Dimension(180, 40));
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SoundManager.getInstance().playSound("click");
                Main.showMenu();
            }
        });

        // Nút Clear
        JButton clearButton = createStyledButton("CLEAR SCORES");
        clearButton.setPreferredSize(new Dimension(180, 40));
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SoundManager.getInstance().playSound("click");
                int confirm = JOptionPane.showConfirmDialog(
                        HighScorePanel.this,
                        "Bạn có chắc muốn xóa tất cả điểm số?",
                        "Xác nhận",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    scoreManager.clearScores();
                    refreshPanel();
                }
            }
        });

        bottomPanel.add(backButton);
        bottomPanel.add(clearButton);

        return bottomPanel;
    }

    private void refreshPanel() {
        removeAll();
        initUI();
        revalidate();
        repaint();
    }

    // Component hiển thị 1 entry điểm
    class ScoreCard extends JPanel {
        private int rank;
        private ScoreEntry entry;
        private boolean hover = false;

        public ScoreCard(int rank, ScoreEntry entry) {
            this.rank = rank;
            this.entry = entry;
            setLayout(null);
            setOpaque(false);

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }

                public void mouseExited(MouseEvent e) {
                    hover = false;
                    repaint();
                }
            });
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Background
            Color bgColor = hover ? new Color(50, 50, 90, 200) : new Color(35, 35, 65, 180);
            g2d.setColor(bgColor);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

            // Viền theo hạng
            Color borderColor;
            if (rank == 1) borderColor = new Color(255, 215, 0); // Vàng
            else if (rank == 2) borderColor = new Color(192, 192, 192); // Bạc
            else if (rank == 3) borderColor = new Color(205, 127, 50); // Đồng
            else borderColor = new Color(100, 150, 255);

            g2d.setColor(borderColor);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 15, 15);

            // Vẽ badge hạng
            drawRankBadge(g2d, borderColor);

            // Tên người chơi
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.drawString(entry.getPlayerName(), 100, 33);

            // Điểm số
            g2d.setColor(new Color(255, 215, 0));
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            String scoreText = String.format("%,d", entry.getScore());
            g2d.drawString(scoreText, getWidth() - 150, 33);

            // Ngày tháng
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dateText = sdf.format(new Date(entry.getTimestamp()));
            g2d.setColor(new Color(150, 150, 150));
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.drawString(dateText, getWidth() - 150, 50);
        }

        private void drawRankBadge(Graphics2D g2d, Color color) {
            // Background badge
            g2d.setColor(color);
            g2d.fillOval(15, 10, 60, 35);

            // Số thứ hạng
            g2d.setColor(rank <= 3 ? Color.BLACK : Color.WHITE);
            g2d.setFont(new Font("Arial Black", Font.BOLD, 22));
            FontMetrics fm = g2d.getFontMetrics();
            String rankText = "#" + rank;
            int x = 45 - fm.stringWidth(rankText) / 2;
            g2d.drawString(rankText, x, 33);
        }
    }

    // Component slot trống
    class EmptyScoreCard extends JPanel {
        private int rank;

        public EmptyScoreCard(int rank) {
            this.rank = rank;
            setLayout(null);
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Background mờ
            g2d.setColor(new Color(25, 25, 45, 100));
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

            // Viền nét đứt
            g2d.setColor(new Color(70, 70, 100));
            float[] dash = {9};
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, dash, 0));
            g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 15, 15);

            // Số hạng
            g2d.setColor(new Color(100, 100, 120));
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.drawString("#" + rank, 30, 33);

            // Text trống
            g2d.setColor(new Color(80, 80, 100));
            g2d.setFont(new Font("Arial", Font.ITALIC, 16));
            g2d.drawString("--- EMPTY SLOT ---", 100, 33);
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
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

        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.putClientProperty("hover", true);
                button.repaint();
            }

            public void mouseExited(MouseEvent e) {
                button.putClientProperty("hover", false);
                button.repaint();
            }
        });

        return button;
    }
}
