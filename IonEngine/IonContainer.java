package IonEngine;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.HashMap;
import java.util.ArrayList;

public class IonContainer {
    //General parameters
    IonPanel thisPanel;
    String thisPanelKey;
    int width;
    int height;
    int x;
    int y;
    boolean widthAuto;
    boolean heightAuto;

    //Border
    Color borderColor;
    Color backgroundColor;
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
    public IonContainer(int width, int height) {
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
        this.width = 0;
        this.height = 0;
        backgroundColor = Color.black;
        borderThickness = 0;
    }
    public void draw(Graphics g) {
        if (widthAuto)
            this.width = (int)thisPanel.getSize().getWidth();
        if (heightAuto)
            this.height = (int)thisPanel.getSize().getHeight();
        
        Graphics2D g2D = (Graphics2D) g;
        Stroke oldStroke = g2D.getStroke();
        g2D.setStroke(new BasicStroke(borderThickness));
        g2D.setColor(backgroundColor);
        g2D.drawRect(x, y, this.width, this.height); //!fix need to add private variables for xy position in panel for correct drawing
        g2D.setStroke(oldStroke);
        
        for (int i = sortedObjects.size() - 1; i >= 0; i--)
        {
            sortedObjects.get(i).draw(g);
        }
        //System.out.println(width + " " + height);
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
    
    public void setWidth(int width) {
        this.width = width;
        repaint();
    }
    public void setHeight(int height) {
        this.height = height;
        repaint();
    }
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
    }


    public void setX(int x) {
        this.x = x;
        repaint();
    }
    public void setY(int y) {
        this.y = y;
        repaint();
    }
    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
        repaint();
    }
    public void moveBy(int x, int y) {
        this.x += x;
        this.y += y;
        repaint();
    }

    public void setWidthAuto(boolean newState) {
        this.widthAuto = newState;
        repaint();
    }
    public void setHeightAuto(boolean newState) {
        this.heightAuto = newState;
        repaint();
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
        objectsMap.put(name, object);
        addToSortedObjects(object);
        object.setContainer(this, name);
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
        this.behaviorPacks.add(pack);
        pack.setContainer(this);
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

    public IonObject getObjectByCoordinates(int x, int y) {
        for (IonObject object: sortedObjects) {
            if (object.getX() < x && object.getX() + object.getWidth() > x && object.getY() < y && object.getY() + object.getHeight() > y)
                return object;
        }
        return null;
    }

    public Object setPropertyToAllObjects(String propertyName, Object property) {
        for (IonObject object: objectsMap.values()) {
            object.setProperty(propertyName, property);
        }
        return property;
    } 

}
