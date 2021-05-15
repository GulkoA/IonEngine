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
        
        
        IonContainer container = panel.addContainer(new IonContainer(500, 500, 10, 10), "container");
        container.changeBorder(2);
        IonRectangleObject rectangle = (IonRectangleObject)container.add(new IonRectangleObject(), "rectangle");
        rectangle.setBackgroundColor(Color.blue);
        rectangle.setZIndex(-1);
        container.add(new IonRectangleObject(), "rectangle2");
        ((IonRectangleObject)container.get("rectangle2")).setBackgroundColor(Color.green);
        container.addBehaviour(new IonDraggableBP());

        //Testing the second container
        IonContainer container2 = panel.addContainer();
        container2.add(new IonRectangleObject(100, 50, 0, 10), "bar");
        container2.get("bar").moveBy(100, 10);
        container2.changeBorder(3);
        frame.setVisible(true);

        IonRunTimeConsole RTC = new IonRunTimeConsole(frame);
        RTC.start();
        RTC.addContainerAppended("container");
        RTC.addContainerAppended("container2");


    }
    
}
