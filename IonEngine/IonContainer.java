package IonEngine;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.HashMap;
import java.util.ArrayList;

public class IonContainer {
    //General parameters
    private IonPanel thisPanel;
    private String thisPanelKey;
    private int width;
    private int height;
    private int x;
    private int y;
    private boolean widthAuto;
    private boolean heightAuto;
    private boolean visible = true;

    //Border
    private Color borderColor;
    private Color backgroundColor;
    private int borderThickness;

    //Stored objects
    private HashMap<String, IonObject> objectsMap = new HashMap<String, IonObject>();

    //Stores sorted objects by z_index
    private ArrayList<IonObject> sortedObjects = new ArrayList<IonObject>();
    
    //BehaviorPacks
    private ArrayList<IonBehaviourPack> behaviorPacks = new ArrayList<IonBehaviourPack>();



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
    public IonContainer(int width, int height) { //sets width/height to auto if 0, 0
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
    public IonContainer() {
        widthAuto = true;
        heightAuto = true;
        this.width = 500;
        this.height = 500;
        backgroundColor = Color.black;
        borderThickness = 0;
    }
    public void draw(Graphics g) {
        if (visible)
        {
            normalizeBorders();
            if (borderThickness > 0)
                drawFrame(g);
            for (int i = sortedObjects.size() - 1; i >= 0; i--)
            {
                if (sortedObjects.get(i) != null)
                    sortedObjects.get(i).draw(g);
            }
        }
    }

    public IonContainer setVisible(boolean newState) {
        visible = newState;
        repaint();
        return this;
    }
    public boolean isVisible() { return visible; }

    public IonContainer normalizeBorders() {
        if (widthAuto)
            this.width = (int)thisPanel.getSize().getWidth();
        if (heightAuto)
            this.height = (int)thisPanel.getSize().getHeight();
        return this;
    }

    public void drawFrame(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        Stroke oldStroke = g2D.getStroke();
        g2D.setStroke(new BasicStroke(borderThickness));
        g2D.setColor(backgroundColor);
        g2D.drawRect(x, y, this.width, this.height); //!fix need to add private variables for xy position in panel for correct drawing
        g2D.setStroke(oldStroke);
    }

    public void repaint() {
        thisPanel.repaint();
    }


    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public boolean isWidthAuto() {
        return this.widthAuto;
    }

    public boolean isHeightAuto() {
        return this.heightAuto;
    }
    
    public IonContainer setWidth(int width) {
        this.width = width;
        repaint();
        return this;
    }
    public IonContainer setHeight(int height) {
        this.height = height;
        repaint();
        return this;
    }
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
    public IonContainer moveTo(int x, int y) {
        this.x = x;
        this.y = y;
        repaint();
        return this;
    }
    public IonContainer moveBy(int x, int y) {
        this.x += x;
        this.y += y;
        repaint();
        return this;
    }

    public IonContainer setWidthAuto(boolean newState) {
        this.widthAuto = newState;
        repaint();
        return this;
    }
    public IonContainer setHeightAuto(boolean newState) {
        this.heightAuto = newState;
        repaint();
        return this;
    }

    public void setPanel(IonPanel panel, String key) {
        this.thisPanel = panel;
        this.thisPanelKey = key;
    }

    public String getName() {
        return this.thisPanelKey;
    }

    public IonContainer changeBorder(Color borderColor, int borderThickness) { this.borderColor = borderColor; this.borderThickness = borderThickness; repaint(); return this; }
    public IonContainer changeBorder(Color borderColor) { this.borderColor = borderColor; repaint(); return this; }
    public IonContainer changeBorder(int borderThickness) { this.borderThickness = borderThickness; repaint(); return this; }
    
    public IonObject add(IonObject object, String name) {
        object.setContainer(this, name);
        objectsMap.put(name, object);
        addToSortedObjects(object);
        return object;
    }

    public IonObject get(String name) {
        return objectsMap.get(name);
    }

    public IonObject remove(String name) {
        IonObject removed = objectsMap.get(name);
        objectsMap.remove(name);
        sortedObjects.remove(removed);
        return removed;
    }
    // !fix does not work! fix plz
    public void remove(String property, boolean removeOnlyOrIgnore) { // removes all objects with property = true or ignores them
        for (String objectName: objectsMap.keySet()) {
            if ((boolean)objectsMap.get(objectName).getProperty(property, false) == removeOnlyOrIgnore)
            {
                sortedObjects.remove(objectsMap.get(objectName));
                objectsMap.remove(objectName);
            }
        }
    } 

    public IonContainer removeAllObjects() {
        objectsMap.clear();
        sortedObjects.clear();
        return this;
    }
    
    //Sorting objects by descending z_index (max z_index is at 0)
    public void addToSortedObjects(IonObject objectToAdd) {
        int i = 0;
        while (i < sortedObjects.size() && objectToAdd.getZIndex() < sortedObjects.get(i).getZIndex())
            i++;
        sortedObjects.add(i, objectToAdd);
    }

    public void resortObjectInSortedObjects(IonObject objectToResort) {
        sortedObjects.remove(objectToResort);
        addToSortedObjects(objectToResort);
    }
    
    public HashMap<String, IonObject> getObjectMap() {
        return objectsMap;
    }

    public ArrayList<IonObject> getSortedObjects() {
        return sortedObjects;
    }

    public IonBehaviourPack addBehaviour(IonBehaviourPack pack) {
        pack.setContainer(this);
        this.behaviorPacks.add(pack);
        return pack;
    }

    public ArrayList<IonBehaviourPack> getBehaviors() {
        return this.behaviorPacks;
    }

    public void mouseEvent(MouseEvent e, String type, int x, int y) {
        for (IonBehaviourPack behaviourPack: behaviorPacks) {
            behaviourPack.mouseEvent(e, type, x, y);
        }
    }
    public void keyEvent(KeyEvent e, String type) {
        for (IonBehaviourPack behaviourPack: behaviorPacks) {
            behaviourPack.keyEvent(e, type);
        }
    }
    
    //if ignoreOrIncludeOnly is true, objects with property = expectedValue will be ignored
    //if ignoreOrIncludeOnly is false, only objects with property = expectedValue will be included
    public IonObject getObjectByCoordinates(int x, int y, String property, Object expectedValue, boolean ignoreOrIncludeOnly) {
        for (IonObject object: sortedObjects) {
            boolean included = !ignoreOrIncludeOnly == (object.getProperty(property, false).equals(expectedValue));
            if (included && object.getX() < x && object.getX() + object.getWidth() > x && object.getY() < y && object.getY() + object.getHeight() > y)
                return object;
        }
        return null;
    }
    public IonObject getObjectByCoordinates(int x, int y) {
        for (IonObject object: sortedObjects) {
            if (object.getX() < x && object.getX() + object.getWidth() > x && object.getY() < y && object.getY() + object.getHeight() > y)
                return object;
        }
        return null;
    }

    public ArrayList<IonObject> getObjectsByCoordinates(int x, int y, String property, boolean ignoreOrIncludeOnly) {
        ArrayList<IonObject> objects = new ArrayList<IonObject>();
        for (IonObject object: sortedObjects) {
            boolean included = !ignoreOrIncludeOnly == (boolean)object.getProperty(property, false);
            if (included && object.getX() < x && object.getX() + object.getWidth() > x && object.getY() < y && object.getY() + object.getHeight() > y)
                objects.add(object);
        }
        return objects;
    }
    public ArrayList<IonObject> getObjectsByCoordinates(int x, int y) {
        ArrayList<IonObject> objects = new ArrayList<IonObject>();
        for (IonObject object: sortedObjects) {
            if (object.getX() < x && object.getX() + object.getWidth() > x && object.getY() < y && object.getY() + object.getHeight() > y)
                objects.add(object);
        }
        return objects;
    }

    public IonContainer setPropertyToAllObjects(String propertyName, Object property) {
        for (IonObject object: objectsMap.values()) {
            object.setProperty(propertyName, property);
        }
        return this;
    } 

}
