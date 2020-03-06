package test;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import appLogger.AppLogger;


public class UniversalWindowTest {
	
	AppLogger log =  AppLogger.getInstance();

	private JFrame frame;
	private JTextPane txtLog;
	private String title = "UniversalWindowTest     0.0";
	private JSplitPane splitPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UniversalWindowTest window = new UniversalWindowTest();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}//run
		});
	}//main

	/**
	 * Create the application.
	 */
	
//	private Preferences getPreferences() {
//		return Preferences.userNodeForPackage(UniversalWindowTest.class).node(this.getClass().getSimpleName());
//	}//getPreferences
	
	private void appClose() {
//		Preferences myPrefs =  getPreferences();
//		Dimension dim = frame.getSize();
//		myPrefs.putInt("Height", dim.height);
//		myPrefs.putInt("Width", dim.width);
//		Point point = frame.getLocation();
//		myPrefs.putInt("LocX", point.x);
//		myPrefs.putInt("LocY", point.y);
//		myPrefs.putInt("Divider", splitPane.getDividerLocation());
//		myPrefs = null;
	}//appClose

	private void appInit() {
		frame.setBounds(100, 100, 736, 488);

//		Preferences myPrefs =  getPreferences();
//		frame.setSize(myPrefs.getInt("Width", 761), myPrefs.getInt("Height", 693));
//		frame.setLocation(myPrefs.getInt("LocX", 100), myPrefs.getInt("LocY", 100));
//		splitPane.setDividerLocation(myPrefs.getInt("Divider", 250));
//		myPrefs = null;
		
		log.setDoc(txtLog.getStyledDocument());
		log.addTimeStamp("Starting....");

	}// appInit
	public UniversalWindowTest() {
		initialize();
		appInit();
	}//Constructor

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				appClose();
			}//windowClosing
		});
		frame.setBounds(100, 100, 736, 488);
		frame.setTitle(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		splitPane = new JSplitPane();
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 0;
		frame.getContentPane().add(splitPane, gbc_splitPane);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setRightComponent(scrollPane);
		
		JLabel lblNewLabel = new JLabel("Application Log");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setForeground(Color.GREEN);
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
		scrollPane.setColumnHeaderView(lblNewLabel);
		
		txtLog = new JTextPane();
		scrollPane.setViewportView(txtLog);
	}//initialize

}//class UniversalWindowTest
