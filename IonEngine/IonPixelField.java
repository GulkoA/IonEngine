package IonEngine;

import java.awt.*;
import java.awt.Graphics;
import java.awt.image.Raster;
import java.awt.image.BufferedImage;

/**
 * A container class that manages a pixel-based drawing field with support for image scaling and manipulation.
 * This class provides functionality for drawing and manipulating pixels both at the display resolution
 * and at a virtual resolution that can be different from the display resolution.
 */
public class IonPixelField extends IonContainer {
    private BufferedImage bufferedImage;  // Display resolution image
    private BufferedImage hiddenImage;    // Virtual resolution image

    /**
     * Creates a new pixel field with specified dimensions and position.
     * @param width The width of the field in pixels
     * @param height The height of the field in pixels
     * @param x The x-coordinate position
     * @param y The y-coordinate position
     */
    public IonPixelField(int width, int height, int x, int y) {
        super(width, height, x, y);
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        hiddenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        hiddenImage.setData(bufferedImage.getData());
    }

    /**
     * Creates a new pixel field with specified dimensions at position (0,0).
     * @param width The width of the field in pixels
     * @param height The height of the field in pixels
     */
    public IonPixelField(int width, int height) {
        super(width, height);
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        hiddenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        hiddenImage.setData(bufferedImage.getData());
    }

    /**
     * Creates a new pixel field with default dimensions and position.
     */
    public IonPixelField() {
        super();
        bufferedImage = new BufferedImage(super.getWidth(), super.getHeight(), BufferedImage.TYPE_INT_RGB);
        hiddenImage = new BufferedImage(super.getWidth(), super.getHeight(), BufferedImage.TYPE_INT_RGB);
        hiddenImage.setData(bufferedImage.getData());
    }

    private boolean rescalingNeeded = false;
    
    /**
     * Scales up the source image to fit the target image dimensions.
     * @param source The source image to scale
     * @param target The target image that will contain the scaled result
     * @return true if scaling was successful, false if target is smaller than source
     */
    private boolean scaleUp(BufferedImage source, BufferedImage target) {
        double factorX = (double)target.getWidth() / source.getHeight();
        double factorY = (double)target.getHeight() / source.getWidth();
        if (factorX < 1 || factorY < 1)
            return false;
        
        for (int x = 0; x < target.getWidth(); x++)
            for (int y = 0; y < target.getHeight(); y++) {
                target.setRGB(x, y, source.getRGB((int)(x / factorX), (int)(y / factorY)));
            }
        return true;
    }

    /**
     * Gets the width of the virtual resolution image.
     * @return The width in pixels
     */
    public int getImageWidth() { return hiddenImage.getWidth(); }

    /**
     * Gets the height of the virtual resolution image.
     * @return The height in pixels
     */
    public int getImageHeight() { return hiddenImage.getHeight(); }

    /**
     * Sets a pixel color at the specified coordinates in the display resolution image.
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @param color The color to set
     * @return true if the pixel was set successfully, false if coordinates are out of bounds
     */
    public boolean setRealPixel(int x, int y, Color color) {
        if (x >= 0 && y >= 0 && x < bufferedImage.getWidth() && y < bufferedImage.getHeight())
        {
            bufferedImage.setRGB(x, y, color.getRGB());
            return true;
        }
        return false;
    }
    
    /**
     * Sets a pixel color at the specified coordinates in the virtual resolution image.
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @param color The color to set
     * @return true if the pixel was set successfully, false if coordinates are out of bounds
     */
    public boolean setPixel(int x, int y, Color color) {
        if (x >= 0 && y >= 0 && x < hiddenImage.getWidth() && y < hiddenImage.getHeight())
        {
            hiddenImage.setRGB(x, y, color.getRGB());
            rescalingNeeded = true;
            return true;
        }
        return false;
    }

    /**
     * Gets the color of a pixel at the specified coordinates from the display resolution image.
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return The color at the specified position, or null if coordinates are out of bounds
     */
    public Color getPixel(int x, int y) {
        if (x >= 0 && y >= 0 && x < bufferedImage.getWidth() && y < bufferedImage.getHeight())
        return new Color(bufferedImage.getRGB(x, y));
        else
        return null;
    }
    
    /**
     * Sets the virtual resolution of the pixel field.
     * @param x The new width in pixels
     * @param y The new height in pixels
     * @return This IonPixelField instance for method chaining
     */
    public IonPixelField setResolution(int x, int y) {
        Raster oldData = hiddenImage.getData();
        hiddenImage = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
        hiddenImage.setData(oldData);
        rescalingNeeded = true;
        return this;
    }

    /**
     * Sets the width of the display resolution image.
     * @param newWidth The new width in pixels
     * @return This IonPixelField instance for method chaining
     */
    public IonPixelField setWidth(int newWidth) {
        Raster oldData = bufferedImage.getData();
        bufferedImage = new BufferedImage(newWidth, bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        bufferedImage.setData(oldData);
        super.setWidth(newWidth);
        return this;
    }
    
    /**
     * Sets the height of the display resolution image.
     * @param newHeight The new height in pixels
     * @return This IonPixelField instance for method chaining
     */
    public IonPixelField setHeight(int newHeight) {
        Raster oldData = bufferedImage.getData();
        bufferedImage = new BufferedImage(bufferedImage.getWidth(), newHeight, BufferedImage.TYPE_INT_RGB);
        bufferedImage.setData(oldData);
        super.setHeight(newHeight);
        return this;
    }

    /**
     * Resizes the display resolution image to new dimensions.
     * @param newWidth The new width in pixels
     * @param newHeight The new height in pixels
     * @return This IonPixelField instance for method chaining
     */
    public IonPixelField resize(int newWidth, int newHeight) {
        Raster oldData = bufferedImage.getData();
        bufferedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        bufferedImage.setData(oldData);
        return this;
    }

    /**
     * Fills a rectangular area with random colored pixels.
     * @param xFirst Starting x-coordinate
     * @param yFirst Starting y-coordinate
     * @param xLast Ending x-coordinate (exclusive)
     * @param yLast Ending y-coordinate (exclusive)
     */
    public void fillWithNoise(int xFirst, int yFirst, int xLast, int yLast) {
        for (int w = xFirst; w < xLast; w++)
            for (int h = yFirst; h < yLast; h++)
                setPixel(w, h, IonFeatures.randomColor());
        super.repaint();
    } 

    /**
     * Fills a horizontal strip with random colored pixels.
     * @param xFirst Starting x-coordinate
     * @param xLast Ending x-coordinate (exclusive)
     */
    public void fillWithNoise(int xFirst, int xLast) {
        fillWithNoise(xFirst, xLast, getWidth(), getHeight());
    }

    /**
     * Fills the entire pixel field with random colored pixels.
     */
    public void fillWithNoise() {
        fillWithNoise(0, 0, getWidth(), getHeight());
    }

    /**
     * Clears the display resolution image by setting all pixels to black.
     */
    public void clear() {
        bufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
    }

    /**
     * Draws the pixel field to the specified graphics context.
     * @param g The graphics context to draw to
     */
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

    /**
     * Gets the display resolution image buffer.
     * @return The BufferedImage used for display
     */
    public BufferedImage getBufferedImage() {
        return this.bufferedImage;
    }

    /**
     * Offsets the entire image by the specified amount.
     * @param moveX The number of pixels to move horizontally
     * @param moveY The number of pixels to move vertically
     */
    public void offsetImageBy(int moveX, int moveY) {
        Raster oldData = bufferedImage.getData();
        for (int x = 0; x < oldData.getWidth(); x++)
            for (int y = 0; y < oldData.getHeight(); y++)
            {
                setPixel(x + moveX, y + moveY, IonFeatures.getRasterPixel(oldData, x, y));
            }

    }
}
