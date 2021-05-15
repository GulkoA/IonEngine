package IonEngine;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.HashMap;

public class IonObject {
    private IonContainer thisContainer;
    private String thisContainerKey;
    private int x = 0;
    private int y = 0;
    private int width = 100;
    private int height = 100;
    private int z_index = 0;
    private HashMap<String, Object> properties = new HashMap<String, Object>();
    
    public IonObject(int width, int height, int x, int y, int z_index) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.z_index = z_index;
    }
    public IonObject(int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }
    public IonObject(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public IonObject() {}

    public void setX(int x) {
        this.x = x;
        normalizeBorders();
        repaint();
    }

    public void setY(int y) {
        this.y = y;
        normalizeBorders();
        repaint();
    }

    public void moveTo(int newX, int newY) {
        x = newX;
        y = newY;
        normalizeBorders();
        repaint();
    }

    public void moveBy(int newX, int newY) {
        x += newX;
        y += newY;
        normalizeBorders();
        repaint();
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        normalizeBorders();
        repaint();
    }

    public void resizeBy(int width, int height) {
        this.width += width;
        this.height += height;
        normalizeBorders();
        repaint();
    }

    public void setWidth(int width) {
        this.width = width;
        normalizeBorders();
        repaint();
    }

    public void setHeight(int height) {
        this.height = height;
        normalizeBorders();
        repaint();
    }

    public void setZIndex(int zIndex) {
        this.z_index = zIndex;
        thisContainer.resortObjectInSortedObjects(this);
    }

    public void normalizeBorders() {
        if (width > thisContainer.getWidth()) {
            width = thisContainer.getWidth();
        }
        if (height > thisContainer.getHeight()) {
            width = thisContainer.getWidth();
        }

        if (x < 0)
            x = 0;
        else if (x + width > thisContainer.getWidth())
            x = thisContainer.getWidth() - width;
        
        if (y < 0)
            y = 0;
        else if (y + height > thisContainer.getHeight())
            y = thisContainer.getHeight() - height;
    }

    public int getX() {return x;}
    public int getY() {return y;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}
    public int getZIndex() {return z_index;}

    public void setContainer(IonContainer container, String containerKey) {
        thisContainer = container;
        thisContainerKey = containerKey;
    }

    public IonContainer getContainer() {
        return thisContainer;
    }

    public void draw(Graphics g) {}

    public void repaint() {
        thisContainer.repaint();
    }

    public Object addProperty(String propertyName, Object property) {
        this.properties.put(propertyName, property);
        return property;
    }

    public Object getProperty(String propertyName) {
        return this.properties.get(propertyName);
    }

    public Object setProperty(String propertyName, Object property) {
        this.properties.replace(propertyName, property);
        return property;
    }
}
