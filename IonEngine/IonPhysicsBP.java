package IonEngine;

import java.awt.event.*;

public class IonPhysicsBP extends IonBehaviourPack {
    private boolean enabled;
    private IonContainer container;

    public IonPhysicsBP(boolean enabled) {
        this.enabled = enabled;
    }
    public IonPhysicsBP() {
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
        process();
    }
    public void off() {
        enabled = false;
    }

    public void setState(boolean state) {
        enabled = state;
        if (enabled)
            process();
    }

    private void process() {
        while (enabled) {

        }
    }

    public void mouseEvent(MouseEvent e, String type, int x, int y) {

    }

    

}
