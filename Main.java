import IonEngine.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        IonFrame frame = new IonFrame();
        IonPanel panel = frame.addPanel();
        IonContainer container = panel.addContainer();
        container.changeBorder(2);
        
        ((IonRectangleObject)container.add(new IonRectangleObject(100, 100, 1, 1, 0), "Bob")).setBackgroundColor(Color.blue);
        ((IonRectangleObject)container.add(new IonRectangleObject(100, 100, 100, 1, 1), "George")).setBackgroundColor(Color.green);
        frame.setVisible(true);
        
        container.get("George").setProperty("free", true);
        container.setPropertyToAllObjects("draggable", true);

        container.addBehaviour(new IonDraggableBP());
        container.addBehaviour(new IonPhysicsBP()).on();

        IonRunTimeConsole rtc = new IonRunTimeConsole(frame);
        rtc.addPanelAppended("panel");
        rtc.addContainerAppended("container");
        rtc.start();

        //container.setPropertyToAllObjects("draggable", true);
        //container.setPropertyToAllObjects("free", true);

    }
}
