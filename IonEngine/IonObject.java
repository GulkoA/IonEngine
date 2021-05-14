package IonEngine;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class IonObject {
    private IonContainer thisContainer;
    private String thisContainerKey;
    private int x = 0;
    private int y = 0;
    private int z_index = 0;
    
    public IonObject(int x, int y, int z_index) {
        this.x = x;
        this.y = y;
        this.z_index = z_index;
    }
    public IonObject(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public IonObject() {
        
    }

    public void moveTo(int newX, int newY) {
        x = newX;
        y = newY;
        thisContainer.repaint();
    }

    public void moveBy(int newX, int newY) {
        x += newX;
        y += newY;
        thisContainer.repaint();
    }

    public int getX() {return x;}
    public int getY() {return y;}

    public void setContainer(IonContainer container, String containerKey) {
        thisContainer = container;
        thisContainerKey = containerKey;
    }

    public IonContainer getContainer() {
        return thisContainer;
    }

    public void draw(Graphics g) {}
}
