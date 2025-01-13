package IonEngine;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.HashMap;

/**
 * Base class for all graphical objects in the Ion Engine.
 * Provides core functionality for positioning, sizing, drawing and property management.
 */
public class IonObject {
    /** The container this object belongs to */
    private IonContainer thisContainer;
    
    /** The key identifying this object in its container */
    private String thisContainerKey;
    
    /** X coordinate relative to container */
    private double x = 0;
    
    /** Y coordinate relative to container */
    private double y = 0;
    
    /** Width in pixels */
    private int width = 100;
    
    /** Height in pixels */
    private int height = 100;
    
    /** Z-index for layering (higher numbers = further back) */
    private int z_index = 0;
    
    /** Custom properties map */
    private HashMap<String, Object> properties = new HashMap<String, Object>();
    
    /**
     * Creates a new IonObject with specified dimensions and position
     * @param width Width in pixels
     * @param height Height in pixels 
     * @param x X coordinate
     * @param y Y coordinate
     * @param z_index Layer index
     */
    public IonObject(int width, int height, double x, double y, int z_index) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.z_index = z_index;
    }
    public IonObject(int width, int height, double x, double y) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }
    /**
     * Creates a new IonObject with default size (100x100) at the specified position.
     * @param x The X coordinate in the container
     * @param y The Y coordinate in the container
     */
    public IonObject(double x, double y) {
        this.x = x;
        this.y = y;
    }
    /**
     * Creates a new IonObject with default properties.
     * Initial size will be 100x100 pixels at position (0,0).
     */
    public IonObject() {}

    /**
     * Sets the X coordinate of this object relative to its container.
     * Triggers a change event and updates the object's position.
     * @param x The new X coordinate
     */
    public void setX(double x) {
        this.x = x;
        sendObjectEvent("changeX");
        normalizeBorders();
        repaint();
    }

    /**
     * Sets the Y coordinate of this object relative to its container.
     * Triggers a change event and updates the object's position.
     * @param y The new Y coordinate
     */
    public void setY(double y) {
        this.y = y;
        sendObjectEvent("changeY");
        normalizeBorders();
        repaint();
    }

    /**
     * Moves the object to a specific position in its container.
     * @param newX The target X coordinate
     * @param newY The target Y coordinate
     */
    public void moveTo(double newX, double newY) {
        x = newX;
        y = newY;
        sendObjectEvent("movedTo");
        normalizeBorders();
        repaint();
    }

    /**
     * Moves the object relative to its current position.
     * @param newX The X distance to move
     * @param newY The Y distance to move
     */
    public void moveBy(double newX, double newY) {
        x += newX;
        y += newY;
        sendObjectEvent("movedBy");
        normalizeBorders();
        repaint();
    }

    /**
     * Resizes the object to specific dimensions.
     * @param width The new width in pixels
     * @param height The new height in pixels
     */
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        sendObjectEvent("resized");
        normalizeBorders();
        repaint();
    }

    /**
     * Resizes the object to be a square with the specified size.
     * @param size The new width and height in pixels
     */
    public void resize(int size) {
        this.width = size;
        this.height = size;
        sendObjectEvent("resized");
        normalizeBorders();
        repaint();
    }

    /**
     * Changes the object's size relative to its current dimensions.
     * @param width Amount to change width by in pixels
     * @param height Amount to change height by in pixels
     */
    public void resizeBy(int width, int height) {
        this.width += width;
        this.height += height;
        sendObjectEvent("resized");
        normalizeBorders();
        repaint();
    }

    /**
     * Changes both width and height by the same amount.
     * @param change Amount to change both dimensions by in pixels
     */
    public void resizeBy(int change) {
        this.width += change;
        this.height += change;
        sendObjectEvent("resized");
        normalizeBorders();
        repaint();
    }

    /**
     * Multiplies the object's dimensions by the given coefficients.
     * @param widthCoefficient Multiplier for width
     * @param heightCoefficient Multiplier for height
     */
    public void scale(int widthCoefficient, int heightCoefficient) {
        this.width *= widthCoefficient;
        this.height *= heightCoefficient;
        sendObjectEvent("scaled");
        normalizeBorders();
        repaint();
    }

    /**
     * Multiplies both width and height by the same coefficient.
     * @param coefficient Multiplier for both dimensions
     */
    public void scale(int coefficient) {
        this.width *= coefficient;
        this.height *= coefficient;
        sendObjectEvent("scaled");
        normalizeBorders();
        repaint();
    }

    /**
     * Sets the object's width.
     * @param width New width in pixels
     */
    public void setWidth(int width) {
        this.width = width;
        sendObjectEvent("setWidth");
        normalizeBorders();
        repaint();
    }

    /**
     * Sets the object's height.
     * @param height New height in pixels
     */
    public void setHeight(int height) {
        this.height = height;
        sendObjectEvent("setHeight");
        normalizeBorders();
        repaint();
    }

    /**
     * Sets the Z-index of this object, determining its layer order.
     * Higher numbers appear further back.
     * @param zIndex The new Z-index value
     */
    public void setZIndex(int zIndex) {
        this.z_index = zIndex;
        thisContainer.resortObjectInSortedObjects(this);
        repaint();
    }

    /**
     * Ensures the object stays within its container's bounds.
     * Adjusts position if necessary to prevent overflow.
     */
    public void normalizeBorders() {
        if (x < 0)
            x = 0;
        else if (x + width > thisContainer.getWidth())
            x = thisContainer.getWidth() - width;
        
        if (y < 0)
            y = 0;
        else if (y + height > thisContainer.getHeight())
            y = thisContainer.getHeight() - height;
    }

    /**
     * Checks if the object is touching the bottom of its container within a margin of error.
     * @param error The margin of error in pixels
     * @return true if touching bottom, false otherwise
     */
    public boolean touchesBottom(double error) {
        return y + height + error >= thisContainer.getHeight();
    }
    public boolean touchesBottom() {
        return y + height == thisContainer.getHeight();
    }

    /**
     * Checks if this object is touching the top edge of its container within a margin of error.
     * @param error The margin of error in pixels (allows for "close enough" detection)
     * @return true if the object is within the error margin of the top edge
     */
    public boolean touchesTop(double error) {
        return y - error <= 0;
    }
    /**
     * Checks if this object is exactly touching the top edge of its container.
     * @return true if the object is exactly at the top edge
     */
    public boolean touchesTop() {
        return y == 0;
    }

    /**
     * Checks if this object is touching the left edge of its container within a margin of error.
     * @param error The margin of error in pixels (allows for "close enough" detection)
     * @return true if the object is within the error margin of the left edge
     */
    public boolean touchesLeft(double error) {
        return x - error <= 0;
    }
    /**
     * Checks if this object is exactly touching the left edge of its container.
     * @return true if the object is exactly at the left edge
     */
    public boolean touchesLeft() {
        return x == 0;
    }

    /**
     * Checks if this object is touching the right edge of its container within a margin of error.
     * @param error The margin of error in pixels (allows for "close enough" detection)
     * @return true if the object is within the error margin of the right edge
     */
    public boolean touchesRight(double error) {
        return x + width + error >= thisContainer.getWidth();
    }
    /**
     * Checks if this object is exactly touching the right edge of its container.
     * @return true if the object is exactly at the right edge
     */
    public boolean touchesRight() {
        return x + width == thisContainer.getWidth();
    }
    
    /**
     * Gets the X coordinate of this object relative to its container.
     * @return The X coordinate
     */
    public double getX() {return x;}
    /**
     * Gets the Y coordinate of this object relative to its container.
     * @return The Y coordinate
     */
    public double getY() {return y;}
    /**
     * Gets the X coordinate of the center point of this object.
     * @return The X coordinate of the object's center
     */
    public double getXMiddle() {return x + width / 2;}
    /**
     * Gets the Y coordinate of the center point of this object.
     * @return The Y coordinate of the object's center
     */
    public double getYMiddle() {return y + height / 2;}
    /**
     * Gets the width of this object in pixels.
     * @return The width in pixels
     */
    public int getWidth() {return width;}
    /**
     * Gets the height of this object in pixels.
     * @return The height in pixels
     */
    public int getHeight() {return height;}
    /**
     * Gets the Z-index of this object, which determines its layer order.
     * Higher Z-index values appear further back.
     * @return The Z-index value
     */
    public int getZIndex() {return z_index;}

    /**
     * Sets the container this object belongs to and its key within that container.
     * @param container The IonContainer this object will belong to
     * @param containerKey The key identifying this object in the container
     */
    public void setContainer(IonContainer container, String containerKey) {
        thisContainer = container;
        thisContainerKey = containerKey;
        normalizeBorders();
    }

    public IonContainer getContainer() {
        return thisContainer;
    }

    /**
     * Draws this object on the specified Graphics context.
     * This is the base implementation and does nothing - subclasses should override
     * this method to provide their own drawing behavior.
     * @param g The Graphics context to draw on
     */
    public void draw(Graphics g) {}

    /**
     * Requests a repaint of this object's container, which will cause
     * all objects to be redrawn in their proper order.
     */
    public void repaint() {
        thisContainer.repaint();
    }

    /**
     * Adds a custom property to this object.
     * @param propertyName The name of the property
     * @param property The property value
     * @return The added property value
     */
    public Object addProperty(String propertyName, Object property) {
        this.properties.put(propertyName, property);
        return property;
    }

    /**
     * Gets a property value, returning a default if the property doesn't exist.
     * @param propertyName The name of the property to retrieve
     * @param defaultProperty The default value to return if property is null
     * @return The property value or defaultProperty if not found
     */
    public Object getProperty(String propertyName, Object defaultProperty) { //default is returned if property is null
        Object toReturn = this.properties.get(propertyName);
        if (toReturn == null)
            return defaultProperty;
        return toReturn;
    }

    /**
     * Sets a property value, creating it if it doesn't exist.
     * @param propertyName The name of the property
     * @param property The new property value
     * @return This object for method chaining
     */
    public IonObject setProperty(String propertyName, Object property) {
        if (properties.get(propertyName) == null)
            addProperty(propertyName, property);
        else
            properties.replace(propertyName, property);
        return this;
    }
    public IonObject setProperties(String[] propertyNames, Object[] propertyValues) {
        for (int i = 0; i < propertyNames.length; i++)
            setProperty(propertyNames[i], propertyValues[i]);
        return this;
    }

    /**
     * Notifies all behavior packs in the container that an event has occurred on this object.
     * @param type The type of event that occurred (e.g., "changeX", "movedTo", "resized")
     */
    public void sendObjectEvent(String type) {
        for (IonBehaviourPack pack: thisContainer.getBehaviors()) {
            pack.objectEvent(this, type);
        }
    }

    /**
     * Moves this object up one layer in its container.
     * Adjusts Z-indices to maintain proper layering.
     */
    public void moveUp() {
        int i = thisContainer.getSortedObjects().indexOf(this);
        if (i <= 0)
            return;
        int i2 = i - 1;
        while (i2 > 0 && thisContainer.getSortedObjects().get(i2).getZIndex() <= getZIndex()) {i2--;}
        // System.out.println("Before:\n" + i2 + " and " + i);
        // System.out.println(thisContainer.getSortedObjects().get(i2).getZIndex() + " and " + thisContainer.getSortedObjects().get(i).getZIndex());
        // System.out.println(thisContainer.getSortedObjects().get(i2).getProperty("name") + " and " + thisContainer.getSortedObjects().get(i).getProperty("name"));
        
        IonObject objectAbove = thisContainer.getSortedObjects().get(i2);
        int zIndexBuffer = getZIndex();
        int zIndexBuffer2 = thisContainer.getSortedObjects().get(i2).getZIndex();
        setZIndex(zIndexBuffer2);
        objectAbove.setZIndex(zIndexBuffer);
        
        // System.out.println("After:\n" + i2 + " and " + i);
        // System.out.println(thisContainer.getSortedObjects().get(i2).getZIndex() + " and " + thisContainer.getSortedObjects().get(i).getZIndex());
        // System.out.println(thisContainer.getSortedObjects().get(i2).getProperty("name") + " and " + thisContainer.getSortedObjects().get(i).getProperty("name"));
    }

    /**
     * Moves this object down one layer in its container.
     * Adjusts Z-indices to maintain proper layering.
     */
    public void moveDown() {
        int i = thisContainer.getSortedObjects().indexOf(this);
        if (i <= 0)
            return;
        int i2 = i + 1;
        while (i2 < thisContainer.getSortedObjects().size() && thisContainer.getSortedObjects().get(i2).getZIndex() >= getZIndex()) {i2++;}

        IonObject objectAbove = thisContainer.getSortedObjects().get(i2);
        int zIndexBuffer = getZIndex();
        int zIndexBuffer2 = thisContainer.getSortedObjects().get(i2).getZIndex();
        setZIndex(zIndexBuffer2);
        objectAbove.setZIndex(zIndexBuffer);
    }

    /**
     * Moves this object to the topmost layer in its container.
     */
    public void moveToTop() {
        if (thisContainer.getSortedObjects().indexOf(this) <= 0)
            return;
        while (thisContainer.getSortedObjects().indexOf(this) > 0 && getZIndex() != thisContainer.getSortedObjects().get(0).getZIndex()) {moveUp();}
    }

    /**
     * Moves this object to the bottommost layer in its container.
     */
    public void moveToBottom() {
        if (thisContainer.getSortedObjects().indexOf(this) < 0)
            return;
        while (thisContainer.getSortedObjects().indexOf(this) < thisContainer.getSortedObjects().size() && getZIndex() != thisContainer.getSortedObjects().get(thisContainer.getSortedObjects().size() - 1).getZIndex()) {moveDown();}
    }

    /**
     * Removes this object from its container.
     */
    public void remove() {
        thisContainer.remove(thisContainerKey);
    }

    /**
     * Gets the key identifying this object in its container.
     * @return The container key for this object
     */
    public String getName() { return thisContainerKey; }
}
