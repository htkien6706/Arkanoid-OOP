import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class HUD {
    private int score;
    private int lives;
    private int level;
    private int combo;
    private int coins;
    
    // Power-up active
    private ArrayList<ActivePowerUp> activePowerUps;
    
    // Animation cho combo
    private float comboGlowIntensity = 0;
    private float comboGlowDirection = 1;
    
    // Animation cho điểm số tăng
    private ArrayList<FloatingText> floatingTexts;
    
    public HUD() {
        this.score = 0;
        this.lives = 3;
        this.level = 1;
        this.combo = 0;
        this.coins = 0;
        this.activePowerUps = new ArrayList<>();
        this.floatingTexts = new ArrayList<>();
    }
    
    public void draw(Graphics2D g2d, int panelWidth, int panelHeight) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Vẽ HUD chính ở góc trên trái
        drawMainHUD(g2d);
        
        // Vẽ power-ups active
        drawActivePowerUps(g2d, panelWidth);
        
        // Vẽ floating texts
        drawFloatingTexts(g2d);
    }
    
    private void drawMainHUD(Graphics2D g2d) {
        int x = 20;
        int y = 25;
        int spacing = 50;
        
        // === SCORE (to, nổi bật) ===
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 28));
        
        // Shadow cho score
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.drawString("SCORE", x + 2, y + 2);
        
        // Score text
        g2d.setColor(new Color(255, 215, 0)); // Vàng
        g2d.drawString("SCORE", x, y);
        
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 32));
        g2d.setColor(Color.WHITE);
        g2d.drawString(String.format("%06d", score), x, y + 35);
        
        y += spacing + 30;
        
        // === LIVES (icon trái tim) ===
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 20));
        g2d.setColor(new Color(255, 100, 100));
        g2d.drawString("LIVES", x, y);
        
        // Vẽ các trái tim
        for (int i = 0; i < lives; i++) {
            drawHeart(g2d, x + i * 35, y + 10, 25);
        }
        
        y += spacing;
        
        // === LEVEL ===
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 20));
        g2d.setColor(new Color(100, 200, 255));
        g2d.drawString("LEVEL", x, y);
        
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 28));
        g2d.setColor(Color.WHITE);
        g2d.drawString(String.valueOf(level), x + 80, y);
        
        y += spacing;
        
        // === COMBO (với hiệu ứng glow nếu > 1) ===
        if (combo > 1) {
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 20));
            
            // Hiệu ứng glow
            int glowAlpha = (int)(100 + comboGlowIntensity * 155);
            g2d.setColor(new Color(255, 150, 0, glowAlpha));
            
            for (int i = 3; i > 0; i--) {
                g2d.drawString("COMBO", x - i, y - i);
            }
            
            g2d.setColor(new Color(255, 200, 0));
            g2d.drawString("COMBO", x, y);
            
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 32));
            g2d.setColor(new Color(255, 150, 0));
            g2d.drawString("x" + combo, x + 90, y);
            
            y += spacing;
        }
        
        // === COINS (icon đồng xu) ===
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 20));
        g2d.setColor(new Color(255, 215, 0));
        
        // Vẽ icon coin
        drawCoin(g2d, x, y - 15, 20);
        
        g2d.drawString(String.valueOf(coins), x + 30, y);
    }
    
    private void drawHeart(Graphics2D g2d, int x, int y, int size) {
        // Tạo hình trái tim
        GeneralPath heart = new GeneralPath();
        
        float scale = size / 20f;
        x += size / 2;
        
        heart.moveTo(x, y + 6 * scale);
        
        // Nửa trái
        heart.curveTo(
            x, y + 3 * scale,
            x - 5 * scale, y - 2 * scale,
            x - 10 * scale, y + 3 * scale
        );
        
        heart.curveTo(
            x - 10 * scale, y + 8 * scale,
            x, y + 13 * scale,
            x, y + 18 * scale
        );
        
        // Nửa phải
        heart.curveTo(
            x, y + 13 * scale,
            x + 10 * scale, y + 8 * scale,
            x + 10 * scale, y + 3 * scale
        );
        
        heart.curveTo(
            x + 10 * scale, y - 2 * scale,
            x, y + 3 * scale,
            x, y + 6 * scale
        );
        
        heart.closePath();
        
        // Gradient fill
        GradientPaint gp = new GradientPaint(
            x, y, new Color(255, 50, 50),
            x, y + 18 * scale, new Color(200, 0, 0)
        );
        g2d.setPaint(gp);
        g2d.fill(heart);
        
        // Border
        g2d.setColor(new Color(150, 0, 0));
        g2d.setStroke(new BasicStroke(2f));
        g2d.draw(heart);
    }
    
    private void drawCoin(Graphics2D g2d, int x, int y, int size) {
        // Vẽ đồng xu
        GradientPaint gp = new GradientPaint(
            x, y, new Color(255, 223, 0),
            x, y + size, new Color(255, 180, 0)
        );
        g2d.setPaint(gp);
        g2d.fillOval(x, y, size, size);
        
        // Border
        g2d.setColor(new Color(200, 150, 0));
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawOval(x, y, size, size);
        
        // Biểu tượng C
        g2d.setColor(new Color(220, 180, 0));
        g2d.setFont(new Font("Segoe UI", Font.BOLD, size - 4));
        g2d.drawString("C", x + 5, y + size - 4);
    }
    
    private void drawActivePowerUps(Graphics2D g2d, int panelWidth) {
        if (activePowerUps.isEmpty()) return;
        
        int x = panelWidth - 220;
        int y = 20;
        
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        for (int i = 0; i < activePowerUps.size(); i++) {
            ActivePowerUp powerUp = activePowerUps.get(i);
            
            // Background
            g2d.setColor(new Color(40, 40, 70, 200));
            g2d.fillRoundRect(x, y, 200, 50, 15, 15);
            
            // Border
            g2d.setColor(new Color(100, 150, 255));
            g2d.setStroke(new BasicStroke(2f));
            g2d.drawRoundRect(x, y, 200, 50, 15, 15);
            
            // Icon
            drawPowerUpIcon(g2d, x + 10, y + 10, 30, powerUp.type);
            
            // Tên power-up
            g2d.setColor(Color.WHITE);
            g2d.drawString(powerUp.name, x + 50, y + 20);
            
            // Thanh thời gian
            float timePercent = powerUp.remainingTime / powerUp.totalTime;
            int barWidth = (int)(140 * timePercent);
            
            // Background thanh
            g2d.setColor(new Color(50, 50, 50));
            g2d.fillRoundRect(x + 50, y + 28, 140, 12, 6, 6);
            
            // Thanh thời gian
            Color barColor = timePercent > 0.3f ? new Color(100, 255, 100) : new Color(255, 100, 100);
            g2d.setColor(barColor);
            g2d.fillRoundRect(x + 50, y + 28, barWidth, 12, 6, 6);
            
            y += 60;
        }
    }
    
    private void drawPowerUpIcon(Graphics2D g2d, int x, int y, int size, String type) {
        g2d.setColor(new Color(100, 200, 255));
        g2d.fillRoundRect(x, y, size, size, 8, 8);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        String icon = "?";
        switch(type) {
            case "EXPAND": icon = "↔"; break;
            case "SLOW": icon = "⏱"; break;
            case "MULTI": icon = "●"; break;
            case "FIRE": icon = "🔥"; break;
        }
        
        FontMetrics fm = g2d.getFontMetrics();
        int textX = x + (size - fm.stringWidth(icon)) / 2;
        int textY = y + (size + fm.getAscent()) / 2 - 2;
        g2d.drawString(icon, textX, textY);
    }
    
    private void drawFloatingTexts(Graphics2D g2d) {
        for (int i = floatingTexts.size() - 1; i >= 0; i--) {
            FloatingText text = floatingTexts.get(i);
            text.update();
            
            if (text.isFinished()) {
                floatingTexts.remove(i);
            } else {
                text.draw(g2d);
            }
        }
    }
    
    public void update() {
        // Update combo glow animation
        comboGlowIntensity += comboGlowDirection * 0.05f;
        if (comboGlowIntensity >= 1.0f) {
            comboGlowIntensity = 1.0f;
            comboGlowDirection = -1;
        } else if (comboGlowIntensity <= 0.0f) {
            comboGlowIntensity = 0.0f;
            comboGlowDirection = 1;
        }
        
        // Update active power-ups
        for (int i = activePowerUps.size() - 1; i >= 0; i--) {
            ActivePowerUp powerUp = activePowerUps.get(i);
            powerUp.remainingTime -= 0.016f;
            
            if (powerUp.remainingTime <= 0) {
                activePowerUps.remove(i);
            }
        }
    }
    
    // === GETTERS & SETTERS ===
    public void addScore(int points, int x, int y) {
        score += points;
        floatingTexts.add(new FloatingText("+" + points, x, y, new Color(255, 215, 0)));
    }
    
    public void increaseCombo() {
        combo++;
    }
    
    public void resetCombo() {
        combo = 0;
    }
    
    public void loseLife() {
        lives--;
        resetCombo();
    }
    
    public void addLife() {
        lives++;
    }
    
    public void nextLevel() {
        level++;
        resetCombo();
    }
    
    public void addCoins(int amount) {
        coins += amount;
    }
    
    public void addPowerUp(String type, String name, float duration) {
        activePowerUps.add(new ActivePowerUp(type, name, duration));
    }
    
    public int getScore() { return score; }
    public int getLives() { return lives; }
    public int getLevel() { return level; }
    public int getCombo() { return combo; }
    public int getCoins() { return coins; }
    
    public void setScore(int score) { this.score = score; }
    public void setLives(int lives) { this.lives = lives; }
    public void setLevel(int level) { this.level = level; }
    public void setCoins(int coins) { this.coins = coins; }
    
    // === INNER CLASSES ===
    private class ActivePowerUp {
        String type;
        String name;
        float totalTime;
        float remainingTime;
        
        ActivePowerUp(String type, String name, float duration) {
            this.type = type;
            this.name = name;
            this.totalTime = duration;
            this.remainingTime = duration;
        }
    }
    
    private class FloatingText {
        String text;
        float x, y;
        Color color;
        float alpha;
        float velocity;
        
        FloatingText(String text, int x, int y, Color color) {
            this.text = text;
            this.x = x;
            this.y = y;
            this.color = color;
            this.alpha = 1.0f;
            this.velocity = 2.0f;
        }
        
        void update() {
            y -= velocity;
            alpha -= 0.02f;
            if (alpha < 0) alpha = 0;
        }
        
        void draw(Graphics2D g2d) {
            Composite old = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 24));
            g2d.setColor(color);
            g2d.drawString(text, x, y);
            
            g2d.setComposite(old);
        }
        
        boolean isFinished() {
            return alpha <= 0;
        }
    }
}