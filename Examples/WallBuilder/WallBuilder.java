package Examples.WallBuilder;

import java.awt.Color;
import java.awt.event.*;

import IonEngine.*;

/**
 * WallBuilder - A 3D raycasting game where players can build and destroy walls
 * Controls:
 * - WASD: Move player
 * - Mouse: Look around
 * - Left click: Destroy wall
 * - Right click: Place wall
 * - Up/Down arrows: Adjust wall height
 * - Left/Right arrows: Adjust fisheye effect
 * - ESC: Toggle menu
 * - F11: Toggle fullscreen
 */

class WallBuilder {
    public static void main(String[] args) {
        
        IonFrame frame = new IonFrame(1500, 740);
        IonPanel panel = frame.addPanel();
       
        
        IonContainer mapContainer = panel.addContainer(new IonContainer(210, 210), "mapContainer");
        mapContainer.addBehaviour(new IonDraggableBP());
        IonPixelField pixelField = (IonPixelField)panel.addContainer(new IonPixelField(), "0-PixelField");
        pixelField.setResolution(pixelField.getWidth() / 2, pixelField.getHeight() / 2);

        IonContainer GUI = panel.addContainer(new IonContainer(1000, 500), "GUI");
        
        Game game = new Game(mapContainer, pixelField, panel);
        pixelField.addBehaviour(new PlayerController(game, frame));
        
        frame.setVisible(true);
        frame.setTitle("Wall Builder");

        IonRectangleObject menuBackground = (IonRectangleObject)GUI.add(new IonRectangleObject(500, 300, panel.getWidth() / 2 - 250, panel.getHeight() / 2 - 150), "Menu background");
        menuBackground.setBackgroundColor(IonColors.MaterialDarkGrey);
        IonImageObject instructions = (IonImageObject)GUI.add(new IonImageObject(500, 300, panel.getWidth() / 2 - 250, panel.getHeight() / 2 - 150), "instructions");
        instructions.loadImage("Examples/WallBuilder/menu.png");
        IonRectangleObject exitButton = (IonRectangleObject)GUI.add(new IonRectangleObject(400, 42, panel.getWidth() / 2 - 200, panel.getHeight() / 2 + 100), "exitButton");
        exitButton.setBackgroundColor(IonColors.GoodLookingDarkRed).setText("exit");
        IonRectangleObject worldResetButton = (IonRectangleObject)GUI.add(new IonRectangleObject(400, 42, panel.getWidth() / 2 - 200, panel.getHeight() / 2 + 35), "resetButton");
        worldResetButton.setBackgroundColor(IonColors.NiceNavyBlue).setText("reset world");
        GUI.setVisible(false);
        
        frame.hideCursor();
        IonRunTimeConsole RTC = new IonRunTimeConsole(frame);
        RTC.start();
    }
}

class Game {
    // View configuration
    private static final double DEFAULT_VIEW_ANGLE = 1.2;  // Controls the field of view width
    private static final double DEFAULT_FISHEYE = 1.0;     // Corrects fisheye distortion in rendering
    private static final int DEFAULT_RENDER_DISTANCE = 20; // Maximum ray distance for wall detection
    private static final double INITIAL_ROTATION = 0.5 * Math.PI; // Starting rotation (facing right)
    
    // Player position in pixel coordinates
    private double playerX = 0;  // X position within the map container
    private double playerY = 0;  // Y position within the map container
    private double playerRotation = INITIAL_ROTATION;

    // Player position in grid cell coordinates
    private double playerCellX;  // Current grid cell X coordinate
    private double playerCellY;  // Current grid cell Y coordinate

    // Map configuration
    private int[][] map;         // Current map state (0=empty, 1=wall, 2=blue wall)
    private int[][] defaultMap = {
        { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, 
        { 1, 0, 0, 0, 0, 2, 2, 2, 0, 0, 2, 1 }, 
        { 1, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
        { 1, 0, 2, 2, 2, 0, 2, 2, 2, 2, 2, 1 }, 
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, 
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, 
        { 1, 0, 0, 2, 2, 2, 2, 2, 0, 0, 0, 1 }, 
        { 1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 1 },
        { 1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 1 },
        { 1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 1 },
        { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 } };

    private IonContainer mapContainer;
    private IonPixelField pixelField;
    private IonPanel panel;
    IonContainer GUI;
    
    private int cellSize;
    private IonRectangleObject playerObject;
    private int playerSize;

    private double viewAngle = DEFAULT_VIEW_ANGLE;
    private double fisheye = DEFAULT_FISHEYE;
    private int renderDistance = DEFAULT_RENDER_DISTANCE;
    
    private Color ceilingColor = new Color(250, 250, 255);
    private Color floorColor = new Color(50, 20, 20);

    /**
     * Creates a new Game instance
     * @param mapContainer Container for the 2D map view
     * @param pixelField Field for rendering the 3D view
     * @param panel Main panel containing the game
     */
    public Game(IonContainer mapContainer, IonPixelField pixelField, IonPanel panel) {
        this.mapContainer = mapContainer;
        this.pixelField = pixelField;
        this.panel = panel;
        this.GUI = panel.getContainer("GUI");

        resetMap();
        resetPlayer();
        drawMap();
    }

    /**
     * Resets the player position and size to default values
     */
    public void resetPlayer() {
        playerSize = mapContainer.getWidth() / map[0].length / 2;
        cellSize = mapContainer.getWidth() / map[0].length;

        playerX = (cellSize * 3 - playerSize) / 2;
        playerY = mapContainer.getHeight() - cellSize * 1.5 - playerSize / 2;

        playerCellX = playerX / cellSize;
        playerCellY = playerY / cellSize;
    }

    public void resetMap() {
        map = new int[defaultMap.length][defaultMap[0].length];
        for (int x = 0; x < defaultMap.length; x++)
            for (int y = 0; y < defaultMap[0].length; y++)
                map[x][y] = defaultMap[x][y];
    }

    public void telportPlayer(double moveX, double moveY) {
        playerX = moveX;
        playerY = mapContainer.getHeight() - moveY;
        playerObject.moveTo(playerX + playerSize / 2, mapContainer.getHeight() - playerY - playerSize * 1.5);
    }
    public void telportPlayer() {
        telportPlayer(playerObject.getX(), playerObject.getY());
    }

    public void movePlayer(double moveX, double moveY) {
        double newPlayerX = playerX + Math.cos(playerRotation) * moveY - Math.sin(playerRotation) * moveX;
        double newPlayerY = playerY + Math.sin(playerRotation) * moveY + Math.cos(playerRotation) * moveX;
        boolean redraw = false;
        if (map[(int)(newPlayerY / cellSize)][(int)(playerX / cellSize)] == 0)
        {
            playerY = newPlayerY;
            if (playerY < 0) playerY = 0;
            redraw = true;
        }
        
        if (map[(int)(playerY / cellSize)][(int)(newPlayerX / cellSize)] == 0)
        {
            playerX = newPlayerX;
            if (playerX < 0) playerX = 0;
            redraw = true;
        }

        if (redraw)
        {
            playerObject.moveTo(playerX, playerY);
            drawFrame();
        }
    }

    public void rotatePlayer(double rotation) {
        this.playerRotation += rotation;
        playerRotation = playerRotation % (2 * Math.PI);
        if (playerRotation <= 0) playerRotation += 2 * Math.PI;
        playerObject.setRotation(playerRotation);
        drawFrame();
    }

    public void changeViewAngle(double change) {
        viewAngle += change;
        drawFrame();
    }

    public void changeFisheye(double change) {
        fisheye += change;
        drawFrame();
    }

    public double getPlayerX() {return playerX;}
    public double getPlayerY() {return playerY;}


    public void drawMap() {
        mapContainer.removeAllObjects();
        for (int x = 0; x < map[0].length; x++)
            for (int y = 0; y < map.length; y++)
            {
                IonRectangleObject mapCell = (IonRectangleObject)mapContainer.add(new IonRectangleObject(cellSize, cellSize, x * cellSize, y * cellSize), ("mapCell-" + x + "-" + y));
                mapCell.setBorderSize(1);
                if (map[y][x] == 1)
                    mapCell.setBackgroundColor(IonColors.MaterialDarkGrey);
                else if (map[y][x] == 2)
                    mapCell.setBackgroundColor(IonColors.GoodBlue);
                else
                    mapCell.setBackgroundColor(Color.white);
            }
        playerObject = (IonRectangleObject)mapContainer.add(new IonRectangleObject(playerSize, playerSize, playerX, playerY), "player");
        playerObject.setRotation(playerRotation);
        playerObject.setProperty("draggable", true);
    }


    /**
     * Casts rays to render the 3D view
     * Uses raycasting algorithm to detect walls and render appropriate heights
     */
    public void drawFrame() {
        for (int x = 0; x < pixelField.getImageWidth(); x++)
        {
            IonObject oldRay = mapContainer.get("ray" + x);
            if (oldRay != null)
                oldRay.remove();

            double rayAngle = (x - ((double)pixelField.getImageWidth() / 2)) * viewAngle / 250 + playerRotation;
            rayAngle = rayAngle % (2 * Math.PI);
            if (rayAngle <= 0) rayAngle += 2 * Math.PI;

            int rayX = (int)(playerX / cellSize);
            int rayY = (int)(playerY / cellSize);
            double rayDotX = playerX;
            double rayDotY = playerY;

            IonLineObject ray = ((IonLineObject)mapContainer.add(new IonLineObject(playerX, playerY), "ray" + x)).setLineColor(Color.red);
            
            // Variables for DDA (Digital Differential Analysis) ray casting
            double dx, dy;           // Distance to next grid line
            double distX, distY;     // Length of ray to next X or Y side
            double totalDist;        // Total ray distance
            double raySin, rayCos;   // Precalculated trig values
            double mapCell;          // Value of current map cell
            int dirX, dirY;          // Ray direction (-1 or 1)
            
            distX = 0;
            distY = 0;
            totalDist = 0;
            raySin = Math.sin(rayAngle);
            rayCos = Math.cos(rayAngle);
            mapCell = 0;

            // Determine ray direction based on angle
            if (rayAngle > Math.PI * 0.5 && rayAngle < Math.PI * 1.5) 
                dirX = -1;  // Facing left
            else 
                dirX = 1;   // Facing right

            if (rayAngle > Math.PI) 
                dirY = -1;  // Facing up
            else 
                dirY = 1;   // Facing down

            int iter = 0;

            while (mapCell == 0 && rayDotX > 0 && rayDotY > 0 && rayDotX < mapContainer.getWidth() && rayDotY < mapContainer.getHeight() && iter < renderDistance)
            {
                dx = rayDotX - rayX * cellSize;
                dy = rayDotY - rayY * cellSize;

                if (dirX == -1) //facing left
                {
                    distX = Math.abs(dx / rayCos);
                    dx = -dx;
                }
                else //facing right
                {
                    distX = Math.abs((cellSize - dx) / rayCos);
                    dx = (cellSize - dx);
                }

                if (dirY == -1) //facing up
                {
                    distY = Math.abs(dy / raySin);
                    dy = -dy;
                }
                else //facing down
                {
                    distY = Math.abs((cellSize - dy) / raySin);
                    dy = (cellSize - dy);
                }

                if (distX < distY)
                {
                    rayDotX += dx;
                    rayDotY += distX * raySin;
                    totalDist += distX;
                }
                else
                {
                    rayDotY += dy;
                    rayDotX += distY * rayCos;
                    totalDist += distY;
                }

                rayX = (int)((rayDotX + dirX) / cellSize);
                rayY = (int)((rayDotY + dirY) / cellSize);
                mapCell = map[rayY][rayX];

                iter++;
            }

            ray.setY2(rayDotY);
            ray.setX2(rayDotX);

            int height = (int)(pixelField.getImageHeight() / totalDist * 20 * fisheye);
            
            
            Color wallColor = Color.white;
            
            if (iter >= renderDistance)
            {
                wallColor = new Color(200, 200, 255);
                height = pixelField.getImageHeight() / 2;
            }
            else if (mapCell == 1)
            {
                int gray = 100 - (int)totalDist;

                if (distX < distY)
                gray -= 40;

                if (gray > 255) gray = 255;
                else if (gray < 0) gray = 0; 
                wallColor = new Color(gray, gray, gray);
            }
            else if (mapCell == 2)
            {
                int blue = 200 - (int)(totalDist / 2);

                if (distX < distY)
                blue -= 40;

                if (blue > 255) blue = 255;
                else if (blue < 0) blue = 0; 
                wallColor = new Color(5, 5, blue);
            }
            
            if (height > pixelField.getImageHeight()) height = pixelField.getImageHeight();
            if (height < 0) height = 0;

            int margin = (pixelField.getImageHeight() - height) / 2;
            
            for (int y = 0; y < margin; y++)
                pixelField.setPixel(x, y, ceilingColor);

            for (int y = margin; y < pixelField.getImageHeight() - margin; y++)
                pixelField.setPixel(x, y, wallColor);

            for (int y = pixelField.getImageHeight() - margin; y < pixelField.getImageHeight(); y++)
                pixelField.setPixel(x, y, floorColor);

            pixelField.repaint();
        }
    }

    /**
     * Handles world editing (placing/destroying walls)
     * @param setCell 0 to destroy wall, 2 to place blue wall
     * Uses player's position and rotation to determine target cell
     */
    public void worldEdit(int setCell) {
        if (setCell != 0 && setCell != 2) {
            throw new IllegalArgumentException("setCell must be 0 or 2");
        }
        
        playerCellX = playerX / cellSize;
        playerCellY = playerY / cellSize;

        int actionCellX = (int)(playerCellX + Math.cos(playerRotation) * 1);
        int actionCellY = (int)(playerCellY + Math.sin(playerRotation) * 1);

        if (setCell == 0 && map[actionCellY][actionCellX] == 0 || setCell != 0)
        {
            actionCellX = (int)(playerCellX + Math.cos(playerRotation) * 2);
            actionCellY = (int)(playerCellY + Math.sin(playerRotation) * 2);
        }

        if (map[actionCellY][actionCellX] != 1)
            map[actionCellY][actionCellX] = setCell;
        
        
        drawMap();
        drawFrame();
    }

    private boolean inMenu = false;
    public boolean isInMenu() { return inMenu; }
    public void toggleMenu() {
        inMenu = !inMenu;

        GUI.setVisible(inMenu);
        if (inMenu)
        {
            panel.getFrame().resetCursor();
            IonRectangleObject menuBackground = (IonRectangleObject)GUI.get("Menu background");
            menuBackground.resize(500, 300);
            menuBackground.moveTo(panel.getWidth() / 2 - 250, panel.getHeight() / 2 - 150);
        }
        else
        {
            panel.getFrame().hideCursor();
        }
    }

    public void menuClick(MouseEvent e, int x, int y) {
        IonObject objectClicked = GUI.getObjectByCoordinates(x, y);
        if (objectClicked != null)
        switch (objectClicked.getName())
        {
            case "exitButton":
                System.exit(0);
                return;
            case "resetButton":
                resetMap();
                resetPlayer();
                drawMap();
                drawFrame();
                toggleMenu();
                return;
        }
    }
}

class PlayerController extends IonBehaviourPack {
    // Movement and control constants
    private static final double FORWARD_SPEED = 3.0;        // Forward movement speed
    private static final double BACKWARD_SPEED = 3.0;       // Backward movement speed
    private static final double STRAFE_SPEED = 2.0;         // Sideways movement speed
    private static final double ROTATION_SENSITIVITY = 42.0; // Mouse look sensitivity
    private static final double VIEW_ANGLE_DELTA = 0.1;     // Field of view adjustment increment
    private static final double FISHEYE_DELTA = 0.1;        // Fisheye correction adjustment increment

    // Mouse rotation handling variables
    private int prevX;                          // Previous mouse X position
    private long lastTimeMovedInMillis;         // Timestamp of last mouse movement

    private Game game;
    private IonFrame frame;

    public PlayerController(Game game, IonFrame frame) {
        this.game = game;
        this.frame = frame;
    }
    

    /**
     * Handles key events for player movement
     * @param e KeyEvent object representing the key event
     * @param type Type of key event (pressed or released)
     */
    public void keyEvent(KeyEvent e, String type) {
        if (type == "pressed") {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    if (!game.isInMenu())
                        game.movePlayer(0, FORWARD_SPEED);
                    return;
                case KeyEvent.VK_S:
                    if (!game.isInMenu())
                        game.movePlayer(0, -BACKWARD_SPEED);
                    return;
                case KeyEvent.VK_A:
                    if (!game.isInMenu())
                        game.movePlayer(-STRAFE_SPEED, 0);
                    return;
                case KeyEvent.VK_D:
                    if (!game.isInMenu())
                        game.movePlayer(STRAFE_SPEED, 0);
                    return;

                case KeyEvent.VK_UP:
                    game.changeViewAngle(VIEW_ANGLE_DELTA);
                    return;
                case KeyEvent.VK_DOWN:
                    game.changeViewAngle(-VIEW_ANGLE_DELTA);
                    return;

                case KeyEvent.VK_LEFT:
                    game.changeFisheye(FISHEYE_DELTA);
                    return;
                case KeyEvent.VK_RIGHT:
                    game.changeFisheye(-FISHEYE_DELTA);
                    return;

                case KeyEvent.VK_F11:
                    frame.toggleFullScreen();
                    game.drawFrame();
                    return;

                case KeyEvent.VK_ESCAPE:
                    game.toggleMenu();
                    return;
            }
        }        
    }

    /**
     * Handles mouse events for player rotation
     * Centers mouse periodically to allow continuous rotation
     */
    public void mouseEvent(MouseEvent e, String type, int x, int y) {
        switch (type)
        {
            case "moved":
                if (!game.isInMenu()) {
                    handleMouseRotation(x);
                }
                return;
            case "clicked":
                if (!game.isInMenu())
                {
                    if (e.getButton() == MouseEvent.BUTTON1)
                        game.worldEdit(0);
                    else if (e.getButton() == MouseEvent.BUTTON3)
                        game.worldEdit(2);
                }
                else
                    game.menuClick(e, x, y);
                return;
        }
    }

    /**
     * Handles mouse rotation logic
     * @param x X coordinate of the mouse movement
     */
    private void handleMouseRotation(int x) {
        if (System.currentTimeMillis() - lastTimeMovedInMillis > 100) {
            prevX = frame.getWidth() / 2 - 7;
            frame.robot().mouseMove(
                frame.getX() + frame.getWidth() / 2,
                frame.getY() + frame.getHeight() / 2
            );
            lastTimeMovedInMillis = System.currentTimeMillis();
        } else {
            game.rotatePlayer((double)(x - prevX) / ROTATION_SENSITIVITY);
            prevX = x;
            lastTimeMovedInMillis = System.currentTimeMillis();
        }
    }
}