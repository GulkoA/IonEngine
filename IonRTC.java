package GameEngine;
import java.util.Scanner;
import java.util.HashMap;

class IonRunTimeConsole{
    private boolean runtimeConsoleEnabled = false;
    private HashMap<String, IonFrame> framesMap = new HashMap<String, IonFrame>();
    private HashMap<String, IonPanel> panelsMap = new HashMap<String, IonPanel>();
    private HashMap<String, IonContainer> containersMap = new HashMap<String, IonContainer>();
    private HashMap<String, IonObject> objectsMap = new HashMap<String, IonObject>();

    public IonRunTimeConsole(IonFrame frame) {
        addFrame(frame, "frame");
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
        System.out.println("IonRTC: Object " + objectName + " is selected");

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
        {
            System.out.println("IonRTC: Command not found!");
            return;
        }

        String commandParams = "";
        for (int i = commandStopIndex; i < input.length(); i++) {
            if (input.charAt(i) == ')')
            {   
                commandParams = input.substring(commandStopIndex + 1, i);
                break;
            }
        }

        System.out.println("IonRTC: Command " + command + " is detected with params " + commandParams);


        if (objectName.equals("rtc") || objectName.equals("RTC"))
        {
            switch(command) {
                case "check":
                    System.out.println("Everything is working fine!");
                    return;
                case "displayFrames":
                    System.out.println(framesMap);
                    return;
                default:
                    System.out.println("IonRTC: Command" + command + " not found!");
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
                        break;
                        
                }
            }
        }

    }


}