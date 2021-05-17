package IonEngine;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class IonRectangleObject extends IonObject{
    private Color backgroundColor = Color.black;

    public IonRectangleObject(int width, int height, int x, int y, int z_index) {
        super(width, height, x, y, z_index);
    }
    public IonRectangleObject(int width, int height, int x, int y) {
        super(width, height, x, y);
    }
    public IonRectangleObject(int x, int y) {
        super(x, y);
    }
    public IonRectangleObject() {
        super();
    }
    
    public void setBackgroundColor(Color newColor) {
        backgroundColor = newColor;
        super.objectEvent("changedBackgroundColor");
        super.repaint();
    }

    public void draw(Graphics g) {
        //System.out.println(super.getX() + " " + super.getY() + " " + width + " " + height);
        Color oldColor = g.getColor();
        g.setColor(backgroundColor);
        int x = super.getContainer().getX() + super.getX();
        int y = super.getContainer().getY() + super.getY();
        g.fillRect(x, y, super.getWidth(), super.getHeight());
        g.setColor(oldColor);
    }
}
