import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

// handles saving the script that is currently in the script area
// it does this by saving it to a text file
class saveScriptListener implements ActionListener {
    private final JTextArea scriptArea;

    saveScriptListener(JTextArea scriptArea) {
        this.scriptArea = scriptArea;
    }

    public void actionPerformed(ActionEvent event) {
        // get the text from the script area that needs to be saved
        String textToSave = scriptArea.getText();
        // dialog to ask user where they would like the file to be saved
        JFileChooser fileChooser = new JFileChooser();
        // with a file filter for .tsc turtle script files
        FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("turtle script file",
                ".tsc");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(fileFilter);
        int returnVal = fileChooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            // if the user tries to save a file with the wrong or missing extension add .tsc to the end
            File file = fileChooser.getSelectedFile();
            if (!file.toString().endsWith(".tsc")) {
                file = new File(file.toString() + ".tsc");
            }
            try {
                // write text to file
                FileWriter writer = new FileWriter(file.getAbsolutePath());
                writer.write(textToSave);
                writer.close();
            } catch (IOException e) {
                // if the file could not be written to for some reason display an error message
                JOptionPane.showMessageDialog(null,
                        "An IO error occurred when trying to save your file",
                        "IO Error",
                        JOptionPane.ERROR_MESSAGE);
            }


        }
    }
}

// class to handle loading a script into the script area from a file
class loadScriptListener implements ActionListener {
    // class needs access to script area so this is defined as a attibute
    private final JTextArea scriptArea;

    // and passed as an argument to the constructor
    loadScriptListener(JTextArea scriptArea) {
        this.scriptArea = scriptArea;
    }

    // run when save script menu option is pressed
    public void actionPerformed(ActionEvent event) {
        // dialog to allow user to chooser where to save the file
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            // get the file the user selected
            File file = fileChooser.getSelectedFile();
            try {
                // clear script area ready for new file
                scriptArea.setText("");
                // scan file line by line using scanner class and add it to the script area
                Scanner fileScanner = new Scanner(file);
                while (fileScanner.hasNextLine()) {
                    scriptArea.append(fileScanner.nextLine() + '\n');
                }
                fileScanner.close();
            } catch (IOException exception) {
                // if file could not be read display an error message
                JOptionPane.showMessageDialog(null,
                        "An IO error occurred while trying to load your script.",
                        "IO Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

// class for the menubar containing file operations
public class MenuBar extends JMenuBar {
    // this class needs access to the turtle graphics panel to function
    private final ExtendedTurtleGraphics graphicsPanel;
    // this is used to create a dialog if the user attempts to load a new file while the current one has not been saved
    private final threeWayDialog saveChanges;

    // resets graphics panel if the new button has been pressed
    private class newListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            graphicsPanel.clear();
            graphicsPanel.reset();
            main.imageSaved = true;
        }
    }

    // deals with displaying the example image if the about menu item is selected
    private class aboutListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            graphicsPanel.about();
        }
    }

    // deals with saving image when save menu item is selected
    private class saveListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            // dialog for allowing user to select where to save the file
            JFileChooser fileChooser = new JFileChooser();
            // with file filter for png images
            FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("image/png", "png");
            fileChooser.addChoosableFileFilter(fileFilter);
            fileChooser.setFileFilter(fileFilter);
            int returnVal = fileChooser.showSaveDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                // get the file the user selected
                File file = fileChooser.getSelectedFile();
                // get the image from the graphics panel
                BufferedImage imageToSave = graphicsPanel.getBufferedImage();
                try {
                    // write the image to the file
                    ImageIO.write(imageToSave, "PNG", file);
                } catch (IOException exception) {
                    // show an error message if the file could not be saved for some reason
                    JOptionPane.showMessageDialog(null,
                            "An IO error occurred when trying to save your file",
                            "IO Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // when the user tried to open an image, without saving the current one, this window is displayed
    // I devised this when I could not find a dialog built into java that supported having three buttons
    private class threeWayDialog extends JFrame implements ActionListener {
        final private JButton button1, button2, button3;

        threeWayDialog() {
            super();
            setSize(400, 400);
            setTitle("Image not saved");

            FlowLayout layout = new FlowLayout();
            setLayout(layout);

            // buttons for the three options
            JTextPane message = new JTextPane();
            button1 = new JButton("Save image");
            button2 = new JButton("Discard image");
            button3 = new JButton("Cancel");

            // action listener is this class
            button1.addActionListener(this);
            button2.addActionListener(this);
            button3.addActionListener(this);

            message.setText("The current image is not saved");

            add(message);
            add(button1);
            add(button2);
            add(button3);

            setVisible(false);
        }

        public void showDialog() {
            setVisible(true);
        }

        // used for the three buttons
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == button1) {
                setVisible(false);
                // if user selected to save the current file then save the current file
                (new saveListener()).actionPerformed(event);
                // then load the new file
                (new loadListener()).loadFile();
            } else if (event.getSource() == button2) {
                setVisible(false);
                (new loadListener()).loadFile();
            } else if (event.getSource() == button3) {
                setVisible(false);
            }
        }
    }

    // class that handles loading new files with the help of threeWayDialog
    private class loadListener implements ActionListener {

        // method that runs when item is selected
        public void actionPerformed(ActionEvent event) {
            // if the image has been save begin loading the file
            if (main.imageSaved) {
                loadFile();
            } else {
                // otherwise display three way dialog
                saveChanges.showDialog();
            }
        }

        // method that actually handles loading the file
        private void loadFile() {
            // dialog to get user to choose which file they would like to load
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("image/png", "png");
            fileChooser.addChoosableFileFilter(fileFilter);
            fileChooser.setFileFilter(fileFilter);
            int returnVal = fileChooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                // get the selected file from the dialog
                File file = fileChooser.getSelectedFile();
                BufferedImage imageData;
                try {
                    // read image from file into an image buffer
                    imageData = ImageIO.read(file);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null,
                            "An IO error occurred when trying to load your file",
                            "IO Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // set the contents of the graphics panel to the buffered image
                graphicsPanel.setBufferedImage(imageData);
                // set flag to show image has been saved
                main.imageSaved = true;
            }
        }
    }

    // action listener which closes program when user selects exit from menu
    private static class exitListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            System.exit(0);
        }
    }

    // constructor for menubar
    MenuBar(ExtendedTurtleGraphics gp, saveScriptListener ssl, loadScriptListener lsl) {
        // get graphics panel from invoking method
        graphicsPanel = gp;

        // create file menu and help menu
        JMenu file = new JMenu("File");
        JMenu help = new JMenu("Help");

        // create menu items
        JMenuItem newFile = new JMenuItem("New");
        JMenuItem load = new JMenuItem("Load");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem loadScript = new JMenuItem("Load Script");
        JMenuItem saveScript = new JMenuItem("Save Script");
        JMenuItem exit = new JMenuItem("Exit");
        JMenuItem about = new JMenuItem("about");

        // setup three way dialog ready for use
        saveChanges = new threeWayDialog();

        // add action listeners to menu items
        newFile.addActionListener(new newListener());
        about.addActionListener(new aboutListener());
        load.addActionListener(new loadListener());
        save.addActionListener(new saveListener());
        loadScript.addActionListener(lsl);
        saveScript.addActionListener(ssl);
        exit.addActionListener(new exitListener());

        // add menu items to their respective menus
        file.add(newFile);
        file.add(load);
        file.add(save);
        file.add(loadScript);
        file.add(saveScript);
        file.add(exit);
        help.add(about);

        // add menus to the menu bar
        add(file);
        add(help);
    }
}