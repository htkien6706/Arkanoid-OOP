import java.awt.*;

public class Skin {
    private String id;
    private String name;
    private Color paddleColor;
    private Color ballColor;
    private int price;
    private boolean unlocked;

    public Skin(String id, String name, Color paddleColor, Color ballColor, int price, boolean unlocked) {
        this.id = id;
        this.name = name;
        this.paddleColor = paddleColor;
        this.ballColor = ballColor;
        this.price = price;
        this.unlocked = unlocked;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public Color getPaddleColor() { return paddleColor; }
    public Color getBallColor() { return ballColor; }
    public int getPrice() { return price; }
    public boolean isUnlocked() { return unlocked; }

    // Setters
    public void setUnlocked(boolean unlocked) { this.unlocked = unlocked; }
}
