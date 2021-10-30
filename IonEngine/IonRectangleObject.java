package IonEngine;
import java.awt.*;
import java.awt.Font;
import java.awt.geom.AffineTransform;

public class IonRectangleObject extends IonObject{
    private double rotation = 0;
    private double rotationCenterX = 0;
    private double rotationCenterY = 0;
    private Color backgroundColor = new Color(20, 20, 22);
    private Color borderColor = Color.black;
    private int borderSize = 0;
    private String text = "";
    private Color textColor = Color.white;
    private Font textFont = new Font("Arial", Font.PLAIN, 20);

    public IonRectangleObject(int width, int height, double x, double y, int z_index) {
        super(width, height, x, y, z_index);
    }
    public IonRectangleObject(int width, int height, double x, double y) {
        super(width, height, x, y);
    }
    public IonRectangleObject(int x, int y) {
        super(x, y);
    }
    public IonRectangleObject() {
        super();
    }
    
    public IonRectangleObject setBackgroundColor(Color newColor) {
        backgroundColor = newColor;
        super.sendObjectEvent("changedBackgroundColor");
        super.repaint();
        return this;
    }

    public void setRotationCenter(double x, double y) {
        rotationCenterX = x;
        rotationCenterY = y;
    }

    public void setBorderSize(int borderSize) {this.borderSize = borderSize;}
    public void setRotation(double newRotation) {this.rotation = newRotation; super.repaint();}
    public void rotateBy(double rotation) {this.rotation += rotation; super.repaint();}

    public IonRectangleObject setText(String text) { this.text = text; return this; }
    public String getText() { return text; }
    //!add setFont method
    public void draw(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
    
        AffineTransform oldTransform = g2D.getTransform();
        
        if (rotation != 0)
            g2D.rotate(rotation, super.getX() + super.getWidth() / 2 + rotationCenterX, super.getY() + super.getHeight() / 2 + rotationCenterY);

        Color oldColor = g2D.getColor();
        g2D.setColor(backgroundColor);
        int x = (int)(super.getContainer().getX() + super.getX());
        int y = (int)(super.getContainer().getY() + super.getY());
        g2D.fillRect(x, y, super.getWidth(), super.getHeight());
        g2D.setColor(oldColor);
        
        if (borderSize > 0)
        {
            Stroke oldStroke = g2D.getStroke();
            g2D.setStroke(new BasicStroke(borderSize));
            g2D.setColor(borderColor);
            g2D.drawRect(x, y, super.getWidth(), super.getHeight()); //!fix need to add private variables for xy position in panel for correct drawing
            g2D.setStroke(oldStroke);
        }

        if (text != "")
        {
            g2D.setColor(textColor);
            g2D.setFont(textFont);
            g2D.drawString(text, x + (int)(super.getWidth() / 2 - text.length() * 4.5), y + super.getHeight() / 2 + 5);
        }

        g2D.setTransform(oldTransform);
    }
}
