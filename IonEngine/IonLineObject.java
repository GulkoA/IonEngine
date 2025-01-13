package IonEngine;
import java.awt.Color;
import java.awt.*;

/**
 * Represents a line object in the Ion Engine that can be drawn between two points.
 * This class extends IonObject to provide line-specific functionality.
 */
public class IonLineObject extends IonObject {
    private double X2;
    private double Y2;
    private Color lineColor = Color.black;

    /**
     * Creates a line object with specified start and end points and z-index.
     * 
     * @param x The x-coordinate of the start point
     * @param y The y-coordinate of the start point
     * @param X2 The x-coordinate of the end point
     * @param Y2 The y-coordinate of the end point
     * @param z_index The z-index determining the drawing order
     */
    public IonLineObject(double x, double y, double X2, double Y2, int z_index) {
        super((int)Math.abs(X2 - x), (int)Math.abs(Y2 - y), x, y);
        this.X2 = X2;
        this.Y2 = Y2;
        super.setZIndex(z_index);
    }

    /**
     * Creates a line object with specified start and end points.
     * 
     * @param x The x-coordinate of the start point
     * @param y The y-coordinate of the start point
     * @param X2 The x-coordinate of the end point
     * @param Y2 The y-coordinate of the end point
     */
    public IonLineObject(double x, double y, double X2, double Y2) {
        super((int)Math.abs(X2 - x), (int)Math.abs(Y2 - y), x, y);
        this.X2 = X2;
        this.Y2 = Y2;
    }

    /**
     * Creates a line object with a specified start point and angle.
     * The line will have a length of 100 units.
     * 
     * @param x The x-coordinate of the start point
     * @param y The y-coordinate of the start point
     * @param angleInRadians The angle of the line in radians
     */
    public IonLineObject(double x, double y, double angleInRadians) {
        super(0, 0, x, y);
        this.X2 = Math.cos(angleInRadians) * 100 + x;
        this.Y2 = Math.sin(angleInRadians) * 100 + y;
        // System.out.println(X2 + " " + Y2 + " " + angleInRadians);
        // super.setWidth((int)Math.abs(X2 - x)); //!fix add correct width and height setting here
        // super.setHeight((int)Math.abs(Y2 - y));
    }

    /**
     * Creates a line object with equal start and end points.
     * 
     * @param x The x-coordinate of both points
     * @param y The y-coordinate of both points
     */
    public IonLineObject(double x, double y) {
        super(0, 0, x, y);
        this.X2 = x;
        this.Y2 = x;
    }

    /**
     * Creates a default line object with length 10 at position (0,0).
     */
    public IonLineObject() {
        super(10, 10, 0, 0);
        this.X2 = 10;
        this.Y2 = 10;
    }

    /**
     * Gets the x-coordinate of the line's end point.
     * 
     * @return The x-coordinate of the end point
     */
    public double getX2() {
        return this.X2;
    }

    /**
     * Sets the x-coordinate of the line's end point.
     * 
     * @param X2 The new x-coordinate of the end point
     */
    public void setX2(double X2) {
        this.X2 = X2;
    }

    /**
     * Gets the y-coordinate of the line's end point.
     * 
     * @return The y-coordinate of the end point
     */
    public double getY2() {
        return this.Y2;
    }

    /**
     * Sets the y-coordinate of the line's end point.
     * 
     * @param Y2 The new y-coordinate of the end point
     */
    public void setY2(double Y2) {
        this.Y2 = Y2;
    }

    /**
     * Moves both points of the line to new coordinates.
     * 
     * @param newX The new x-coordinate of the start point
     * @param newY The new y-coordinate of the start point
     * @param newX2 The new x-coordinate of the end point
     * @param newY2 The new y-coordinate of the end point
     */
    public void moveTo(double newX, double newY, double newX2, double newY2) {
        this.X2 = newX2;
        this.Y2 = newY2;
        super.setX(newX);
        super.setY(newY);
    }

    /**
     * Moves the start point while maintaining the relative position of the end point.
     * 
     * @param newX The new x-coordinate of the start point
     * @param newY The new y-coordinate of the start point
     */
    public void moveTo(double newX, double newY) {
        this.X2 = X2 - super.getX() + newX;
        this.Y2 = Y2 - super.getY() + newY;
        super.setX(newX);
        super.setY(newY);
    }

    /**
     * Moves both points of the line by the specified amounts.
     * 
     * @param moveX The amount to move in the x direction
     * @param moveY The amount to move in the y direction
     */
    public void moveBy(double moveX, double moveY) {
        this.X2 += moveX;
        this.Y2 += moveY;
        super.setX(super.getX() + moveX);
        super.setY(super.getX() + moveY);
    }

    /**
     * Sets the color of the line.
     * 
     * @param lineColor The color to set for the line
     * @return This IonLineObject instance for method chaining
     */
    public IonLineObject setLineColor(Color lineColor) {
        this.lineColor = lineColor;
        return this;
    }

    /**
     * Draws the line on the specified graphics context.
     * The line is drawn relative to its container's position.
     * 
     * @param g The graphics context to draw on
     */
    public void draw(Graphics g) {
        int drawX = (int)(super.getContainer().getX() + super.getX());
        int drawY = (int)(super.getContainer().getY() + super.getY());
        int drawX2 = (int)(super.getContainer().getX() + X2);
        int drawY2 = (int)(super.getContainer().getY() + Y2);

        Color oldColor = g.getColor();
        g.setColor(lineColor);
        g.drawLine(drawX, drawY, drawX2, drawY2);
        g.setColor(oldColor);
    }
}