package devlopment;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

public class WinTemplate_Simple {

	private JFrame frmTemplate;
	private JButton btnOne;
	private JButton btnTwo;
	private JButton btnThree;
	private JButton btnFour;
	private JSplitPane splitPane1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WinTemplate_Simple window = new WinTemplate_Simple();
					window.frmTemplate.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}//try
			}//run
		});
	}// main

	/* Standard Stuff */
	
	private void doBtnOne(){
		
	}//doBtnOne
	
	private void doBtnTwo(){
		
	}//doBtnTwo
	
	private void doBtnThree(){
		
	}//doBtnThree
	
	private void doBtnFour(){
		
	}//doBtnFour
	
	//---------------------------------------------------------
	
	private void doFileNew(){
		
	}//doFileNew
	private void doFileOpen(){
		
	}//doFileOpen
	private void doFileSave(){
		
	}//doFileSave
	private void doFileSaveAs(){
		
	}//doFileSaveAs
	private void doFilePrint(){
		
	}//doFilePrint
	private void doFileExit(){
		appClose();
		System.exit(0);
	}//doFileExit


	private void appClose() {
		Preferences myPrefs =  Preferences.userNodeForPackage(WinTemplate_Simple.class).node(this.getClass().getSimpleName());
		Dimension dim = frmTemplate.getSize();
		myPrefs.putInt("Height", dim.height);
		myPrefs.putInt("Width", dim.width);
		Point point = frmTemplate.getLocation();
		myPrefs.putInt("LocX", point.x);
		myPrefs.putInt("LocY", point.y);
		myPrefs.putInt("Divider", splitPane1.getDividerLocation());
		myPrefs = null;
	}//appClose

	private void appInit() {
		Preferences myPrefs =  Preferences.userNodeForPackage(WinTemplate_Simple.class).node(this.getClass().getSimpleName());
		frmTemplate.setSize(myPrefs.getInt("Width", 761), myPrefs.getInt("Height", 693));
		frmTemplate.setLocation(myPrefs.getInt("LocX", 100), myPrefs.getInt("LocY", 100));
		splitPane1.setDividerLocation(myPrefs.getInt("Divider", 250));
		myPrefs = null;
	}// appInit

	public WinTemplate_Simple() {
		initialize();
		appInit();
	}// Constructor

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTemplate = new JFrame();
		frmTemplate.setTitle("WinTemplate_Simple");
		frmTemplate.setBounds(100, 100, 450, 300);
		frmTemplate.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTemplate.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				appClose();
			}
		});
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 25, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		frmTemplate.getContentPane().setLayout(gridBagLayout);
		
		JPanel panelForButtons = new JPanel();
		GridBagConstraints gbc_panelForButtons = new GridBagConstraints();
		gbc_panelForButtons.anchor = GridBagConstraints.NORTH;
		gbc_panelForButtons.insets = new Insets(0, 0, 5, 0);
		gbc_panelForButtons.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelForButtons.gridx = 0;
		gbc_panelForButtons.gridy = 0;
		frmTemplate.getContentPane().add(panelForButtons, gbc_panelForButtons);
		GridBagLayout gbl_panelForButtons = new GridBagLayout();
		gbl_panelForButtons.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panelForButtons.rowHeights = new int[]{0, 0};
		gbl_panelForButtons.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelForButtons.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panelForButtons.setLayout(gbl_panelForButtons);
		
		btnOne = new JButton("Button 1");
		btnOne.setMinimumSize(new Dimension(100, 20));
		GridBagConstraints gbc_btnOne = new GridBagConstraints();
		gbc_btnOne.insets = new Insets(0, 0, 0, 5);
		gbc_btnOne.gridx = 0;
		gbc_btnOne.gridy = 0;
		panelForButtons.add(btnOne, gbc_btnOne);
		btnOne.setAlignmentX(Component.RIGHT_ALIGNMENT);
		btnOne.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		btnOne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doBtnOne();
			}
		});
		btnOne.setMaximumSize(new Dimension(0, 0));
		btnOne.setPreferredSize(new Dimension(100, 20));
		
		btnTwo = new JButton("Button 2");
		btnTwo.setMinimumSize(new Dimension(100, 20));
		GridBagConstraints gbc_btnTwo = new GridBagConstraints();
		gbc_btnTwo.insets = new Insets(0, 0, 0, 5);
		gbc_btnTwo.gridx = 1;
		gbc_btnTwo.gridy = 0;
		panelForButtons.add(btnTwo, gbc_btnTwo);
		btnTwo.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		btnTwo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doBtnTwo();
			}
		});
		btnTwo.setPreferredSize(new Dimension(100, 20));
		btnTwo.setMaximumSize(new Dimension(0, 0));
		
		btnThree = new JButton("Button 3");
		btnThree.setMinimumSize(new Dimension(100, 20));
		GridBagConstraints gbc_btnThree = new GridBagConstraints();
		gbc_btnThree.insets = new Insets(0, 0, 0, 5);
		gbc_btnThree.gridx = 3;
		gbc_btnThree.gridy = 0;
		panelForButtons.add(btnThree, gbc_btnThree);
		btnThree.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		btnThree.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doBtnThree();
			}
		});
		btnThree.setPreferredSize(new Dimension(100, 20));
		btnThree.setMaximumSize(new Dimension(0, 0));
		
		btnFour = new JButton("Button 4");
		btnFour.setMinimumSize(new Dimension(100, 20));
		GridBagConstraints gbc_btnFour = new GridBagConstraints();
		gbc_btnFour.gridx = 4;
		gbc_btnFour.gridy = 0;
		panelForButtons.add(btnFour, gbc_btnFour);
		btnFour.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		btnFour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doBtnFour();
			}
		});
		btnFour.setPreferredSize(new Dimension(100, 20));
		btnFour.setMaximumSize(new Dimension(0, 0));
		
		splitPane1 = new JSplitPane();
		GridBagConstraints gbc_splitPane1 = new GridBagConstraints();
		gbc_splitPane1.insets = new Insets(0, 0, 5, 0);
		gbc_splitPane1.fill = GridBagConstraints.BOTH;
		gbc_splitPane1.gridx = 0;
		gbc_splitPane1.gridy = 1;
		frmTemplate.getContentPane().add(splitPane1, gbc_splitPane1);
		
		JPanel panelLeft = new JPanel();
		splitPane1.setLeftComponent(panelLeft);
		GridBagLayout gbl_panelLeft = new GridBagLayout();
		gbl_panelLeft.columnWidths = new int[]{0};
		gbl_panelLeft.rowHeights = new int[]{0};
		gbl_panelLeft.columnWeights = new double[]{Double.MIN_VALUE};
		gbl_panelLeft.rowWeights = new double[]{Double.MIN_VALUE};
		panelLeft.setLayout(gbl_panelLeft);
		
		JPanel panelRight = new JPanel();
		splitPane1.setRightComponent(panelRight);
		GridBagLayout gbl_panelRight = new GridBagLayout();
		gbl_panelRight.columnWidths = new int[]{0};
		gbl_panelRight.rowHeights = new int[]{0};
		gbl_panelRight.columnWeights = new double[]{Double.MIN_VALUE};
		gbl_panelRight.rowWeights = new double[]{Double.MIN_VALUE};
		panelRight.setLayout(gbl_panelRight);
		splitPane1.setDividerLocation(250);
		
		JPanel panelStatus = new JPanel();
		panelStatus.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		GridBagConstraints gbc_panelStatus = new GridBagConstraints();
		gbc_panelStatus.fill = GridBagConstraints.BOTH;
		gbc_panelStatus.gridx = 0;
		gbc_panelStatus.gridy = 2;
		frmTemplate.getContentPane().add(panelStatus, gbc_panelStatus);

		JMenuBar menuBar = new JMenuBar();
		frmTemplate.setJMenuBar(menuBar);
		
		JMenu mnuFile = new JMenu("File");
		menuBar.add(mnuFile);
		
		JMenuItem mnuFileNew = new JMenuItem("New");
		mnuFileNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doFileNew();
			}
		});
		mnuFile.add(mnuFileNew);
		
		JMenuItem mnuFileOpen = new JMenuItem("Open...");
		mnuFileOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doFileOpen();
			}
		});
		mnuFile.add(mnuFileOpen);
		
		JSeparator separator = new JSeparator();
		mnuFile.add(separator);
		
		JMenuItem mnuFileSave = new JMenuItem("Save...");
		mnuFileSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doFileSave();
			}
		});
		mnuFile.add(mnuFileSave);
		
		JMenuItem mnuFileSaveAs = new JMenuItem("Save As...");
		mnuFileSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doFileSaveAs();
			}
		});
		mnuFile.add(mnuFileSaveAs);
		
		JSeparator separator_2 = new JSeparator();
		mnuFile.add(separator_2);
		
		JMenuItem mnuFilePrint = new JMenuItem("Print...");
		mnuFilePrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doFilePrint();
			}
		});
		mnuFile.add(mnuFilePrint);
		
		
		JSeparator separator_1 = new JSeparator();
		mnuFile.add(separator_1);
		
		JMenuItem mnuFileExit = new JMenuItem("Exit");
		mnuFileExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doFileExit();
			}
		});
		mnuFile.add(mnuFileExit);

		

	}// initialize

}// class GUItemplate