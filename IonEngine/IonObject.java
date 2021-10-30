package IonEngine;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.HashMap;

public class IonObject {
    private IonContainer thisContainer;
    private String thisContainerKey;
    private double x = 0;
    private double y = 0;
    private int width = 100;
    private int height = 100;
    private int z_index = 0;
    private HashMap<String, Object> properties = new HashMap<String, Object>();
    
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
    public IonObject(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public IonObject() {}

    public void setX(double x) {
        this.x = x;
        sendObjectEvent("changeX");
        normalizeBorders();
        repaint();
    }

    public void setY(double y) {
        this.y = y;
        sendObjectEvent("changeY");
        normalizeBorders();
        repaint();
    }

    public void moveTo(double newX, double newY) {
        x = newX;
        y = newY;
        sendObjectEvent("movedTo");
        normalizeBorders();
        repaint();
    }

    public void moveBy(double newX, double newY) {
        x += newX;
        y += newY;
        sendObjectEvent("movedBy");
        normalizeBorders();
        repaint();
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        sendObjectEvent("resized");
        normalizeBorders();
        repaint();
    }
    public void resize(int size) {
        this.width = size;
        this.height = size;
        sendObjectEvent("resized");
        normalizeBorders();
        repaint();
    }

    public void resizeBy(int width, int height) {
        this.width += width;
        this.height += height;
        sendObjectEvent("resized");
        normalizeBorders();
        repaint();
    }
    public void resizeBy(int change) {
        this.width += change;
        this.height += change;
        sendObjectEvent("resized");
        normalizeBorders();
        repaint();
    }

    public void scale(int widthCoefficient, int heightCoefficient) {
        this.width *= widthCoefficient;
        this.height *= heightCoefficient;
        sendObjectEvent("scaled");
        normalizeBorders();
        repaint();
    }
    public void scale(int coefficient) {
        this.width *= coefficient;
        this.height *= coefficient;
        sendObjectEvent("scaled");
        normalizeBorders();
        repaint();
    }

    public void setWidth(int width) {
        this.width = width;
        sendObjectEvent("setWidth");
        normalizeBorders();
        repaint();
    }

    public void setHeight(int height) {
        this.height = height;
        sendObjectEvent("setHeight");
        normalizeBorders();
        repaint();
    }

    public void setZIndex(int zIndex) {
        this.z_index = zIndex;
        thisContainer.resortObjectInSortedObjects(this);
        repaint();
    }

    public void normalizeBorders() {
        // if (width > thisContainer.getWidth()) {
        //     width = thisContainer.getWidth();
        // }
        // if (height > thisContainer.getHeight()) {
        //     height = thisContainer.getHeight();
        // }

        if (x < 0)
            x = 0;
        else if (x + width > thisContainer.getWidth())
            x = thisContainer.getWidth() - width;
        
        if (y < 0)
            y = 0;
        else if (y + height > thisContainer.getHeight())
            y = thisContainer.getHeight() - height;
    }

    public boolean touchesBottom(double error) {
        return y + height + error >= thisContainer.getHeight();
    }
    public boolean touchesBottom() {
        return y + height == thisContainer.getHeight();
    }

    public boolean touchesTop(double error) {
        return y - error <= 0;
    }
    public boolean touchesTop() {
        return y == 0;
    }

    public boolean touchesLeft(double error) {
        return x - error <= 0;
    }
    public boolean touchesLeft() {
        return x == 0;
    }

    public boolean touchesRight(double error) {
        return x + width + error >= thisContainer.getWidth();
    }
    public boolean touchesRight() {
        return x + width == thisContainer.getWidth();
    }
    
    public double getX() {return x;}
    public double getY() {return y;}
    public double getXMiddle() {return x + width / 2;}
    public double getYMiddle() {return y + height / 2;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}
    public int getZIndex() {return z_index;}

    public void setContainer(IonContainer container, String containerKey) {
        thisContainer = container;
        thisContainerKey = containerKey;
        normalizeBorders();
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

    public Object getProperty(String propertyName, Object defaultProperty) { //default is returned if property is null
        Object toReturn = this.properties.get(propertyName);
        if (toReturn == null)
            return defaultProperty;
        return toReturn;
    }

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

    public void sendObjectEvent(String type) {
        for (IonBehaviourPack pack: thisContainer.getBehaviors()) {
            pack.objectEvent(this, type);
        }
    }

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

    public void moveToTop() {
        if (thisContainer.getSortedObjects().indexOf(this) <= 0)
            return;
        while (thisContainer.getSortedObjects().indexOf(this) > 0 && getZIndex() != thisContainer.getSortedObjects().get(0).getZIndex()) {moveUp();}
    }

    public void moveToBottom() {
        if (thisContainer.getSortedObjects().indexOf(this) < 0)
            return;
        while (thisContainer.getSortedObjects().indexOf(this) < thisContainer.getSortedObjects().size() && getZIndex() != thisContainer.getSortedObjects().get(thisContainer.getSortedObjects().size() - 1).getZIndex()) {moveDown();}
    }

    public void remove() {
        thisContainer.remove(thisContainerKey);
    }

    public String getName() { return thisContainerKey; }
}
