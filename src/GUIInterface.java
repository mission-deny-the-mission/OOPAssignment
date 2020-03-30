import uk.ac.leedsbeckett.oop.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class MenuBar extends JMenuBar{
	private JMenu file, help;
	private JMenuItem newFile, load, save, exit, about;
	private TurtleGraphics graphicsPanel;
	class newListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			graphicsPanel.clear();
		}
	}
	class aboutListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			graphicsPanel.about();
		}
	}
	MenuBar(TurtleGraphics gp) {
		graphicsPanel = gp;
		
		file = new JMenu("File");
		help = new JMenu("Help");
		
		newFile = new JMenuItem("New");
		load = new JMenuItem("Load");
		save = new JMenuItem("Save");
		exit = new JMenuItem("Exit");
		about = new JMenuItem("about");
		
		newFile.addActionListener(new newListener());
		about.addActionListener(new aboutListener());
		
		file.add(newFile);
		file.add(load);
		file.add(save);
		file.add(exit);
		help.add(about);
		
		add(file);
		add(help);
		setVisible(true);
	}
}

class commandHandler implements ActionListener {
	private TurtleGraphics graphicsPanel;
	commandHandler(TurtleGraphics graphicsPanel) {
		this.graphicsPanel = graphicsPanel;
	}
	public void actionPerformed(ActionEvent event) {
		// write code here
	}
}

class Contents extends JPanel {
	private MenuBar mbar;
	private JTextArea ta;
	private JScrollPane sp;
	private TurtleGraphics graphicsPanel;
	Contents() {
		setLayout(new BorderLayout());
		
		graphicsPanel = new TurtleGraphics();
		mbar = new MenuBar(graphicsPanel);
		ta = new JTextArea();
		sp = new JScrollPane(ta);
		sp.setPreferredSize(new Dimension(500, 250));
		
		add(BorderLayout.NORTH, mbar);
		add(BorderLayout.CENTER, graphicsPanel);
		add(BorderLayout.SOUTH, sp);
	}
}
public class GUIInterface {

	public static void main(String[] args) {
		Contents windowContents = new Contents();
		JFrame frame = new JFrame();
		frame.getContentPane().add(windowContents);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 700);
	}

}
