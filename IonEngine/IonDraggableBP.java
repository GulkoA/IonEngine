package IonEngine;

import java.awt.event.*;

public class IonDraggableBP extends IonBehaviourPack {
    public boolean enabled;
    public IonContainer container;

    public IonDraggableBP(boolean enabled) {
        this.enabled = enabled;
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
    private int pickX = 0;
    private int pickY = 0;

    //settings
    private boolean moveToFront = true;

    public void mouseEvent(MouseEvent e, String type, int x, int y) {
        if (enabled)
        switch(type) {
            case "moved":
            return;
            case "pressed":
                chosenObject = container.getObjectByCoordinates(x, y);
                if (chosenObject != null && chosenObject.getProperty("draggable") != null && chosenObject.getProperty("draggable").equals("true")) {
                    dragging = true;
                    pickX = x - chosenObject.getX();
                    pickY = y - chosenObject.getY();
                }
                return;
            case "released":
                dragging = false;
                return;
            case "clicked":
                return;
            case "dragged":
                if (dragging)
                    chosenObject.moveTo(x - pickX, y - pickY);

                //if (moveToFront)
                    //container.moveToTop(chosenObject);
                return;
            case "entered":
                return;
            case "exited":
                return;
            }
    }
}
