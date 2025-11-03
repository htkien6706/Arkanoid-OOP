import java.awt.*;
import java.util.ArrayList;

public class AnimationUtil {
    
    public static class ScreenTransition {
        private float alpha;
        private boolean fadingIn;
        private boolean fadingOut;
        private float fadeSpeed;
        private Runnable onComplete;
        
        public ScreenTransition() {
            this.alpha = 1.0f;
            this.fadingIn = false;
            this.fadingOut = false;
            this.fadeSpeed = 0.05f;
        }
        
        public void startFadeIn(Runnable onComplete) {
            this.alpha = 1.0f;
            this.fadingIn = true;
            this.fadingOut = false;
            this.onComplete = onComplete;
        }
        
        public void startFadeOut(Runnable onComplete) {
            this.alpha = 0.0f;
            this.fadingOut = true;
            this.fadingIn = false;
            this.onComplete = onComplete;
        }
        
        public void update() {
            if (fadingIn) {
                alpha -= fadeSpeed;
                if (alpha <= 0) {
                    alpha = 0;
                    fadingIn = false;
                    if (onComplete != null) {
                        onComplete.run();
                        onComplete = null;
                    }
                }
            } else if (fadingOut) {
                alpha += fadeSpeed;
                if (alpha >= 1.0f) {
                    alpha = 1.0f;
                    fadingOut = false;
                    if (onComplete != null) {
                        onComplete.run();
                        onComplete = null;
                    }
                }
            }
        }
        
        public void draw(Graphics2D g2d, int width, int height) {
            if (alpha > 0) {
                Composite old = g2d.getComposite();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, width, height);
                g2d.setComposite(old);
            }
        }
        
        public boolean isAnimating() {
            return fadingIn || fadingOut;
        }
    }
    
    public static class BrickDestroyAnimation {
        private ArrayList<AnimatedBrick> animatingBricks;
        
        public BrickDestroyAnimation() {
            this.animatingBricks = new ArrayList<>();
        }
        
        public void startAnimation(int x, int y, int width, int height, Color color) {
            animatingBricks.add(new AnimatedBrick(x, y, width, height, color));
        }
        
        public void update() {
            for (int i = animatingBricks.size() - 1; i >= 0; i--) {
                AnimatedBrick brick = animatingBricks.get(i);
                brick.update();
                if (brick.isFinished()) {
                    animatingBricks.remove(i);
                }
            }
        }
        
        public void draw(Graphics2D g2d) {
            for (AnimatedBrick brick : animatingBricks) {
                brick.draw(g2d);
            }
        }
        
        public void clear() {
            animatingBricks.clear();
        }
        
        private class AnimatedBrick {
            float x, y, width, height;
            float scale;
            float alpha;
            float rotation;
            Color color;
            float centerX, centerY;
            
            AnimatedBrick(int x, int y, int width, int height, Color color) {
                this.x = x;
                this.y = y;
                this.width = width;
                this.height = height;
                this.color = color;
                this.scale = 1.0f;
                this.alpha = 1.0f;
                this.rotation = 0;
                this.centerX = x + width / 2f;
                this.centerY = y + height / 2f;
            }
            
            void update() {
                scale -= 0.05f;
                alpha -= 0.08f;
                rotation += 0.15f;
                
                if (scale < 0) scale = 0;
                if (alpha < 0) alpha = 0;
            }
            
            void draw(Graphics2D g2d) {
                if (alpha <= 0 || scale <= 0) return;
                
                Composite oldComposite = g2d.getComposite();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                
                java.awt.geom.AffineTransform oldTransform = g2d.getTransform();
                
                g2d.translate(centerX, centerY);
                g2d.rotate(rotation);
                g2d.scale(scale, scale);
                
                int drawX = (int)(-width / 2);
                int drawY = (int)(-height / 2);
                
                g2d.setColor(color);
                g2d.fillRoundRect(drawX, drawY, (int)width, (int)height, 8, 8);
                
                g2d.setColor(color.darker());
                g2d.setStroke(new BasicStroke(2f));
                g2d.drawRoundRect(drawX, drawY, (int)width, (int)height, 8, 8);
                
                g2d.setTransform(oldTransform);
                g2d.setComposite(oldComposite);
            }
            
            boolean isFinished() {
                return alpha <= 0 || scale <= 0;
            }
        }
    }
    
    public static class ScreenShake {
        private int intensity;
        private int duration;
        private int timer;
        private int offsetX;
        private int offsetY;
        private java.util.Random random;
        
        public ScreenShake() {
            this.intensity = 0;
            this.duration = 0;
            this.timer = 0;
            this.offsetX = 0;
            this.offsetY = 0;
            this.random = new java.util.Random();
        }
        
        public void start(int intensity, int duration) {
            this.intensity = intensity;
            this.duration = duration;
            this.timer = 0;
        }
        
        public void update() {
            if (timer < duration) {
                float progress = (float)timer / duration;
                int currentIntensity = (int)(intensity * (1 - progress));
                
                offsetX = random.nextInt(currentIntensity * 2 + 1) - currentIntensity;
                offsetY = random.nextInt(currentIntensity * 2 + 1) - currentIntensity;
                
                timer++;
            } else {
                offsetX = 0;
                offsetY = 0;
            }
        }
        
        public void apply(Graphics2D g2d) {
            if (timer < duration) {
                g2d.translate(offsetX, offsetY);
            }
        }
        
        public void reset(Graphics2D g2d) {
            if (timer < duration) {
                g2d.translate(-offsetX, -offsetY);
            }
        }
        
        public boolean isShaking() {
            return timer < duration;
        }
    }
}