package test.hdnComponents;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.prefs.Preferences;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;

import myComponents.AppLogger;
import myComponents.hdnComponents.HDNumberBox;
import myComponents.hdnComponents.HDNumberValueChangeEvent;
import myComponents.hdnComponents.HDNumberValueChangeListener;
import myComponents.hdnComponents.HDSeekPanel;

public class hdnTest {

	private JFrame frmTemplate;
	private JButton btnOne;
	private JButton btnTwo;
	private JButton btnThree;
	private JButton btnFour;
	private JSplitPane splitPane1;

	private AppLogger log = AppLogger.getInstance();
	private JTextPane txtLog;
	private JPopupMenu popupLog;
	private AdapterLog logAdaper = new AdapterLog();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					hdnTest window = new hdnTest();
					window.frmTemplate.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				} // try
			}// run
		});
	}// main

	/* Standard Stuff */

	private void doBtnOne() {
//		DefaultBoundedRangeModel brm = new DefaultBoundedRangeModel();
		hdn1 = new HDNumberBox(Integer.MIN_VALUE,Integer.MAX_VALUE,0,true);
		hdn1.addHDNumberValueChangedListener(new HDNumberValueChangeListener() {

			@Override
			public void valueChanged(HDNumberValueChangeEvent hDNumberValueChangeEvent) {
				int oldValue = hDNumberValueChangeEvent.getOldValue();
				int newValue = hDNumberValueChangeEvent.getNewValue();
				log.specialf("Value changed from %d to %d",oldValue,newValue);
			}
			
		});
		

		for (int i = 0; i < 20; i++) {
			if((i %2) ==0) {
				hdn1.setValue(i);
				
			}else {
				hdn1.setValueQuiet(i);
			}
			log.infof(String.format("i = %d, value = %d", i, hdn1.getValue()));
		} // for
		
		

	}// doBtnOne

	private void doBtnTwo() {
		seekPanel.addHDNumberValueChangedListener(new HDNumberValueChangeListener() {
			@Override
			public void valueChanged(HDNumberValueChangeEvent hDNumberValueChangeEvent) {
				int oldValue = hDNumberValueChangeEvent.getOldValue();
				int newValue = hDNumberValueChangeEvent.getNewValue();
				log.specialf("Value changed from %d to %d",oldValue,newValue);	
			}
			
		});
		

	}// doBtnTwo

	private void doBtnThree() {

	}// doBtnThree

	private void doBtnFour() {

	}// doBtnFour

	// ---------------------------------------------------------

	private void doFileNew() {

	}// doFileNew

	private void doFileOpen() {

	}// doFileOpen

	private void doFileSave() {

	}// doFileSave

	private void doFileSaveAs() {

	}// doFileSaveAs

	private void doFilePrint() {

	}// doFilePrint

	private void doFileExit() {
		appClose();
		System.exit(0);
	}// doFileExit

	private void appClose() {
		Preferences myPrefs = Preferences.userNodeForPackage(hdnTest.class).node(this.getClass().getSimpleName());
		Dimension dim = frmTemplate.getSize();
		myPrefs.putInt("Height", dim.height);
		myPrefs.putInt("Width", dim.width);
		Point point = frmTemplate.getLocation();
		myPrefs.putInt("LocX", point.x);
		myPrefs.putInt("LocY", point.y);
		myPrefs.putInt("Divider", splitPane1.getDividerLocation());
		myPrefs = null;
	}// appClose

	private void appInit() {
		Preferences myPrefs = Preferences.userNodeForPackage(hdnTest.class).node(this.getClass().getSimpleName());
		frmTemplate.setSize(myPrefs.getInt("Width", 761), myPrefs.getInt("Height", 693));
		frmTemplate.setLocation(myPrefs.getInt("LocX", 100), myPrefs.getInt("LocY", 100));
		splitPane1.setDividerLocation(myPrefs.getInt("Divider", 250));
		myPrefs = null;

		txtLog.setText(EMPTY_STRING);

		log.setDoc(txtLog.getStyledDocument());
		log.info("Starting....");
		tb1.setSelected(false);
		tb1.setSelected(true);
		tb1.setSelected(false);

	}// appInit

	public hdnTest() {
		initialize();
		appInit();
	}// Constructor

	private void doLogClear() {
		log.clear();
	}// doLogClear

	private void doLogPrint() {

		Font originalFont = txtLog.getFont();
		try {
			// textPane.setFont(new Font("Courier New", Font.PLAIN, 8));
			txtLog.setFont(originalFont.deriveFont(8.0f));
			MessageFormat header = new MessageFormat("Identic Log");
			MessageFormat footer = new MessageFormat(new Date().toString() + "           Page - {0}");
			txtLog.print(header, footer);
			// textPane.setFont(new Font("Courier New", Font.PLAIN, 14));
			txtLog.setFont(originalFont);
		} catch (PrinterException e) {
			e.printStackTrace();
		} // try

	}// doLogPrint

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTemplate = new JFrame();
		frmTemplate.setTitle("hdn Components test    1.0");
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
		gbl_panelForButtons.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_panelForButtons.rowHeights = new int[] { 0, 0 };
		gbl_panelForButtons.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panelForButtons.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
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
		gbl_panelLeft.columnWidths = new int[] { 0, 0 };
		gbl_panelLeft.rowHeights = new int[] { 0, 0, 0 };
		gbl_panelLeft.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panelLeft.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		panelLeft.setLayout(gbl_panelLeft);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 255), 1, true), "Simple hdn",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		panelLeft.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		Component verticalStrut = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut.gridx = 1;
		gbc_verticalStrut.gridy = 0;
		panel.add(verticalStrut, gbc_verticalStrut);

		hdn1 = new HDNumberBox();
		GridBagConstraints gbc_hdn1 = new GridBagConstraints();
		gbc_hdn1.insets = new Insets(0, 0, 5, 5);
		gbc_hdn1.anchor = GridBagConstraints.NORTH;
		gbc_hdn1.fill = GridBagConstraints.HORIZONTAL;
		gbc_hdn1.gridx = 1;
		gbc_hdn1.gridy = 1;
		panel.add(hdn1, gbc_hdn1);
		GridBagLayout gbl_hdn1 = new GridBagLayout();
		gbl_hdn1.columnWidths = new int[] { 0 };
		gbl_hdn1.rowHeights = new int[] { 0 };
		gbl_hdn1.columnWeights = new double[] { Double.MIN_VALUE };
		gbl_hdn1.rowWeights = new double[] { Double.MIN_VALUE };
		hdn1.setLayout(gbl_hdn1);
		
				tb1 = new JToggleButton("Decimal");
				tb1.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent itemEvent) {
						hdn1.setDecimalDisplay(tb1.isSelected());
						tb1.setText(tb1.isSelected() ? "Decimal" : "Hex");
					}
				});
				GridBagConstraints gbc_tb1 = new GridBagConstraints();
				gbc_tb1.insets = new Insets(0, 0, 5, 0);
				gbc_tb1.gridx = 2;
				gbc_tb1.gridy = 1;
				panel.add(tb1, gbc_tb1);
				
				spinnerMin = new JSpinner();
				spinnerMin.setMinimumSize(new Dimension(50, 20));
				spinnerMin.setPreferredSize(new Dimension(50, 20));
				GridBagConstraints gbc_spinnerMin = new GridBagConstraints();
				gbc_spinnerMin.anchor = GridBagConstraints.EAST;
				gbc_spinnerMin.insets = new Insets(0, 0, 5, 5);
				gbc_spinnerMin.gridx = 1;
				gbc_spinnerMin.gridy = 3;
				panel.add(spinnerMin, gbc_spinnerMin);
				
				JLabel lblMin = new JLabel("Min");
				GridBagConstraints gbc_lblMin = new GridBagConstraints();
				gbc_lblMin.anchor = GridBagConstraints.WEST;
				gbc_lblMin.insets = new Insets(0, 0, 5, 0);
				gbc_lblMin.gridx = 2;
				gbc_lblMin.gridy = 3;
				panel.add(lblMin, gbc_lblMin);
				
				spinnerMax = new JSpinner();
				spinnerMax.setModel(new SpinnerNumberModel(new Integer(100), null, null, new Integer(1)));
				spinnerMax.setMinimumSize(new Dimension(50, 20));
				spinnerMax.setPreferredSize(new Dimension(50, 20));
				GridBagConstraints gbc_spinnerMax = new GridBagConstraints();
				gbc_spinnerMax.anchor = GridBagConstraints.EAST;
				gbc_spinnerMax.insets = new Insets(0, 0, 5, 5);
				gbc_spinnerMax.gridx = 1;
				gbc_spinnerMax.gridy = 5;
				panel.add(spinnerMax, gbc_spinnerMax);
				
				JLabel lblMax = new JLabel("Max");
				GridBagConstraints gbc_lblMax = new GridBagConstraints();
				gbc_lblMax.anchor = GridBagConstraints.WEST;
				gbc_lblMax.insets = new Insets(0, 0, 5, 0);
				gbc_lblMax.gridx = 2;
				gbc_lblMax.gridy = 5;
				panel.add(lblMax, gbc_lblMax);
				
				spinnerValue = new JSpinner();
				spinnerValue.setModel(new SpinnerNumberModel(new Integer(32), null, null, new Integer(1)));
				spinnerValue.setMinimumSize(new Dimension(50, 20));
				spinnerValue.setPreferredSize(new Dimension(50, 20));
				GridBagConstraints gbc_spinnerValue = new GridBagConstraints();
				gbc_spinnerValue.anchor = GridBagConstraints.EAST;
				gbc_spinnerValue.insets = new Insets(0, 0, 5, 5);
				gbc_spinnerValue.gridx = 1;
				gbc_spinnerValue.gridy = 7;
				panel.add(spinnerValue, gbc_spinnerValue);
				
				JLabel lblValue = new JLabel("Value");
				GridBagConstraints gbc_lblValue = new GridBagConstraints();
				gbc_lblValue.anchor = GridBagConstraints.WEST;
				gbc_lblValue.insets = new Insets(0, 0, 5, 0);
				gbc_lblValue.gridx = 2;
				gbc_lblValue.gridy = 7;
				panel.add(lblValue, gbc_lblValue);
				
				btnSetHDN = new JButton("Set HDN");
				btnSetHDN.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						hdn1.setMinValue((int) spinnerMin.getValue());
						hdn1.setMaxValue((int) spinnerMax.getValue());
						hdn1.setValue((int) spinnerValue.getValue());
					}
				});
				GridBagConstraints gbc_btnSetHDN = new GridBagConstraints();
				gbc_btnSetHDN.insets = new Insets(0, 0, 5, 0);
				gbc_btnSetHDN.gridwidth = 2;
				gbc_btnSetHDN.anchor = GridBagConstraints.EAST;
				gbc_btnSetHDN.gridx = 1;
				gbc_btnSetHDN.gridy = 9;
				panel.add(btnSetHDN, gbc_btnSetHDN);
				
				txtFormat = new JTextField();
				GridBagConstraints gbc_txtFormat = new GridBagConstraints();
				gbc_txtFormat.insets = new Insets(0, 0, 5, 5);
				gbc_txtFormat.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtFormat.gridx = 1;
				gbc_txtFormat.gridy = 11;
				panel.add(txtFormat, gbc_txtFormat);
				txtFormat.setColumns(10);
				
				JButton btnResetBoth = new JButton("Reset Both");
				btnResetBoth.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						hdn1.restDisplayFormat();
					}
					
				});
				GridBagConstraints gbc_btnResetBoth = new GridBagConstraints();
				gbc_btnResetBoth.insets = new Insets(0, 0, 5, 0);
				gbc_btnResetBoth.gridx = 2;
				gbc_btnResetBoth.gridy = 11;
				panel.add(btnResetBoth, gbc_btnResetBoth);
				
				JButton btnSetDecimal = new JButton("set Decimal");
				btnSetDecimal.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						hdn1.setDecimalDisplay(txtFormat.getText());
					}
				});
				GridBagConstraints gbc_btnSetDecimal = new GridBagConstraints();
				gbc_btnSetDecimal.insets = new Insets(0, 0, 5, 0);
				gbc_btnSetDecimal.gridx = 2;
				gbc_btnSetDecimal.gridy = 12;
				panel.add(btnSetDecimal, gbc_btnSetDecimal);
				
				JButton btnSetHex = new JButton("set Hex");
				btnSetHex.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						hdn1.setHexDisplay(txtFormat.getText());
					}
				});
				GridBagConstraints gbc_btnSetHex = new GridBagConstraints();
				gbc_btnSetHex.fill = GridBagConstraints.HORIZONTAL;
				gbc_btnSetHex.gridx = 2;
				gbc_btnSetHex.gridy = 13;
				panel.add(btnSetHex, gbc_btnSetHex);
				
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(new LineBorder(new Color(255, 0, 0), 1, true), "Seek Panel", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				GridBagConstraints gbc_panel_1 = new GridBagConstraints();
				gbc_panel_1.fill = GridBagConstraints.BOTH;
				gbc_panel_1.gridx = 0;
				gbc_panel_1.gridy = 1;
				panelLeft.add(panel_1, gbc_panel_1);
				GridBagLayout gbl_panel_1 = new GridBagLayout();
				gbl_panel_1.columnWidths = new int[]{0, 0};
				gbl_panel_1.rowHeights = new int[]{0, 0, 0};
				gbl_panel_1.columnWeights = new double[]{1.0, Double.MIN_VALUE};
				gbl_panel_1.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
				panel_1.setLayout(gbl_panel_1);
				
				Component verticalStrut_1 = Box.createVerticalStrut(20);
				GridBagConstraints gbc_verticalStrut_1 = new GridBagConstraints();
				gbc_verticalStrut_1.insets = new Insets(0, 0, 5, 0);
				gbc_verticalStrut_1.gridx = 0;
				gbc_verticalStrut_1.gridy = 0;
				panel_1.add(verticalStrut_1, gbc_verticalStrut_1);
				
				JPanel panel_2 = new JPanel();
				GridBagConstraints gbc_panel_2 = new GridBagConstraints();
				gbc_panel_2.fill = GridBagConstraints.BOTH;
				gbc_panel_2.gridx = 0;
				gbc_panel_2.gridy = 1;
				panel_1.add(panel_2, gbc_panel_2);
				GridBagLayout gbl_panel_2 = new GridBagLayout();
				gbl_panel_2.columnWidths = new int[]{0, 0, 0};
				gbl_panel_2.rowHeights = new int[]{0, 0};
				gbl_panel_2.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
				gbl_panel_2.rowWeights = new double[]{0.0, Double.MIN_VALUE};
				panel_2.setLayout(gbl_panel_2);
				
				seekPanel = new HDSeekPanel();
				GridBagConstraints gbc_seekPanel = new GridBagConstraints();
				gbc_seekPanel.anchor = GridBagConstraints.NORTH;
				gbc_seekPanel.insets = new Insets(0, 0, 0, 5);
				gbc_seekPanel.fill = GridBagConstraints.HORIZONTAL;
				gbc_seekPanel.gridx = 0;
				gbc_seekPanel.gridy = 0;
				panel_2.add(seekPanel, gbc_seekPanel);
				GridBagLayout gbl_seekPanel = new GridBagLayout();
				gbl_seekPanel.columnWidths = new int[]{0};
				gbl_seekPanel.rowHeights = new int[]{0};
				gbl_seekPanel.columnWeights = new double[]{Double.MIN_VALUE};
				gbl_seekPanel.rowWeights = new double[]{Double.MIN_VALUE};
				seekPanel.setLayout(gbl_seekPanel);
				
				tb2 = new JToggleButton("Decimal");
				tb2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent actionEvent) {
						seekPanel.setDecimalDisplay(tb2.isSelected());
						tb2.setText(tb2.isSelected() ? "Decimal" : "Hex");
					}
				});
				tb2.setSelected(false);
				GridBagConstraints gbc_tb2 = new GridBagConstraints();
				gbc_tb2.gridx = 1;
				gbc_tb2.gridy = 0;
				panel_2.add(tb2, gbc_tb2);

		JPanel panelForLog = new JPanel();
		splitPane1.setRightComponent(panelForLog);
		GridBagLayout gbl_panelForLog = new GridBagLayout();
		gbl_panelForLog.columnWidths = new int[] { 0, 0 };
		gbl_panelForLog.rowHeights = new int[] { 0, 0 };
		gbl_panelForLog.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panelForLog.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		panelForLog.setLayout(gbl_panelForLog);

		JScrollPane scrollPaneForLog = new JScrollPane();
		GridBagConstraints gbc_scrollPaneForLog = new GridBagConstraints();
		gbc_scrollPaneForLog.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneForLog.gridx = 0;
		gbc_scrollPaneForLog.gridy = 0;
		panelForLog.add(scrollPaneForLog, gbc_scrollPaneForLog);

		txtLog = new JTextPane();
		scrollPaneForLog.setViewportView(txtLog);

		popupLog = new JPopupMenu();
		addPopup(txtLog, popupLog);

		JMenuItem popupLogClear = new JMenuItem("Clear Log");
		popupLogClear.setName(PUM_LOG_CLEAR);
		popupLogClear.addActionListener(logAdaper);
		popupLog.add(popupLogClear);

		JSeparator separator = new JSeparator();
		popupLog.add(separator);

		JMenuItem popupLogPrint = new JMenuItem("Print Log");
		popupLogPrint.setName(PUM_LOG_PRINT);
		popupLogPrint.addActionListener(logAdaper);
		popupLog.add(popupLogPrint);

		JLabel lblNewLabel = new JLabel("Application Log");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setForeground(new Color(0, 128, 0));
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 14));
		scrollPaneForLog.setColumnHeaderView(lblNewLabel);
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

		JSeparator separator99 = new JSeparator();
		mnuFile.add(separator99);

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

	private static final String PUM_LOG_PRINT = "popupLogPrint";
	private static final String PUM_LOG_CLEAR = "popupLogClear";

	static final String EMPTY_STRING = "";
	private HDNumberBox hdn1;
	private JToggleButton tb1;
	private JButton btnSetHDN;
	private JSpinner spinnerMin;
	private JSpinner spinnerMax;
	private JSpinner spinnerValue;
	private HDSeekPanel seekPanel;
	private JToggleButton tb2;
	private JTextField txtFormat;

	//////////////////////////////////////////////////////////////////////////

	class AdapterLog implements ActionListener {// , ListSelectionListener
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			String name = ((Component) actionEvent.getSource()).getName();
			switch (name) {
			case PUM_LOG_PRINT:
				doLogPrint();
				break;
			case PUM_LOG_CLEAR:
				doLogClear();
				break;
			}// switch
		}// actionPerformed
	}// class AdapterAction

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				} // if popup Trigger
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}// addPopup

}// class GUItemplate