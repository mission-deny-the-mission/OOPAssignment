import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Contents of main window is setup in this class
class Contents extends JPanel {
    // text area for scripts
    private final JTextArea scripArea;
    // actual turtle graphics panel
    private final ExtendedTurtleGraphics graphicsPanel;
    // text field for single commands
    private final JTextField commandArea;

    // converts string to Color object using case statement
    // throws an exception if the string does not translate to a valid colour
    private Color colourCase(String colourName) throws Exception {
        switch (colourName) {
            case "black":
                return Color.black;
            case "white":
                return Color.white;
            case "red":
                return Color.red;
            case "green":
                return Color.green;
            case "yellow":
                return Color.yellow;
            case "blue":
                return Color.blue;
            case "cyan":
                return Color.cyan;
            case "magenta":
                return Color.magenta;
            case "pink":
                return Color.pink;
            default:
                throw new Exception("Invalid colour");
        }
    }

    // executes one line of turtle graphics code
    // returns true if the line could not be executed/was invalid
    // and false if the line was executed successfully
    private boolean executeLine(String line) {
        // split line into segments
        String[] lineSections = line.split(" ");
        // if the line has no segments it must be invalid
        // so return true
        if (lineSections.length == 0) {
            return true;
        }
        switch (lineSections[0]) {
            case "penup":
                // penup should have only one word
                if (lineSections.length == 1) {
                    graphicsPanel.penUp();
                } else {
                    return true;
                }
                break;
            case "pendown":
                // pendown should have only one word
                if (lineSections.length == 1) {
                    graphicsPanel.penDown();
                } else {
                    return true;
                }
                break;
            case "forward":
                // forward command should be followed by a number giving it two segments
                if (lineSections.length == 2) {
                    // try converting the number to an integer and executing the forward command
                    try {
                        int amount = Integer.parseInt(lineSections[1]);
                        graphicsPanel.forward(amount);
                    } catch (NumberFormatException exception) {
                        // if the number could not be converted return true to indicate that the line was invalid
                        return true;
                    }
                }
                break;
            case "backward":
                // backward command should have a length giving it two segments
                if (lineSections.length == 2) {
                    try {
                        int amount = Integer.parseInt(lineSections[1]);
                        // executing forward with a negative argument causes the turtle to move backwards
                        graphicsPanel.forward(-amount);
                    } catch (NumberFormatException exception) {
                        // return true if number could not be converted to an integer
                        return true;
                    }
                }
                break;
            case "turnright":
                // turn right can have no or one arguments
                if (lineSections.length == 1) {
                    // if no arguments supplied execute command
                    graphicsPanel.turnRight();
                } else if (lineSections.length == 2) {
                    // if an argument is supplied
                    try {
                        // attempt to convert the argument to an integer
                        int ammount = Integer.parseInt(lineSections[1]);
                        graphicsPanel.turnRight(ammount);
                    } catch (NumberFormatException exception) {
                        // and return true for invalid if it cannot be converted
                        return true;
                    }
                    // if the number of arguments is wrong return invalid
                } else {
                    return true;
                }
                break;
            case "turnleft":
                // turnleft can have no or one arguments
                if (lineSections.length == 1) {
                    graphicsPanel.turnLeft();
                } else if (lineSections.length == 2) {
                    try {
                        int amount = Integer.parseInt(lineSections[1]);
                        graphicsPanel.turnLeft(amount);
                    } catch (NumberFormatException exception) {
                        return true;
                    }
                } else {
                    return true;
                }
                break;
            case "clear":
                // clear can have no arguments
                if (lineSections.length == 1) {
                    graphicsPanel.clear();
                } else {
                    return true;
                }
                break;
            case "pencolour":
                // pencolour must have one argument
                if (lineSections.length == 2) {
                    try {
                        // colour case method defined earlier deals with converting the argument to a color object
                        graphicsPanel.setPenColour(colourCase(lineSections[1]));
                    } catch (Exception except) {
                        return true;
                    }
                } else {
                    return true;
                }
                break;
            case "circle":
                // circle command can have one argument
                if (lineSections.length == 2) {
                    try {
                        // that is to be converted to an integer
                        int radius = Integer.parseInt(lineSections[1]);
                        graphicsPanel.circle(radius);
                    } catch (NumberFormatException exception) {
                        return true;
                    }
                } else {
                    return true;
                }
                break;
            case "square":
                // square must have one argument
                if (lineSections.length == 2) {
                    try {
                        // that is to be converted to an integer
                        int radius = Integer.parseInt(lineSections[1]);
                        graphicsPanel.square(radius);
                    } catch (NumberFormatException exception) {
                        return true;
                    }
                } else {
                    return true;
                }
                break;
            default:
                return true;
        }
        return false;
    }

    // method to execute a script
    public void executeScript() {
        // get the script from the script area
        String rawCommands = scripArea.getText();
        // split the script into seperate lines
        String[] commands = rawCommands.split("\n");
        // for each line in the script decode and execute the command
        for (int i = 0; i < commands.length; i++) {
            // call the executeLine function to execute each line of the script
            if (executeLine(commands[i])) {
                // if the execute line function determines that there is an invalid command
                // show an error message/dialog
                String message = "Invalid command at line " + (i + 1);
                JOptionPane.showMessageDialog(null,
                        message,
                        "Invalid Command",
                        JOptionPane.ERROR_MESSAGE);
                // and return from the function early to prevent the rest of the script being run
                return;
            } else {
                // if a command executed successfully change the status of the imageSaved to false
                // this is so that if the user attempts to load an image without saving the current one
                // a dialog will come up warning them they have unsaved changes
                main.imageSaved = false;
            }
        }
    }

    // Action Listener to clear the text area when the clear button is pressed
    private class clearListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            commandArea.setText("");
        }

    }

    // Action Listener to execute command when the execute button is pressed
    private class executeListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            // get the text from the text area
            String text = commandArea.getText();
            // if the command is run then call the executeScript method to execute the script in the script area
            if (text.equals("run")) {
                executeScript();
            } else {
                // otherwise call execute line
                if (executeLine(text)) {
                    // if executeLine determines the command is invalid show an error dialog
                    JOptionPane.showMessageDialog(null,
                            "Command entered was invalid and could not be executed",
                            "Invalid command",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    // if the command run successfully change imageSaved to reflect the fact the image has changed
                    main.imageSaved = false;
                }
            }
        }
    }

    // constructor for this class
    // sets up different elements of the gui, action listeners, etc.
    Contents() {
        // set layout to border layout
        setLayout(new BorderLayout());

        // create graphics panel and other panels
        graphicsPanel = new ExtendedTurtleGraphics();
        // Panels for commands to be entered and the buttons to execute them
        JPanel commandPanel = new JPanel();
        JPanel commandTextPanel = new JPanel();

        // create panels for command input and associated buttons
        commandTextPanel.setLayout(new BoxLayout(commandTextPanel, BoxLayout.Y_AXIS));
        commandPanel.setLayout(new BoxLayout(commandPanel, BoxLayout.X_AXIS));

        // setup command area and script area that is used for entering commands
        commandArea = new JTextField();
        scripArea = new JTextArea();
        // setup scroll pane for script area so it does not overflow it's bounds
        // makes script area scrollable
        JScrollPane scriptScrollPane = new JScrollPane(scripArea);
        // set size for script area
        scriptScrollPane.setPreferredSize(new Dimension(500, 250));

        // create MenuBar
        // menu bar using custom class based on JMenuBar
        // defined in MenuBar.java
        MenuBar mbar = new MenuBar(graphicsPanel, new saveScriptListener(scripArea), new loadScriptListener(scripArea));

        // create buttons to clear and execute commands from the command area
        JButton executeCommands = new JButton("Execute");
        // buttons to clear and execute commands
        JButton clearCommands = new JButton("Clear");

        // add action listeners to the buttons that have just been created
        executeCommands.addActionListener(new executeListener());
        clearCommands.addActionListener(new clearListener());

        // add all the elements to their respective panels
        commandTextPanel.add(scriptScrollPane);
        commandTextPanel.add(commandArea);
        commandPanel.add(commandTextPanel);
        commandPanel.add(executeCommands);
        commandPanel.add(clearCommands);

        // add the panels to the overall gui
        add(BorderLayout.NORTH, mbar);
        add(BorderLayout.CENTER, graphicsPanel);
        add(BorderLayout.SOUTH, commandPanel);
    }
}

// main window
public class GUIinterface extends JFrame {

    GUIinterface() {
        super();
        // setup window contents
        Contents windowContents = new Contents();
        // add window contents to main window
        getContentPane().add(windowContents);
        // set close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // set size and title of main window
        setSize(800, 700);
        setTitle("Turtle Graphics");
        // make window visible
        setVisible(true);
    }

}
