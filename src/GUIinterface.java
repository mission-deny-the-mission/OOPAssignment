import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Contents extends JPanel {
    private MenuBar mbar;
    private JTextArea ta;
    private JScrollPane sp;
    private ExtendedTurtleGraphics graphicsPanel;
    private JPanel commandPanel, commandTextPanel;
    private JButton clearCommands, executeCommands;
    private JTextField tf;

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

    private boolean executeLine(String line) {
        String[] lineSections = line.split(" ");
        if (lineSections.length == 0) {
            return true;
        }
        switch (lineSections[0]) {
            case "penup":
                if (lineSections.length == 1) {
                    graphicsPanel.penUp();
                } else {
                    return true;
                }
                break;
            case "pendown":
                if (lineSections.length == 1) {
                    graphicsPanel.penDown();
                } else {
                    return true;
                }
                break;
            case "forward":
                if (lineSections.length == 2) {
                    try {
                        int ammount = Integer.parseInt(lineSections[1]);
                        graphicsPanel.forward(ammount);
                    } catch (NumberFormatException exception) {
                        return true;
                    }
                }
                break;
            case "backward":
                if (lineSections.length == 2) {
                    try {
                        int ammount = Integer.parseInt(lineSections[1]);
                        graphicsPanel.forward(-ammount);
                    } catch (NumberFormatException exception) {
                        return true;
                    }
                }
                break;
            case "turnright":
                if (lineSections.length == 1) {
                    graphicsPanel.turnRight();
                } else if (lineSections.length == 2) {
                    try {
                        int ammount = Integer.parseInt(lineSections[1]);
                        graphicsPanel.turnRight(ammount);
                    } catch (NumberFormatException exception) {
                        return true;
                    }
                } else {
                    return true;
                }
                break;
            case "turnleft":
                if (lineSections.length == 1) {
                    graphicsPanel.turnLeft();
                } else if (lineSections.length == 2) {
                    try {
                        int ammount = Integer.parseInt(lineSections[1]);
                        graphicsPanel.turnLeft(ammount);
                    } catch (NumberFormatException exception) {
                        return true;
                    }
                } else {
                    return true;
                }
                break;
            case "clear":
                if (lineSections.length == 1) {
                    graphicsPanel.clear();
                } else {
                    return true;
                }
                break;
            case "pencolour":
                if (lineSections.length == 2) {
                    try {
                        graphicsPanel.setPenColour(colourCase(lineSections[1]));
                    } catch (Exception except) {
                        return true;
                    }
                } else {
                    return true;
                }
                break;
            case "circle":
                if (lineSections.length == 2) {
                    try {
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
                if (lineSections.length == 2) {
                    try {
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

    public void executeScript() {
        String rawCommands = ta.getText();
        String[] commands = rawCommands.split("\n");
        int i;
        for (i = 0; i < commands.length; i++) {
            boolean invalid = executeLine(commands[i]);
            if (invalid) {
                String message = "Invalid command at line " + (i + 1);
                JOptionPane.showMessageDialog(null,
                        message,
                        "Invalid Command",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }

    private class clearListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            tf.setText("");
        }

    }

    private class executeListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String text = tf.getText();
            if (text.equals("run")) {
                executeScript();
            } else {
                executeLine(text);
            }
        }
    }

    Contents() {
        setLayout(new BorderLayout());


        graphicsPanel = new ExtendedTurtleGraphics();
        commandPanel = new JPanel();
        commandTextPanel = new JPanel();

        commandTextPanel.setLayout(new BoxLayout(commandTextPanel, BoxLayout.Y_AXIS));
        commandPanel.setLayout(new BoxLayout(commandPanel, BoxLayout.X_AXIS));

        tf = new JTextField();
        ta = new JTextArea();
        sp = new JScrollPane(ta);
        sp.setPreferredSize(new Dimension(500, 250));

        mbar = new MenuBar(graphicsPanel, new saveScriptListener(ta), new loadScriptListener(ta));

        executeCommands = new JButton("Execute");
        clearCommands = new JButton("Clear");

        executeCommands.addActionListener(new executeListener());
        clearCommands.addActionListener(new clearListener());

        commandTextPanel.add(sp);
        commandTextPanel.add(tf);
        commandPanel.add(commandTextPanel);
        commandPanel.add(executeCommands);
        commandPanel.add(clearCommands);

        add(BorderLayout.NORTH, mbar);
        add(BorderLayout.CENTER, graphicsPanel);
        add(BorderLayout.SOUTH, commandPanel);
    }
}

public class GUIinterface extends JFrame {

    GUIinterface() {
        super();
        Contents windowContents = new Contents();
        getContentPane().add(windowContents);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 700);
        setTitle("Turtle Graphics");
    }

}
