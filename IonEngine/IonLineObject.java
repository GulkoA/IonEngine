package IonEngine;
import java.awt.Color;
import java.awt.*;

public class IonLineObject extends IonObject {
    private double X2;
    private double Y2;
    private Color lineColor = Color.black;

    public IonLineObject(double x, double y, double X2, double Y2, int z_index) {
        super((int)Math.abs(X2 - x), (int)Math.abs(Y2 - y), x, y);
        this.X2 = X2;
        this.Y2 = Y2;
        super.setZIndex(z_index);
    }
    public IonLineObject(double x, double y, double X2, double Y2) {
        super((int)Math.abs(X2 - x), (int)Math.abs(Y2 - y), x, y);
        this.X2 = X2;
        this.Y2 = Y2;
    }
    public IonLineObject(double x, double y, double angleInRadians) {
        super(0, 0, x, y);
        this.X2 = Math.cos(angleInRadians) * 100 + x;
        this.Y2 = Math.sin(angleInRadians) * 100 + y;
        // System.out.println(X2 + " " + Y2 + " " + angleInRadians);
        // super.setWidth((int)Math.abs(X2 - x)); //!fix add correct width and height setting here
        // super.setHeight((int)Math.abs(Y2 - y));
    }
    public IonLineObject(double x, double y) {
        super(0, 0, x, y);
        this.X2 = x;
        this.Y2 = x;
    }
    public IonLineObject() {
        super(10, 10, 0, 0);
        this.X2 = 10;
        this.Y2 = 10;
    }

    public double getX2() {
        return this.X2;
    }

    public void setX2(double X2) {
        this.X2 = X2;
    }

    public double getY2() {
        return this.Y2;
    }

    public void setY2(double Y2) {
        this.Y2 = Y2;
    }

    public void moveTo(double newX, double newY, double newX2, double newY2) {
        this.X2 = newX2;
        this.Y2 = newY2;
        super.setX(newX);
        super.setY(newY);
    }

    public void moveTo(double newX, double newY) {
        this.X2 = X2 - super.getX() + newX;
        this.Y2 = Y2 - super.getY() + newY;
        super.setX(newX);
        super.setY(newY);
    }

    public void moveBy(double moveX, double moveY) {
        this.X2 += moveX;
        this.Y2 += moveY;
        super.setX(super.getX() + moveX);
        super.setY(super.getX() + moveY);
    }

    public IonLineObject setLineColor(Color lineColor) {
        this.lineColor = lineColor;
        return this;
    }

    public void draw(Graphics g) {
        int drawX = (int)(super.getContainer().getX() + super.getX());
        int drawY = (int)(super.getContainer().getY() + super.getY());
        int drawX2 = (int)(super.getContainer().getX() + X2);
        int drawY2 = (int)(super.getContainer().getY() + Y2);

        Color oldColor = g.getColor();
        g.setColor(lineColor);
        g.drawLine(drawX, drawY, drawX2, drawY2);
        g.setColor(oldColor);
    }
}