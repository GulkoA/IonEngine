package Examples;

import java.awt.Color;
import IonEngine.*;

/**
 * Demonstrates creation of a smooth color gradient using IonEngine's PixelField.
 * Creates a 255x255 window displaying a gradient
 * components while keeping blue constant.
 */
class Gradient {
    // Window dimensions - using constants makes the code more maintainable
    private static final int WINDOW_WIDTH = 255;
    private static final int WINDOW_HEIGHT = 255;
    
    public static void main(String[] args) {
        // Create and configure the main window
        IonFrame frame = new IonFrame();
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        
        // Add a panel to hold our pixel field
        IonPanel panel = frame.addPanel();
        
        // Create a pixel field to draw our gradient
        IonPixelField pixelField = (IonPixelField)panel.addContainer(
            new IonPixelField(WINDOW_WIDTH, WINDOW_HEIGHT), 
            "PixelField"
        );
        
        // Make the window visible before drawing
        frame.setVisible(true);
        
        // Draw the gradient
        createGradient(pixelField);
    }
    
    /**
     * Creates a smooth color gradient where:
     * - Red component varies with x position
     * - Green component varies with y position
     * - Blue component stays constant at maximum value
     * 
     * @param pixelField The pixel field to draw the gradient on
     */
    private static void createGradient(IonPixelField pixelField) {
        for (int x = 0; x < WINDOW_WIDTH; x++) {
            for (int y = 0; y < WINDOW_HEIGHT; y++) {
                // Create color with varying red and green components
                Color color = new Color(x, y, 255);
                pixelField.setPixel(x, y, color);
            }
        }
    }
}