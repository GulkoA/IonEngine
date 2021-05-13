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
    private int borderThickness;

    //Stored objects
    private ArrayList<IonObject> objectList = new ArrayList<IonObject>();

    public IonContainer(int width, int height) {
        this.width = width;
        this.height = height;
        width_auto = width <= 0;
        height_auto = height <= 0;
        borderColor = Color.black;
        backgroundColor = Color.black;
        borderThickness = 0;
    }
    public IonContainer() {
        width_auto = true;
        height_auto = true;
        this.width = 0;
        this.height = 0;
        backgroundColor = Color.black;
        borderThickness = 0;
    }
    public void draw(Graphics g) {
        if (width_auto)
            this.width = (int)thisPanel.getSize().getWidth();
        if (height_auto)
            this.height = (int)thisPanel.getSize().getHeight();
        
        Graphics2D g2D = (Graphics2D) g;
        Stroke oldStroke = g2D.getStroke();
        g2D.setStroke(new BasicStroke(borderThickness));
        g2D.setColor(backgroundColor);
        g2D.drawRect(0, 0, this.width, this.height); //!fix need to add private variables for xy position in panel for correct drawing
        g2D.setStroke(oldStroke);
        for (IonObject object: objectList)
        {
            if (object != null)
                object.draw(g);
        }
        //System.out.println(width + " " + height);
    }

    public void repaint() {
        thisPanel.repaint();
    }

    public void setPanel(IonPanel panel, int ID) {
        this.thisPanel = panel;
        this.thisPanelID = ID;
    }

    public IonContainer changeBorder(Color borderColor, int borderThickness) { this.borderColor = borderColor; this.borderThickness = borderThickness; return this; }
    public IonContainer changeBorder(Color borderColor) { this.borderColor = borderColor; return this; }
    public IonContainer changeBorder(int borderThickness) { this.borderThickness = borderThickness; return this; }
    
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
