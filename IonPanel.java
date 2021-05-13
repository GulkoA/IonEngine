package GameEngine;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.HashMap;

class IonPanel extends JPanel {
    private HashMap<String, IonContainer> containerMap = new HashMap<String, IonContainer>();

    public IonPanel() {
        //set Background Color
        setBackground(Color.white);
        
        //MouseMotionListener, MouseListener
        MouseList mouseList = new MouseList();
        addMouseMotionListener(mouseList);
        addMouseListener(mouseList);
    }

    private class MouseList implements MouseMotionListener, MouseListener {
        public void mouseDragged(MouseEvent e){}
        public void mousePressed(MouseEvent e){}
		public void mouseReleased(MouseEvent e){}
        public void mouseMoved(MouseEvent e){}
        public void mouseClicked(MouseEvent e){}
		public void mouseEntered(MouseEvent e){}
		public void mouseExited(MouseEvent e){}
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
        this.containerMap.put("container", container);
        container.setPanel(this, "container");
        return container;
    }

    public IonContainer getContainer(String name) {
        return containerMap.get(name);
    }

    public HashMap<String, IonContainer> getContainerMap() {
        return containerMap;
    }
}
