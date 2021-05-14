import IonEngine.*;

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
        IonRectangleObject rectangle = (IonRectangleObject)container.add(new IonRectangleObject(), "rectangle");

        
        IonContainer container2 = panel.addContainer();
        container2.add(new IonRectangleObject(100, 50, 0, 10), "bar");
        container2.get("bar").moveBy(100, 10);
        container.changeBorder(2);
        container2.changeBorder(3);
        rectangle.moveTo(10000, 10000);
        frame.setVisible(true);

        IonRunTimeConsole RTC = new IonRunTimeConsole(frame);
        RTC.start();
        RTC.addContainerAppended("container");
        RTC.addContainerAppended("container2");


    }
    
}
