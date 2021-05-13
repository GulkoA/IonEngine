package GameEngine;
import java.util.ArrayList;

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
        IonContainer container = panel.addContainer(new IonContainer(500, 500), "square");
        IonRectangleObject rectangle = (IonRectangleObject)container.add(new IonRectangleObject());
        
        container.changeBorder(5);
        rectangle.moveTo(100, 100);
        frame.setVisible(true);

        IonRunTimeConsole RTC = new IonRunTimeConsole(frame);
        RTC.start();
    }
    
}
