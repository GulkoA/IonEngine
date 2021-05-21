package IonEngine;

import java.awt.*;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class IonPixelField extends IonContainer{
    //private int pixelSize = 1;
    private BufferedImage bufferedImage;

    public IonPixelField(int width, int height, int x, int y) {
        super(width, height, x, y);
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }
    public IonPixelField(int width, int height) {
        super(width, height);
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    }
    public IonPixelField() {
        super();
        bufferedImage = new BufferedImage(super.getWidth(), super.getHeight(), BufferedImage.TYPE_INT_RGB);
    }

    public void setPixel(int x, int y, Color color) {
        bufferedImage.setRGB(x, y, color.getRGB());
    }

    public void paint(Graphics g) {
        super.normalizeBorders();
        super.drawFrame(g);
        g.drawImage(bufferedImage, super.getX(), super.getY(), null);
    }
 

}
