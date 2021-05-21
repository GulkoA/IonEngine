import IonEngine.*;
import java.awt.*;

//IonEngine 0.5 beta
//GravityBoxes demo

public class Main {
    public static void main(String[] args) {
        IonFrame frame = new IonFrame();
        IonPanel panel = frame.addPanel();
        IonContainer container = panel.addContainer();
        container.changeBorder(2);
        
        ((IonRectangleObject)container.add(new IonRectangleObject(100, 100, 1, 1, 0), "Bob")).setBackgroundColor(Color.blue);
        ((IonRectangleObject)container.add(new IonRectangleObject(100, 100, 101, 1, 1), "George")).setBackgroundColor(Color.green);
        ((IonRectangleObject)container.add(new IonRectangleObject(100, 100, 202, 1, 2), "Robin")).setBackgroundColor(Color.red);
        frame.setVisible(true);
        
        container.get("George").setProperty("free", true);
        container.get("George").setProperty("mass", 2.0);

        container.get("Bob").setProperty("free", true);
        container.get("Bob").setProperty("mass", 1.0);

        container.get("Robin").setProperty("free", true);
        container.get("Robin").setProperty("mass", 0.5);

        container.setPropertyToAllObjects("draggable", true);


        container.addBehaviour(new IonDraggableBP());
        container.addBehaviour(new TracerBP(panel.addContainer()));
        container.addBehaviour(new IonPhysicsBP()).on();

        IonRunTimeConsole rtc = new IonRunTimeConsole(frame);
        rtc.addPanelAppended("panel");
        rtc.addContainerAppended("container");
        rtc.start();

        //container.setPropertyToAllObjects("draggable", true);
        //container.setPropertyToAllObjects("free", true);

    }
}

class TracerBP extends IonBehaviourPack {
    private int i = 0;
    private IonContainer canvas;
    public TracerBP(IonContainer canvas) {this.canvas = canvas;}
    public void objectEvent(IonObject object, String type) {
        if (type == "movedBy")
        {
            IonRectangleObject dot = (IonRectangleObject)canvas.add(new IonRectangleObject(5, 5, object.getXMiddle(), object.getYMiddle()), ("dot" + i));
            int r = (int)(double)object.getProperty("velocityY", 0.0);
            if (r < 0) r = -r;
            if (r > 255) r = 255;
            int g = (int)(double)object.getProperty("velocityX", 0.0);
            if (g < 0) g = -g;
            if (g > 255) g = 255;
            int b = 255 - Math.abs((r+g)/2);
            if (b > 255) b = 255;
            try {
            dot.setBackgroundColor(new Color(r, g, b));
            } catch (Exception e) {
                System.out.println("Color error! r " + r + " g " + g + " b " + b);
            }
            i++;
        }
        else if (type == "released")
        {
            canvas.removeAllObjects();
        }
    }
}
