import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

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
            }


        }
    }
}

class loadScriptListener implements ActionListener {
    private JTextArea scriptArea;

    loadScriptListener(JTextArea scriptArea) {
        this.scriptArea = scriptArea;
    }

    public void actionPerformed(ActionEvent event) {
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                Scanner fileScanner = new Scanner(file);
                scriptArea.setText("");
                while (fileScanner.hasNextLine()) {
                    scriptArea.append(fileScanner.nextLine() + '\n');
                }
                fileScanner.close();
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(null,
                        "An IO error occurred while trying to load your script.",
                        "IO Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

public class MenuBar extends JMenuBar {
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

    MenuBar(ExtendedTurtleGraphics gp, saveScriptListener ssl, loadScriptListener lsl) {
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
        loadScript.addActionListener(lsl);
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