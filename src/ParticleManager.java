import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleManager {
    private final List<Particle> particles;
    private final Random random;
    private static final int MAX_PARTICLES = 300; // Giới hạn tổng particle

    public ParticleManager() {
        particles = new ArrayList<>();
        random = new Random();
    }

    public void createBrickParticles(Brick brick) {
        if (particles.size() > MAX_PARTICLES) return; // Ngăn quá tải

        Color baseColor = brick.getBaseColor();
        int numParticles = 8 + random.nextInt(8); // Chỉ 8–15 hạt/gạch → nhẹ hơn

        Rectangle bounds = brick.getBounds();
        for (int i = 0; i < numParticles; i++) {
            if (particles.size() >= MAX_PARTICLES) break;

            double px = bounds.x + random.nextDouble() * bounds.width;
            double py = bounds.y + random.nextDouble() * bounds.height;

            // Vận tốc ngẫu nhiên, hướng ra ngoài
            double angle = random.nextDouble() * Math.PI * 2;
            double speed = 2 + random.nextDouble() * 4;
            double pdx = Math.cos(angle) * speed;
            double pdy = Math.sin(angle) * speed - 2; // Hơi hướng lên

            int psize = 3 + random.nextInt(3); // 3–5px
            int plife = 20 + random.nextInt(15); // 20–35 frame

            particles.add(new Particle(px, py, pdx, pdy, psize, baseColor, plife));
        }
    }

    public void update() {
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            p.update();
            if (p.isDead()) {
                particles.remove(i);
            }
        }
    }

    public void draw(Graphics2D g2d) {
        for (Particle p : particles) {
            p.draw(g2d);
        }
    }

    public void clear() {
        particles.clear();
    }
}