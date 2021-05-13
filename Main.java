package GameEngine;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args)
    {
        IonFrame frame = new IonFrame();
        frame.setTitle("Ion");
        IonPanel panel = frame.addPanel();
        IonContainer container = panel.addContainer();
        IonRectangleObject rectangle = (IonRectangleObject)container.add(new IonRectangleObject());
        
        rectangle.moveTo(10, 10);
        frame.setVisible(true);
    }
    
}
