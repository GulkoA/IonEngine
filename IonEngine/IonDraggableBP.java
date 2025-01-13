package IonEngine;

import java.awt.event.*;

/**
 * A behavior pack that enables dragging functionality for IonObjects.
 * Objects with this behavior can be clicked and dragged with the mouse.
 */
public class IonDraggableBP extends IonBehaviourPack {
    public boolean enabled;
    public IonContainer container;

    //settings
    private boolean moveToFront = true;

    /**
     * Creates a new draggable behavior pack with specified settings.
     * @param enabled Whether the dragging behavior is initially enabled
     * @param moveToFront Whether dragged objects should move to the front of the container
     */
    public IonDraggableBP(boolean enabled, boolean moveToFront) {
        this.enabled = enabled;
        this.moveToFront = moveToFront;
    }

    /**
     * Creates a new draggable behavior pack with default settings.
     * Dragging is enabled by default.
     */
    public IonDraggableBP() {
        this.enabled = true;
    }

    /**
     * Sets the container that this behavior pack operates on.
     * @param container The IonContainer to associate with this behavior pack
     */
    public void setContainer(IonContainer container) {
        this.container = container;
    }

    /**
     * Gets the container associated with this behavior pack.
     * @return The IonContainer this behavior pack operates on
     */
    public IonContainer getContainer() {
        return container;
    }

    /**
     * Enables the dragging behavior.
     */
    public void on() {
        enabled = true;
    }

    /**
     * Disables the dragging behavior.
     */
    public void off() {
        enabled = false;
    }

    /**
     * Sets whether the dragging behavior is enabled or disabled.
     * @param state true to enable, false to disable
     */
    public void setState(boolean state) {
        enabled = state;
    }

    //some vars for dragging
    private IonObject chosenObject;
    private boolean dragging = false;
    private boolean wasFree = false;
    private double pickX = 0;
    private double pickY = 0;

    /**
     * Handles mouse events for dragging functionality.
     * Processes mouse press, release, drag, and other mouse events to implement
     * dragging behavior for objects marked as "draggable".
     *
     * @param e The MouseEvent that triggered this call
     * @param type The type of mouse event ("moved", "pressed", "released", etc.)
     * @param x The x-coordinate of the mouse event
     * @param y The y-coordinate of the mouse event
     */
    public void mouseEvent(MouseEvent e, String type, int x, int y) {
        if (enabled)
        switch(type) {
            case "moved":
            return;
            case "pressed":
                chosenObject = container.getObjectByCoordinates(x, y, "draggable", true, false);
                if (chosenObject != null) {
                    dragging = true;
                    pickX = x - chosenObject.getX();
                    pickY = y - chosenObject.getY();
                    wasFree = (boolean)chosenObject.getProperty("free", false);
                    chosenObject.setProperty("free", false);
                    chosenObject.sendObjectEvent("grabbed");
                }
                return;
            case "released":
                if (dragging)
                {
                    dragging = false;
                    if (wasFree)
                        chosenObject.setProperty("free", true);
                    chosenObject.sendObjectEvent("released");
                }
                return;
            case "clicked":
                return;
            case "dragged":
                if (dragging)
                {
                    chosenObject.moveTo(x - pickX, y - pickY);
                    if (moveToFront)
                        chosenObject.moveToTop();
                }
                return;
            case "entered":
                return;
            case "exited":
                return;
            }
    }
}
