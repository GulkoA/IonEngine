package GameEngine;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args)
    {
        IonFrame frame = new IonFrame();
        frame.setTitle("Ion");
        IonPanel panel = frame.addPanel();
        IonContainer container = panel.addContainer(new IonContainer(500, 500));
        IonRectangleObject rectangle = (IonRectangleObject)container.add(new IonRectangleObject());
        
        container.changeBorder(10);
        rectangle.moveTo(100, 100);
        frame.setVisible(true);
    }
    
}
