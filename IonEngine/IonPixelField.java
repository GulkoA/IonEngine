package IonEngine;

import java.awt.*;
import java.awt.Graphics;
import java.awt.image.Raster;
import java.awt.image.BufferedImage;

public class IonPixelField extends IonContainer{
    private BufferedImage bufferedImage;
    private BufferedImage hiddenImage; //stores image with size of resolution

    public IonPixelField(int width, int height, int x, int y) {
        super(width, height, x, y);
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        hiddenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        hiddenImage.setData(bufferedImage.getData());
    }
    public IonPixelField(int width, int height) {
        super(width, height);
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        hiddenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        hiddenImage.setData(bufferedImage.getData());
    }
    public IonPixelField() {
        super();
        bufferedImage = new BufferedImage(super.getWidth(), super.getHeight(), BufferedImage.TYPE_INT_RGB);
        hiddenImage = new BufferedImage(super.getWidth(), super.getHeight(), BufferedImage.TYPE_INT_RGB);
        hiddenImage.setData(bufferedImage.getData());
    }

    private boolean rescalingNeeded = false;
    
    private boolean scaleUp(BufferedImage source, BufferedImage target) {
        double factorX = (double)target.getWidth() / source.getHeight();
        double factorY = (double)target.getHeight() / source.getWidth();
        if (factorX < 1 || factorY < 1)
            return false; //wtf bro
        
        for (int x = 0; x < target.getWidth(); x++)
            for (int y = 0; y < target.getHeight(); y++) {
                target.setRGB(x, y, source.getRGB((int)(x / factorX), (int)(y / factorY)));
            }
        return true;
    }

    public int getImageWidth() { return hiddenImage.getWidth(); }
    public int getImageHeight() { return hiddenImage.getHeight(); }
    // public int getSizeX() { return getWidth() * resolutionX; }
    // public int getSizeY() { return getHeight() * resolutionY; }

    
    public boolean setRealPixel(int x, int y, Color color) {
        if (x >= 0 && y >= 0 && x < bufferedImage.getWidth() && y < bufferedImage.getHeight())
        {
            bufferedImage.setRGB(x, y, color.getRGB());
            return true;
        }
        return false;
    }
    
    public boolean setPixel(int x, int y, Color color) {
        if (x >= 0 && y >= 0 && x < hiddenImage.getWidth() && y < hiddenImage.getHeight())
        {
            hiddenImage.setRGB(x, y, color.getRGB());
            rescalingNeeded = true;
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
    
    public IonPixelField setResolution(int x, int y) {
        Raster oldData = hiddenImage.getData();
        hiddenImage = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
        hiddenImage.setData(oldData);
        rescalingNeeded = true;
        return this;
    }

    public IonPixelField setWidth(int newWidth) {
        Raster oldData = bufferedImage.getData();
        bufferedImage = new BufferedImage(newWidth, bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        bufferedImage.setData(oldData);
        super.setWidth(newWidth);
        return this;
    }
    
    public IonPixelField setHeight(int newHeight) {
        Raster oldData = bufferedImage.getData();
        bufferedImage = new BufferedImage(bufferedImage.getWidth(), newHeight, BufferedImage.TYPE_INT_RGB);
        bufferedImage.setData(oldData);
        super.setHeight(newHeight);
        return this;
    }

    public IonPixelField resize(int newWidth, int newHeight) {
        Raster oldData = bufferedImage.getData();
        bufferedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        bufferedImage.setData(oldData);
        return this;
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

    public void clear() {
        bufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
    }

    public void draw(Graphics g) {
        if (rescalingNeeded)
        {
            scaleUp(hiddenImage, bufferedImage);
            rescalingNeeded = false;
        }
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
