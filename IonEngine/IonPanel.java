package IonEngine;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.HashMap;

/**
 * IonPanel is the core container class that manages and renders IonContainers.
 * It extends JPanel and handles mouse/keyboard input routing to the appropriate containers.
 */
public class IonPanel extends JPanel {
    /** Maps container names to IonContainer instances */
    private HashMap<String, IonContainer> containerMap = new HashMap<String, IonContainer>();
    /** Event listener that redirects input events to containers */
    private Redirect listener;
    /** Parent IonFrame reference */
    private IonFrame frame;

    /**
     * Creates a new IonPanel with a white background and initializes input handling.
     */
    public IonPanel() {
        //set Background Color
        setBackground(Color.white);
        
        //MouseMotionListener, MouseListener
        listener = new Redirect();

        addMouseMotionListener(listener);
        addMouseWheelListener(listener);
        addMouseListener(listener);
    }

    /**
     * Sets the parent IonFrame for this panel.
     * @param frame The IonFrame that contains this panel
     */
    public void setFrame(IonFrame frame) { this.frame = frame; }

    /**
     * Gets the parent IonFrame of this panel.
     * @return The parent IonFrame
     */
    public IonFrame getFrame() { return this.frame; }

    /**
     * Gets the event listener that handles input redirection.
     * @return The Redirect event listener instance
     */
    public Redirect getListener() { return listener; }

    /**
     * Internal class that handles input event redirection to appropriate containers.
     */
    private class Redirect implements MouseMotionListener, MouseListener, MouseWheelListener, KeyListener {
        public void mouseMoved(MouseEvent e){ forwardMouseEvent(e, "moved"); }
        public void mousePressed(MouseEvent e){ forwardMouseEvent(e, "pressed"); }
		public void mouseReleased(MouseEvent e){ forwardMouseEvent(e, "released"); }
        public void mouseClicked(MouseEvent e){ forwardMouseEvent(e, "clicked"); }
        public void mouseDragged(MouseEvent e){ forwardMouseEvent(e, "dragged"); }
		public void mouseEntered(MouseEvent e){ forwardMouseEvent(e, "entered"); }
		public void mouseExited(MouseEvent e){ forwardMouseEvent(e, "exited"); }
        public void mouseWheelMoved(MouseWheelEvent e) { forwardMouseEvent(e, "wheelMoved"); }

        /**
         * Forwards mouse events to containers that are under the mouse cursor.
         * Calculates relative mouse positions for each container.
         * @param e The mouse event
         * @param type The type of mouse event (moved, pressed, etc.)
         */
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

        /**
         * Forwards keyboard events to all containers.
         * @param e The keyboard event
         * @param type The type of keyboard event (typed, pressed, released)
         */
        private void forwardKeyEvent(KeyEvent e, String type) {
            for(IonContainer container: containerMap.values())
            {
                container.keyEvent(e, type);
            }
        }
    }

    /**
     * Paints all containers in this panel.
     * @param g The Graphics context to paint on
     */
    public void paintComponent(Graphics g) {
		super.paintComponent(g);
        for (IonContainer container: containerMap.values())
        {
            if (container != null)
                container.draw(g);
        }
    }

    /**
     * Adds a container to this panel with a specified name.
     * @param container The container to add
     * @param name The unique name for the container
     * @return The added container
     */
    public IonContainer addContainer(IonContainer container, String name) {
        this.containerMap.put(name, container);
        container.setPanel(this, name);
        return container;
    }

    /**
     * Creates and adds a new container with a specified name.
     * If the name is already taken, appends a number to make it unique.
     * @param name The desired name for the container
     * @return The newly created container
     */
    public IonContainer addContainer(String name) {
        IonContainer container = new IonContainer();
        int i = 1;
        while (containerMap.get(name) != null) {i++; name = "container" + i;}
        this.containerMap.put(name, container);
        container.setPanel(this, name);
        return container;
    }

    /**
     * Creates and adds a new container with an automatically generated name.
     * Names are in the format "container1", "container2", etc.
     * @return The newly created container
     */
    public IonContainer addContainer() {
        IonContainer container = new IonContainer();
        int i = 1;
        String name = "container";
        while (containerMap.get(name) != null) {i++; name = "container" + i;}
        this.containerMap.put(name, container);
        container.setPanel(this, name);
        return container;
    }

    /**
     * Retrieves a container by its name.
     * @param name The name of the container to retrieve
     * @return The container with the specified name, or null if not found
     */
    public IonContainer getContainer(String name) {
        return containerMap.get(name);
    }

    /**
     * Gets the map of all containers in this panel.
     * @return HashMap mapping container names to IonContainer instances
     */
    public HashMap<String, IonContainer> getContainerMap() {
        return containerMap;
    }
}
