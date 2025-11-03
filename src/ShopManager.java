import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ShopManager {
    private List<Skin> skins;
    private String selectedSkin;
    private long rainbowTime;

    public ShopManager() {
        skins = new ArrayList<>();
        initSkins();
        selectedSkin = "classic";
        rainbowTime = System.currentTimeMillis();
    }

    private void initSkins() {
        skins.add(new Skin("classic", "CLASSIC", new Color(0, 200, 255), new Color(255, 100, 100), 0, true));
        skins.add(new Skin("neon", "NEON", new Color(255, 0, 255), new Color(0, 255, 255), 100, false));
        skins.add(new Skin("gold", "GOLD", new Color(255, 215, 0), new Color(255, 237, 78), 200, false));
        skins.add(new Skin("fire", "FIRE", new Color(255, 69, 0), new Color(255, 99, 71), 150, false));
        skins.add(new Skin("ice", "ICE", new Color(0, 191, 255), new Color(135, 206, 235), 150, false));
        skins.add(new Skin("toxic", "TOXIC", new Color(127, 255, 0), new Color(173, 255, 47), 180, false));
        skins.add(new Skin("rainbow", "RAINBOW", null, null, 300, false));
    }

    public List<Skin> getSkins() {
        return skins;
    }

    public String getSelectedSkin() {
        return selectedSkin;
    }

    private Skin findSkin(String skinId) {
        for (Skin skin : skins) {
            if (skin.getId().equals(skinId)) {
                return skin;
            }
        }
        return null;
    }

    public boolean buySkin(String skinId, CoinManager coinManager) {
        Skin skin = findSkin(skinId);
        if (skin != null && !skin.isUnlocked()) {
            if (coinManager.spendCoins(skin.getPrice())) {
                skin.setUnlocked(true);
                return true;
            }
        }
        return false;
    }

    public void equipSkin(String skinId) {
        Skin skin = findSkin(skinId);
        if (skin != null && skin.isUnlocked()) {
            selectedSkin = skinId;
        }
    }

    public Color getPaddleColor() {
        if (selectedSkin.equals("rainbow")) {
            return getRainbowColor();
        }
        Skin skin = findSkin(selectedSkin);
        return skin != null ? skin.getPaddleColor() : new Color(0, 200, 255);
    }

    public Color getBallColor() {
        if (selectedSkin.equals("rainbow")) {
            return getRainbowColor();
        }
        Skin skin = findSkin(selectedSkin);
        return skin != null ? skin.getBallColor() : new Color(255, 100, 100);
    }

    private Color getRainbowColor() {
        long elapsed = System.currentTimeMillis() - rainbowTime;
        float hue = (elapsed % 3000) / 3000f;
        return Color.getHSBColor(hue, 1.0f, 1.0f);
    }
}
