package IonEngine;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.HashMap;

public class IonPanel extends JPanel {
    private HashMap<String, IonContainer> containerMap = new HashMap<String, IonContainer>();
    private Redirect listener;
    private IonFrame frame;

    public IonPanel() {
        //set Background Color
        setBackground(Color.white);
        
        //MouseMotionListener, MouseListener
        listener = new Redirect();

        addMouseMotionListener(listener);
        addMouseWheelListener(listener);
        addMouseListener(listener);
    }

    public void setFrame(IonFrame frame) { this.frame = frame; }

    public Redirect getListener() { return listener; }

    private class Redirect implements MouseMotionListener, MouseListener, MouseWheelListener, KeyListener {
        public void mouseMoved(MouseEvent e){ forwardMouseEvent(e, "moved"); }
        public void mousePressed(MouseEvent e){ forwardMouseEvent(e, "pressed"); }
		public void mouseReleased(MouseEvent e){ forwardMouseEvent(e, "released"); }
        public void mouseClicked(MouseEvent e){ forwardMouseEvent(e, "clicked"); }
        public void mouseDragged(MouseEvent e){ forwardMouseEvent(e, "dragged"); }
		public void mouseEntered(MouseEvent e){ forwardMouseEvent(e, "entered"); }
		public void mouseExited(MouseEvent e){ forwardMouseEvent(e, "exited"); }
        public void mouseWheelMoved(MouseWheelEvent e) { forwardMouseEvent(e, "wheelMoved"); }

        private void forwardMouseEvent(MouseEvent e, String type) {
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

        public void keyTyped(KeyEvent e) { forwardKeyEvent(e, "typed"); }
        public void keyPressed(KeyEvent e) { forwardKeyEvent(e, "pressed"); }
        public void keyReleased(KeyEvent e) { forwardKeyEvent(e, "released"); }

        private void forwardKeyEvent(KeyEvent e, String type) {
            for(IonContainer container: containerMap.values())
            {
                container.keyEvent(e, type);
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
