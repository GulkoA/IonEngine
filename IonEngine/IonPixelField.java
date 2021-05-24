package IonEngine;

import java.awt.*;
import java.awt.Graphics;
import java.awt.image.Raster;
import java.awt.image.BufferedImage;

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

    public boolean setPixel(int x, int y, Color color) {
        if (x >= 0 && y >= 0 && x < bufferedImage.getWidth() && y < bufferedImage.getHeight())
        {
            bufferedImage.setRGB(x, y, color.getRGB());
            return true;
        }
        return false;
    }

    public Color getPixel(int x, int y) {
        if (x >= 0 && y >= 0 && x < bufferedImage.getWidth() && y < bufferedImage.getHeight())
            return new Color(bufferedImage.getRGB(x, y));
        else
            return null;
    }

    public void setWidth(int newWidth) {
        Raster oldData = bufferedImage.getData();
        bufferedImage = new BufferedImage(newWidth, bufferedImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        bufferedImage.setData(oldData);
        super.setWidth(newWidth);
    }

    public void setHeight(int newHeight) {
        Raster oldData = bufferedImage.getData();
        bufferedImage = new BufferedImage(bufferedImage.getWidth(), newHeight, BufferedImage.TYPE_INT_ARGB);
        bufferedImage.setData(oldData);
        super.setHeight(height);
    }

    public void resize(int newWidth, int newHeight) {
        Raster oldData = bufferedImage.getData();
        bufferedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        bufferedImage.setData(oldData);
    }

    public void fillWithNoise(int xFirst, int yFirst, int xLast, int yLast) {
        for (int w = xFirst; w < xLast; w++)
            for (int h = yFirst; h < yLast; h++)
                setPixel(w, h, IonFeatures.randomColor());
        super.repaint();
    }
    public void fillWithNoise(int xFirst, int xLast) {
        fillWithNoise(xFirst, xLast, getWidth(), getHeight());
    }
    public void fillWithNoise() {
        fillWithNoise(0, 0, getWidth(), getHeight());
    }

    public void draw(Graphics g) {
        super.normalizeBorders();
        super.drawFrame(g);
        if (bufferedImage.getWidth() != super.getWidth() || bufferedImage.getHeight() != super.getHeight())
            resize(super.getWidth(), super.getHeight());
        g.drawImage(bufferedImage, super.getX(), super.getY(), null);
    }

    public BufferedImage getBufferedImage() {
        return this.bufferedImage;
    }

    
    
    public void offsetImageBy(int moveX, int moveY) {
        Raster oldData = bufferedImage.getData();
        for (int x = 0; x < oldData.getWidth(); x++)
            for (int y = 0; y < oldData.getHeight(); y++)
            {
                setPixel(x + moveX, y + moveY, IonFeatures.getRasterPixel(oldData, x, y));
            }

    }
}
