package devlopment;

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class RegexDriver {

	private AdapterForRegexDriver adapterForRegexDriver = new AdapterForRegexDriver();
	private StyledDocument doc;
	private SimpleAttributeSet attrBlack = new SimpleAttributeSet();
	private SimpleAttributeSet attrGreen = new SimpleAttributeSet();
	private SimpleAttributeSet attrRed = new SimpleAttributeSet();
	private SimpleAttributeSet attrBlue = new SimpleAttributeSet();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegexDriver window = new RegexDriver();
					window.frmRegexDriver.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				} // try
			}// run
		});
	}// main

	// +++++++++++++++++++++++++++++++++++++++++++++++++
	/* placeholder for trying things out */
	private void actOnResult() {

	}// actOnResult
		// ---------------------------------------------------------

	private void clearResult() {
		try {
			doc.remove(0, doc.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		} // try
	}// clearResult

	private void doMatch() {
		cleanOutput();
		String logMessage0 = "No Match";
		try {
			Pattern pattern = Pattern.compile(txtRegexCode.getText());
			Matcher matcher = pattern.matcher(txtSourceString.getText());

			// pattern.matches(txtRegexCode.getText(), input)

			if (matcher.matches()) {
				logMessage0 = foundIt("Match");
				txtLog.append(String.format("end = %d, start = %s%n", matcher.end(), matcher.start()));
				showGroup(matcher);
			} else {
				noChange("<< MATCHES - nothing matched >>");
			} //
		} catch (Exception e) {
			String errMessage = String.format("%s - %s%n%n", "In Catch", e.getMessage());
			txtLog.append(errMessage);
		} //
		postLogMessage(logMessage0);
	}// doMatch

	private void doFind() {
		cleanOutput();
		String logMessage0 = "Not Found";
		String sourceText = txtSourceString.getText();

		try {
			Pattern pattern = Pattern.compile(txtRegexCode.getText());
			Matcher matcher = pattern.matcher(txtSourceString.getText());
			if (matcher.find()) {
				logMessage0 = foundIt("Find");
				txtLog.append(String.format("end = %d, start = %s%n", matcher.end(), matcher.start()));
				txtLog.append(String.format("group = |%s|%n", matcher.group()));
				txtLog.append(String.format("Before group = |%s|%n", sourceText.substring(0, matcher.start())));
				showGroup(matcher);
			} else {
				noChange("<< FIND - nothing found >>");
			} // if
		} catch (Exception e) {
			String errMessage = String.format("%s - %s%n%n", "In Catch", e.getMessage());
			txtLog.append(errMessage);
		}
		postLogMessage(logMessage0);
	}// doFind

	private void doLookingAt() {
		cleanOutput();
		String logMessage0 = "Not looking at";

		try {
			Pattern pattern = Pattern.compile(txtRegexCode.getText());
			Matcher matcher = pattern.matcher(txtSourceString.getText());

			if (matcher.lookingAt()) {
				logMessage0 = foundIt("lookingAT");
				txtLog.append(String.format("end = %d, start = %s%n", matcher.end(), matcher.start()));
				showGroup(matcher);
			} else {
				noChange("<< LOOKING AT - not looking at >>");
			} //
		} catch (Exception e) {
			String errMessage = String.format("%s - %s%n%n", "In Catch", e.getMessage());
			txtLog.append(errMessage);
		} //
		postLogMessage(logMessage0);

	}// doLookingAt

	
	private void doReplace(boolean all){
		cleanOutput();
		Pattern pattern = Pattern.compile(txtRegexCode.getText());
		Matcher matcher = pattern.matcher(txtSourceString.getText());
		String original = txtSourceString.getText();
		String ans ;
		
		if(all){
			 ans = matcher.replaceAll(txtReplacement.getText());
		}else{
			 ans = matcher.replaceFirst(txtReplacement.getText());
	
		}// if all
		SimpleAttributeSet attributeColor;
		String msg;
		if (ans.equals(original)) {
			attributeColor = attrRed;
			msg = "<< Nothing Replaced >>";
		} else {
			attributeColor = attrBlack;
			msg = ans;
		} // if
		try {
			doc.insertString(doc.getLength(), msg, attributeColor);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // try

	}//doReplace

	// ......................................................
	private void saveLog() {
		try {
			File logFile = new File("c://tmp//logFile.txt");
			FileWriter fw = new FileWriter(logFile);
			fw.write(txtLog.getText());
			fw.close();
		} catch (IOException e) {
			System.err.printf("unable to save the Log %s.%n");
			e.printStackTrace();
		}
	}// saveLog

	// ---------------------------------------------------------
	private String foundIt(String typeOfOperation) {
		return typeOfOperation;
	}// foundIt

	private void noChange(String message) {
		try {
			doc.remove(0, doc.getLength());
			doc.insertString(0, message, attrRed);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // try
	}// foundItNot

	private void postLogMessage(String logMessage0) {
		String logMessage = String.format("%s \t%s - \t%s%n", logMessage0, txtSourceString.getText(),
				txtRegexCode.getText());
		txtLog.append(logMessage);
	}// postLogMessage

	private void cleanOutput() {
		lblGroupBoundary.setText(" ~ - ~");
		clearResult();
	}// cleanOutput

	private void showGroup(Matcher matcher) {

		lblGroupBoundary.setText(String.format("%d - %d", matcher.start(), matcher.end()));
		String originalString = txtSourceString.getText();
		String beforeGroup = originalString.substring(0, matcher.start());
		String group = matcher.group();
		String afterGroup = originalString.substring(matcher.end(), originalString.length());
		// System.out.printf("[showGroup] beforeGroup: %s%n", beforeGroup);
		// System.out.printf("[showGroup] group: %s%n", group);
		// System.out.printf("[showGroup] afterGroup: %s%n", afterGroup);
		try {
			doc.insertString(doc.getLength(), beforeGroup, attrBlack);
			doc.insertString(doc.getLength(), group, attrBlue);
			doc.insertString(doc.getLength(), afterGroup, attrBlack);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}// showGroup

	private void setAttributes() {
		StyleConstants.setForeground(attrBlack, Color.BLACK);
		StyleConstants.setForeground(attrGreen, Color.GREEN);
		StyleConstants.setForeground(attrRed, Color.RED);
		StyleConstants.setForeground(attrBlue, Color.BLUE);
	}// setAttributes

	// ........................................................................
	private void appClose() {
		Preferences myPrefs = Preferences.userNodeForPackage(RegexDriver.class);
		Dimension dim = frmRegexDriver.getSize();
		myPrefs.putInt("Height", dim.height);
		myPrefs.putInt("Width", dim.width);
		Point point = frmRegexDriver.getLocation();
		myPrefs.putInt("LocX", point.x);
		myPrefs.putInt("LocY", point.y);
		myPrefs.putInt("Divider", splitPane1.getDividerLocation());
		myPrefs.put("RegexCode", txtRegexCode.getText());
		myPrefs.put("SourceString", txtSourceString.getText());
		myPrefs.put("Replacement", txtReplacement.getText());
		myPrefs = null;
	}// appClose

	private void appInit() {
		Preferences myPrefs = Preferences.userNodeForPackage(RegexDriver.class);
		frmRegexDriver.setSize(1203, 724);
		frmRegexDriver.setLocation(myPrefs.getInt("LocX", 100), myPrefs.getInt("LocY", 100));
		splitPane1.setDividerLocation(myPrefs.getInt("Divider", 250));
		txtRegexCode.setText(myPrefs.get("RegexCode", "<nothing>"));
		txtSourceString.setText(myPrefs.get("SourceString", "<nothing>"));
		txtReplacement.setText(myPrefs.get("Replacement", "<nothing>"));
		myPrefs = null;

		doc = tpResult.getStyledDocument();
		setAttributes();
	}// appInit

	public RegexDriver() {
		initialize();
		appInit();
	}// Constructor

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmRegexDriver = new JFrame();
		frmRegexDriver.setTitle("Regex Driver");
		frmRegexDriver.setBounds(100, 100, 450, 300);
		frmRegexDriver.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 25, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		frmRegexDriver.getContentPane().setLayout(gridBagLayout);

		splitPane1 = new JSplitPane();
		GridBagConstraints gbc_splitPane1 = new GridBagConstraints();
		gbc_splitPane1.insets = new Insets(0, 0, 5, 0);
		gbc_splitPane1.fill = GridBagConstraints.BOTH;
		gbc_splitPane1.gridx = 0;
		gbc_splitPane1.gridy = 1;
		frmRegexDriver.getContentPane().add(splitPane1, gbc_splitPane1);

		JPanel panelLeft = new JPanel();
		splitPane1.setLeftComponent(panelLeft);
		GridBagLayout gbl_panelLeft = new GridBagLayout();
		gbl_panelLeft.columnWidths = new int[] { 0, 0, 0 };
		gbl_panelLeft.rowHeights = new int[] { 0, 0, 0, 0, 0, 10, 0, 0, 0, 25, 0, 30, 0 };
		gbl_panelLeft.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panelLeft.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
		panelLeft.setLayout(gbl_panelLeft);

		JLabel lblRegexCode = new JLabel("Regex Code");
		GridBagConstraints gbc_lblRegexCode = new GridBagConstraints();
		gbc_lblRegexCode.insets = new Insets(0, 0, 5, 5);
		gbc_lblRegexCode.gridx = 0;
		gbc_lblRegexCode.gridy = 1;
		panelLeft.add(lblRegexCode, gbc_lblRegexCode);

		txtRegexCode = new JTextField();
		txtRegexCode.setFont(new Font("Courier New", Font.PLAIN, 18));
		GridBagConstraints gbc_txtRegexCode = new GridBagConstraints();
		gbc_txtRegexCode.anchor = GridBagConstraints.NORTH;
		gbc_txtRegexCode.insets = new Insets(0, 0, 5, 0);
		gbc_txtRegexCode.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtRegexCode.gridx = 1;
		gbc_txtRegexCode.gridy = 1;
		panelLeft.add(txtRegexCode, gbc_txtRegexCode);
		txtRegexCode.setColumns(10);

		JLabel lblSourceString = new JLabel("Source String");
		GridBagConstraints gbc_lblSourceString = new GridBagConstraints();
		gbc_lblSourceString.insets = new Insets(0, 0, 5, 5);
		gbc_lblSourceString.gridx = 0;
		gbc_lblSourceString.gridy = 3;
		panelLeft.add(lblSourceString, gbc_lblSourceString);

		txtSourceString = new JTextField();
		txtSourceString.setFont(new Font("Courier New", Font.PLAIN, 18));
		GridBagConstraints gbc_txtSourceString = new GridBagConstraints();
		gbc_txtSourceString.insets = new Insets(0, 0, 5, 0);
		gbc_txtSourceString.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSourceString.gridx = 1;
		gbc_txtSourceString.gridy = 3;
		panelLeft.add(txtSourceString, gbc_txtSourceString);
		txtSourceString.setColumns(10);

		tpResult = new JTextPane();
		tpResult.setEditable(false);
		tpResult.setFont(new Font("Courier New", Font.BOLD, 18));
		GridBagConstraints gbc_tpResult = new GridBagConstraints();
		gbc_tpResult.insets = new Insets(0, 0, 5, 0);
		gbc_tpResult.fill = GridBagConstraints.BOTH;
		gbc_tpResult.gridx = 1;
		gbc_tpResult.gridy = 4;
		panelLeft.add(tpResult, gbc_tpResult);

		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 1;
		gbc_panel_2.gridy = 6;
		panelLeft.add(panel_2, gbc_panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_panel_2.rowHeights = new int[] { 0, 0 };
		gbl_panel_2.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel_2.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel_2.setLayout(gbl_panel_2);

		JLabel lblGroupStart = new JLabel("Group Boundary:");
		GridBagConstraints gbc_lblGroupStart = new GridBagConstraints();
		gbc_lblGroupStart.anchor = GridBagConstraints.EAST;
		gbc_lblGroupStart.insets = new Insets(0, 0, 0, 5);
		gbc_lblGroupStart.gridx = 2;
		gbc_lblGroupStart.gridy = 0;
		panel_2.add(lblGroupStart, gbc_lblGroupStart);

		lblGroupBoundary = new JLabel(" ~ - ~");
		GridBagConstraints gbc_lblGroupBoundary = new GridBagConstraints();
		gbc_lblGroupBoundary.insets = new Insets(0, 0, 0, 5);
		gbc_lblGroupBoundary.anchor = GridBagConstraints.EAST;
		gbc_lblGroupBoundary.gridx = 6;
		gbc_lblGroupBoundary.gridy = 0;
		panel_2.add(lblGroupBoundary, gbc_lblGroupBoundary);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 7;
		panelLeft.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 25, 0, 25, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		JButton btnMatches = new JButton("Matches");
		btnMatches.setToolTipText("Attempts to match the entire region against the pattern.");
		btnMatches.addActionListener(adapterForRegexDriver);

		JButton btnFind = new JButton("Find");
		btnFind.setToolTipText("Attempts to find the next subsequence of the input sequence that matches the pattern.");
		btnFind.addActionListener(adapterForRegexDriver);
		btnFind.setActionCommand("btnFind");
		GridBagConstraints gbc_btnFind = new GridBagConstraints();
		gbc_btnFind.insets = new Insets(0, 0, 0, 5);
		gbc_btnFind.fill = GridBagConstraints.BOTH;
		gbc_btnFind.gridx = 0;
		gbc_btnFind.gridy = 0;
		panel.add(btnFind, gbc_btnFind);
		btnMatches.setActionCommand("btnMatches");
		GridBagConstraints gbc_btnMatches = new GridBagConstraints();
		gbc_btnMatches.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnMatches.insets = new Insets(0, 0, 0, 5);
		gbc_btnMatches.gridx = 2;
		gbc_btnMatches.gridy = 0;
		panel.add(btnMatches, gbc_btnMatches);

		JButton btnLookingAt = new JButton("Looking At");
		btnLookingAt.setToolTipText(
				"Attempts to match the input sequence, starting at the beginning of the region, against the pattern.");
		btnLookingAt.addActionListener(adapterForRegexDriver);
		btnLookingAt.setActionCommand("btnLookingAt");
		GridBagConstraints gbc_btnLookingAt = new GridBagConstraints();
		gbc_btnLookingAt.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnLookingAt.gridx = 4;
		gbc_btnLookingAt.gridy = 0;
		panel.add(btnLookingAt, gbc_btnLookingAt);

		Component verticalStrut = Box.createVerticalStrut(20);
		verticalStrut.setPreferredSize(new Dimension(0, 40));
		verticalStrut.setMinimumSize(new Dimension(0, 40));
		GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut.gridx = 0;
		gbc_verticalStrut.gridy = 9;
		panelLeft.add(verticalStrut, gbc_verticalStrut);

		JPanel panel_3 = new JPanel();
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.insets = new Insets(0, 0, 5, 0);
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 1;
		gbc_panel_3.gridy = 10;
		panelLeft.add(panel_3, gbc_panel_3);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_panel_3.rowHeights = new int[] { 0, 0 };
		gbl_panel_3.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel_3.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel_3.setLayout(gbl_panel_3);

		JButton btnReplaceFirst = new JButton("Replace First");
		btnReplaceFirst.setToolTipText(
				"Replaces the first subsequence of the input sequence that matches the pattern with the given replacement string.");
		GridBagConstraints gbc_btnReplaceFirst = new GridBagConstraints();
		gbc_btnReplaceFirst.insets = new Insets(0, 0, 0, 5);
		gbc_btnReplaceFirst.gridx = 0;
		gbc_btnReplaceFirst.gridy = 0;
		panel_3.add(btnReplaceFirst, gbc_btnReplaceFirst);
		btnReplaceFirst.addActionListener(adapterForRegexDriver);
		btnReplaceFirst.setActionCommand("btnReplaceFirst");

		JButton btnReplaceAll = new JButton("ReplaceAll");
		btnReplaceAll.setToolTipText(
				"Replaces every subsequence of the input sequence that matches the pattern with the given replacement string.");
		btnReplaceAll.addActionListener(adapterForRegexDriver);
		btnReplaceAll.setActionCommand("btnReplaceAll");
		GridBagConstraints gbc_btnReplaceAll = new GridBagConstraints();
		gbc_btnReplaceAll.insets = new Insets(0, 0, 0, 5);
		gbc_btnReplaceAll.gridx = 2;
		gbc_btnReplaceAll.gridy = 0;
		panel_3.add(btnReplaceAll, gbc_btnReplaceAll);

		txtReplacement = new JTextField();
		GridBagConstraints gbc_txtReplacement = new GridBagConstraints();
		gbc_txtReplacement.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtReplacement.gridx = 4;
		gbc_txtReplacement.gridy = 0;
		panel_3.add(txtReplacement, gbc_txtReplacement);
		txtReplacement.setFont(new Font("Courier New", Font.PLAIN, 18));
		txtReplacement.setColumns(10);

		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 12;
		panelLeft.add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_panel_1.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_panel_1.columnWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel_1.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel_1.setLayout(gbl_panel_1);

		JButton btnActOnResult = new JButton("Act On Result");
		GridBagConstraints gbc_btnActOnResult = new GridBagConstraints();
		gbc_btnActOnResult.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnActOnResult.insets = new Insets(0, 0, 5, 5);
		gbc_btnActOnResult.gridx = 0;
		gbc_btnActOnResult.gridy = 1;
		panel_1.add(btnActOnResult, gbc_btnActOnResult);
		btnActOnResult.addActionListener(adapterForRegexDriver);
		btnActOnResult.setActionCommand("btnActOnResult");
		
				JButton btnSaveLog = new JButton("Save Log");
				GridBagConstraints gbc_btnSaveLog = new GridBagConstraints();
				gbc_btnSaveLog.insets = new Insets(0, 0, 5, 5);
				gbc_btnSaveLog.fill = GridBagConstraints.HORIZONTAL;
				gbc_btnSaveLog.gridx = 1;
				gbc_btnSaveLog.gridy = 1;
				panel_1.add(btnSaveLog, gbc_btnSaveLog);
				btnSaveLog.setToolTipText("c:/tmp/logFile.txt");
				btnSaveLog.addActionListener(adapterForRegexDriver);
				btnSaveLog.setActionCommand("btnSaveLog");

		JPanel panelRight = new JPanel();
		splitPane1.setRightComponent(panelRight);
		GridBagLayout gbl_panelRight = new GridBagLayout();
		gbl_panelRight.columnWidths = new int[] { 0, 0 };
		gbl_panelRight.rowHeights = new int[] { 0, 0 };
		gbl_panelRight.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panelRight.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		panelRight.setLayout(gbl_panelRight);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		panelRight.add(scrollPane, gbc_scrollPane);

		txtLog = new JTextArea();
		txtLog.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() >= 2) {
					if (me.getComponent().getName() == "txtLog") {
						((JTextComponent) me.getComponent()).setText("");
					} // inner if
				} // outer if
			}
		});
		txtLog.setName("txtLog");
		scrollPane.setViewportView(txtLog);
		splitPane1.setDividerLocation(250);

		JPanel panelStatus = new JPanel();
		panelStatus.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		GridBagConstraints gbc_panelStatus = new GridBagConstraints();
		gbc_panelStatus.fill = GridBagConstraints.BOTH;
		gbc_panelStatus.gridx = 0;
		gbc_panelStatus.gridy = 2;
		frmRegexDriver.getContentPane().add(panelStatus, gbc_panelStatus);

		JMenuBar menuBar = new JMenuBar();
		frmRegexDriver.setJMenuBar(menuBar);

		frmRegexDriver.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				appClose();
			}
		});
	}// initialize

	private JFrame frmRegexDriver;
	private JSplitPane splitPane1;
	private JTextArea txtLog;
	private JTextField txtRegexCode;
	private JTextField txtSourceString;
	private JTextField txtReplacement;

	public class AdapterForRegexDriver implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			String actionCommand = actionEvent.getActionCommand();
			switch (actionCommand) {
			case "btnMatches":
				doMatch();
				break;
			case "btnFind":
				doFind();
				break;
			case "btnLookingAt":
				doLookingAt();
				break;
			case "btnSaveLog":
				saveLog();
				break;
			case "btnActOnResult":
				actOnResult();
				break;
			case "btnReplaceFirst":
				doReplace(false);
				break;
			case "btnReplaceAll":
				doReplace(true);
				break;
			default:
				System.err.printf("Unknown Action Command %s.%n", actionCommand);
				break;
			}// switch
		}// actionPerformed

	}// class AdapterForRegexDriver

	private static final String EMPTY_STRING = "";
	private JTextPane tpResult;
	private JLabel lblGroupBoundary;

}// class GUItemplate