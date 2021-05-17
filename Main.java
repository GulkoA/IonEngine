import IonEngine.*;
import java.awt.*;

//                    ___           ___     
//        ___        /\  \         /\__\    
//       /\  \      /::\  \       /::|  |   
//       \:\  \    /:/\:\  \     /:|:|  |   
//       /::\__\  /:/  \:\  \   /:/|:|  |__ 
//    __/:/\/__/ /:/__/ \:\__\ /:/ |:| /\__\
//   /\/:/  /    \:\  \ /:/  / \/__|:|/:/  /
//   \::/__/      \:\  /:/  /      |:/:/  / 
//    \:\__\       \:\/:/  /       |::/  /  
//     \/__/        \::/  /        /:/  /   
//                   \/__/         \/__/    engine

public class Main {

    public static void main(String[] args)
    {
        IonFrame frame = new IonFrame();
        frame.setTitle("Ion");
        IonPanel panel = frame.addPanel();
        
        //Testing the first container
        IonContainer container = panel.addContainer(new IonContainer(500, 500, 10, 10), "container");
        container.changeBorder(2);

        IonRectangleObject rectangle = (IonRectangleObject)container.add(new IonRectangleObject(), "rectangle");
        rectangle.setBackgroundColor(Color.blue);
        rectangle.setZIndex(2);

        container.add(new IonRectangleObject(), "rectangle2");
        ((IonRectangleObject)container.get("rectangle2")).setBackgroundColor(Color.green);

        container.addBehaviour(new IonDraggableBP());
        container.setPropertyToAllObjects("draggable", "true");

        //Testing the second container
        IonContainer container2 = panel.addContainer();
        container2.add(new IonRectangleObject(100, 50, 0, 10), "bar").setProperty("draggable", "true");
        container2.changeBorder(3);
        container2.addBehaviour(new IonDraggableBP());

        frame.setVisible(true);

        IonRunTimeConsole RTC = new IonRunTimeConsole(frame);
        RTC.start();
        RTC.addContainerAppended("container");
        RTC.addContainerAppended("container2");


    }
    
}
