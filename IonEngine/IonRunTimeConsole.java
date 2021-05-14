package IonEngine;
import java.util.Scanner;
import java.util.HashMap;

public class IonRunTimeConsole {
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
        addPanel(framesMap.get(frameName).getPanel(), "panel");
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


    public void start() {
        runtimeConsoleEnabled = true;
        System.out.println("Ion runtime console enabled");
        while (runtimeConsoleEnabled)
            prompt();
    }

    public void stop() {
        runtimeConsoleEnabled = false;
        System.out.println("Ion runtime console disabled");
    }


    public void prompt() {
        System.out.print("> ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String objectName = "";
        int objectNameStopIndex = 0;
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
                    System.out.println(".displayFrames - lists the names for all imported Ion Frames");
                    System.out.println(".displayPanels - lists the names for all imported Ion Panels");
                    System.out.println(".displayContainers - lists the names for all imported Ion Containers");
                    System.out.println(".displayObjects - lists the names for all imported Ion Objects");
                    System.out.println(".stop - stops the rtc");
                case "check":
                    System.out.println("Everything is working fine!");
                    return;
                case "displayFrames":
                    System.out.println(framesMap.keySet());
                    return;
                case "displayPanels":
                    System.out.println(panelsMap.keySet());
                    return;
                case "displayContainers":
                    System.out.println(containersMap.keySet());
                    return;
                case "displayObjects":
                    System.out.println(objectsMap.keySet());
                    return;
                case "stop":
                    stop();
                    return;
                default:
                    System.out.println("IonRTC:  " + command + " not found!");
                    return;
            }
        }
        if (objectName.equals("ion") || objectName.equals("ION"))
        {
            switch(command) {
                case "version":
                    System.out.println("Are you serious?! You are the developer and this thing is not even published yet. It is version -11 if you wish.");
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
                }
            }
        }
        for (String containerName: containersMap.keySet())
        {
            if (objectName.equals(containerName))
            {
                switch(command) {
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
                    default:
                        System.out.println("IonRTC:  " + command + " not found!");
                        return;
                    
                }
            }
        }

    }


}