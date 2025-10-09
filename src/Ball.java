import java.awt.*;
import java.awt.image.BufferedImage;

public class Ball {
    int x,y;
    double dX, dY;
    final double SPEED = 6.0;
    int diameter;

    Ball(int x, int y) {
        this.x = x;
        this.y = y;
        diameter = 16;
        dX = SPEED * 0.6; // just initial speed of the ball, change later
        dY = SPEED * 0.6; // also like dX
    }

    //

    public boolean isFallingToGround(double PANEL_HEIGHT) {
        if(y >= PANEL_HEIGHT) {
            return true;
        }

        return false;
    }

    public void move() {
        x = (int) (x + dX);
        y = (int) (y + dY);
    }

    public void checkWallColiision(int PANEL_HEIGHT, int PANEL_WIDTH) {
        if(x < 0 || x + diameter > PANEL_WIDTH) {
            dX = -dX; // đổi hướng chuyện động vì góc toi bang goc phan xa
        }

        else if(y < 0) {
            dY = -dY; // đổi hướng chuyển động
        }
    }

    public void resetBall(int x, int y) {
        this.x = x;
        this.y = y;
        dX = SPEED * 0.6;
        dY = SPEED * 0.6;
    }


    //vẽ quả bóng // túi nữa gọi trong paintComponent
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Bật anti-aliasing cho mướt
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // giống box shadow html css
        g2d.setColor(new Color(0, 0, 0, 50)); // Đen trong suốt
        g2d.fillOval(x + 2, y + 2, diameter, diameter);

        // Vẽ body chính - gradient từ sáng đến tối
        GradientPaint bodyGradient = new GradientPaint(
                x, y, new Color(255, 100, 100),              // Đỏ sáng
                x + diameter, y + diameter, new Color(200, 0, 0)  // Đỏ đậm
        );
        g2d.setPaint(bodyGradient);
        g2d.fillOval(x, y, diameter, diameter);

        RadialGradientPaint highlight = new RadialGradientPaint(
                x + diameter * 0.3f, y + diameter * 0.3f,
                diameter * 0.4f,
                new float[]{0.0f, 1.0f},
                new Color[]{
                        new Color(255, 255, 255, 180),
                        new Color(255, 255, 255, 0)
                }
        );
        g2d.setPaint(highlight);
        g2d.fillOval(x, y, diameter, diameter);

        g2d.setColor(new Color(100, 0, 0));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawOval(x, y, diameter, diameter);

        g2d.setColor(new Color(255, 150, 150, 100));
        g2d.setStroke(new BasicStroke(1f));
        g2d.drawOval(x + 1, y + 1, diameter - 2, diameter - 2);
    }

}
