import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

public class ParticleSystem {
    private ArrayList<Particle> particles;
    private ArrayList<Trail> trails;
    private ArrayList<Firework> fireworks;
    private Random random;
    
    public ParticleSystem() {
        particles = new ArrayList<>();
        trails = new ArrayList<>();
        fireworks = new ArrayList<>();
        random = new Random();
    }
    
    public void draw(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        for (Trail trail : trails) {
            trail.draw(g2d);
        }
        
        for (Particle particle : particles) {
            particle.draw(g2d);
        }
        
        for (Firework firework : fireworks) {
            firework.draw(g2d);
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
        
        for (int i = trails.size() - 1; i >= 0; i--) {
            Trail t = trails.get(i);
            t.update();
            if (t.isDead()) {
                trails.remove(i);
            }
        }
        
        for (int i = fireworks.size() - 1; i >= 0; i--) {
            Firework f = fireworks.get(i);
            f.update();
            if (f.isDead()) {
                fireworks.remove(i);
            }
        }
    }
    
    public void createBrickExplosion(int x, int y, Color brickColor) {
        int particleCount = 15 + random.nextInt(10);
        
        for (int i = 0; i < particleCount; i++) {
            float angle = (float)(Math.random() * Math.PI * 2);
            float speed = 2 + (float)(Math.random() * 4);
            float vx = (float)(Math.cos(angle) * speed);
            float vy = (float)(Math.sin(angle) * speed);
            
            int size = 3 + random.nextInt(5);
            float life = 0.5f + (float)(Math.random() * 0.5f);
            
            Color color = new Color(
                Math.min(255, brickColor.getRed() + random.nextInt(50) - 25),
                Math.min(255, brickColor.getGreen() + random.nextInt(50) - 25),
                Math.min(255, brickColor.getBlue() + random.nextInt(50) - 25)
            );
            
            particles.add(new Particle(x, y, vx, vy, size, color, life, ParticleType.EXPLOSION));
        }
    }
    
    public void addBallTrail(float x, float y, float diameter, Color ballColor) {
        trails.add(new Trail(x + diameter/2, y + diameter/2, diameter/2, ballColor));
    }
    
    public void createPowerUpSparkle(int x, int y) {
        int particleCount = 20;
        
        for (int i = 0; i < particleCount; i++) {
            float angle = (float)(Math.random() * Math.PI * 2);
            float speed = 1 + (float)(Math.random() * 3);
            float vx = (float)(Math.cos(angle) * speed);
            float vy = (float)(Math.sin(angle) * speed);
            
            int size = 2 + random.nextInt(4);
            float life = 0.8f + (float)(Math.random() * 0.4f);
            
            Color color = random.nextBoolean() ? 
                new Color(255, 255, 100) : 
                new Color(255, 255, 255);
            
            particles.add(new Particle(x, y, vx, vy, size, color, life, ParticleType.SPARKLE));
        }
    }
    
    public void createLevelCompleteFireworks(int panelWidth, int panelHeight) {
        for (int i = 0; i < 5; i++) {
            int x = 100 + random.nextInt(panelWidth - 200);
            int y = 100 + random.nextInt(panelHeight / 2);
            int delay = i * 15;
            
            fireworks.add(new Firework(x, y, delay));
        }
    }
    
    public void createPaddleHitEffect(int x, int y) {
        for (int i = 0; i < 8; i++) {
            float angle = (float)(Math.PI / 4 + (Math.random() * Math.PI / 2));
            float speed = 1 + (float)(Math.random() * 2);
            float vx = (float)(Math.cos(angle) * speed);
            float vy = (float)(-Math.sin(angle) * speed);
            
            Color color = new Color(100, 200, 255);
            particles.add(new Particle(x, y, vx, vy, 3, color, 0.3f, ParticleType.IMPACT));
        }
    }
    
    public void clear() {
        particles.clear();
        trails.clear();
        fireworks.clear();
    }
    
    enum ParticleType {
        EXPLOSION, SPARKLE, IMPACT
    }
    
    private class Particle {
        float x, y;
        float vx, vy;
        int size;
        Color color;
        float life;
        float maxLife;
        float gravity;
        ParticleType type;
        
        Particle(float x, float y, float vx, float vy, int size, Color color, float life, ParticleType type) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.size = size;
            this.color = color;
            this.life = life;
            this.maxLife = life;
            this.type = type;
            this.gravity = type == ParticleType.EXPLOSION ? 0.15f : 0.05f;
        }
        
        void update() {
            x += vx;
            y += vy;
            vy += gravity;
            life -= 0.016f;
            
            vx *= 0.98f;
            vy *= 0.98f;
        }
        
        void draw(Graphics2D g2d) {
            float alpha = life / maxLife;
            if (alpha < 0) alpha = 0;
            if (alpha > 1) alpha = 1;
            
            Composite old = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            
            if (type == ParticleType.SPARKLE) {
                drawStar(g2d, (int)x, (int)y, size);
            } else {
                g2d.setColor(color);
                g2d.fillOval((int)(x - size/2), (int)(y - size/2), size, size);
                
                if (type == ParticleType.IMPACT) {
                    g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(alpha * 50)));
                    g2d.fillOval((int)(x - size), (int)(y - size), size * 2, size * 2);
                }
            }
            
            g2d.setComposite(old);
        }
        
        void drawStar(Graphics2D g2d, int x, int y, int size) {
            int[] xPoints = new int[8];
            int[] yPoints = new int[8];
            
            for (int i = 0; i < 8; i++) {
                double angle = Math.PI / 4 * i;
                int radius = (i % 2 == 0) ? size : size / 2;
                xPoints[i] = x + (int)(Math.cos(angle) * radius);
                yPoints[i] = y + (int)(Math.sin(angle) * radius);
            }
            
            g2d.setColor(color);
            g2d.fillPolygon(xPoints, yPoints, 8);
        }
        
        boolean isDead() {
            return life <= 0;
        }
    }
    
    private class Trail {
        float x, y;
        float size;
        Color color;
        float alpha;
        
        Trail(float x, float y, float size, Color color) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.color = color;
            this.alpha = 0.6f;
        }
        
        void update() {
            alpha -= 0.05f;
            if (alpha < 0) alpha = 0;
            size *= 0.95f;
        }
        
        void draw(Graphics2D g2d) {
            if (alpha <= 0) return;
            
            Composite old = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            
            RadialGradientPaint gradient = new RadialGradientPaint(
                new Point2D.Float(x, y),
                size,
                new float[]{0f, 0.5f, 1f},
                new Color[]{
                    new Color(color.getRed(), color.getGreen(), color.getBlue(), 200),
                    new Color(color.getRed(), color.getGreen(), color.getBlue(), 100),
                    new Color(color.getRed(), color.getGreen(), color.getBlue(), 0)
                }
            );
            
            g2d.setPaint(gradient);
            g2d.fillOval((int)(x - size), (int)(y - size), (int)(size * 2), (int)(size * 2));
            
            g2d.setComposite(old);
        }
        
        boolean isDead() {
            return alpha <= 0;
        }
    }
    
    private class Firework {
        int x, y;
        int delay;
        int timer;
        ArrayList<FireworkParticle> particles;
        boolean exploded;
        Color color;
        
        Firework(int x, int y, int delay) {
            this.x = x;
            this.y = y;
            this.delay = delay;
            this.timer = 0;
            this.particles = new ArrayList<>();
            this.exploded = false;
            
            int colorChoice = random.nextInt(5);
            switch(colorChoice) {
                case 0: color = new Color(255, 50, 50); break;
                case 1: color = new Color(50, 255, 50); break;
                case 2: color = new Color(50, 150, 255); break;
                case 3: color = new Color(255, 255, 50); break;
                case 4: color = new Color(255, 100, 255); break;
            }
        }
        
        void update() {
            timer++;
            
            if (timer >= delay && !exploded) {
                explode();
                exploded = true;
            }
            
            for (int i = particles.size() - 1; i >= 0; i--) {
                FireworkParticle p = particles.get(i);
                p.update();
                if (p.isDead()) {
                    particles.remove(i);
                }
            }
        }
        
        void explode() {
            int particleCount = 40 + random.nextInt(20);
            
            for (int i = 0; i < particleCount; i++) {
                float angle = (float)(Math.random() * Math.PI * 2);
                float speed = 2 + (float)(Math.random() * 4);
                
                particles.add(new FireworkParticle(x, y, angle, speed, color));
            }
        }
        
        void draw(Graphics2D g2d) {
            for (FireworkParticle p : particles) {
                p.draw(g2d);
            }
        }
        
        boolean isDead() {
            return exploded && particles.isEmpty();
        }
    }
    
    private class FireworkParticle {
        float x, y;
        float vx, vy;
        float life;
        Color color;
        
        FireworkParticle(int x, int y, float angle, float speed, Color color) {
            this.x = x;
            this.y = y;
            this.vx = (float)(Math.cos(angle) * speed);
            this.vy = (float)(Math.sin(angle) * speed);
            this.life = 1.0f;
            this.color = color;
        }
        
        void update() {
            x += vx;
            y += vy;
            vy += 0.1f;
            vx *= 0.98f;
            vy *= 0.98f;
            life -= 0.015f;
        }
        
        void draw(Graphics2D g2d) {
            if (life <= 0) return;
            
            Composite old = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, life));
            
            int size = 4;
            
            g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(life * 50)));
            g2d.fillOval((int)(x - size * 2), (int)(y - size * 2), size * 4, size * 4);
            
            g2d.setColor(color);
            g2d.fillOval((int)(x - size/2), (int)(y - size/2), size, size);
            
            g2d.setComposite(old);
        }
        
        boolean isDead() {
            return life <= 0;
        }
    }
}