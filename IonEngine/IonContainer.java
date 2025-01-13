package IonEngine;

import java.awt.event.*;
import java.awt.*;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * A container class that manages and renders graphical objects in the Ion
 * Engine.
 * IonContainer acts as a canvas or panel that can hold multiple IonObjects,
 * managing their
 * layout, rendering, and interaction behaviors.
 * 
 * Features:
 * - Automatic or fixed sizing
 * - Object management with z-index sorting
 * - Border and background customization
 * - Mouse and keyboard event handling
 * - Behavior pack support for extended functionality
 */
public class IonContainer {
    // General parameters
    private IonPanel thisPanel;
    private String thisPanelKey;
    private int width;
    private int height;
    private int x;
    private int y;
    private boolean widthAuto;
    private boolean heightAuto;
    private boolean visible = true;

    // Border
    private Color borderColor;
    private Color backgroundColor;
    private int borderThickness;

    // Stored objects
    private HashMap<String, IonObject> objectsMap = new HashMap<String, IonObject>();

    // Stores sorted objects by z_index
    private ArrayList<IonObject> sortedObjects = new ArrayList<IonObject>();

    // BehaviorPacks
    private ArrayList<IonBehaviourPack> behaviorPacks = new ArrayList<IonBehaviourPack>();

    /**
     * Creates a container with specified dimensions and position.
     * 
     * @param width  The container width (use 0 or negative for auto-width)
     * @param height The container height (use 0 or negative for auto-height)
     * @param x      The x-coordinate position
     * @param y      The y-coordinate position
     */
    public IonContainer(int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        widthAuto = width <= 0;
        heightAuto = height <= 0;
        borderColor = Color.black;
        backgroundColor = Color.black;
        borderThickness = 0;
    }

    /**
     * Creates a container with specified width and height, positioned at (0,0).
     * Use zero or negative values for width/height to enable auto-sizing.
     *
     * @param width The container width
     * @param height The container height
     */
    public IonContainer(int width, int height) { // sets width/height to auto if 0, 0
        this.width = width;
        this.height = height;
        x = 0;
        y = 0;
        widthAuto = width <= 0;
        heightAuto = height <= 0;
        borderColor = Color.black;
        backgroundColor = Color.black;
        borderThickness = 0;
    }

    /**
     * Creates a default container with auto-sizing enabled.
     * Initial size is set to 500x500 pixels.
     */
    public IonContainer() {
        widthAuto = true;
        heightAuto = true;
        this.width = 500;
        this.height = 500;
        backgroundColor = Color.black;
        borderThickness = 0;
    }

    /**
     * Draws the container and all its contained objects.
     * Objects are drawn in order of their z-index (highest to lowest).
     *
     * @param g The Graphics context to draw with
     */
    public void draw(Graphics g) {
        if (visible) {
            normalizeBorders();
            if (borderThickness > 0)
                drawFrame(g);
            for (int i = sortedObjects.size() - 1; i >= 0; i--) {
                if (sortedObjects.get(i) != null)
                    sortedObjects.get(i).draw(g);
            }
        }
    }

    /**
     * Sets the visibility state of the container.
     *
     * @param newState true to make visible, false to hide
     * @return This container for method chaining
     */
    public IonContainer setVisible(boolean newState) {
        visible = newState;
        repaint();
        return this;
    }

    /**
     * Checks if the container is currently visible.
     *
     * @return true if visible, false if hidden
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Updates container dimensions based on auto-sizing settings.
     * Called automatically during drawing.
     *
     * @return This container for method chaining
     */
    public IonContainer normalizeBorders() {
        if (widthAuto)
            this.width = (int) thisPanel.getSize().getWidth();
        if (heightAuto)
            this.height = (int) thisPanel.getSize().getHeight();
        return this;
    }

    /**
     * Draws the container's border frame.
     * Only called if borderThickness > 0.
     *
     * @param g The Graphics context to draw with
     */
    public void drawFrame(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        Stroke oldStroke = g2D.getStroke();
        g2D.setStroke(new BasicStroke(borderThickness));
        g2D.setColor(backgroundColor);
        g2D.drawRect(x, y, this.width, this.height); // !fix need to add private variables for xy position in panel for
                                                     // correct drawing
        g2D.setStroke(oldStroke);
    }

    /**
     * Triggers a repaint of the container and its contents.
     */
    public void repaint() {
        thisPanel.repaint();
    }

    /**
     * @return The current width of the container
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * @return The current height of the container
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * @return The current X position of the container
     */
    public int getX() {
        return this.x;
    }

    /**
     * @return The current Y position of the container
     */
    public int getY() {
        return this.y;
    }

    /**
     * @return true if width is set to auto-size, false if fixed
     */
    public boolean isWidthAuto() {
        return this.widthAuto;
    }

    /**
     * @return true if height is set to auto-size, false if fixed
     */
    public boolean isHeightAuto() {
        return this.heightAuto;
    }

    /**
     * Sets the container width.
     *
     * @param width The new width in pixels
     * @return This container for method chaining
     */
    public IonContainer setWidth(int width) {
        this.width = width;
        repaint();
        return this;
    }

    /**
     * Sets the container height.
     *
     * @param height The new height in pixels
     * @return This container for method chaining
     */
    public IonContainer setHeight(int height) {
        this.height = height;
        repaint();
        return this;
    }

    /**
     * Sets both width and height of the container.
     *
     * @param width The new width in pixels
     * @param height The new height in pixels
     * @return This container for method chaining
     */
    public IonContainer resize(int width, int height) {
        this.width = width;
        this.height = height;
        repaint();
        return this;
    }

    public IonContainer setX(int x) {
        this.x = x;
        repaint();
        return this;
    }

    public IonContainer setY(int y) {
        this.y = y;
        repaint();
        return this;
    }

    /**
     * Moves the container to a new position.
     * 
     * @param x The new x-coordinate
     * @param y The new y-coordinate
     * @return This container for method chaining
     */
    public IonContainer moveTo(int x, int y) {
        this.x = x;
        this.y = y;
        repaint();
        return this;
    }

    /**
     * Moves the container relative to its current position.
     *
     * @param x The x-coordinate offset to move by
     * @param y The y-coordinate offset to move by
     * @return This container for method chaining
     */
    public IonContainer moveBy(int x, int y) {
        this.x += x;
        this.y += y;
        repaint();
        return this;
    }

    /**
     * Enables or disables auto-width sizing.
     *
     * @param newState true to enable auto-width, false for fixed width
     * @return This container for method chaining
     */
    public IonContainer setWidthAuto(boolean newState) {
        this.widthAuto = newState;
        repaint();
        return this;
    }

    /**
     * Enables or disables auto-height sizing.
     *
     * @param newState true to enable auto-height, false for fixed height
     * @return This container for method chaining
     */
    public IonContainer setHeightAuto(boolean newState) {
        this.heightAuto = newState;
        repaint();
        return this;
    }

    /**
     * Sets the parent panel and identifier for this container.
     *
     * @param panel The IonPanel this container belongs to
     * @param key The identifier for this container
     */
    public void setPanel(IonPanel panel, String key) {
        this.thisPanel = panel;
        this.thisPanelKey = key;
    }

    /**
     * @return The identifier of this container
     */
    public String getName() {
        return this.thisPanelKey;
    }

    /**
     * Changes the container's border appearance.
     * 
     * @param borderColor     The color of the border
     * @param borderThickness The thickness of the border in pixels
     * @return This container for method chaining
     */
    public IonContainer changeBorder(Color borderColor, int borderThickness) {
        this.borderColor = borderColor;
        this.borderThickness = borderThickness;
        repaint();
        return this;
    }

    /**
     * Sets the border color of the container.
     *
     * @param borderColor The new border color
     * @return This container for method chaining
     */
    public IonContainer changeBorder(Color borderColor) {
        this.borderColor = borderColor;
        repaint();
        return this;
    }

    /**
     * Sets the border thickness of the container.
     *
     * @param borderThickness The new border thickness in pixels
     * @return This container for method chaining
     */
    public IonContainer changeBorder(int borderThickness) {
        this.borderThickness = borderThickness;
        repaint();
        return this;
    }

    /**
     * Adds an IonObject to this container with a specified name.
     * The object will be automatically sorted by its z-index in the rendering
     * order.
     * 
     * @param object The IonObject to add
     * @param name   A unique identifier for the object
     * @return The added IonObject
     */
    public IonObject add(IonObject object, String name) {
        object.setContainer(this, name);
        objectsMap.put(name, object);
        addToSortedObjects(object);
        return object;
    }

    /**
     * Retrieves an object from the container by its name.
     * 
     * @param name The identifier of the object to retrieve
     * @return The IonObject with the specified name, or null if not found
     */
    public IonObject get(String name) {
        return objectsMap.get(name);
    }

    /**
     * Removes an object from the container by its name.
     *
     * @param name The identifier of the object to remove
     * @return The removed object, or null if not found
     */
    public IonObject remove(String name) {
        IonObject removed = objectsMap.get(name);
        objectsMap.remove(name);
        sortedObjects.remove(removed);
        return removed;
    }

    /**
     * Removes objects from the container based on a property value.
     * 
     * @param property The property name to check
     * @param removeOnlyOrIgnore If true, removes objects with the property set to true.
     *                          If false, removes objects with the property set to false.
     */
    public void remove(String property, boolean removeOnlyOrIgnore) { 
        for (String objectName : objectsMap.keySet()) {
            if ((boolean) objectsMap.get(objectName).getProperty(property, false) == removeOnlyOrIgnore) {
                sortedObjects.remove(objectsMap.get(objectName));
                objectsMap.remove(objectName);
            }
        }
    }

    /**
     * Removes all objects from the container.
     *
     * @return This container for method chaining
     */
    public IonContainer removeAllObjects() {
        objectsMap.clear();
        sortedObjects.clear();
        return this;
    }

    /**
     * Adds an object to the sorted list based on its z-index.
     * Higher z-index objects are drawn first.
     *
     * @param objectToAdd The object to add to the sorted list
     */
    public void addToSortedObjects(IonObject objectToAdd) {
        int i = 0;
        while (i < sortedObjects.size() && objectToAdd.getZIndex() < sortedObjects.get(i).getZIndex())
            i++;
        sortedObjects.add(i, objectToAdd);
    }

    /**
     * Updates an object's position in the sorted list after its z-index changes.
     *
     * @param objectToResort The object to reposition in the sorted list
     */
    public void resortObjectInSortedObjects(IonObject objectToResort) {
        sortedObjects.remove(objectToResort);
        addToSortedObjects(objectToResort);
    }

    /**
     * @return Map of all objects in this container, keyed by their names
     */
    public HashMap<String, IonObject> getObjectMap() {
        return objectsMap;
    }

    /**
     * @return List of objects sorted by z-index (highest to lowest)
     */
    public ArrayList<IonObject> getSortedObjects() {
        return sortedObjects;
    }

    /**
     * Adds a behavior pack to this container to extend its functionality.
     * Behavior packs can handle events and add custom interactions.
     * 
     * @param pack The behavior pack to add
     * @return The added behavior pack
     */
    public IonBehaviourPack addBehaviour(IonBehaviourPack pack) {
        pack.setContainer(this);
        this.behaviorPacks.add(pack);
        return pack;
    }

    /**
     * @return List of all behavior packs attached to this container
     */
    public ArrayList<IonBehaviourPack> getBehaviors() {
        return this.behaviorPacks;
    }

    /**
     * Propagates mouse events to all behavior packs.
     *
     * @param e The mouse event
     * @param type The type of mouse event
     * @param x The x-coordinate of the event
     * @param y The y-coordinate of the event
     */
    public void mouseEvent(MouseEvent e, String type, int x, int y) {
        for (IonBehaviourPack behaviourPack : behaviorPacks) {
            behaviourPack.mouseEvent(e, type, x, y);
        }
    }

    /**
     * Propagates keyboard events to all behavior packs.
     *
     * @param e The keyboard event
     * @param type The type of keyboard event
     */
    public void keyEvent(KeyEvent e, String type) {
        for (IonBehaviourPack behaviourPack : behaviorPacks) {
            behaviourPack.keyEvent(e, type);
        }
    }

    /**
     * Gets all objects at specified coordinates with optional property filtering.
     *
     * @param x The x-coordinate to check
     * @param y The y-coordinate to check
     * @param property The property name to filter by
     * @param ignoreOrIncludeOnly If true, ignore matching objects; if false, include only matching objects
     * @return List of objects at the specified coordinates that match the filter
     */
    public IonObject getObjectByCoordinates(int x, int y, String property, Object expectedValue,
            boolean ignoreOrIncludeOnly) {
        for (IonObject object : sortedObjects) {
            boolean included = !ignoreOrIncludeOnly == (object.getProperty(property, false).equals(expectedValue));
            if (included && object.getX() < x && object.getX() + object.getWidth() > x && object.getY() < y
                    && object.getY() + object.getHeight() > y)
                return object;
        }
        return null;
    }

    /**
     * Gets the first object found at the specified coordinates.
     *
     * @param x The x-coordinate to check
     * @param y The y-coordinate to check
     * @return The first object found at the coordinates, or null if none found
     */
    public IonObject getObjectByCoordinates(int x, int y) {
        for (IonObject object : sortedObjects) {
            if (object.getX() < x && object.getX() + object.getWidth() > x && object.getY() < y
                    && object.getY() + object.getHeight() > y)
                return object;
        }
        return null;
    }

    /**
     * Gets all objects at the specified coordinates that match the property filter.
     *
     * @param x The x-coordinate to check
     * @param y The y-coordinate to check
     * @param property The property name to filter by
     * @param ignoreOrIncludeOnly If true, ignore objects with property=true; if false, include only objects with property=true
     * @return List of matching objects at the specified coordinates
     */
    public ArrayList<IonObject> getObjectsByCoordinates(int x, int y, String property, boolean ignoreOrIncludeOnly) {
        ArrayList<IonObject> objects = new ArrayList<IonObject>();
        for (IonObject object : sortedObjects) {
            boolean included = !ignoreOrIncludeOnly == (boolean) object.getProperty(property, false);
            if (included && object.getX() < x && object.getX() + object.getWidth() > x && object.getY() < y
                    && object.getY() + object.getHeight() > y)
                objects.add(object);
        }
        return objects;
    }

    /**
     * Gets all objects at the specified coordinates.
     *
     * @param x The x-coordinate to check
     * @param y The y-coordinate to check
     * @return List of all objects at the specified coordinates
     */
    public ArrayList<IonObject> getObjectsByCoordinates(int x, int y) {
        ArrayList<IonObject> objects = new ArrayList<IonObject>();
        for (IonObject object : sortedObjects) {
            if (object.getX() < x && object.getX() + object.getWidth() > x && object.getY() < y
                    && object.getY() + object.getHeight() > y)
                objects.add(object);
        }
        return objects;
    }

    /**
     * Sets a property value for all objects in this container.
     * 
     * @param propertyName The name of the property to set
     * @param property     The value to set for the property
     * @return This container for method chaining
     */
    public IonContainer setPropertyToAllObjects(String propertyName, Object property) {
        for (IonObject object : objectsMap.values()) {
            object.setProperty(propertyName, property);
        }
        return this;
    }

}
