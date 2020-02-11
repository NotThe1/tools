package myComponents.asmFormatter;

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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.util.prefs.Preferences;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

//import assembler.SourceLineParts;

public class AsmFormatter {
	private AdapterForFMT adapterForFMT = new AdapterForFMT();

	private String defaultDirectory;
	private File asmSourceFile = null;
	private String sourceFileFullName;
	private String fmtString;

	private int statementStart;
	private int argumentStart;
	private int commentStart;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AsmFormatter window = new AsmFormatter();
					window.frmAsmFormatter.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				} // try
			}// run
		});
	}// main

	// ---------------------------------------------------------
	private void doStart() {
		lblStatus.setText(EMPTY_STRING);
		if (asmSourceFile == null) {
			return; // do nothing
		} // if

		int symbolWide = statementStart - 0;
		int cmdWide = argumentStart - statementStart;
		int argWide = commentStart - argumentStart;
		fmtString = "%-" + symbolWide + "s%-" + cmdWide + "s%-" + argWide + "s%s";

		txtSource.setText(EMPTY_STRING);
		try {
			String sourceLine = EMPTY_STRING;
			String formattedLine = EMPTY_STRING;
			Scanner scanner = new Scanner(asmSourceFile);
			while (scanner.hasNextLine()) {
				sourceLine = scanner.nextLine();
				formattedLine = processLine(sourceLine);

				/* remove trailing spaces */
				int firstTrailingSpace = formattedLine.length() - 1;
				while (true) {
					if (!formattedLine.endsWith(" ")) {
						break;
					} // done
					formattedLine = formattedLine.substring(0, firstTrailingSpace);
					firstTrailingSpace--;
				} // while
				/* remove trailing spaces */

				txtSource.append(formattedLine + LINE_SEPARATOR);
			} // while
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} // try
		btnStart.setEnabled(false);
		btnSaveFile.setEnabled(true);
		txtSource.setCaretPosition(0);
	}// start

	private String processLine(String originalLine) {

		String symbol = "";
		String cmd = "";
		String arg = "";
		String com = "";

		/* remove tabs */
		String line = originalLine.replaceAll("\\t", " ");

		/* if - whole line is comment leave alone */
		if (line.startsWith(SEMICOLON)) {
			return line;
		} // if

		/* does it have a comment */
		if (line.contains(SEMICOLON)) {
			int scPosition = line.indexOf(SEMICOLON);
			com = line.substring(scPosition);
			line = line.substring(0, scPosition);
		} // if

		/* Do we have a symbol/label ? */
		if (line.startsWith(" ")) {
			symbol = "";
		} else {
			int symbolEnd = line.indexOf(" ");
			symbol = symbolEnd == -1 ? line : line.substring(0, symbolEnd);
			line = symbolEnd == -1 ? "" : line.substring(symbolEnd);
		} // if symbol

		/* handle statement and arguments */
		line = line.trim();
		if (!(line.length() == 0)) {
			int cmdEnd = line.indexOf(" ");
			if (cmdEnd == -1) {
				cmd = line;
			} else {
				cmd = line.substring(0, cmdEnd);
				arg = line.substring(cmdEnd);
			} // if
		} // inner if

		String target = String.format(fmtString, symbol.trim(), cmd.trim(), arg.trim(), com);

		return target;
	}// DoIt

	private void prepareSourceFile(File asmSourceFile) {
		lblSourceFilePath.setText(asmSourceFile.getAbsolutePath());
		sourceFileFullName = asmSourceFile.getAbsolutePath();
		lblSourceFileName.setText(asmSourceFile.getName());
		defaultDirectory = asmSourceFile.getParent();

		txtSource.setText(EMPTY_STRING);
		txtSource.setCaretPosition(0);
		btnStart.setEnabled(true);
		btnSaveFile.setEnabled(false);
	}// prepareSourceFile

	private void doOpenFile() {
		JFileChooser fileChooser = new JFileChooser(defaultDirectory);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Assembler Source Code", "Z80", "LIB"));
		fileChooser.setAcceptAllFileFilterUsed(false);

		if (fileChooser.showOpenDialog(frmAsmFormatter) != JFileChooser.APPROVE_OPTION) {
			System.out.printf("%s%n", "You cancelled the file open");
		} else {
			asmSourceFile = fileChooser.getSelectedFile();
			prepareSourceFile(asmSourceFile);
		} // if
	}// openFile

	private void doSaveFile() {
		String backupDir = asmSourceFile.getParent() + FILE_SEPARATOR + "BAK";
		String backupName = backupDir + FILE_SEPARATOR + asmSourceFile.getName();

		if (!Files.exists(Paths.get(backupDir))){
			try {
				Files.createDirectory(Paths.get(backupDir));
			} catch (IOException e) {
				e.printStackTrace();
			}//try		
		}//if
				

		File backupFile = new File(backupName);
		if (backupFile.exists()) {
			boolean statusDelete = backupFile.delete();
			System.out.printf("[doSaveFile] delete status = %s%n", statusDelete);
		} // if
		
		Path original = asmSourceFile.toPath();
		Path backup = Paths.get(backupName);
		try {
			Files.move(original, backup, StandardCopyOption.ATOMIC_MOVE);
		} catch (IOException e1) {
			e1.printStackTrace();
		}//try
		


		try {
			asmSourceFile.createNewFile();
			PrintWriter printWriter = new PrintWriter(asmSourceFile);
			Scanner scanner = new Scanner(txtSource.getText());
			while (scanner.hasNext()) {
				printWriter.println(scanner.nextLine());
			} // while
			scanner.close();
			printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		} // try
	}// doSaveFile

	private void doLoadLastFile() {
		asmSourceFile = new File(sourceFileFullName);
		prepareSourceFile(asmSourceFile);
	}// doLoadLastFile

	private void doSpinnerChanged(String name, int value) {
		// SPINNER_ARGUMENT
		switch (name) {
		case SPINNER_STATEMENT:
			statementStart = value;
			break;
		case SPINNER_ARGUMENT:
			argumentStart = value;
			break;
		case SPINNER_COMMENT:
			commentStart = value;
			break;
		default:
			System.err.printf("[doSpinnerChanged] name: %s, value: %d%n", name, value);
		}// switch
	}// doSpinnerChanged

	private void doFileExit() {
		appClose();
		System.exit(0);
	}// doFileExit

	private void appClose() {
		Preferences myPrefs = Preferences.userNodeForPackage(AsmFormatter.class).node(this.getClass().getSimpleName());
		Dimension dim = frmAsmFormatter.getSize();
		myPrefs.putInt("Height", dim.height);
		myPrefs.putInt("Width", dim.width);
		Point point = frmAsmFormatter.getLocation();
		myPrefs.putInt("LocX", point.x);
		myPrefs.putInt("LocY", point.y);

		myPrefs.put("defaultDirectory", defaultDirectory);
		myPrefs.put("LastFile", sourceFileFullName);
		myPrefs.putInt(SPINNER_STATEMENT, (int) spinnerStatement.getValue());
		myPrefs.putInt(SPINNER_ARGUMENT, (int) spinnerArgument.getValue());
		myPrefs.putInt(SPINNER_COMMENT, (int) spinnerComment.getValue());

		myPrefs = null;
		System.exit(0);
	}// appClose

	private void appInit() {
		Preferences myPrefs = Preferences.userNodeForPackage(AsmFormatter.class).node(this.getClass().getSimpleName());
		frmAsmFormatter.setLocation(myPrefs.getInt("LocX", 100), myPrefs.getInt("LocY", 100));
		frmAsmFormatter.setSize(myPrefs.getInt("Width", 761), myPrefs.getInt("Height", 693));
		defaultDirectory = myPrefs.get("defaultDirectory", DEFAULT_DIRECTORY);
		sourceFileFullName = myPrefs.get("LastFile", "");

		spinnerStatement.setValue(myPrefs.getInt(SPINNER_STATEMENT, 17));
		statementStart = (int) spinnerStatement.getValue();

		spinnerArgument.setValue(myPrefs.getInt(SPINNER_ARGUMENT, 23));
		argumentStart = (int) spinnerArgument.getValue();

		spinnerComment.setValue(myPrefs.getInt(SPINNER_COMMENT, 45));
		commentStart = (int) spinnerComment.getValue();

		myPrefs = null;

		btnSaveFile.setEnabled(false);
	}// appInit

	public AsmFormatter() {
		initialize();
		appInit();
	}// Constructor

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAsmFormatter = new JFrame();
		frmAsmFormatter.setTitle("Z80 Assembler Formatter    1.0");
		frmAsmFormatter.setBounds(100, 100, 450, 300);
		frmAsmFormatter.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAsmFormatter.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				appClose();
			}
		});
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 30, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		frmAsmFormatter.getContentPane().setLayout(gridBagLayout);

		JPanel panelTop = new JPanel();
		GridBagConstraints gbc_panelTop = new GridBagConstraints();
		gbc_panelTop.insets = new Insets(0, 0, 5, 0);
		gbc_panelTop.fill = GridBagConstraints.BOTH;
		gbc_panelTop.gridx = 0;
		gbc_panelTop.gridy = 0;
		frmAsmFormatter.getContentPane().add(panelTop, gbc_panelTop);
		GridBagLayout gbl_panelTop = new GridBagLayout();
		gbl_panelTop.columnWidths = new int[] { 0, 0 };
		gbl_panelTop.rowHeights = new int[] { 0, 0, 0 };
		gbl_panelTop.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panelTop.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		panelTop.setLayout(gbl_panelTop);

		lblSourceFilePath = new JLabel("<No File Selected>");
		lblSourceFilePath.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblSourceFilePath = new GridBagConstraints();
		gbc_lblSourceFilePath.insets = new Insets(0, 0, 5, 0);
		gbc_lblSourceFilePath.gridx = 0;
		gbc_lblSourceFilePath.gridy = 0;
		panelTop.add(lblSourceFilePath, gbc_lblSourceFilePath);

		JPanel panelMain = new JPanel();
		GridBagConstraints gbc_panelMain = new GridBagConstraints();
		gbc_panelMain.fill = GridBagConstraints.BOTH;
		gbc_panelMain.gridx = 0;
		gbc_panelMain.gridy = 1;
		panelTop.add(panelMain, gbc_panelMain);
		GridBagLayout gbl_panelMain = new GridBagLayout();
		gbl_panelMain.columnWidths = new int[] { 80, 0, 0 };
		gbl_panelMain.rowHeights = new int[] { 0, 0 };
		gbl_panelMain.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panelMain.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		panelMain.setLayout(gbl_panelMain);

		JPanel panelLeft = new JPanel();
		GridBagConstraints gbc_panelLeft = new GridBagConstraints();
		gbc_panelLeft.fill = GridBagConstraints.VERTICAL;
		gbc_panelLeft.insets = new Insets(0, 0, 0, 5);
		gbc_panelLeft.gridx = 0;
		gbc_panelLeft.gridy = 0;
		panelMain.add(panelLeft, gbc_panelLeft);
		GridBagLayout gbl_panelLeft = new GridBagLayout();
		gbl_panelLeft.columnWidths = new int[] { 0, 0, 0 };
		gbl_panelLeft.rowHeights = new int[] { 0, 50, 0, 0, 0, 150, 0, 50, 50, 0, 50, 50, 0 };
		gbl_panelLeft.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		gbl_panelLeft.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		panelLeft.setLayout(gbl_panelLeft);

		btnStart = new JButton("Start");
		btnStart.setName(BTN_START);
		btnStart.addActionListener(adapterForFMT);
		btnStart.setEnabled(false);
		GridBagConstraints gbc_btnStart = new GridBagConstraints();
		gbc_btnStart.anchor = GridBagConstraints.NORTH;
		gbc_btnStart.insets = new Insets(0, 0, 5, 5);
		gbc_btnStart.gridx = 0;
		gbc_btnStart.gridy = 0;
		panelLeft.add(btnStart, gbc_btnStart);

		JButton btnLoadLastFile = new JButton("Load Last File");
		btnLoadLastFile.setName(BTN_LOAD_LAST_FILE);
		btnLoadLastFile.addActionListener(adapterForFMT);

		verticalStrut = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut.gridx = 0;
		gbc_verticalStrut.gridy = 1;
		panelLeft.add(verticalStrut, gbc_verticalStrut);
		GridBagConstraints gbc_btnLoadLastFile = new GridBagConstraints();
		gbc_btnLoadLastFile.anchor = GridBagConstraints.NORTH;
		gbc_btnLoadLastFile.insets = new Insets(0, 0, 5, 5);
		gbc_btnLoadLastFile.gridx = 0;
		gbc_btnLoadLastFile.gridy = 2;
		panelLeft.add(btnLoadLastFile, gbc_btnLoadLastFile);

		verticalStrut_1 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_1 = new GridBagConstraints();
		gbc_verticalStrut_1.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut_1.gridx = 0;
		gbc_verticalStrut_1.gridy = 3;
		panelLeft.add(verticalStrut_1, gbc_verticalStrut_1);

		btnSaveFile = new JButton("Save File");
		btnSaveFile.setName(BTN_SAVE_FILE);
		btnSaveFile.addActionListener(adapterForFMT);
		GridBagConstraints gbc_btnSaveFile = new GridBagConstraints();
		gbc_btnSaveFile.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSaveFile.insets = new Insets(0, 0, 5, 5);
		gbc_btnSaveFile.gridx = 0;
		gbc_btnSaveFile.gridy = 4;
		panelLeft.add(btnSaveFile, gbc_btnSaveFile);

		panel = new JPanel();
		panel.setBorder(new CompoundBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Statement",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), null));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 7;
		panelLeft.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		spinnerStatement = new JSpinner();
		spinnerStatement.setName(SPINNER_STATEMENT);
		spinnerStatement.addChangeListener(adapterForFMT);
		GridBagConstraints gbc_spinnerStatement = new GridBagConstraints();
		gbc_spinnerStatement.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerStatement.gridx = 0;
		gbc_spinnerStatement.gridy = 0;
		panel.add(spinnerStatement, gbc_spinnerStatement);

		panel_2 = new JPanel();
		panel_2.setBorder(new CompoundBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true),
				"Argument(s)", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), null));
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 5);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 9;
		panelLeft.add(panel_2, gbc_panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[] { 0, 0 };
		gbl_panel_2.rowHeights = new int[] { 0, 0 };
		gbl_panel_2.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panel_2.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel_2.setLayout(gbl_panel_2);

		spinnerArgument = new JSpinner();
		spinnerArgument.setName(SPINNER_ARGUMENT);
		spinnerArgument.addChangeListener(adapterForFMT);
		GridBagConstraints gbc_spinnerArgument = new GridBagConstraints();
		gbc_spinnerArgument.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerArgument.gridx = 0;
		gbc_spinnerArgument.gridy = 0;
		panel_2.add(spinnerArgument, gbc_spinnerArgument);

		panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Comment", TitledBorder.CENTER,
				TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 0, 5);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 11;
		panelLeft.add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] { 0, 0 };
		gbl_panel_1.rowHeights = new int[] { 0, 0 };
		gbl_panel_1.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panel_1.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel_1.setLayout(gbl_panel_1);

		spinnerComment = new JSpinner();
		spinnerComment.setName(SPINNER_COMMENT);
		spinnerComment.addChangeListener(adapterForFMT);
		GridBagConstraints gbc_spinnerComment = new GridBagConstraints();
		gbc_spinnerComment.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerComment.gridx = 0;
		gbc_spinnerComment.gridy = 0;
		panel_1.add(spinnerComment, gbc_spinnerComment);

		mainPanel = new JPanel();
		// mainPain.setOrientation(JSplitPane.VERTICAL_SPLIT);
		// mainPain.setOneTouchExpandable(true);
		// mainPain.setDividerSize(0);
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 1;
		gbc_splitPane.gridy = 0;
		panelMain.add(mainPanel, gbc_splitPane);
		GridBagLayout gbl_mainPanel = new GridBagLayout();
		gbl_mainPanel.columnWidths = new int[] { 6, 0 };
		gbl_mainPanel.rowHeights = new int[] { 42, 0 };
		gbl_mainPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_mainPanel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		mainPanel.setLayout(gbl_mainPanel);

		spSource = new JScrollPane();
		GridBagConstraints gbc_spSource = new GridBagConstraints();
		gbc_spSource.fill = GridBagConstraints.BOTH;
		gbc_spSource.anchor = GridBagConstraints.NORTHWEST;
		gbc_spSource.gridx = 0;
		gbc_spSource.gridy = 0;
		mainPanel.add(spSource, gbc_spSource);

		lblSourceFileName = new JLabel("<No File Selected>");
		lblSourceFileName.setHorizontalAlignment(SwingConstants.CENTER);
		lblSourceFileName.setForeground(Color.BLUE);
		lblSourceFileName.setFont(new Font("Tahoma", Font.PLAIN, 15));
		spSource.setColumnHeaderView(lblSourceFileName);

		txtSource = new JTextArea();
		txtSource.setFont(new Font("Courier New", Font.PLAIN, 14));
		spSource.setViewportView(txtSource);

		JPanel panelStatus = new JPanel();
		GridBagConstraints gbc_panelStatus = new GridBagConstraints();
		gbc_panelStatus.fill = GridBagConstraints.BOTH;
		gbc_panelStatus.gridx = 0;
		gbc_panelStatus.gridy = 1;
		frmAsmFormatter.getContentPane().add(panelStatus, gbc_panelStatus);
		GridBagLayout gbl_panelStatus = new GridBagLayout();
		gbl_panelStatus.columnWidths = new int[] { 0, 0 };
		gbl_panelStatus.rowHeights = new int[] { 0, 0 };
		gbl_panelStatus.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_panelStatus.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panelStatus.setLayout(gbl_panelStatus);

		lblStatus = new JLabel("");
		lblStatus.setForeground(Color.RED);
		lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_lblStatus = new GridBagConstraints();
		gbc_lblStatus.gridx = 0;
		gbc_lblStatus.gridy = 0;
		panelStatus.add(lblStatus, gbc_lblStatus);

		JMenuBar menuBar = new JMenuBar();
		frmAsmFormatter.setJMenuBar(menuBar);

		JMenu mnuFile = new JMenu("File");
		menuBar.add(mnuFile);

		JMenuItem mnuFileOpen = new JMenuItem("Open...");
		mnuFileOpen.setName(MNU_FILE_OPEN);
		mnuFileOpen.addActionListener(adapterForFMT);
		mnuFile.add(mnuFileOpen);

		JSeparator separator_1 = new JSeparator();
		mnuFile.add(separator_1);

		JMenuItem mnuFileExit = new JMenuItem("Exit");
		mnuFileExit.setName(MNU_FILE_EXIT);
		mnuFileExit.addActionListener(adapterForFMT);
		mnuFile.add(mnuFileExit);

	}// initialize

	//////////////////////////////////////////////////////////////////////////

	private static final String BTN_START = "btnStart";
	private static final String BTN_LOAD_LAST_FILE = "btnLoadLastFile";
	private static final String BTN_SAVE_FILE = "btnSaveFile";
//	private static final String NO_FILE = "<No File Selected>";
	private static final String MNU_FILE_OPEN = "mnuFileOpen";
	private static final String MNU_FILE_PRINT_SOURCE = "mnuFilePrintSource";
	private static final String MNU_FILE_PRINT_RESULT = "mnuFilePrintResult";
	private static final String MNU_FILE_EXIT = "mnuFileExit";

	private static final String SPINNER_STATEMENT = "spinnerStatement";
	private static final String SPINNER_ARGUMENT = "spinnerArgument";
	private static final String SPINNER_COMMENT = "spinnerComment";

	private static final String EMPTY_STRING = "";
//	private static final String SPACE = " ";
	private static final String SEMICOLON = ";";
	private static final String LINE_SEPARATOR = System.lineSeparator();
	private static final String FILE_SEPARATOR = File.separator;

	private static final String DEFAULT_DIRECTORY = "." + FILE_SEPARATOR + "Code" + FILE_SEPARATOR + ".";

	//////////////////////////////////////////////////////////////////////////

	// private static final String LINE_SEPARATOR = System.lineSeparator();
	private JFrame frmAsmFormatter;
	private JPanel mainPanel;
	private JLabel lblSourceFilePath;
	private JLabel lblSourceFileName;
	private JButton btnStart;
	private JTextArea txtSource;
	private JScrollPane spSource;
	private JLabel lblStatus;
	private Component verticalStrut;
	private JPanel panel;
	private JPanel panel_1;
	private JSpinner spinnerStatement;
	private JSpinner spinnerComment;
	private Component verticalStrut_1;
	private JButton btnSaveFile;
	private JPanel panel_2;
	private JSpinner spinnerArgument;
	
///////////////////////////////////////////////////////////////
	
	class AdapterForFMT implements ActionListener, ChangeListener {// AdjustmentListener,

		/* ActionListener */
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			String name = ((Component) actionEvent.getSource()).getName();
			switch (name) {
			// menu
			case MNU_FILE_OPEN:
				doOpenFile();
				break;
			case MNU_FILE_PRINT_SOURCE:
				// printListing(tpSource, sourceFileBase + DOT + SUFFIX_ASSEMBLER_8080);
				break;
			case MNU_FILE_PRINT_RESULT:
				// printListing(tpListing, sourceFileBase + DOT + SUFFIX_LISTING);
				break;
			case MNU_FILE_EXIT:
				doFileExit();
				break;

			// buttons
			case BTN_START:
				doStart();
				break;

			case BTN_SAVE_FILE:
				doSaveFile();
				break;

			case BTN_LOAD_LAST_FILE:
				doLoadLastFile();
				break;
			default:
				System.err.println("AdapterForFMT name = " + name);
			}// switch

		}// actionPerformed

		/* AdjustmentListener */

		// @Override
		// public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
		// if (adjustmentEvent.getSource() instanceof JScrollBar) {
		// int value = ((JScrollBar) adjustmentEvent.getSource()).getValue();
		// sbarSource.setValue(value);
		// // sbarResult.setValue(value);
		// } // if scroll bar
		//
		// }// adjustmentValueChanged

		/* Change Listener */

		@Override
		public void stateChanged(ChangeEvent changeEvent) {
			String name = ((Component) changeEvent.getSource()).getName();
			int value = (int) ((JSpinner) changeEvent.getSource()).getValue();
			doSpinnerChanged(name, value);
		}// stateChanged

	}// class AdapterForASM

}// class GUItemplate