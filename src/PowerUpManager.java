import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.Graphics2D;

public class PowerUpManager {
    private List<PowerUp> powerUps;
    private List<Laser> lasers;
    private Random random;
    private double dropChance = 0.3; // 30% cơ hội rơi power-up

    public PowerUpManager() {
        powerUps = new ArrayList<>();
        lasers = new ArrayList<>();
        random = new Random();
    }

    // Tạo power-up ngẫu nhiên khi phá gạch
    public void trySpawnPowerUp(int brickX, int brickY, int brickWidth) {
        if (random.nextDouble() < dropChance) {
            PowerUpType[] types = PowerUpType.values();
            PowerUpType randomType = types[random.nextInt(types.length)];

            int x = brickX + brickWidth / 2 - 20; // Căn giữa gạch
            powerUps.add(new PowerUp(x, brickY, randomType));
        }
    }

    // Cập nhật vị trí tất cả power-ups
    public void update(int panelHeight) {
        // Di chuyển power-ups
        for (int i = powerUps.size() - 1; i >= 0; i--) {
            PowerUp p = powerUps.get(i);
            p.move();

            if (p.isOffScreen(panelHeight) || p.isCollected()) {
                powerUps.remove(i);
            }
        }

        // Di chuyển lasers
        for (int i = lasers.size() - 1; i >= 0; i--) {
            Laser laser = lasers.get(i);
            laser.move();

            if (laser.isOffScreen() || !laser.isActive()) {
                lasers.remove(i);
            }
        }
    }

    // Vẽ tất cả power-ups và lasers
    public void draw(Graphics2D g2d) {
        for (PowerUp p : powerUps) {
            p.draw(g2d);
        }
        for (Laser laser : lasers) {
            laser.draw(g2d);
        }
    }

    // Thêm laser từ paddle
    public void addLaser(int x, int y) {
        lasers.add(new Laser(x, y));
    }

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    public List<Laser> getLasers() {
        return lasers;
    }

    public void clear() {
        powerUps.clear();
        lasers.clear();
    }
}