package IonEngine;

/**
 * An interface for defining callbacks that can be used to trigger actions with `IonFeatures.setTimeout` and `IonFeatures.setInterval`.
 */
public interface IonCallback {
    /** Whether the callback is activated */
    public boolean activated = true;

    /** The method to be called when the callback is triggered */
    public void call();
}