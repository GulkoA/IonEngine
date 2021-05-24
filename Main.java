import java.awt.Color;

import IonEngine.*;
import java.awt.*;

class Main {
    public static void main(String[] args) {
        IonFrame frame = new IonFrame(1000, 400);
        IonPanel panel = frame.addPanel();
        
        // frame.setExtendedState(IonFrame.MAXIMIZED_BOTH);
        // frame.setUndecorated(true);
        
        System.out.println(frame.getWidth() + " " + frame.getHeight());
        


       
        IonContainer mapContainer = panel.addContainer(new IonContainer(400, 400), "mapContainer");
        mapContainer.changeBorder(1);
        IonPixelField pixelField = (IonPixelField)panel.addContainer(new IonPixelField(600, 400, 400, 0), "PixelField");


        frame.setVisible(true);
         
        
    }
}

class game {
    private double playerX = 0;
    private double playerY = 0;
    private int[][] map = { { 1, 1, 1, 1, 1, 1, 1 }, { 1, 0, 0, 0, 0, 0, 1 }, { 1, 0, 0, 0, 0, 0, 1 },
            { 1, 0, 1, 1, 1, 0, 1 }, { 1, 0, 0, 0, 0, 0, 1 }, { 1, 0, 0, 0, 0, 0, 1 }, { 1, 1, 1, 1, 1, 1, 1 } };
    
}

class PlayerMover extends IonBehaviourPack {

}