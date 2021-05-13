package GameEngine;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;

public class IonContainer {
    //General parameters
    IonPanel thisPanel;
    int thisPanelID;
    int width;
    int height;
    boolean width_auto;
    boolean height_auto;

    //Border
    Color borderColor;
    Color backgroundColor;
    double borderThickness;

    //Stored objects
    private ArrayList<IonObject> objectList = new ArrayList<IonObject>();

    public IonContainer(int width, int height) {
        this.width = width;
        this.height = height;
        width_auto = width <= 0;
        height_auto = height <= 0;
        borderColor = Color.black;
        backgroundColor = Color.white;
        borderThickness = 0;
    }
    public IonContainer() {
        width_auto = true;
        height_auto = true;
        this.width = 0;
        this.height = 0;

    }
    public void draw(Graphics g) {
        if (width_auto)
            this.width = (int)thisPanel.getSize().getWidth();
        if (height_auto)
            this.height = (int)thisPanel.getSize().getHeight();
        
        for (IonObject object: objectList)
        {
            if (object != null)
                object.draw(g);
        }
        //System.out.println(width + " " + height);
    }

    public void setPanel(IonPanel panel, int ID) {
        this.thisPanel = panel;
        this.thisPanelID = ID;
    }

    public IonContainer changeBorder(Color borderColor, double borderThickness) { this.borderColor = borderColor; this.borderThickness = borderThickness; return this; }
    public IonContainer changeBorder(Color borderColor) { this.borderColor = borderColor; return this; }
    public IonContainer changeBorder(double borderThickness) { this.borderThickness = borderThickness; return this; }
    
    public IonObject add(IonObject object) {
        objectList.add(object);
        object.setContainer(this, objectList.size() - 1);
        return object;
    }
    public IonObject removeObject(int ID) {
        IonObject removed = objectList.get(ID);
        objectList.set(ID, null);
        return removed;
    }
}
