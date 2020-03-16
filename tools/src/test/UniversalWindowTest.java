package test;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import appLogger.AppLogger;


public class UniversalWindowTest {
	
	AppLogger log =  AppLogger.getInstance();

	private JFrame frame;
	private JTextPane txtLog;
	private String title = "UniversalWindowTest     0.0";
	private JSplitPane splitPane;
	private JPanel panelStatus;
	private JPanel toolBar;
	private Component verticalStrut;
	private Component verticalStrut_1;

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
		
		log.setTextPane(txtLog);
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
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		toolBar = new JPanel();
		GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.insets = new Insets(0, 0, 5, 0);
		gbc_toolBar.fill = GridBagConstraints.BOTH;
		gbc_toolBar.gridx = 0;
		gbc_toolBar.gridy = 0;
		frame.getContentPane().add(toolBar, gbc_toolBar);
		GridBagLayout gbl_toolBar = new GridBagLayout();
		gbl_toolBar.columnWidths = new int[]{0, 0};
		gbl_toolBar.rowHeights = new int[]{0, 0};
		gbl_toolBar.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_toolBar.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		toolBar.setLayout(gbl_toolBar);
		
		verticalStrut = Box.createVerticalStrut(20);
		verticalStrut.setMaximumSize(new Dimension(0, 20));
		GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.fill = GridBagConstraints.VERTICAL;
		gbc_verticalStrut.gridx = 0;
		gbc_verticalStrut.gridy = 0;
		toolBar.add(verticalStrut, gbc_verticalStrut);
		
		splitPane = new JSplitPane();
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.insets = new Insets(0, 0, 5, 0);
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 1;
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
		
		panelStatus = new JPanel();
		panelStatus.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		GridBagConstraints gbc_panelStatus = new GridBagConstraints();
		gbc_panelStatus.fill = GridBagConstraints.BOTH;
		gbc_panelStatus.gridx = 0;
		gbc_panelStatus.gridy = 2;
		frame.getContentPane().add(panelStatus, gbc_panelStatus);
		GridBagLayout gbl_panelStatus = new GridBagLayout();
		gbl_panelStatus.columnWidths = new int[]{0, 0, 0};
		gbl_panelStatus.rowHeights = new int[]{0, 0};
		gbl_panelStatus.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panelStatus.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panelStatus.setLayout(gbl_panelStatus);
		
		verticalStrut_1 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_1 = new GridBagConstraints();
		gbc_verticalStrut_1.insets = new Insets(0, 0, 0, 5);
		gbc_verticalStrut_1.gridx = 0;
		gbc_verticalStrut_1.gridy = 0;
		panelStatus.add(verticalStrut_1, gbc_verticalStrut_1);
	}//initialize

}//class UniversalWindowTest
