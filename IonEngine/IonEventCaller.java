package IonEngine;
import java.awt.event.*;

/**
 * An interface for objects that can handle events.
 * Implementing classes must define the actionPerformed method.
 */
public interface IonEventCaller {
    public void actionPerformed(ActionEvent e, String eventName);
}
