package IonEngine;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;

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

    public void setFullScreen(boolean state) {
        if (state)
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        else
            setExtendedState(JFrame.NORMAL);
        setUndecorated(state);
    }
    
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
        private String eventName;
        private IonEventCaller eventCaller;
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
    
}
