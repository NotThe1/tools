package devlopment;

import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ToolTipManager;


public class PropertiesList extends JList<Object> {
	
	SortedListModel model;
	Properties tipProps;
	
	public PropertiesList(Properties props) {
		model = new SortedListModel();
		setModel(model);
		ToolTipManager.sharedInstance().registerComponent(this);
		
		tipProps = props;
		addProperties(props);
	}//Constructor
	
	private void addProperties(Properties props) {
		//Load
		Enumeration<?> names = props.propertyNames();
		while (names.hasMoreElements()){
			model.add(names.nextElement());
		}//while
	}//addProperties
	
	public String getToolTipText(MouseEvent mouseEvent) {
		Point p = mouseEvent.getPoint();
		int location = locationToIndex(p);
		String key = (String) model.getElementAt(location);
		String tip = tipProps.getProperty(key);
		return tip;	
	}//getToolTipText
	

	
	public static void main(String[] args) {
		Runnable runner = new Runnable() {
			public void run() {
				JFrame frame = new JFrame("Properties List ");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				Properties props = System.getProperties();
				PropertiesList list = new PropertiesList(props);
				JScrollPane scrollPane = new JScrollPane(list);
				
				frame.add(scrollPane);

				frame.setSize(300, 300);
				frame.setVisible(true);
			}// run
		};// Runnable runner
		EventQueue.invokeLater(runner);
	}// main
	
	
}//class PropertiesList
