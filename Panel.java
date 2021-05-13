package GameEngine;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;

class IonPanel extends JPanel {
    private ArrayList<IonContainer> containerList = new ArrayList<IonContainer>();

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
        for (IonContainer container: containerList)
        {
            if (container != null)
                container.draw(g);
        }
    }

    public IonContainer addContainer(IonContainer container) {
        this.containerList.add(container);
        container.setPanel(this, containerList.size() - 1);
        return container;
    }
    public IonContainer addContainer() {
        IonContainer container = new IonContainer();
        this.containerList.add(container);
        container.setPanel(this, containerList.size() - 1);
        return container;
    }
}
