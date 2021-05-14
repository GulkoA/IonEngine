package IonEngine;

public class IonPhysicsBP implements IonBehaviourPack {
    private IonContainer container;
    private boolean enabled;

    public IonPhysicsBP( IonContainer container, boolean enabled) {
        container = container;
        this.enabled = enabled;
    }
    public IonPhysicsBP(IonContainer newContainer) {
        container = newContainer;
        enabled = false;
    }

    public void enable() {
        enabled = true;
        process();
    }
    public void disable() {
        enabled = false;
    }

    private void process() {
        while (enabled) {

        }
    }

}
