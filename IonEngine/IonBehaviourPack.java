package IonEngine;

import java.awt.event.*;

/**
 * Base class for behavior packs that can be attached to IonObjects to add functionality.
 * Behavior packs handle mouse events, keyboard events, and object-specific events.
 * This class runs on its own thread to manage behavior updates.
 */
public class IonBehaviourPack extends Thread {
    /** The container this behavior pack belongs to */
    private IonContainer container;

    /**
     * Activates the behavior pack.
     * Override this method to define behavior when the pack is turned on.
     */
    public void on() {}

    /**
     * Deactivates the behavior pack.
     * Override this method to define behavior when the pack is turned off.
     */
    public void off() {}

    /**
     * Sets the active state of the behavior pack.
     * @param state true to activate, false to deactivate
     */
    public void setState(boolean state) {
        if (state) {
            on();
        } else {
            off();
        }
    }

    /**
     * Sets the container for this behavior pack.
     * @param container the IonContainer this behavior pack belongs to
     */
    public void setContainer(IonContainer container) {
        this.container = container;
    }

    /**
     * Gets the container this behavior pack belongs to.
     * @return the IonContainer instance
     */
    public IonContainer getContainer() {
        return container;
    }

    /**
     * Handles mouse events for the behavior pack.
     * Override this method to implement custom mouse event handling.
     * @param e the MouseEvent object
     * @param type the type of mouse event (moved, pressed, released, etc.)
     * @param x the x-coordinate of the mouse event
     * @param y the y-coordinate of the mouse event
     */
    public void mouseEvent(MouseEvent e, String type, int x, int y) {
        switch(type) {
            case "moved":
            case "pressed":
            case "released":
            case "clicked":
            case "dragged":
            case "entered":
            case "exited":
            case "wheelMoved":
                break;
        }
    }

    /**
     * Handles keyboard events for the behavior pack.
     * Override this method to implement custom keyboard event handling.
     * @param e the KeyEvent object
     * @param type the type of key event (typed, pressed, released)
     */
    public void keyEvent(KeyEvent e, String type) {
        switch(type) {
            case "typed":
            case "pressed":
            case "released":
                break;
        }
    }

    /**
     * Handles object-specific events for the behavior pack.
     * Override this method to implement custom object event handling.
     * @param object the IonObject that triggered the event
     * @param type the type of object event
     */
    public void objectEvent(IonObject object, String type) {
        switch(type) {
            case "movedTo":
            case "movedBy":
            case "chngedX":    // Note: there's a typo in "chngedX"
            case "changedY":
            case "resized":
            case "scaled":
            case "changedWidth":
            case "changedHeight":
            case "changedBackgroundColor":
            case "grabbed":    // for Draggable BP
            case "released":
                break;
        }
    }
}
