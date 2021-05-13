package GameEngine;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class IonRectangleObject extends IonObject{
    private int width, height;
    public IonRectangleObject(int width, int height, int x, int y, int z_index) {
        super(x, y, z_index);
        this.width = width;
        this.height = height;
    }
    public IonRectangleObject(int width, int height, int x, int y) {
        super(x, y);
        this.width = width;
        this.height = height;
    }
    public IonRectangleObject(int x, int y) {
        super(x, y);
        width = 100;
        height = 100;
    }
    public IonRectangleObject() {
        super();
        width = 100;
        height = 100;
    }

    public void draw(Graphics g) {
        //System.out.println(super.getX() + " " + super.getY() + " " + width + " " + height);
        g.fillRect(super.getX(), super.getY(), width, height);
    }
}
