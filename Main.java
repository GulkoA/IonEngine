import java.awt.Color;

import IonEngine.*;
import java.awt.*;
import java.awt.event.*;

class Main {
    public static void main(String[] args) {
        IonFrame frame = new IonFrame(1500, 740);
        IonPanel panel = frame.addPanel();
       
        IonContainer mapContainer = panel.addContainer(new IonContainer(700, 700), "mapContainer");
        mapContainer.changeBorder(1);
        IonPixelField pixelField = (IonPixelField)panel.addContainer(new IonPixelField(800, 700, 700, 0), "PixelField");

        Game game = new Game(mapContainer, pixelField);
        pixelField.addBehaviour(new PlayerController(game));

        frame.setVisible(true);
        //frame.addKeyListener(new L());  
    }
}
class Game {
    private double playerX = 0;
    private double playerY = 0;
    private double playerRotation = 0;
    private int[][] map = { { 1, 1, 1, 1, 1, 1, 1 }, { 1, 0, 0, 0, 0, 0, 1 }, { 1, 0, 0, 0, 0, 0, 1 },
            { 1, 0, 1, 1, 1, 0, 1 }, { 1, 0, 0, 0, 0, 0, 1 }, { 1, 0, 0, 0, 0, 0, 1 }, { 1, 1, 1, 1, 1, 1, 1 } };

    private IonContainer mapContainer;
    private IonPixelField pixelField;
    
    private int cellSize;
    private IonRectangleObject playerObject;
    private int playerSize;

    public Game(IonContainer mapContainer, IonPixelField pixelField) {
        this.mapContainer = mapContainer;
        this.pixelField = pixelField;

        playerSize = mapContainer.getWidth() / map[0].length / 2;
        cellSize = mapContainer.getWidth() / map[0].length;

        playerX = cellSize;
        playerY = cellSize;
        drawMap();
    }
    
    public int getMapCell(int x, int y) {
        return map[x][y];
    }

    public void telportPlayer(double moveX, double moveY) {
        playerX += moveX;
        playerY += moveY;
        playerObject.moveTo(playerX + playerSize / 2, mapContainer.getHeight() - playerY - playerSize * 1.5);
        //System.out.println(playerX + " " + playerY);
    }

    public void movePlayer(double move) {
        playerX += Math.sin(playerRotation) * move;
        playerY += Math.cos(playerRotation) * move;
        playerObject.moveTo(playerX + playerSize / 2, mapContainer.getHeight() - playerY - playerSize * 1.5);
    }

    public void rotatePlayer(double rotation) {
        this.playerRotation += rotation;
        playerObject.setRotation(playerRotation);
    }

    public double getPlayerX() {return playerX;}
    public double getPlayerY() {return playerY;}


    public void drawMap() {
        mapContainer.removeAllObjects();
        for (int x = 0; x < map[0].length; x++)
            for (int y = 0; y < map.length; y++)
            {
                IonRectangleObject mapCell = (IonRectangleObject)mapContainer.add(new IonRectangleObject(cellSize, cellSize, x * cellSize, y * cellSize), ("mapCell-" + x + "-" + y));
                if (getMapCell(y, x) == 1)
                    mapCell.setBackgroundColor(IonColors.GoodBlue);
                else
                    mapCell.setBackgroundColor(Color.white);
            }
        playerObject = (IonRectangleObject)mapContainer.add(new IonRectangleObject(playerSize, playerSize, playerX + playerSize / 2, mapContainer.getHeight() - playerY - playerSize * 1.5), "player");
        playerObject.setRotation(playerRotation);
        playerObject.setBorderSize(0);

    }
}

class PlayerController extends IonBehaviourPack {
    private Game game;

    public PlayerController(Game game) {
        this.game = game;
    }

    public void keyEvent(KeyEvent e, String type) {
        if (type == "pressed") {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    game.movePlayer(3);
                    return;
                case KeyEvent.VK_S:
                    game.movePlayer(-3);
                    return;
                case KeyEvent.VK_A:
                    game.rotatePlayer(-0.1);
                    return;
                case KeyEvent.VK_D:
                    game.rotatePlayer(0.1);
                    return;
            }
        }        
    }
}