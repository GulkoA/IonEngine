package IonEngine;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.HashMap;

public class IonPanel extends JPanel {
    private HashMap<String, IonContainer> containerMap = new HashMap<String, IonContainer>();

    public IonPanel() {
        //set Background Color
        setBackground(Color.white);
        
        //MouseMotionListener, MouseListener
        MouseRedirect mouseListener = new MouseRedirect();
        addMouseMotionListener(mouseListener);
        addMouseWheelListener(mouseListener);
        addMouseListener(mouseListener);
    }

    private class MouseRedirect implements MouseMotionListener, MouseListener, MouseWheelListener {
        public void mouseMoved(MouseEvent e){ forwardEvent(e, "moved"); }
        public void mousePressed(MouseEvent e){ forwardEvent(e, "pressed"); }
		public void mouseReleased(MouseEvent e){ forwardEvent(e, "released"); }
        public void mouseClicked(MouseEvent e){ forwardEvent(e, "clicked"); }
        public void mouseDragged(MouseEvent e){ forwardEvent(e, "dragged"); }
		public void mouseEntered(MouseEvent e){ forwardEvent(e, "entered"); }
		public void mouseExited(MouseEvent e){ forwardEvent(e, "exited"); }
        public void mouseWheelMoved(MouseWheelEvent e) { forwardEvent(e, "wheelMoved"); }

        private void forwardEvent(MouseEvent e, String type) {
            int relMouseX;
            int relMouseY;
            for(IonContainer container: containerMap.values())
            {
                relMouseX = e.getX() - container.getX();
                relMouseY = e.getY() - container.getY();
                if (relMouseX > 0 && relMouseX < container.getWidth() && relMouseY > 0 && relMouseY < container.getHeight() );
                    container.mouseEvent(e, type, relMouseX, relMouseY);
            }
        }
    }

    public void paintComponent(Graphics g) {
		super.paintComponent(g);
        for (IonContainer container: containerMap.values())
        {
            if (container != null)
                container.draw(g);
        }
    }

    public IonContainer addContainer(IonContainer container, String name) {
        this.containerMap.put(name, container);
        container.setPanel(this, name);
        return container;
    }
    public IonContainer addContainer() {
        IonContainer container = new IonContainer();
        int i = 1;
        String name = "container";
        while (containerMap.get(name) != null) {i++; name = "container" + i;}
        this.containerMap.put(name, container);
        container.setPanel(this, name);
        return container;
    }

    public IonContainer getContainer(String name) {
        return containerMap.get(name);
    }

    public HashMap<String, IonContainer> getContainerMap() {
        return containerMap;
    }
}
