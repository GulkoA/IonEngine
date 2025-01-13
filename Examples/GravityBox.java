package Examples;

import IonEngine.*;
import java.awt.*;

/**
 * GravityBox Demo - Demonstrates physics simulation with draggable objects
 * that leave color trails based on their velocity.
 * 
 * Features:
 * - Three interactive boxes with different masses
 * - Gravity physics simulation
 * - Velocity-based color trails
 * - Runtime console for debugging
 */
public class GravityBox {
    public static void main(String[] args) {
        // Set up the main window and container
        IonFrame frame = new IonFrame();
        IonPanel panel = frame.addPanel();
        IonContainer container = panel.addContainer();
        container.changeBorder(2);
        
        ((IonRectangleObject)container.add(new IonRectangleObject(100, 100, 1, 1, 0), "Bob")).setBackgroundColor(Color.blue);
        ((IonRectangleObject)container.add(new IonRectangleObject(100, 100, 101, 1, 1), "George")).setBackgroundColor(Color.green);
        ((IonRectangleObject)container.add(new IonRectangleObject(100, 100, 202, 1, 2), "Robin")).setBackgroundColor(Color.red);
        frame.setVisible(true);
        
        container.get("George").setProperty("mass", 2.0);
        container.get("Bob").setProperty("mass", 1.0);
        container.get("Robin").setProperty("mass", 0.5);
        container.setPropertyToAllObjects("draggable", true);
        container.setPropertyToAllObjects("free", true);

        // Add behavior packs for interaction and visualization
        container.addBehaviour(new IonDraggableBP());
        container.addBehaviour(new TracerBP(panel.addContainer()));
        container.addBehaviour(new IonPhysicsBP()).start();

        // Set up runtime console for debugging
        setupRuntimeConsole(frame);
    }

    /**
     * Helper method to set up the runtime console
     */
    private static void setupRuntimeConsole(IonFrame frame) {
        IonRunTimeConsole rtc = new IonRunTimeConsole(frame);
        rtc.addPanelAppended("panel");
        rtc.addContainerAppended("container");
        rtc.start();
    }
}

/**
 * TracerBP - Behavior Pack that creates colored dots behind moving objects
 * The dot colors are determined by the object's velocity:
 * - Red component: based on vertical velocity
 * - Green component: based on horizontal velocity
 * - Blue component: inverse of average velocity
 */
class TracerBP extends IonBehaviourPack {
    private int i = 0;
    private IonContainer canvas;
    
    public TracerBP(IonContainer canvas) {
        this.canvas = canvas;
    }
    
    @Override
    public void objectEvent(IonObject object, String type) {
        switch (type) {
            case "movedBy":
                // Create a dot at the object's current position
                IonRectangleObject dot = (IonRectangleObject)canvas.add(new IonRectangleObject(5, 5, object.getXMiddle(), object.getYMiddle()), ("dot" + i));
                // Calculate color components based on velocity
                int r = (int)(double)object.getProperty("velocityY", 0.0);
                if (r < 0) r = -r;
                if (r > 255) r = 255;
                int g = (int)(double)object.getProperty("velocityX", 0.0);
                if (g < 0) g = -g;
                if (g > 255) g = 255;
                int b = 255 - (r+g)/2;
                if (b > 255) b = 255;
                try {
                    // Set the dot's background color
                    dot.setBackgroundColor(new Color(r, g, b));
                } catch (Exception e) {
                    // Handle color error if the color is not valid
                    System.out.println("Color error! r " + r + " g " + g + " b " + b);
                }
                i++;
                return;

            case "released":
                // Remove all dots when the object is released
                canvas.removeAllObjects();
                return;
        }
    }
}
