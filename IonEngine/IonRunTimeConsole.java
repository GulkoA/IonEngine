package IonEngine;
import java.util.Scanner;
import java.util.HashMap;

/**
 * A runtime console that provides interactive command-line control over Ion Engine components.
 * This console allows users to manipulate frames, panels, containers, and objects during runtime.
 */
public class IonRunTimeConsole extends Thread {
    private boolean runtimeConsoleEnabled = false;
    private Scanner scanner;
    private HashMap<String, IonFrame> framesMap = new HashMap<String, IonFrame>();
    private HashMap<String, IonPanel> panelsMap = new HashMap<String, IonPanel>();
    private HashMap<String, IonContainer> containersMap = new HashMap<String, IonContainer>();
    private HashMap<String, IonObject> objectsMap = new HashMap<String, IonObject>();

    /**
     * Creates a new runtime console with a pre-configured frame.
     * @param frame The IonFrame to be added to the console's control
     */
    public IonRunTimeConsole(IonFrame frame) {
        addFrame(frame, "frame");
        addFrameAppended("frame");
    }

    /**
     * Creates a new empty runtime console.
     */
    public IonRunTimeConsole() {}

    /**
     * Adds a frame to the console's control.
     * @param frame The frame to be added
     * @param name The name used to reference this frame in console commands
     * @return The added frame
     */
    public IonFrame addFrame(IonFrame frame, String name) {
        framesMap.put(name, frame);
        return frame;
    }

    /**
     * Imports all panels from a specified frame into the console's control.
     * @param frameName The name of the frame whose panels should be imported
     */
    public void addFrameAppended(String frameName) {
        for (IonPanel panel: framesMap.get(frameName).getPanelList())
        addPanel(panel, "panel");
        addPanelAppended("panel");
    }

    /**
     * Adds a panel to the console's control.
     * @param panel The panel to be added
     * @param name The name used to reference this panel in console commands
     * @return The added panel
     */
    public IonPanel addPanel(IonPanel panel, String name) {
        panelsMap.put(name, panel);
        return panel;
    }

    /**
     * Imports all containers from a specified panel into the console's control.
     * @param panelName The name of the panel whose containers should be imported
     */
    public void addPanelAppended(String panelName) {
        containersMap.putAll(panelsMap.get(panelName).getContainerMap());
    }

    /**
     * Adds a container to the console's control.
     * @param container The container to be added
     * @param name The name used to reference this container in console commands
     * @return The added container
     */
    public IonContainer addContainer(IonContainer container, String name) {
        containersMap.put(name, container);
        return container;
    }

    /**
     * Imports all objects from a specified container into the console's control.
     * @param containerName The name of the container whose objects should be imported
     */
    public void addContainerAppended(String containerName) {
        objectsMap.putAll(containersMap.get(containerName).getObjectMap());
    }

    /**
     * Adds an object to the console's control.
     * @param object The object to be added
     * @param name The name used to reference this object in console commands
     * @return The added object
     */
    public IonObject addObject(IonObject object, String name) {
        objectsMap.put(name, object);
        return object;
    }

    /**
     * Starts the runtime console, enabling command input.
     * The console will continue running until explicitly stopped.
     */
    @Override
    public void run() {
        runtimeConsoleEnabled = true;
        scanner = new Scanner(System.in);
        System.out.println("Ion runtime console enabled! Type rtc.help for the command list");
        while (runtimeConsoleEnabled)
            prompt();
    }

    /**
     * Stops the runtime console.
     */
    public void exit() {
        runtimeConsoleEnabled = false;
        scanner.close();
        System.out.println("Ion runtime console disabled");
    }

    /**
     * Processes a single command input from the user.
     * This method handles the parsing and execution of console commands.
     * Available commands can be viewed using the 'rtc.help' command.
     */
    public void prompt() {
        System.out.print("> ");
        String input = scanner.nextLine();
        String objectName = "";
        int objectNameStopIndex = 0;
        if (input.equals("help")) {
            System.out.println("You need to begin every command with object name. Try rtc.help");
            return;
        }
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '.')
            {   
                objectName = input.substring(0, i);
                objectNameStopIndex = i;
                break;
            }
        }
        if (objectName == "")
        {
            System.out.println("IonRTC: Object name not found!");
            return;
        }
        //System.out.println("IonRTC: Object " + objectName + " is selected");

        String command = "";
        int commandStopIndex = 0;
        for (int i = objectNameStopIndex; i < input.length(); i++) {
            if (input.charAt(i) == '(')
            {   
                command = input.substring(objectNameStopIndex + 1, i);
                commandStopIndex = i;
                break;
            }
        }
        if (command == "")
            command = input.substring(objectNameStopIndex + 1);

        String commandParams = "";
        for (int i = commandStopIndex; i < input.length(); i++) {
            if (input.charAt(i) == ')')
            {   
                commandParams = input.substring(commandStopIndex + 1, i);
                break;
            }
        }

        //System.out.println("IonRTC: Command " + command + " is detected with params " + commandParams);

        
        if (objectName.equals("rtc") || objectName.equals("RTC"))
        {
            switch(command) {
                case "help":
                    System.out.println("With rtc you can use:");
                    System.out.println(".check - checks that rtc works correctly");
                    System.out.println(".frames - lists the names for all imported Ion Frames");
                    System.out.println(".panels - lists the names for all imported Ion Panels");
                    System.out.println(".containers - lists the names for all imported Ion Containers");
                    System.out.println(".objects - lists the names for all imported Ion Objects");
                    System.out.println(".exit - exits the rtc");
                    System.out.println("\nType the name of any frame/panel/container/object imported or \"ion\" + .help to see available commands");
                    return;
                case "check":
                    System.out.println("Everything is working fine!");
                    return;
                case "frames":
                    System.out.println(framesMap.keySet());
                    return;
                case "panels":
                    System.out.println(panelsMap.keySet());
                    return;
                case "containers":
                    System.out.println(containersMap.keySet());
                    return;
                case "objects":
                    System.out.println(objectsMap.keySet());
                    return;
                case "exit":
                    exit();
                    return;
                default:
                    System.out.println("IonRTC:  " + command + " not found!");
                    return;
            }
        }
        if (objectName.equals("ion") || objectName.equals("ION"))
        {
            switch(command) {
                case "help":
                    System.out.println("With ion you can use:");
                    System.out.println(".version - to check the engine version");
                    System.out.println(".exit - to stop the program");
                    return;
                case "version":
                    System.out.println("IonEngine version 1.0.0");
                    return;
                case "exit":
                    System.out.println("Goodbye!");
                    System.exit(0);
                    return;
                default:
                    System.out.println("IonRTC:  " + command + " not found!");
                    return;
                
            }
        }
        for (String frameName: framesMap.keySet())
        {
            if (objectName.equals(frameName))
            {
                switch(command) {
                    case "help":
                        System.out.println(".setVisible + (true/false) - sets the frame visibility");
                        return;
                    case "setVisible":
                        if (commandParams.equals("true"))
                            framesMap.get(frameName).setVisible(true);
                        else if (commandParams.equals("false"))
                            framesMap.get(frameName).setVisible(false);
                        else
                            System.out.println("IonRTC: command error! " + commandParams + " cannot be correctly parsed as boolean!");
                        return;
                    default:
                        System.out.println("IonRTC:  " + command + " not found!");
                        return;

                        
                }
            }
        }
        for (String panelName: panelsMap.keySet())
        {
            if (objectName.equals(panelName))
            {
                switch(command) {
                    //!add Add some useful rtc functions for Ion Panels
                    case "help":
                        System.out.println(".importContainers - imports appended to the panel containers to the RTC");
                        return;
                    case "importContainers":
                        try {
                            addPanelAppended(objectName);
                        }
                        catch (Exception e) {
                            System.out.println("IonRTC: command error! Panel " + objectName + " not found!");
                        }
                        return;
                    default:
                        System.out.println("IonRTC:  " + command + " not found!");
                        return;
                }
            }
        }
        for (String containerName: containersMap.keySet())
        {
            if (objectName.equals(containerName))
            {
                switch(command) {
                    case "help":
                        System.out.println(".changeBorder + integer - changes container's border to the specified thickness");
                        System.out.println(".setWidth + integer - sets container's width to the specified value");
                        System.out.println(".setHeight + integer - sets (surprise surprise) container's height to the specified value");
                        System.out.println(".setWidthAuto + true/false - turns container's autowidth on and off accordingly");
                        System.out.println(".setHeightAuto + true/false - turns container's autoheight on and off accordingly");
                        System.out.println(".setX + int - sets container's x coordinate to the specified value");
                        System.out.println(".setY + int - sets container's y coordinate to the specified value");
                        System.out.println(".importObjects - imports appended to the panel containers to the RTC");
                        return;
                    case "changeBorder":
                        try {
                            containersMap.get(containerName).changeBorder(Integer.parseInt(commandParams));
                        }
                        catch (Exception e) {
                            System.out.println("IonRTC: command error! " + commandParams + " cannot be correctly parsed as int");
                        }
                        return;

                    case "setWidth":
                        try {
                            containersMap.get(containerName).setWidth(Integer.parseInt(commandParams));
                        }
                        catch (Exception e) {
                            System.out.println("IonRTC: command error! " + commandParams + " cannot be correctly parsed as int");
                        }
                        return;

                    case "setHeight":
                        try {
                            containersMap.get(containerName).setHeight(Integer.parseInt(commandParams));
                        }
                        catch (Exception e) {
                            System.out.println("IonRTC: command error! " + commandParams + " cannot be correctly parsed as int");
                        }
                        return;

                    case "setWidthAuto":
                        if (commandParams.equals("true"))
                            containersMap.get(containerName).setWidthAuto(true);
                        else if (commandParams.equals("false"))
                            containersMap.get(containerName).setWidthAuto(false);
                        else
                            System.out.println("IonRTC: command error! " + commandParams + " cannot be correctly parsed as boolean!");
                        return;
                    case "setHeightAuto":
                        if (commandParams.equals("true"))
                            containersMap.get(containerName).setHeightAuto(true);
                        else if (commandParams.equals("false"))
                            containersMap.get(containerName).setHeightAuto(false);
                        else
                            System.out.println("IonRTC: command error! " + commandParams + " cannot be correctly parsed as boolean!");
                        return;
                    
                    case "setX":
                        try {
                            containersMap.get(containerName).setX(Integer.parseInt(commandParams));
                        }
                        catch (Exception e) {
                            System.out.println("IonRTC: command error! " + commandParams + " cannot be correctly parsed as int");
                        }
                        return;

                    case "setY":
                        try {
                            containersMap.get(containerName).setY(Integer.parseInt(commandParams));
                        }
                        catch (Exception e) {
                            System.out.println("IonRTC: command error! " + commandParams + " cannot be correctly parsed as int");
                        }
                        return;

                    case "importObjects":
                        try {
                            addContainerAppended(objectName);
                        }
                        catch (Exception e) {
                            System.out.println("IonRTC: command error! Container " + objectName + " not found!");
                        }
                        return;
                    
                    default:
                        System.out.println("IonRTC:  " + command + " not found!");
                        return;
                    
                }
            }
        }
        for (String objectNameMap: objectsMap.keySet())
        {
            if (objectName.equals(objectNameMap))
            {
                switch(command) {
                    case "help":
                        System.out.println(".setWidth + integer - sets object's width to the specified value");
                        System.out.println(".setHeight + integer - sets (surprise surprise) object's height to the specified value");
                        System.out.println(".setX + int - sets object's x coordinate to the specified value");
                        System.out.println(".setY + int - sets object's y coordinate to the specified value");
                        System.out.println(".setZ + int - sets object's z index to the specified value");
                        System.out.println(".moveUp - move the object up by z index");
                        return;
                    case "setWidth":
                        try {
                            objectsMap.get(objectNameMap).setWidth(Integer.parseInt(commandParams));
                        }
                        catch (Exception e) {
                            System.out.println("IonRTC: command error! " + commandParams + " cannot be correctly parsed as int");
                        }
                        return;

                    case "setHeight":
                        try {
                            objectsMap.get(objectNameMap).setHeight(Integer.parseInt(commandParams));
                        }
                        catch (Exception e) {
                            System.out.println("IonRTC: command error! " + commandParams + " cannot be correctly parsed as int");
                        }
                        return;

                    case "setX":
                        try {
                            objectsMap.get(objectNameMap).setX(Integer.parseInt(commandParams));
                        }
                        catch (Exception e) {
                            System.out.println("IonRTC: command error! " + commandParams + " cannot be correctly parsed as int");
                        }
                        return;

                    case "setY":
                        try {
                            objectsMap.get(objectNameMap).setY(Integer.parseInt(commandParams));
                        }
                        catch (Exception e) {
                            System.out.println("IonRTC: command error! " + commandParams + " cannot be correctly parsed as int");
                        }
                        return;

                    case "setZ":
                        try {
                            objectsMap.get(objectNameMap).setZIndex(Integer.parseInt(commandParams));
                        }
                        catch (Exception e) {
                            System.out.println("IonRTC: command error! " + commandParams + " cannot be correctly parsed as int");
                        }
                        return;

                    case "moveUp":
                            objectsMap.get(objectNameMap).moveUp();
                        return;
                    
                    default:
                        System.out.println("IonRTC:  " + command + " not found!");
                        return;
                    
                }
            }
        }

    }


}