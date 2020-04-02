import uk.ac.leedsbeckett.oop.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class saveScriptListener implements ActionListener {
    private JTextArea scriptArea;

    saveScriptListener(JTextArea scriptArea) {
        this.scriptArea = scriptArea;
    }

    public void actionPerformed(ActionEvent event) {
        String textToSave = scriptArea.getText();
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                FileWriter writer = new FileWriter(file.getAbsolutePath());
                writer.write(textToSave);
                writer.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                        "An IO error occured when trying to save your file",
                        "IO Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }


        }
    }
}

class MenuBar extends JMenuBar {
    private JMenu file, help;
    private JMenuItem newFile, load, save, loadScript, saveScript, exit, about;
    private ExtendedTurtleGraphics graphicsPanel;

    private class newListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            graphicsPanel.clear();
            graphicsPanel.reset();
        }
    }

    private class aboutListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            graphicsPanel.about();
        }
    }

    private class saveListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            // TODO: setup default file extension
            JFileChooser fileChooser = new JFileChooser();
            int returnVal = fileChooser.showSaveDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                BufferedImage imageToSave = graphicsPanel.getBufferedImage();
                try {
                    ImageIO.write(imageToSave, "PNG", file);
                } catch (IOException exception) {
                    JOptionPane.showMessageDialog(null,
                            "An IO error occured when trying to save your file",
                            "IO Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class loadListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            // TODO: change to only except PNG files
            JFileChooser fileChooser = new JFileChooser();
            int returnVal = fileChooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                BufferedImage imageData = null;
                try {
                    imageData = ImageIO.read(file);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null,
                            "An IO error occured when tring to load your file",
                            "IO Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                graphicsPanel.setBufferedImage(imageData);
            }
        }
    }

    private class exitListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            System.exit(0);
        }
    }

    MenuBar(ExtendedTurtleGraphics gp, saveScriptListener ssl) {
        graphicsPanel = gp;

        file = new JMenu("File");
        help = new JMenu("Help");

        newFile = new JMenuItem("New");
        load = new JMenuItem("Load");
        save = new JMenuItem("Save");
        loadScript = new JMenuItem("Load Script");
        saveScript = new JMenuItem("Save Script");
        exit = new JMenuItem("Exit");
        about = new JMenuItem("about");

        newFile.addActionListener(new newListener());
        about.addActionListener(new aboutListener());
        load.addActionListener(new loadListener());
        save.addActionListener(new saveListener());
        saveScript.addActionListener(ssl);
        exit.addActionListener(new exitListener());

        file.add(newFile);
        file.add(load);
        file.add(save);
        file.add(loadScript);
        file.add(saveScript);
        file.add(exit);
        help.add(about);

        add(file);
        add(help);
        setVisible(true);
    }
}

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

        mbar = new MenuBar(graphicsPanel, new saveScriptListener(ta));

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

public class GUIinterface {

    public static void main(String[] args) {
        Contents windowContents = new Contents();
        JFrame frame = new JFrame();
        frame.getContentPane().add(windowContents);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 700);
    }

}
