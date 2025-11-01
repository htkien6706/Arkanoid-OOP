import java.awt.*;

public class HUD {
    private int score = 0;
    private int lives = 3;
    private int level = 1;
    private int combo = 1;
    private int coins = 0;

    public void setScore(int s) { score = s; }
    public void setLives(int l) { lives = l; }
    public void setLevel(int lv) { level = lv; }
    public void setCombo(int c) { combo = c; }
    public void setCoins(int c) { coins = c; }

    public void draw(Graphics2D g) {
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.setColor(Color.WHITE);

        g.drawString("Score: " + score, 20, 25);
        g.drawString("Lives: " + lives, 20, 50);
        g.drawString("Level: " + level, 20, 75);
        g.drawString("Coins: " + coins, 20, 100);

        if (combo > 1) {
            g.setColor(Color.YELLOW);
            g.drawString("Combo x" + combo, 20, 125);
        }
    }
}
