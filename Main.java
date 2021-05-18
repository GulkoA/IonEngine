import java.awt.event.MouseEvent;

import IonEngine.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
    public static void main(String[] args)
    {
        IonFrame frame = new IonFrame(518, 559);
        frame.setVisible(true);
        frame.setTitle("Paint, but bad");
        
        IonPanel panel = frame.addPanel();
        IonContainer canvas = panel.addContainer(new IonContainer(500, 500, 0, 0), "canvas");
        canvas.changeBorder(1);
        IonRectangleObject brush = (IonRectangleObject)canvas.add(new IonRectangleObject(10, 10, 0, 0, 10), "brush");
        brush.setProperty("brushSize", 10);
        canvas.addBehaviour(new BrushBP(brush, canvas));
        

        frame.addMenuBar();

        String[] items = {"1", "5", "10", "15", "20", "30"};
        frame.addMenuItems(frame.addMenu("Brush size"), items, new brushSizeListener(brush));
    }
}
class brushSizeListener implements IonEventCaller{
    private IonRectangleObject brush;
    public brushSizeListener(IonRectangleObject brush) {this.brush = brush;}
    public void actionPerformed(ActionEvent e, String type) {
        try {
            brush.setProperty("brushSize", Integer.parseInt(type));
            brush.resize(Integer.parseInt(type), Integer.parseInt(type));
        } catch (Exception exc) {}
    }
}

class BrushBP extends IonBehaviourPack {
    private IonRectangleObject brush;
    private IonContainer canvas;
    private static int dots = 0;
    private int curButton = 1;
    public BrushBP(IonRectangleObject brush, IonContainer canvas) {this.brush = brush; this.canvas = canvas;}

    public void mouseEvent(MouseEvent e, String type, int x, int y) {
        switch(type) {
            case "pressed":
            curButton = e.getButton();
            break;
            
            case "moved":
                brush.moveTo(x - brush.getWidth()/2, y - brush.getHeight()/2);
                break;
            
            case "dragged":
                brush.moveTo(x - brush.getWidth()/2, y - brush.getHeight()/2);
                if (curButton == 1)
                {
                    int brushSize = (int)brush.getProperty("brushSize");
                    IonObject dot = new IonRectangleObject(brushSize, brushSize, x - brush.getWidth()/2, y - brush.getHeight()/2, 0);
                    canvas.add(dot, ("dot" + dots));
                    dot.setProperty("dot", true);
                    dots++;
                }
                else if (curButton == 3)
                {
                    IonObject object = canvas.getObjectByCoordinates(x, y, "dot", false);
                    if (object != null)
                        object.remove();
                }
                break;
            
            case "wheelMoved":
                int wheelBrushSize = ((MouseWheelEvent)e).getWheelRotation() * -2;
                brush.resizeBy(wheelBrushSize);
                brush.setProperty("brushSize", brush.getWidth());
                break;
        }
    }
}