package devlopment.wordStar;

import java.awt.Color;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;

import appLogger.AppLogger;

public class WSconverter {

	private JFrame frame;
	private static String APP_NAME = "WordStart Document Converter";
	private String version = "A.0";
	private ApplicationAdapter applicationAdapter = new ApplicationAdapter();
	private String hostDirectory;
	private String sourceFileName = NO_SOURCE_FILE;
	private StyledDocument docResult;
	AppLogger log = AppLogger.getInstance();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WSconverter window = new WSconverter();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				} // try
			}// run
		});
	}// main

	private void processSourceFile(Path sourcePath) {
		Pattern filePattern = Pattern.compile("\\.FI");
		Pattern dotPattern = Pattern.compile("\\.");

		final byte MASK = (byte) 0x7F;
		try {
			byte[] contents = Files.readAllBytes(sourcePath);
			// byte[] newContents = new byte[contents.length];
			// ArrayList<Character> contentsArray = new ArrayList<Character>();
			byte workByte;
			log.infof("%n%,d bytes in Opend file: %s%n", contents.length, sourcePath.getFileName());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < contents.length; i++) {
				workByte = (byte) (contents[i] & MASK);

				if (workByte >= SPACE) {// ASCII printables
					sb.append((char) workByte);
				} else if (workByte == LF | workByte == CR) {
					sb.append((char) workByte);
				} // if

			} // for
			// String s = new String(contentsArray.toString());
			Scanner scanner = new Scanner(sb.toString());
			String aLine;
			while (scanner.hasNextLine()) {
				aLine = scanner.nextLine();
				aLine += System.lineSeparator();
				Matcher fileMatcher = filePattern.matcher(aLine);
				Matcher dotMatcher = dotPattern.matcher(aLine);
				if (fileMatcher.lookingAt()) {// Its a File
					String fileName = aLine.substring(fileMatcher.end()).trim();
					// appendToDoc(docResult, " FILE: <" + fileName + "> " +aLine);
					processSourceFile(Paths.get(hostDirectory, fileName));
				} else if (dotMatcher.lookingAt()) {// Its a dot command
					// appendToDoc(docResult, " DOT: " +aLine);
				} else {
					appendToDoc(docResult, aLine);
				} // if - else

			} // while

			scanner.close();
		} catch (Exception e) {
			// TODO: handle exception
		} // try

	}// processSourceFile

	private void appendToDoc(Document doc, String textToAppend) {
		appendToDoc(doc, textToAppend, null);
	}// appendToDocASM

	private void appendToDoc(Document doc, String textToAppend, AttributeSet attributeSet) {
		try {
			doc.insertString(doc.getLength(), textToAppend, attributeSet);
		} catch (BadLocationException e) {
			log.errorf("Failed to append text: %s %n", textToAppend);
			e.printStackTrace();
		} // try
	}// appendToDocASM

	private void clearDocument(Document doc) {
		if (doc == null) {
			return;
		} // if
		try {
			doc.remove(0, doc.getLength());
		} catch (BadLocationException e) {
			// ignore
			e.printStackTrace();
		} // try
	}// clearDocument

	// private int doDotCommand(byte[] sourceBuffer, int index) {
	// index += 3;
	// byte b1 = sourceBuffer[index];
	// byte b2 = sourceBuffer[index];
	// if (b1 == PERIOD) {
	// StringBuilder sb = new StringBuilder();
	// while (b2 != LF) {
	//
	// } // while
	// } // if
	// return index;
	// }// doDotCommand

	private void doFileOpen() {
		Path sourcePath = Paths.get(hostDirectory);
		JFileChooser chooser = new JFileChooser(sourcePath.resolve(hostDirectory).toString());
		if (chooser.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) {
			return;
		} // if
		hostDirectory = chooser.getSelectedFile().getParent();
		sourceFileName = chooser.getSelectedFile().getName();
		frame.setTitle(APP_NAME + "   " + version + "      " + sourceFileName);
		processSourceFile(Paths.get(chooser.getSelectedFile().getAbsolutePath()));
	}// doFileOpen

//	private void doFileSave() {

		// String fileType = btn8080Z80.getText().equals("Z80") ? ".Z80" : ".asm";
		// String fileName = binaryFileName.replaceAll("(?i)\\.COM", fileType);
		// String absoluteSourcePath = hostDirectory + FILE_SEPARATOR + fileName;
		// try {
		// FileWriter fw = new FileWriter(new File(absoluteSourcePath));
		// PrintWriter pw = new PrintWriter(fw);
		// Scanner scanner = new Scanner(txtSource.getText());
		// String listingLine;
		// while (scanner.hasNextLine()) {
		// listingLine = scanner.nextLine();
		// listingLine = listingLine.replaceAll("\\s++$", EMPTY_STRING);
		// if (listingLine.equals(EMPTY_STRING)) {
		// continue; // skip empty line
		// } // if
		// pw.println(listingLine);
		// } // while
		// scanner.close();
		// pw.close();
		// fw.close();
		// JOptionPane.showMessageDialog(frame, "Saved source file " + absoluteSourcePath, "Save Source",
		// JOptionPane.INFORMATION_MESSAGE);
		// log.infof("Saved source file %s%n", absoluteSourcePath);
		// } catch (Exception e) {
		// log.error("%nFailed to Save source file %s%n", fileName);
		// log.errorf("error message : %s%n%n", e.getMessage());
		// } // try

//	}// doFileSave

	private void appClose() {
		Preferences myPrefs = Preferences.userNodeForPackage(WSconverter.class).node(this.getClass().getSimpleName());
		Dimension dim = frame.getSize();
		myPrefs.putInt("Height", dim.height);
		myPrefs.putInt("Width", dim.width);
		Point point = frame.getLocation();
		myPrefs.putInt("LocX", point.x);
		myPrefs.putInt("LocY", point.y);

		myPrefs.put("HostDirectory", hostDirectory);
		myPrefs.putInt("Divider", splitPaneMain.getDividerLocation());

		// myPrefs.put("AssemblerType", btn8080Z80.getText());
		// myPrefs = null;

		System.exit(0);
	}// appClose

	private void appInit() {
		StyledDocument styledDoc = txtLog.getStyledDocument();
		txtLog.setFont(new Font("Arial", Font.PLAIN, 15));
		AppLogger log = AppLogger.getInstance();
		log.setDoc(styledDoc);
		log.setTextPane(txtLog, "Wordtar 4 Converter");

		Preferences myPrefs = Preferences.userNodeForPackage(WSconverter.class).node(this.getClass().getSimpleName());
		frame.setSize(myPrefs.getInt("Width", 761), myPrefs.getInt("Height", 693));
		frame.setLocation(myPrefs.getInt("LocX", 100), myPrefs.getInt("LocY", 100));
		splitPaneMain.setDividerLocation(myPrefs.getInt("Divider", 200));
		hostDirectory = myPrefs.get("HostDirectory", System.getProperty(USER_HOME, THIS_DIR));
		myPrefs = null;

		docResult = txtResult.getStyledDocument();
		clearDocument(docResult);

		frame.setTitle(APP_NAME + "   " + version + "      " + sourceFileName);

	}// appInit

	public WSconverter() {
		initialize();
		appInit();
	}// Constructor

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				appClose();
			}
		});

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 220, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		frame.getContentPane().setLayout(gridBagLayout);

		JToolBar toolBar = new JToolBar();
		GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.insets = new Insets(0, 0, 5, 0);
		gbc_toolBar.gridx = 1;
		gbc_toolBar.gridy = 0;
		frame.getContentPane().add(toolBar, gbc_toolBar);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		frame.getContentPane().add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0 };
		gbl_panel.rowHeights = new int[] { 0 };
		gbl_panel.columnWeights = new double[] { Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		splitPaneMain = new JSplitPane();
		splitPaneMain.setOneTouchExpandable(true);
		splitPaneMain.setOrientation(JSplitPane.VERTICAL_SPLIT);
		GridBagConstraints gbc_splitPaneMain = new GridBagConstraints();
		gbc_splitPaneMain.insets = new Insets(0, 0, 5, 0);
		gbc_splitPaneMain.fill = GridBagConstraints.BOTH;
		gbc_splitPaneMain.gridx = 1;
		gbc_splitPaneMain.gridy = 1;
		frame.getContentPane().add(splitPaneMain, gbc_splitPaneMain);

		JScrollPane scrollPaneSource = new JScrollPane();
		splitPaneMain.setLeftComponent(scrollPaneSource);

		txtLog = new JTextPane();
		scrollPaneSource.setViewportView(txtLog);

		JLabel labelSource = new JLabel("Application Log");
		labelSource.setFont(new Font("Courier New", Font.PLAIN, 15));
		labelSource.setHorizontalAlignment(SwingConstants.CENTER);
		labelSource.setForeground(Color.BLUE);
		scrollPaneSource.setColumnHeaderView(labelSource);

		JScrollPane scrollPaneResult = new JScrollPane();
		splitPaneMain.setRightComponent(scrollPaneResult);

		txtResult = new JTextPane();
		scrollPaneResult.setViewportView(txtResult);

		JLabel labelResult = new JLabel("New label");
		labelResult.setForeground(Color.BLUE);
		labelResult.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPaneResult.setColumnHeaderView(labelResult);

		JToolBar toolBar_1 = new JToolBar();
		GridBagConstraints gbc_toolBar_1 = new GridBagConstraints();
		gbc_toolBar_1.gridx = 1;
		gbc_toolBar_1.gridy = 2;
		frame.getContentPane().add(toolBar_1, gbc_toolBar_1);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnuFile = new JMenu("File");
		menuBar.add(mnuFile);

		JMenuItem mnuOpenFile = new JMenuItem("Open File");
		mnuOpenFile.setActionCommand(MNU_FILE_OPEN);
		mnuOpenFile.addActionListener(applicationAdapter);
		mnuFile.add(mnuOpenFile);

		JSeparator separator = new JSeparator();
		mnuFile.add(separator);

		JMenuItem mnuFileSave = new JMenuItem("Save");
		mnuFileSave.setActionCommand(MNU_FILE_SAVE);
		mnuFileSave.addActionListener(applicationAdapter);
		mnuFile.add(mnuFileSave);

		JSeparator separator_1 = new JSeparator();
		mnuFile.add(separator_1);

		JMenuItem mnuFileSaveAs = new JMenuItem("Save As ...");
		mnuFileSaveAs.setActionCommand(MNU_FILE_SAVE_AS);
		mnuFileSaveAs.addActionListener(applicationAdapter);
		mnuFile.add(mnuFileSaveAs);

		JMenuItem mnuFilePrint = new JMenuItem("Print...");
		mnuFilePrint.setActionCommand(MNU_FILE_PRINT);
		mnuFilePrint.addActionListener(applicationAdapter);
		mnuFile.add(mnuFilePrint);

		JSeparator separator_2 = new JSeparator();
		mnuFile.add(separator_2);

		JMenuItem mnuFileExit = new JMenuItem("Exit");
		mnuFileExit.setActionCommand(MNU_FILE_EXIT);
		mnuFileExit.addActionListener(applicationAdapter);
		mnuFile.add(mnuFileExit);

	}// initialize()

	private JSplitPane splitPaneMain;
	private JTextPane txtLog;

	public static final String USER_HOME = "user.home";
	public static final String THIS_DIR = ".";
	public static final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");
	public final static String FILE_LOCATION = System.getenv("APPDATA") + FILE_SEPARATOR + "Disassembler";
	public final static String NO_SOURCE_FILE = "< No File Selected>";
	public final static String MNU_FILE = "mnuFile";
	public final static String MNU_FILE_OPEN = "mnuFileOpen";
	public final static String MNU_FILE_SAVE = "mnuFileSave";
	public final static String MNU_FILE_SAVE_AS = "mnuFileSaveAs";
	public final static String MNU_FILE_PRINT = "mnuFilePrint";
	public final static String MNU_FILE_EXIT = "mnuFileExit";
	static final Byte SPACE = 0X20;
	static final Byte PERIOD = 0X2E;
	static final Byte LF = 0X0A;
	static final Byte CR = 0X0D;

	/**********************************************************************/
	private static HashMap<Byte, ByteType> byteMeanings;
	private JTextPane txtResult;
	static {
		byteMeanings = new HashMap<Byte, ByteType>();
		for (byte i = 0x20; i < (byte) 0x7F; i++) {
			byteMeanings.put(i, ByteType.ASCII);
		} // for

		for (byte i = (byte) 0x80; i < (byte) 0; i++) {
			byteMeanings.put(i, ByteType.BIT8_SET);
		} // for

		byteMeanings.put((byte) 0x00, ByteType.CONTROL);// NULL
		byteMeanings.put((byte) 0x01, ByteType.TOGGLE);// Alternate Font
		byteMeanings.put((byte) 0x02, ByteType.TOGGLE);// Bold Mode
		byteMeanings.put((byte) 0x03, ByteType.CONTROL);// Pause print for response
		byteMeanings.put((byte) 0x04, ByteType.TOGGLE);// Double Strike
		byteMeanings.put((byte) 0x05, ByteType.CONTROL);// Custom Print
		byteMeanings.put((byte) 0x06, ByteType.CONTROL);// Phantom Space
		byteMeanings.put((byte) 0x07, ByteType.CONTROL);// Phantom Rubout
		byteMeanings.put((byte) 0x08, ByteType.CONTROL);// Overprint previous character
		byteMeanings.put((byte) 0x09, ByteType.CONTROL);// Tab
		byteMeanings.put((byte) 0x0A, ByteType.CONTROL);// LineFeed: follows CR for line break
		byteMeanings.put((byte) 0x0B, ByteType.CONTROL);// Center line
		byteMeanings.put((byte) 0x0C, ByteType.CONTROL);// Form Feed
		byteMeanings.put((byte) 0x0D, ByteType.CRLF);// Carriage Return :precedes LF for line break
		byteMeanings.put((byte) 0x0E, ByteType.CONTROL);// Return to normal char width
		byteMeanings.put((byte) 0x0F, ByteType.CONTROL);// Non-breaking space
		byteMeanings.put((byte) 0x10, ByteType.CONTROL);// unused
		byteMeanings.put((byte) 0x11, ByteType.CONTROL);// Custom Print Control
		byteMeanings.put((byte) 0x12, ByteType.CONTROL);// Custom Print Control
		byteMeanings.put((byte) 0x13, ByteType.TOGGLE);// Underline
		byteMeanings.put((byte) 0x14, ByteType.TOGGLE);// Superscript
		byteMeanings.put((byte) 0x15, ByteType.CONTROL);// Unused
		byteMeanings.put((byte) 0x16, ByteType.TOGGLE);// Subscript
		byteMeanings.put((byte) 0x17, ByteType.CONTROL);// Custom Print Control
		byteMeanings.put((byte) 0x18, ByteType.TOGGLE);// Overstrike
		byteMeanings.put((byte) 0x19, ByteType.TOGGLE);// Italic
		byteMeanings.put((byte) 0x1A, ByteType.CONTROL);// End Of File ^Z
		byteMeanings.put((byte) 0x1B, ByteType.CONTROL);// Marks following char is extended char
		byteMeanings.put((byte) 0x1C, ByteType.CONTROL);// Marks preceding char is extended char
		byteMeanings.put((byte) 0x1D, ByteType.CONTROL);// Start/Stop ??
		byteMeanings.put((byte) 0x1E, ByteType.CONTROL);// Inactive soft hyphen
		byteMeanings.put((byte) 0x1F, ByteType.CONTROL);// Active soft hyphen

	}// static

	/**********************************************************************/
	/**********************************************************************/
	private class ApplicationAdapter implements ActionListener {

		/* ActionListener */
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			String actionCommand = actionEvent.getActionCommand();
			String message = null;
			switch (actionCommand) {
			case MNU_FILE_OPEN:
				doFileOpen();
				break;

			case MNU_FILE_SAVE:
				// doFileSave();
				break;
			case MNU_FILE_SAVE_AS:
				// doFileSaveAs();
				break;
			case MNU_FILE_PRINT:
				// doFilePrint();
				break;
			case MNU_FILE_EXIT:
				appClose();
				break;
			default:
				message = actionCommand;
				log.error(message);
			}// switch

		}// actionPerformed

	}// class ApplicationAdapter

	public enum ByteType {
		ASCII, CONTROL, TOGGLE, BIT8_SET, CRLF
	}//
}// class WSconverter
