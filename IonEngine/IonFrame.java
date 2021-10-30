package IonEngine;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

public class IonFrame extends JFrame {
    private MenuBar menuBar;
    private ArrayList<IonPanel> panelList = new ArrayList<IonPanel>();

    public IonFrame(int width, int height) {
        setSize(new Dimension(width, height));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.getComponents();
    }
    public IonFrame() {
        setSize(new Dimension(500, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private boolean isFullScreen = false;
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
    public IonFrame toggleFullScreen() {
        setFullScreen(!isFullScreen);
        return this;
    }
    public boolean getFullScreen() { return isFullScreen; }
    
    public MenuBar addMenuBar() {
		menuBar = new MenuBar();
        this.setMenuBar(menuBar);
        return menuBar;
    }
    public MenuBar getMenuBar() {
        return menuBar;
    }

    public Menu addMenu(String name) {
        Menu newMenuItem = new Menu(name);
        menuBar.add(newMenuItem);
        return newMenuItem;
    }

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

    public IonPanel addPanel(IonPanel newPanel) {
        IonPanel panel = newPanel;
        this.add(panel);
        panel.setFrame(this);
        panelList.add(panel);
        addKeyListener(panel.getListener());
        return newPanel;
    }
    public IonPanel addPanel() {
        IonPanel panel = new IonPanel();
        this.add(panel);
        panel.setFrame(this);
        panelList.add(panel);
        addKeyListener(panel.getListener());
        return panel;
    }

    public ArrayList<IonPanel> getPanelList() {
        return panelList;
    }

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

    public IonFrame resetCursor() {
        super.getContentPane().setCursor(Cursor.getDefaultCursor());
        //from https://stackoverflow.com/questions/1984071/how-to-hide-cursor-in-a-swing-application

        return this;
    }

    public int getMiddleX() { return getWidth() / 2; }
    public int getMiddleY() { return getHeight() / 2; }

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
