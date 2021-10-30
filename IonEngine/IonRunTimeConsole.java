package IonEngine;
import java.util.Scanner;
import java.util.HashMap;

public class IonRunTimeConsole extends Thread {
    private boolean runtimeConsoleEnabled = false;
    private HashMap<String, IonFrame> framesMap = new HashMap<String, IonFrame>();
    private HashMap<String, IonPanel> panelsMap = new HashMap<String, IonPanel>();
    private HashMap<String, IonContainer> containersMap = new HashMap<String, IonContainer>();
    private HashMap<String, IonObject> objectsMap = new HashMap<String, IonObject>();

    public IonRunTimeConsole(IonFrame frame) {
        addFrame(frame, "frame");
        addFrameAppended("frame");
    }
    public IonRunTimeConsole() {}

    public IonFrame addFrame(IonFrame frame, String name) {
        framesMap.put(name, frame);
        return frame;
    }

    public void addFrameAppended(String frameName) {
        for (IonPanel panel: framesMap.get(frameName).getPanelList())
        addPanel(panel, "panel");
        addPanelAppended("panel");
    }

    public IonPanel addPanel(IonPanel panel, String name) {
        panelsMap.put(name, panel);
        return panel;
    }

    public void addPanelAppended(String panelName) {
        containersMap.putAll(panelsMap.get(panelName).getContainerMap());
    }

    public IonContainer addContainer(IonContainer container, String name) {
        containersMap.put(name, container);
        return container;
    }

    public void addContainerAppended(String containerName) {
        objectsMap.putAll(containersMap.get(containerName).getObjectMap());
    }

    public IonObject addObject(IonObject object, String name) {
        objectsMap.put(name, object);
        return object;
    }

    public void run() {
        runtimeConsoleEnabled = true;
        System.out.println("Ion runtime console enabled! Type rtc.help for the command list");
        while (runtimeConsoleEnabled)
            prompt();
    }

    public void exit() {
        runtimeConsoleEnabled = false;
        System.out.println("Ion runtime console disabled");
    }


    public void prompt() {
        System.out.print("> ");
        Scanner scanner = new Scanner(System.in);
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
                    System.out.println("Are you serious?! You are the developer and this thing is not even published yet. It is version -11 if you wish.");
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