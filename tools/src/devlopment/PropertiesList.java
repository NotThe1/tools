package devlopment;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ToolTipManager;

public class PropertiesList extends JList<String> {
	private static final long serialVersionUID = 1L;

	SortedListModel model;
	Properties tipProps;

	public PropertiesList(Properties props) {
		setFont(new Font("Courier New", Font.BOLD, 16));
		model = new SortedListModel();
		setModel(model);
		ToolTipManager.sharedInstance().registerComponent(this);

		tipProps = props;
		addProperties(props);
	}// Constructor

	private void addProperties(Properties props) {
		// Load
		Enumeration<?> names = props.propertyNames();
		while (names.hasMoreElements()) {
			model.add((String) names.nextElement());
		} // while
	}// addProperties

	public String getToolTipText(MouseEvent mouseEvent) {
		Point p = mouseEvent.getPoint();
		int location = locationToIndex(p);
		String key = (String) model.getElementAt(location);
		String tip = tipProps.getProperty(key);
		tip = tip.equals("") ? "<no value>" : tip;
		return tip;
	}// getToolTipText

	public static void main(String[] args) {
		Runnable runner = new Runnable() {
			public void run() {
				JFrame frame = new JFrame("Properties List 1.0");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setIconImage(Toolkit.getDefaultToolkit().getImage(RegexDriver.class.getResource("/view-media-equalizer.png")));
//				frame.setIconImage(Toolkit.getDefaultToolkit().getImage(PropertiesList.class.getResource("/view-media-equalizer.png")));

				Properties props = System.getProperties();
				PropertiesList list = new PropertiesList(props);
				JScrollPane scrollPane = new JScrollPane(list);

				frame.add(scrollPane);

				frame.setSize(400, 600);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}// run
		};// Runnable runner
		EventQueue.invokeLater(runner);
	}// main

}// class PropertiesList

class SortedListModel extends AbstractListModel<String> {

	private static final long serialVersionUID = 1L;
	SortedSet<String> model;

	public SortedListModel() {
		model = new TreeSet<String>();
	}// Constructor

	@Override
	public String getElementAt(int index) {
		return (String) model.toArray()[index];
	}// getElementAt

	public void add(String element) {
		if (model.add(element)) {
			fireContentsChanged(this, 0, getSize());
		} // if
	}// add

	@Override
	public int getSize() {
		return model.size();
	}// getSize

}// class SortedListModel<String>
