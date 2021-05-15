package IonEngine;

import java.awt.event.*;

public class IonDraggableBP implements IonBehaviourPack {
    public boolean enabled;
    public IonContainer container;

    public IonDraggableBP(boolean enabled) {
        this.enabled = enabled;
    }
    public IonDraggableBP() {
        this.enabled = false;
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

    private IonObject chosenObject;
    private boolean dragging = false;
    private int pickX = 0;
    private int pickY = 0;

    public void mouseEvent(MouseEvent e, String type, int x, int y) {
        switch(type) {
            case "moved":
            
            return;
            case "pressed":
                chosenObject = container.getObjectByCoordinates(x, y);
                if (chosenObject != null) {
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
                return;
            case "entered":
                return;
            case "exited":
                return;
            }
    }
}
