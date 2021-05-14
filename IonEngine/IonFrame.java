package IonEngine;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class IonFrame extends JFrame {
    private MenuBar menuBar;
    private IonPanel panel;

    public IonFrame(int width, int height) {
        setSize(new Dimension(width, height));
    }
    public IonFrame() {
        setSize(new Dimension(500, 500));
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

    public IonPanel addPanel(IonPanel newPanel) {
        panel = newPanel;
        this.add(panel);
        return newPanel;
    }
    public IonPanel addPanel() {
        panel = new IonPanel();
        this.add(panel);
        return panel;
    }

    public IonPanel getPanel() {
        return panel;
    }
}
