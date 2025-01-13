package IonEngine;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

/**
 * The main window frame class for the Ion Engine.
 * Extends JFrame to provide core window functionality with additional features
 * like fullscreen support, menu bars, panels, and cursor control.
 * 
 * This class serves as the primary container for all visual elements in an Ion application.
 * It manages window properties, menu systems, and panel organization.
 */
public class IonFrame extends JFrame {
    private MenuBar menuBar;
    private ArrayList<IonPanel> panelList = new ArrayList<IonPanel>();

    /**
     * Creates a new IonFrame with specified dimensions.
     * 
     * @param width The width of the frame in pixels
     * @param height The height of the frame in pixels
     */
    public IonFrame(int width, int height) {
        setSize(new Dimension(width, height));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.getComponents();
    }

    /**
     * Creates a new IonFrame with default dimensions (500x500 pixels).
     */
    public IonFrame() {
        setSize(new Dimension(500, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private boolean isFullScreen = false;

    /**
     * Sets the fullscreen state of the window
     * @param state true to enter fullscreen, false to exit
     * @return this IonFrame instance for method chaining
     */
    public IonFrame setFullScreen(boolean state) {
        if (state)
        {
            GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this);
        }
        else
        {
            GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(null);
        }
        isFullScreen = state;
        return this;
    }

    /**
     * Toggles between fullscreen and windowed mode.
     * 
     * @return this IonFrame instance for method chaining
     */
    public IonFrame toggleFullScreen() {
        setFullScreen(!isFullScreen);
        return this;
    }

    /**
     * Gets the current fullscreen state.
     * 
     * @return true if the window is in fullscreen mode, false otherwise
     */
    public boolean getFullScreen() { return isFullScreen; }
    
    /**
     * Creates and adds a menu bar to the frame.
     * 
     * @return the newly created MenuBar instance
     */
    public MenuBar addMenuBar() {
		menuBar = new MenuBar();
        this.setMenuBar(menuBar);
        return menuBar;
    }

    /**
     * Retrieves the current menu bar.
     * 
     * @return the current MenuBar instance, or null if none exists
     */
    public MenuBar getMenuBar() {
        return menuBar;
    }

    /**
     * Creates and adds a new menu to the menu bar.
     * 
     * @param name the name of the menu to create
     * @return the newly created Menu instance
     */
    public Menu addMenu(String name) {
        Menu newMenuItem = new Menu(name);
        menuBar.add(newMenuItem);
        return newMenuItem;
    }

    /**
     * Adds multiple menu items to a menu with associated event handling.
     * 
     * @param menu the Menu to add items to
     * @param itemNames array of names for the menu items
     * @param eventCaller the event handler for the menu items
     * @return array of created MenuItem instances
     */
    public MenuItem[] addMenuItems(Menu menu, String[] itemNames, IonEventCaller eventCaller) {
        MenuItem[] items = new MenuItem[itemNames.length];
        for(int i = 0; i < items.length; i++) {
            items[i] = new MenuItem(itemNames[i]);
            items[i].addActionListener(new ActionRedirecter(eventCaller, itemNames[i]));
            menu.add(items[i]);
        }
        return items;
    }

    private class ActionRedirecter implements ActionListener {
        private IonEventCaller eventCaller;
        private String eventName;
        public ActionRedirecter(IonEventCaller eventCaller, String eventName) {this.eventName = eventName; this.eventCaller = eventCaller;}
        public void actionPerformed(ActionEvent e)
        {
            eventCaller.actionPerformed(e, eventName);
        } 
    }

    /**
     * Adds a custom IonPanel to the frame.
     * 
     * @param newPanel the IonPanel to add
     * @return the added IonPanel instance
     */
    public IonPanel addPanel(IonPanel newPanel) {
        IonPanel panel = newPanel;
        this.add(panel);
        panel.setFrame(this);
        panelList.add(panel);
        addKeyListener(panel.getListener());
        return newPanel;
    }

    /**
     * Creates and adds a new default IonPanel to the frame.
     * 
     * @return the newly created IonPanel instance
     */
    public IonPanel addPanel() {
        IonPanel panel = new IonPanel();
        this.add(panel);
        panel.setFrame(this);
        panelList.add(panel);
        addKeyListener(panel.getListener());
        return panel;
    }

    /**
     * Gets the list of all panels currently added to the frame.
     * 
     * @return ArrayList containing all IonPanel instances
     */
    public ArrayList<IonPanel> getPanelList() {
        return panelList;
    }

    /**
     * Hides the cursor within the frame.
     * Creates an invisible cursor for use when cursor display is not desired.
     * 
     * @return this IonFrame instance for method chaining
     */
    public IonFrame hideCursor() {
        // Transparent 16 x 16 pixel cursor image.
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

        // Create a new blank cursor.
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
            cursorImg, new Point(0, 0), "blank cursor");

        // Set the blank cursor to the JFrame.
        super.getContentPane().setCursor(blankCursor);
        //from https://stackoverflow.com/questions/1984071/how-to-hide-cursor-in-a-swing-application

        return this;
    }

    /**
     * Restores the default cursor appearance.
     * 
     * @return this IonFrame instance for method chaining
     */
    public IonFrame resetCursor() {
        super.getContentPane().setCursor(Cursor.getDefaultCursor());
        //from https://stackoverflow.com/questions/1984071/how-to-hide-cursor-in-a-swing-application

        return this;
    }

    /**
     * Gets the horizontal center coordinate of the frame.
     * 
     * @return x-coordinate of the frame's center
     */
    public int getMiddleX() { return getWidth() / 2; }

    /**
     * Gets the vertical center coordinate of the frame.
     * 
     * @return y-coordinate of the frame's center
     */
    public int getMiddleY() { return getHeight() / 2; }

    /**
     * Gets or creates a Robot instance for automated input control.
     * The Robot instance is created only once and cached for future use.
     * 
     * @return the Robot instance for this frame
     */
    public Robot javaRobot;
    public Robot robot() {
        if (javaRobot == null)
        {
            try {
                javaRobot = new Robot();
                return javaRobot;
            } catch (Exception e) {}
        }
        return javaRobot;
    }
    
}
