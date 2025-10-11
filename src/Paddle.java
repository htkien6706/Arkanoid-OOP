import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

class Paddle {
    int x, y; // toa do x, y cua paddle de sau minh co the draw duoc
    int width, height;
    int dx = 20; // modify later
    BufferedImage image;

    public Paddle() {
        int THRESHOLD = 35;
        double scaleFactor = 0.1;
        String imagePath = "resources/Panel.jpg";
        displayScaledImage(imagePath, scaleFactor);
        image = cropImage(image, THRESHOLD);

        width = 100;
        height = 10;
    }



    //function to resize and refresh the image's quiality
    public void displayScaledImage(String imagePath, double scaleFactor) {
        try{
            // nos khác với cả ImageIcon ở chỗ đây nó đọc raw file, và cách đọc là ImageIo.read(FILE = new File(imagePath))
            BufferedImage original = ImageIO.read(new File(imagePath));

            double old_width = original.getWidth();
            double old_height = original.getHeight();

            int new_width = (int) (original.getWidth() * scaleFactor);
            int new_height = (int) (original.getHeight() * scaleFactor);

            image = new BufferedImage(new_width, new_height, BufferedImage.TYPE_INT_ARGB); // image là khung rỗng với kích thước mới
            Graphics2D g2d = image.createGraphics(); // cho phep ve len anh
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            g2d.drawImage(original, 0, 0, new_width, new_height, null); // g2d là cái graphic của image rồi, và vẽ nguyên cái original vào thôi
            g2d.dispose(); // gioongs delete, giải phóng tài nguyên tranhs memory leak
        } catch(Exception e){
            e.printStackTrace(); // research stack overflow, print a sequence of error
        }
    }

    public BufferedImage cropImage(BufferedImage image, int THRESHOLD) {
        int width = image.getWidth();
        int height = image.getHeight();

        int minX = width, maxX = 0;
        int minY = height, maxY = 0;

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                Color c = new Color(image.getRGB(x, y));
                int dr = c.getRed();
                int dg = c.getGreen();
                int db = c.getBlue();

                // check có màu, vì nhiệm vụ là d dang tìm  phần có màu tí còn crop
                if(dr > THRESHOLD || dg > THRESHOLD || db > THRESHOLD) {
                    minX = Math.min(minX, x);
                    maxX = Math.max(maxX, x);
                    minY = Math.min(minY, y);
                    maxY = Math.max(maxY, y);
                }
            }
        }

        int new_width = maxX - minX + 1;
        int new_height = maxY - minY + 1;

        // kết thúc phần crop
        BufferedImage new_image = image.getSubimage(minX, minY, new_width, new_height);

        return new_image;
    }


    // dunfg tis conf gọi ở trong gamepanel, vì gamepanel extends jpanel, nên mới có thể vẽ được vì jpanel mới có hàm paintComponent ở swing và paint ở awt
    public void draw(Graphics2D g2d) {
        for (int i = 8; i > 0; i--) {
            float alpha = (float) i / 16;
            g2d.setColor(new Color(0, 255, 255, (int) (alpha * 255)));
            g2d.fillRoundRect(x - i, y - i, width + i * 2, height + i * 2, 20, 20);
        }

        g2d.setColor(new Color(0, 200, 255));
        g2d.fillRoundRect(x, y, width, height, 15, 15);

        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y, width, height, 15, 15);
    }

    void moveLeft() {
        x = x - dx;
        if(x < 0) {
            x = 0;
        }
    }

    void moveRight(int panelWidth) {
        int paddle_width = image.getWidth();
        x = x + dx;

        if(x > panelWidth - paddle_width) {
            x = panelWidth - paddle_width;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}