package IonEngine;
import java.awt.*;
import java.awt.Font;
import java.awt.geom.AffineTransform;

/**
 * A rectangular graphical object that can be drawn on an IonPanel.
 * Supports background color, border, rotation, and text rendering.
 */
public class IonRectangleObject extends IonObject {
    private double rotation = 0;
    private double rotationCenterX = 0;
    private double rotationCenterY = 0;
    private Color backgroundColor = new Color(20, 20, 22);
    private Color borderColor = Color.black;
    private int borderSize = 0;
    private String text = "";
    private Color textColor = Color.white;
    private Font textFont = new Font("Arial", Font.PLAIN, 20);

    /**
     * Creates a rectangle with specified dimensions, position and z-index.
     * @param width The width of the rectangle in pixels
     * @param height The height of the rectangle in pixels
     * @param x The x-coordinate position
     * @param y The y-coordinate position
     * @param z_index The layer depth (higher numbers appear on top)
     */
    public IonRectangleObject(int width, int height, double x, double y, int z_index) {
        super(width, height, x, y, z_index);
    }

    /**
     * Creates a rectangle with specified dimensions and position.
     * @param width The width of the rectangle in pixels
     * @param height The height of the rectangle in pixels
     * @param x The x-coordinate position
     * @param y The y-coordinate position
     */
    public IonRectangleObject(int width, int height, double x, double y) {
        super(width, height, x, y);
    }

    /**
     * Creates a rectangle at the specified position with default dimensions.
     * @param x The x-coordinate position
     * @param y The y-coordinate position
     */
    public IonRectangleObject(int x, int y) {
        super(x, y);
    }

    /**
     * Creates a rectangle with default dimensions at position (0,0).
     */
    public IonRectangleObject() {
        super();
    }
    
    /**
     * Sets the background color of the rectangle.
     * @param newColor The new background color
     * @return This object for method chaining
     */
    public IonRectangleObject setBackgroundColor(Color newColor) {
        backgroundColor = newColor;
        super.sendObjectEvent("changedBackgroundColor");
        super.repaint();
        return this;
    }

    /**
     * Sets the center point for rotation operations.
     * @param x The x-coordinate of the rotation center relative to the rectangle's position
     * @param y The y-coordinate of the rotation center relative to the rectangle's position
     */
    public void setRotationCenter(double x, double y) {
        rotationCenterX = x;
        rotationCenterY = y;
    }

    /**
     * Sets the border thickness of the rectangle.
     * @param borderSize The border thickness in pixels
     */
    public void setBorderSize(int borderSize) {
        this.borderSize = borderSize;
    }

    /**
     * Sets the absolute rotation of the rectangle.
     * @param newRotation The rotation angle in radians
     */
    public void setRotation(double newRotation) {
        this.rotation = newRotation;
        super.repaint();
    }

    /**
     * Rotates the rectangle by a relative angle.
     * @param rotation The angle to rotate by in radians
     */
    public void rotateBy(double rotation) {
        this.rotation += rotation;
        super.repaint();
    }

    /**
     * Sets the text to be displayed in the center of the rectangle.
     * @param text The text to display
     * @return This object for method chaining
     */
    public IonRectangleObject setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * Gets the current text being displayed.
     * @return The current text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the font used for text rendering.
     * @param font The font to use for text
     */
    public void setFont(Font font) {
        this.textFont = font;
    }

    /**
     * Draws the rectangle with all its properties (background, border, text, rotation).
     * This method is called internally by the rendering system.
     * @param g The graphics context to draw on
     */
    public void draw(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
    
        AffineTransform oldTransform = g2D.getTransform();
        
        if (rotation != 0)
            g2D.rotate(rotation, super.getX() + super.getWidth() / 2 + rotationCenterX, super.getY() + super.getHeight() / 2 + rotationCenterY);

        Color oldColor = g2D.getColor();
        g2D.setColor(backgroundColor);
        int x = (int)(super.getContainer().getX() + super.getX());
        int y = (int)(super.getContainer().getY() + super.getY());
        g2D.fillRect(x, y, super.getWidth(), super.getHeight());
        g2D.setColor(oldColor);
        
        if (borderSize > 0)
        {
            Stroke oldStroke = g2D.getStroke();
            g2D.setStroke(new BasicStroke(borderSize));
            g2D.setColor(borderColor);
            g2D.drawRect(x, y, super.getWidth(), super.getHeight()); //!fix need to add private variables for xy position in panel for correct drawing
            g2D.setStroke(oldStroke);
        }

        if (text != "")
        {
            g2D.setColor(textColor);
            g2D.setFont(textFont);
            g2D.drawString(text, x + (int)(super.getWidth() / 2 - text.length() * 4.5), y + super.getHeight() / 2 + 5);
        }

        g2D.setTransform(oldTransform);
    }
}
