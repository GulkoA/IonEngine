package IonEngine;

import java.awt.event.*;

public class IonDraggableBP extends IonBehaviourPack {
    public boolean enabled;
    public IonContainer container;

    //settings
    private boolean moveToFront = true;

    public IonDraggableBP(boolean enabled, boolean moveToFront) {
        this.enabled = enabled;
        this.moveToFront = moveToFront;
    }
    public IonDraggableBP() {
        this.enabled = true;
    }

    public void setContainer(IonContainer container) {
        this.container = container;
    }

    public IonContainer getContainer() {
        return container;
    }

    public void on() {
        enabled = true;
    }

    public void off() {
        enabled = false;
    }

    public void setState(boolean state) {
        enabled = state;
    }

    //some vars for dragging
    private IonObject chosenObject;
    private boolean dragging = false;
    private boolean wasFree = false;
    private double pickX = 0;
    private double pickY = 0;

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
