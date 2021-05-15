package IonEngine;

import java.awt.event.*;

public interface IonBehaviourPack {
    public void on();
    public void off();
    public void setState(boolean state);
    public void setContainer(IonContainer container);
    public void mouseEvent(MouseEvent e, String type, int x, int y);
}
