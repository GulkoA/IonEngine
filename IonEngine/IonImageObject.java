package IonEngine;

import javax.imageio.ImageIO;
import javax.swing.WindowConstants;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * A class representing an image object in the Ion Engine.
 * This class extends IonObject and provides functionality to load and draw images.
 */
public class IonImageObject extends IonObject{
    /** The BufferedImage that stores the loaded image data */
    private BufferedImage image;

    /**
     * Creates an image object with specified dimensions, position and z-index.
     * 
     * @param path The file path to the image (not loaded automatically)
     * @param width The width of the image object
     * @param height The height of the image object
     * @param x The x-coordinate position
     * @param y The y-coordinate position
     * @param z_index The layer index for rendering order
     */
    public IonImageObject(String path, int width, int height, double x, double y, int z_index) {
        super(width, height, x, y, z_index);
    }

    /**
     * Creates an image object with specified dimensions and position.
     * 
     * @param width The width of the image object
     * @param height The height of the image object
     * @param x The x-coordinate position
     * @param y The y-coordinate position
     */
    public IonImageObject(int width, int height, double x, double y) {
        super(width, height, x, y);
    }

    /**
     * Creates an image object at the specified position with default dimensions.
     * 
     * @param x The x-coordinate position
     * @param y The y-coordinate position
     */
    public IonImageObject(int x, int y) {
        super(x, y);
    }

    /**
     * Creates an image object with default values.
     */
    public IonImageObject() {
        super();
    }

    /**
     * Loads an image from the specified file path.
     * The width and height of the object will be automatically set to match the image dimensions.
     * 
     * @param path The file path to the image to load
     * @return This IonImageObject instance for method chaining
     */
    public IonImageObject loadImage(String path) {
        try {
            this.image = ImageIO.read(new File(path));
            setWidth(this.image.getWidth());
            setHeight(this.image.getHeight());
        }
        catch (Exception e) {
            System.out.println("File load error! \n" + e + "\n at " + path);
        }
        return this;
    }

    /**
     * Draws the image on the specified Graphics context.
     * The image is drawn at its position relative to its container.
     * If no image is loaded, nothing will be drawn.
     * 
     * @param g The Graphics context to draw on
     */
    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, (int)(super.getContainer().getX() + super.getX()), (int)(super.getContainer().getY() + super.getY()), null);
        }
    }
}
