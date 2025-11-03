public class CoinManager {
    private int coins;

    public CoinManager() {
        this.coins = 500; // Bắt đầu với 500 coins
    }

    public int getCoins() {
        return coins;
    }

    public void addCoins(int amount) {
        coins += amount;
    }

    public boolean spendCoins(int amount) {
        if (coins >= amount) {
            coins -= amount;
            return true;
        }
        return false;
    }

    public void earnCoinsFromBrick() {
        addCoins(10); // Mỗi gạch bị phá = 10 coins
    }
}
