package IonEngine;

import java.awt.event.*;

public class IonBehaviourPack {
    private IonContainer container;
    public void on() {}
    public void off() {}
    public void setState(boolean state) {}
    public void setContainer(IonContainer container) {this.container = container;}
    public IonContainer getContainer() {return container;}
    public void mouseEvent(MouseEvent e, String type, int x, int y) {
        switch(type) {
            case "moved":
            return;
            case "pressed":
                return;
            case "released":
                return;
            case "clicked":
                return;
            case "dragged":
                return;
            case "entered":
                return;
            case "exited":
                return;
            case "wheelMoved":
                return;
        }
    }
    public void objectEvent(IonObject object, String type) {
        switch(type) {
            case "movedTo":
                return;
            case "movedBy":
                return;
            case "chngedX":
                return;
            case "changedY":
                return;
            case "resized":
                return;
            case "scaled":
                return;
            case "changedWidth":
                return;
            case "changedHeight":
                return;
            case "changedBackgroundColor":
                return;
        }
    }

}
