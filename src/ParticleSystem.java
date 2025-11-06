import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;



public class ParticleSystem {
    private final List<Particle> particles = new ArrayList<>();
    
    public void spawn(float x, float y, Color color, int count) {
        for (int i = 0; i < count; i++) {
            particles.add(new Particle(x, y, color));
        }
    }

    public void update() {
        particles.removeIf(p -> !p.update());
    }

    public void draw(Graphics2D g) {
        for (Particle p : particles) p.draw(g);
    }
}
