package devlopment.manualDisassembler;

import java.awt.Color;
import java.awt.Dimension;

//package codeSupport;

//import hardware.ConditionCodeRegister;

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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.prefs.Preferences;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import myComponents.AppLogger;
import myComponents.Hex64KSpinner;

public class ManualDisassembler {

	private ApplicationAdapter applicationAdapter = new ApplicationAdapter();
	private myComponents.AppLogger log = myComponents.AppLogger.getInstance();
	private String hostDirectory;
	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ManualDisassembler manualDisassembler = new ManualDisassembler();
					manualDisassembler.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void addFragement() {
		int startLocNew = (int) spinnerBeginFragment.getValue();
		int endLocNew = (int) spinnerEndFragment.getValue();
		String type = groupFragment.getSelection().getActionCommand();
		codeFragmentModel.addItem(new CodeFragment(startLocNew, endLocNew, type));
		listCodeFragments.updateUI();
	}// addFragement

	private void removeFragment() {
		int index = listCodeFragments.getSelectedIndex();
		if (index == -1) {
			return;
		} // if
			// System.out.printf("<removeFragment> getSelectedIndex() = %s%n", listCodeFragments.getSelectedIndex());
		codeFragmentModel.removeItem(listCodeFragments.getSelectedIndex());
		if (index > codeFragmentModel.getSize() - 1) {
			listCodeFragments.setSelectedIndex(codeFragmentModel.getSize() - 1);
		} // if
		listCodeFragments.updateUI();
	}// removeFragment

	private void processFragment() {
		int index = listCodeFragments.getSelectedIndex();
		if (index == -1) {
			return;
		} // if
		CodeFragment cf = codeFragmentModel.getElementAt(index);
		if (!cf.type.equals(CodeFragment.UNKNOWN)) {
			return;
		} // if
		System.out.printf("<processFragment> - DoIt!%n");
		entryPoints.push(cf.startLoc);
		buildFragments();
		listCodeFragments.updateUI();
	}//

	private void actionStart() {
		btnProcessFragment.setEnabled(true);
		btnAddFragment.setEnabled(true);
		btnRemoveFragment.setEnabled(true);
		btnCombineFragments.setEnabled(true);
		btn8080Z80.setEnabled(true);
		listCodeFragments.setEnabled(true);

		// int lastLocation = binaryData.capacity();

		labels = new HashSet<Integer>();

		beenThere = new HashSet<Integer>();
		beenThere.add(5);
		beenThere.add(0);

		entryPoints.push(OFFSET);
		// int counter = 0;
		while (!entryPoints.isEmpty()) {
			if (!beenThere.contains(entryPoints.peek())) {
				buildFragments();
			} else {
				entryPoints.pop(); // toss the entry point , been there!
			}
		} // while
			// combineFragments();
		listCodeFragments.updateUI();
		tabPaneDisplays.setSelectedIndex(TAB_WIP);
	}// actionStart

	private SimpleAttributeSet[] makeAttributes() {
		// int baseFontSize = txtWIPsource.getFont().getSize();
		SimpleAttributeSet baseAttributes = new SimpleAttributeSet();
		StyleConstants.setFontFamily(baseAttributes, "Courier New");

		SimpleAttributeSet[] workingSets = new SimpleAttributeSet[4];
		workingSets[ATTR_ADDRESS] = new SimpleAttributeSet(baseAttributes);
		workingSets[ATTR_BINARY_CODE] = new SimpleAttributeSet(baseAttributes);
		workingSets[ATTR_ASM_CODE] = new SimpleAttributeSet(baseAttributes);
		workingSets[ATTR_FUNCTION] = new SimpleAttributeSet(baseAttributes);

		StyleConstants.setForeground(workingSets[ATTR_ADDRESS], Color.GRAY);
		StyleConstants.setForeground(workingSets[ATTR_BINARY_CODE], Color.BLUE);
		StyleConstants.setForeground(workingSets[ATTR_ASM_CODE], Color.RED);
		StyleConstants.setForeground(workingSets[ATTR_FUNCTION], Color.GRAY);
		return workingSets;
	}

	private void combineFragments() {
		ArrayList<CodeFragment> tempFragments = new ArrayList<CodeFragment>();

		if (codeFragmentModel.getSize() < 2) {
			return;
		}
		CodeFragment cfOriginal = null;
		CodeFragment cfNew = codeFragmentModel.getElementAt(0);

		for (int i = 1; i < codeFragmentModel.getSize(); i++) {
			cfOriginal = codeFragmentModel.getElementAt(i);
			if (!cfOriginal.type.equals(cfNew.type)) {
				tempFragments.add(cfNew);
				cfNew = codeFragmentModel.getElementAt(i);
			} else if (cfOriginal.startLoc == cfNew.endLoc + 1) {
				cfNew.endLoc = cfOriginal.endLoc;
			} else {
				tempFragments.add(cfNew);
				cfNew = codeFragmentModel.getElementAt(i);
			} // if
		}
		tempFragments.add(cfNew);
		codeFragmentModel.clear();
		for (int i = 0; i < tempFragments.size(); i++) {
			codeFragmentModel.addItem(tempFragments.get(i));
		} // for - rebuild codeFragments
		listCodeFragments.updateUI();
	}

//	private void buildFragments0() {
//		int startLocation = 0;
//		int currentLocation = 0;
//		OpcodeStructure8080 currentOpCode = null;
//		;
//		PCaction pcAction = PCaction.NORMAL;
//		;
//
//		startLocation = entryPoints.pop();
//		currentLocation = startLocation;
//		boolean keepGoing = true;
//		int targetAddress;
//		while (keepGoing) {
//			// int a = currentLocation;
//			if (currentLocation > binaryData.capacity()) {
//				return;
//			}
//
//			if (beenThere.add(currentLocation) == false) {
//				// System.out.printf("already visited %04X%n", startLocation);
//				codeFragmentModel.addItem(new CodeFragment(startLocation, (currentLocation - 1), CodeFragment.CODE));
//
//				return;
//			} //
//			try {
//				currentOpCode = opcodeMap.get(binaryData.get(currentLocation));
//			} catch (Exception ex) {
//				ex.printStackTrace();
//
//			}
//
//			// System.out.printf("Location = %04X, opcode = %s%n", currentLocation, currentOpCode.getInstruction());
//			pcAction = currentOpCode.getPcAction();
//
//			switch (pcAction) {
//
//			case NORMAL: // regular opcodes and conditional RETURNS
//				currentLocation += currentOpCode.getSize();
//				keepGoing = true;
//				break;
//			case CONTINUATION: // All CALLs and all Conditional RETURNS
//				targetAddress = makeTargetAddress(currentLocation);
//				entryPoints.push(targetAddress);
//				labels.add(targetAddress);
//				currentLocation += currentOpCode.getSize();
//				keepGoing = true;
//				break;
//			case TERMINATES: // RET
//				currentLocation += currentOpCode.getSize();
//				keepGoing = false;
//				break;
//			case TOTAL: // JUMP PCHL
//				if (currentOpCode.getInstruction().equals("JMP")) { // only for JMP
//					targetAddress = makeTargetAddress(currentLocation);
//					entryPoints.push(targetAddress);
//					labels.add(targetAddress);
//				} // if
//				currentLocation += currentOpCode.getSize();
//				keepGoing = false;
//				break;
//			}// switch
//		} // - keep going
//		codeFragmentModel.addItem(new CodeFragment(startLocation, (currentLocation - 1), CodeFragment.CODE));
//		return;
//	}// buildFragments

	private void buildFragments() {
		int startLocation = 0;
		int currentLocation = 0;
		OperationStructure currentOpCode = null;
		;
		PCaction pcAction = PCaction.NORMAL;
		;

		startLocation = entryPoints.pop();
		currentLocation = startLocation;
		boolean keepGoing = true;
		int targetAddress;
		String mapKey;
		while (keepGoing) {

			if (currentLocation >= binaryData.capacity()) {
				return;
			}

			if (beenThere.add(currentLocation) == false) {
				// System.out.printf("already visited %04X%n", startLocation);
				codeFragmentModel.addItem(new CodeFragment(startLocation, (currentLocation - 1), CodeFragment.CODE));

				return;
			} //
			try {
				mapKey = String.format("%02X", binaryData.get(currentLocation));
				currentOpCode = OpCodeMap.get(mapKey);
//				currentOpCode = OpCodeMap8080.get(mapKey);
				// currentOpCode = OpCodeMap8080.get(binaryData.get(currentLocation));
			} catch (Exception ex) {
				ex.printStackTrace();

			} //

			// System.out.printf("Location = %04X, opcode = %s%n", currentLocation, currentOpCode.getInstruction());
			pcAction = currentOpCode.getPCaction();

			switch (pcAction) {

			case NORMAL: // regular opcodes and conditional RETURNS
				currentLocation += currentOpCode.getSize();
				keepGoing = true;
				break;
			case CONTINUATION: // All CALLs and all Conditional RETURNS
				targetAddress = makeTargetAddress(currentLocation);
				entryPoints.push(targetAddress);
				labels.add(targetAddress);
				currentLocation += currentOpCode.getSize();
				keepGoing = true;
				break;
			case TERMINATES: // RET
				currentLocation += currentOpCode.getSize();
				keepGoing = false;
				break;
			case TOTAL: // JUMP PCHL
				if (currentOpCode.getInstruction().equals("JMP")) { // only for JMP
					targetAddress = makeTargetAddress(currentLocation);
					entryPoints.push(targetAddress);
					labels.add(targetAddress);
				} // if
				currentLocation += currentOpCode.getSize();
				keepGoing = false;
				break;
			}// switch
		} // - keep going
		codeFragmentModel.addItem(new CodeFragment(startLocation, (currentLocation - 1), CodeFragment.CODE));
		return;
	}// buildFragments1

	private int makeTargetAddress(int currentLocation) {
		// int hi = (binaryData.get(currentLocation + 2) & 0xFF) * 256;
		// int lo = binaryData.get(currentLocation + 1);
		// int v = hi + lo;
		// System.out.printf("<makeTargetAddress> target = %04X from currentLocation %04X%n", v, currentLocation);
		return ((binaryData.get(currentLocation + 2) & 0xFF) * 256) + (binaryData.get(currentLocation + 1) & 0xFF);
	}// makeTargetAddress

	private void openBinaryFile() {
		// String fileLocation = "C:\\Users\\admin\\Dropbox\\Resources\\NativeFiles";

		Path sourcePath = Paths.get(hostDirectory);
		JFileChooser chooser = new JFileChooser(sourcePath.resolve(hostDirectory).toString());
		if (chooser.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) {
			return;
		} // if
		hostDirectory = chooser.getSelectedFile().getParent();
		binaryFile = chooser.getSelectedFile();
		log.infof("Opening Binary File %s%n", chooser.getSelectedFile());
		if (!processBianryFile(binaryFile)) {
			JOptionPane.showMessageDialog(null, "Problem with binary Source file" + binaryFile.getAbsolutePath(),
					"openBinaryFile()", JOptionPane.ERROR_MESSAGE);
			return;
		} // if
		codeFragmentModel.removeItem(0);
		codeFragmentModel
				.addItem(new CodeFragment(OFFSET, (int) (binaryFile.length() + OFFSET) - 1, CodeFragment.UNKNOWN));

	}// openBinaryFile

	private boolean processBianryFile(File binaryFile) {
		boolean result = true; // assume all goes well
		binaryFileName = binaryFile.getName();
		binaryFilePath = binaryFile.getAbsolutePath();

		FileChannel fcIn = null;
		FileInputStream fout = null;
		try {
			fout = new FileInputStream(binaryFile);
			fcIn = fout.getChannel();
		} catch (FileNotFoundException fileNotFoundException) {
			JOptionPane.showMessageDialog(null, binaryFile.getAbsolutePath() + "not found", "processBianryFile",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} // exit gracefully

		long fileSize = binaryFile.length();
		int roundedFileSize = ((((int) fileSize + OFFSET) / CHARACTERS_PER_LINE)) * CHARACTERS_PER_LINE;
		binaryData = ByteBuffer.allocate(roundedFileSize);
		binaryData.position(OFFSET);
		// byte[] sectorData = new byte[sectorSize];
		try {
			fcIn.read(binaryData);
			fcIn.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, binaryFile.getAbsolutePath() + " - IOException ", "processBianryFile",
					JOptionPane.ERROR_MESSAGE);
			try {
				fout.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // try

			return false; // exit gracefully
		} // try
			// binaryData.flip();
		displayBinaryFile(binaryData, (int) roundedFileSize);
		haveBinanryFile(true);
		frame.setTitle(APP_NAME + " - " + binaryFileName);
		tabPaneDisplays.setSelectedIndex(TAB_BINARY_FILE);
		try {
			fout.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // try
		return result;
	}

	private void displayBinaryFile(ByteBuffer binaryData, int roundedFileSize) {
		clearDocument(docBinary);
		// docBinary = txtBinaryFile.getDocument();
		txtWIPbinary.setDocument(docBinary);
		byte[] displayLine = new byte[CHARACTERS_PER_LINE];
		binaryData.position(OFFSET);
		try {
			int lineNumber = OFFSET / CHARACTERS_PER_LINE;
			while (binaryData.position() + CHARACTERS_PER_LINE <= binaryData.capacity()) {
				// System.out.printf("<displayBinaryFile> lineNumber = %d, position = %d%n", lineNumber,
				// binaryData.position());
				binaryData.get(displayLine, 0, CHARACTERS_PER_LINE);
				docBinary.insertString(docBinary.getLength(), formatLine(displayLine, lineNumber++), null);
			} // while
		} catch (BadLocationException badLocationException) {
			badLocationException.printStackTrace();

		} catch (IndexOutOfBoundsException indexOutOfBoundsException) {
			indexOutOfBoundsException.printStackTrace();
		}
		binaryData.rewind();
		txtBinaryFile.setCaretPosition(0);
		txtWIPbinary.setCaretPosition(0);
	}// displayBinaryFile

	private String formatLine(byte[] lineToDisplay, int lineNumber) {
		StringBuilder sbHex = new StringBuilder();
		StringBuilder sbDot = new StringBuilder();

		// byte[] lineOfBytes = new byte[CHARACTERS_PER_LINE];
		byte subject;

		sbHex.append(String.format("%04X: ", lineNumber * CHARACTERS_PER_LINE));
		// int bufferIndex;
		for (int i = 0; i < lineToDisplay.length; i++) {
			// bufferIndex = (lineNumber * CHARACTERS_PER_LINE) + i;
			subject = lineToDisplay[i];
			sbHex.append(String.format("%02X ", subject));
			sbDot.append((subject >= 0x20 && subject <= 0x7F) ? (char) subject : ".");
			if (i == 7) {
				sbHex.append(" ");
				sbDot.append(" ");
			} // if
		} // for
		sbHex.append(" ");
		sbDot.append(String.format("%n"));
		return sbHex.toString() + sbDot.toString();
	}// formatLine

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

	private void haveBinanryFile(boolean state) {
		btnStart.setEnabled(state);
		spinnerBeginFragment.setEnabled(state);
		spinnerEndFragment.setEnabled(state);
		panelFragmentTypes.setEnabled(state);
		mnuFileSaveWIP.setEnabled(state);
		mnuFileSaveWIPas.setEnabled(state);
		mnuFileReset.setEnabled(state);

		mnuFileLoadWIP.setEnabled(!state);
		mnuFileOpenBinaryFile.setEnabled(!state);

		if (state) {

		} else {
			btnAddFragment.setEnabled(state);
			btnRemoveFragment.setEnabled(state);
			btnProcessFragment.setEnabled(state);
			btnCombineFragments.setEnabled(state);
			btnBuildASM.setEnabled(state);
			listCodeFragments.setEnabled(state);
		} // if
	}// haveBinanryFile

	private void setFragmentRadioButton(String type) {
		switch (type) {
		case CodeFragment.CODE:
			rbCode.setSelected(true);
			break;
		case CodeFragment.CONSTANT:
			rbConstant.setSelected(true);
			break;
		case CodeFragment.LITERAL:
			rbLiteral.setSelected(true);
			break;
		case CodeFragment.RESERVED:
			rbReserved.setSelected(true);
			break;
		case CodeFragment.UNKNOWN:
			rbUnknown.setSelected(true);
			break;
		}// switch
	}// //

	private void displayFragmenBinary(JTextArea txtArea, int startLocation, int endLocation) {
		Highlighter.HighlightPainter yellowPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.yellow);
			txtArea.getHighlighter().removeAllHighlights();

		try {
			int lineStart = txtArea.getLineStartOffset((startLocation / CHARACTERS_PER_LINE));
			int lineEnd = txtArea.getLineEndOffset((endLocation / CHARACTERS_PER_LINE));

			txtArea.getHighlighter().removeAllHighlights();
			if (startLocation>=endLocation) {
				return;
			}// if
			txtArea.getHighlighter().addHighlight(lineStart, lineEnd, yellowPainter);
			txtArea.setCaretPosition(lineStart);
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		} //
	}// displayFragmentSource

	private void displayFragmentSource(JTextPane txtArea, CodeFragment codeFragment) {
		String type = codeFragment.type;
		switch (type) {
		case CodeFragment.CODE:
			showFragmentCode(txtArea.getDocument(), codeFragment);
			break;
		case CodeFragment.CONSTANT:
			break;
		case CodeFragment.LITERAL:
			break;
		case CodeFragment.RESERVED:
			break;
		case CodeFragment.UNKNOWN:
			showFragmentCode(txtArea.getDocument(), codeFragment);
			break;
		default:
		}// switch
		txtArea.setCaretPosition(0);
	}// displayFragmentSource

	private void showFragmentCode(Document doc, CodeFragment codeFragment) {
		int startLocation = codeFragment.startLoc;
		int endLocation = codeFragment.endLoc;
		// int codeSize = codeFragment.size();
		clearDocument(doc);
		// OpcodeStructure8080 currentOpCode = null;
		OperationStructure currentOpCode = null;

		// workingSets[ATTR_ADDRESS] = new SimpleAttributeSet(baseAttributes);
		// workingSets[ATTR_BINARY_CODE] = new SimpleAttributeSet(baseAttributes);
		// workingSets[ATTR_ASM_CODE] = new SimpleAttributeSet(baseAttributes);
		// workingSets[ATTR_FUNCTION] = new SimpleAttributeSet(baseAttributes);

		SimpleAttributeSet[] attributeSets = makeAttributes();

		int currentLocation = startLocation;
		int opCodeSize;
		byte currentValue0, currentValue1, currentValue2, currentValue3;
		String part1 = "", part2 = "", part3 = "", part3A= "", part4 = "";
		String fmt00 = "";
		while (currentLocation <= endLocation) {
			String mapKey = String.format("%02X", binaryData.get(currentLocation));
			currentOpCode = OpCodeMap.get(mapKey);
//			currentOpCode = OpCodeMap8080.get(mapKey);bbb
			// currentOpCode = opcodeMap.get(binaryData.get(currentLocation));
			currentValue0 = binaryData.get(currentLocation);
			opCodeSize = currentOpCode.getSize();

			currentValue1 = opCodeSize > 1 ? binaryData.get(currentLocation + 1) : 0;
			currentValue2 = opCodeSize > 2 ? binaryData.get(currentLocation + 2) : 0;
			currentValue3 = opCodeSize > 3 ? binaryData.get(currentLocation + 2) : 0;
			try {

				part1 = String.format("%04X%4s", currentLocation, "");
				doc.insertString(doc.getLength(), part1, attributeSets[ATTR_ADDRESS]);

				switch (opCodeSize) {
				case 1:
					part2 = String.format("%02X%8s", currentValue0, "");
					break;
				case 2:
					part2 = String.format("%02X%02X%6s", currentValue0, currentValue1, "");
					break;
				case 3:
					part2 = String.format("%02X%02X%02X%4s", currentValue0, currentValue1, currentValue2, "");
					// doc.insertString(doc.getLength(), part2, attributeSets[ATTR_BINARY_CODE]);
					// part3 = String.format("%-15s", currentOpCode.getAssemblerCode(currentValue1, currentValue2));
					// doc.insertString(doc.getLength(), part3, attributeSets[ATTR_ASM_CODE]);
					break;
				default:
					log.errorf("Bad opCode size : %d at Location: %04X%n%n", opCodeSize,currentLocation);
				}// switch
				doc.insertString(doc.getLength(), part2, attributeSets[ATTR_BINARY_CODE]);

				switch (currentOpCode.getType()) {
				case I00:
					fmt00 = "%-5s";
					part3A = String.format(fmt00, currentOpCode.getInstruction());
					 part3 = String.format("%-21s",part3A);
					break;
				case I01:
					fmt00 = "%-5s%s";
					part3A = String.format(fmt00, currentOpCode.getInstruction(),
							 currentOpCode.getDestination());
					 part3 = String.format("%-21s",part3A);
					break;
				case I02:
					fmt00 = "%-5s%s,%s";
					part3A = String.format(fmt00, currentOpCode.getInstruction(),
							 currentOpCode.getDestination(),
							 currentOpCode.getSource());
					 part3 = String.format("%-21s",part3A);
					break;
				case I10:
					 fmt00 = "%-5s%02XH";
					 part3A = String.format(fmt00, currentOpCode.getInstruction(),
							currentValue1);
					 part3 = String.format("%-21s",part3A);
					break;
				case I11:
					 fmt00 = "%-5s%s,%02X";
					 part3A = String.format(fmt00, currentOpCode.getInstruction(),
							currentOpCode.getDestination(),currentValue1);
					 part3 = String.format("%-21s",part3A);
					break;
				case I12:
					 fmt00 = "%-5s%02XH";
					 part3A = String.format(fmt00, currentOpCode.getInstruction(),
							currentValue1);
					 part3 = String.format("%-21s",part3A);
					break;
				case I13:
					break;
				case I14:
					break;
				case I15:
					break;
				case I16:
					break;
				case I20:
					 fmt00 = "%-5s%02X%02XH";
					 part3A = String.format(fmt00, currentOpCode.getInstruction(),
							 currentValue2,currentValue1);
					 part3 = String.format("%-21s",part3A);
					break;
				case I21:
					 fmt00 = "%-5s%s,%02X%02XH";
					 part3A = String.format(fmt00, currentOpCode.getInstruction(),
							 currentOpCode.getDestination(),
							 currentValue2,currentValue1);
					 part3 = String.format("%-21s",part3A);
					break;
				case I22:
					break;
				case I23:
					break;
				case I24:
					fmt00 = "%-5s(%02X%02XH),%s";
					 part3A = String.format(fmt00, currentOpCode.getInstruction(),
							 
							 currentValue2,currentValue1,currentOpCode.getSource());
					 part3 = String.format("%-21s",part3A);
					break;
				case I25:
					break;
				case I26:
					fmt00 = "%-5s%s,(%02X%02XH)";
					 part3A = String.format(fmt00, currentOpCode.getInstruction(),
							 
							 currentOpCode.getDestination(),currentValue2,currentValue1);
					 part3 = String.format("%-21s",part3A);
					break;
				case I27:
					break;
				default:
					log.errorf("Bad Instruction type : %s at Location: %04X%n%n",
							currentOpCode.getType().toString(),currentLocation);
				}// Type
				
				 doc.insertString(doc.getLength(), part3, attributeSets[ATTR_ASM_CODE]);
				 part3 = "";

				part4 = String.format("%s%n", currentOpCode.getFunction());
				doc.insertString(doc.getLength(), part4, attributeSets[ATTR_FUNCTION]);
			} catch (BadLocationException badLocationException) {
				badLocationException.printStackTrace();
			}
			currentLocation += opCodeSize;

		} // while opcodeMap
	}// showFragmentCode

	private void showFragmentCode00(Document doc, CodeFragment codeFragment) {
		int startLocation = codeFragment.startLoc;
		int endLocation = codeFragment.endLoc;
		// int codeSize = codeFragment.size();
		clearDocument(doc);
		OpcodeStructure8080 currentOpCode = null;

		// workingSets[ATTR_ADDRESS] = new SimpleAttributeSet(baseAttributes);
		// workingSets[ATTR_BINARY_CODE] = new SimpleAttributeSet(baseAttributes);
		// workingSets[ATTR_ASM_CODE] = new SimpleAttributeSet(baseAttributes);
		// workingSets[ATTR_FUNCTION] = new SimpleAttributeSet(baseAttributes);

		SimpleAttributeSet[] attributeSets = makeAttributes();

		int currentLocation = startLocation;
		int opCodeSize;
		byte currentValue0, currentValue1, currentValue2;
		String part1, part2, part3, part4;
		while (currentLocation <= endLocation) {
			currentOpCode = opcodeMap.get(binaryData.get(currentLocation));
			currentValue0 = binaryData.get(currentLocation);
			opCodeSize = currentOpCode.getSize();
			currentValue1 = opCodeSize > 1 ? binaryData.get(currentLocation + 1) : 0;
			currentValue2 = opCodeSize > 2 ? binaryData.get(currentLocation + 2) : 0;
			try {

				part1 = String.format("%04X%4s", currentLocation, "");
				doc.insertString(doc.getLength(), part1, attributeSets[ATTR_ADDRESS]);
				switch (opCodeSize) {
				case 1:
					part2 = String.format("%02X%8s", currentValue0, "");
					doc.insertString(doc.getLength(), part2, attributeSets[ATTR_BINARY_CODE]);
					part3 = String.format("%-15s", currentOpCode.getAssemblerCode());
					doc.insertString(doc.getLength(), part3, attributeSets[ATTR_ASM_CODE]);
					break;
				case 2:
					part2 = String.format("%02X%02X%6s", currentValue0, currentValue1, "");
					doc.insertString(doc.getLength(), part2, attributeSets[ATTR_BINARY_CODE]);
					part3 = String.format("%-15s", currentOpCode.getAssemblerCode(currentValue1));
					doc.insertString(doc.getLength(), part3, attributeSets[ATTR_ASM_CODE]);
					break;
				case 3:
					part2 = String.format("%02X%02X%02X%4s", currentValue0, currentValue1, currentValue2, "");
					doc.insertString(doc.getLength(), part2, attributeSets[ATTR_BINARY_CODE]);
					part3 = String.format("%-15s", currentOpCode.getAssemblerCode(currentValue1, currentValue2));
					doc.insertString(doc.getLength(), part3, attributeSets[ATTR_ASM_CODE]);
					break;
				default:
				}// switch
				part4 = String.format("%s%n", currentOpCode.getFunction());
				doc.insertString(doc.getLength(), part4, attributeSets[ATTR_FUNCTION]);
			} catch (BadLocationException badLocationException) {
				badLocationException.printStackTrace();
			}
			currentLocation += opCodeSize;

		} // while opcodeMap
	}// showFragmentCode00

	private void buildSourceHeader(Document doc) {
		lblSourceHeader.setText(binaryFilePath);
		clearDocument(docASM);
		String header = String.format(";Source File name - %s%n", binaryFileName);
		appendToDocASM(header);
		header = String.format(";Generated by - ManualDisassembler V A.0 on %s%n%n", new Date().toString());
		appendToDocASM(header);
		header = String.format("%-10s %-7s %-10s %-25s%n", "NULL", "EQU", "00H", "; Null");
		appendToDocASM(header);
		header = String.format("%-10s %-7s %-10s %-25s%n", "SOH", "EQU", "01H", "; Start of Heading");
		appendToDocASM(header);
		header = String.format("%-10s %-7s %-10s %-25s%n", "BELL", "EQU", "07H", "; Bell");
		appendToDocASM(header);
		header = String.format("%-10s %-7s %-10s %-25s%n", "LF", "EQU", "0AH", "; Line Feed");
		appendToDocASM(header);
		header = String.format("%-10s %-7s %-10s %-25s%n", "CR", "EQU", "0DH", "; Carriage Return");
		appendToDocASM(header);
		header = String.format("%-10s %-7s %-10s %-25s%n", "DOLLAR", "EQU", "24H", "; Dollar Sign");
		appendToDocASM(header);
		header = String.format("%-10s %-7s %-10s %-25s%n", "QMARK", "EQU", "3FH", "; Question Mark");
		appendToDocASM(header);

		header = String.format("%n%n%15s  %05XH%n%n", "ORG", OFFSET);
		appendToDocASM(header);

	}// buildSourceHeader

	private void buildFragmentHeader(Document doc, CodeFragment codeFragment) {
		String displayText = String.format("%n;     <New %s fragment-----from %04X to %04X (%3$4X : %3$4d)>%n",
				codeFragment.type, codeFragment.startLoc, codeFragment.endLoc, codeFragment.size());
		appendToDocASM(displayText);
		displayText = String.format(";%17s  %05XH%n", "ORG", codeFragment.startLoc);
		appendToDocASM(displayText);
	}

	private void appendToDocASM(String textToAppend) {
		appendToDocASM(textToAppend, null);
	}// appendToDoc

	private void appendToDocASM(String textToAppend, AttributeSet attributeSet) {
		try {
			docASM.insertString(docASM.getLength(), textToAppend, attributeSet);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // try
	}// appendToDoc

	private void setAssemblerType() {
		if (btn8080Z80.getText() == "Z80") {
			btn8080Z80.setText("8080");
		} else {
			btn8080Z80.setText("Z80");
		} // if
		setOpCodeMap();
	}// setAssemblerType
	private void setOpCodeMap() {
		OpCodeMap = null;
		if(btn8080Z80.getText()== "Z80") {
			OpCodeMap = new OpCodeMapZ80();
		}else {
			OpCodeMap = new OpCodeMapIntel();
		}//if
		
	}//setOpCodeMap



	private void buildASM() {
		if (codeFragmentModel.getSize() < 2) {
			return;
		} // if
		clearDocument(docASM); // Star with clean doc
		buildSourceHeader(docASM);
		CodeFragment codeFragment;

		for (int i = 0; i < codeFragmentModel.getSize(); i++) {
			codeFragment = codeFragmentModel.getElementAt(i);
			switch (codeFragment.type) {
			case CodeFragment.CODE:
				buildCodeFragement(txtASM.getDocument(), codeFragment);
				break;
			case CodeFragment.CONSTANT:
				buildConstantFragment(txtASM.getDocument(), codeFragment);
				break;
			case CodeFragment.LITERAL:
				buildLiteralFragment(txtASM.getDocument(), codeFragment);
				break;
			case CodeFragment.RESERVED:
				buildUnknownFragment(txtASM.getDocument(), codeFragment);
				break;
			case CodeFragment.UNKNOWN:
				buildUnknownFragment(txtASM.getDocument(), codeFragment);
				break;
			default:
			}// switch

		} // for
		txtASM.setCaretPosition(0);
	}// buildASM

	private void buildLiteralFragment(Document doc, CodeFragment codeFragment) {
		HashMap<Byte, String> literals = new HashMap<Byte, String>();
		literals.put((byte) 0x00, "NULL");
		literals.put((byte) 0x01, "SOH");
		literals.put((byte) 0x07, "BELL");
		literals.put((byte) 0x01, "SOH");
		literals.put((byte) 0x0A, "LF");
		literals.put((byte) 0x0D, "CR");
		literals.put((byte) 0x24, "QMARK");
		literals.put((byte) 0x3F, "DOLLAR");

		buildFragmentHeader(doc, codeFragment);
		byte[] literalValues = new byte[codeFragment.size()];
		binaryData.position(codeFragment.startLoc);
		binaryData.get(literalValues, 0, codeFragment.size());
		// int crCount = 0;
		// int lfCount = 0;
		String literalData = null;
		int subFragmentStart = 0;
		int subLength = 0;
		for (int i = 0; i < codeFragment.size(); i++) {
			switch (literalValues[i]) {
			case 0x00: // NULL
			case 0x01: // SOH
			case 0x07: // BELL
			case 0x0A: // LF
			case 0x0D: // CR
			case 0x24: // QMARK
			case 0x3F: // DOLLAR
				subLength = i - subFragmentStart;
				if (subLength > 0) {
					byte[] subFragmentString = new byte[subLength];
					for (int j = 0; j < subLength; j++) {
						subFragmentString[j] = literalValues[j + subFragmentStart];
					} // for
					literalData = new String(subFragmentString);
					String displayText = String.format("%17s  '%s'%n", "DB", literalData);
					appendToDocASM(displayText);
				} // if
				String displayText = String.format("%17s  %s%n", "DB", literals.get(literalValues[i]));
				appendToDocASM(displayText);
				subFragmentStart = i + 1;
				break;
			default:
			}// switch
		} // for
		int remainder = codeFragment.size() - subFragmentStart;
		if (remainder > 0) {
			byte[] subFragmentString = new byte[remainder];
			for (int k = 0; k < remainder; k++) {
				subFragmentString[k] = literalValues[k + subFragmentStart];
			} // for
			literalData = new String(subFragmentString);
			String displayText = String.format("%17s  '%s'%n", "DB", literalData);
			appendToDocASM(displayText);
		} // if - remaing data

	}// buildUnknownFragent

	private void buildConstantFragment(Document doc, CodeFragment codeFragment) {
		int dbPerLine = 5;
		int locBase = codeFragment.startLoc;
		buildFragmentHeader(doc, codeFragment);
		String displayText = null;
		for (int i = codeFragment.size(); i > 0;) {
			if (i == 0) {
				break;
			} // if
			switch (i % dbPerLine) {
			case 0:
				displayText = String.format("%17s  %03XH,%03XH,%03XH,%03XH,%03XH%n", "DB", binaryData.get(locBase),
						binaryData.get(locBase + 1), binaryData.get(locBase + 2), binaryData.get(locBase + 3),
						binaryData.get(locBase + 4));
				i -= dbPerLine;
				locBase += dbPerLine;
				break;
			case 4:
				displayText = String.format("%17s  %03XH,%03XH,%03XH,%03XH%n", "DB", binaryData.get(locBase),
						binaryData.get(locBase + 1), binaryData.get(locBase + 2), binaryData.get(locBase + 3));
				i -= dbPerLine - 1;
				locBase += dbPerLine - 1;
				break;
			case 3:
				displayText = String.format("%17s  %03XH,%03XH,%03XH%n", "DB", binaryData.get(locBase),
						binaryData.get(locBase + 1), binaryData.get(locBase + 2));
				i -= dbPerLine - 2;
				locBase += dbPerLine - 2;
				break;
			case 2:
				displayText = String.format("%17s  %03XH,%03XH%n", "DB", binaryData.get(locBase),
						binaryData.get(locBase + 1));
				i -= dbPerLine - 3;
				locBase += dbPerLine - 3;
				break;
			case 1:
				displayText = String.format("%17s  %03XH%n", "DB", binaryData.get(locBase));
				i -= dbPerLine - 4;
				locBase += dbPerLine - 4;
				break;
			}// switch
				// displayText = String.format("%17s %05XH%n", "DS", codeFragment.size());
			appendToDocASM(displayText);

		}

	}// buildUnknownFragent

	private void buildUnknownFragment(Document doc, CodeFragment codeFragment) {

		buildFragmentHeader(doc, codeFragment);
		String displayText = String.format("%17s  %05XH%n", "DS", codeFragment.size());
		appendToDocASM(displayText);

	}// buildUnknownFragent

	private void buildCodeFragement(Document doc, CodeFragment codeFragment) {
		buildFragmentHeader(doc, codeFragment);
		OpcodeStructure8080 currentOpCode = null;

		int currentLocation = codeFragment.startLoc;
		int endLocation = codeFragment.endLoc;
		int opCodeSize;

		byte currentValue1, currentValue2;
		String part3;
		while (currentLocation <= endLocation) {
			currentOpCode = opcodeMap.get(binaryData.get(currentLocation));
			// currentValue0 = binaryData.get(currentLocation);
			opCodeSize = currentOpCode.getSize();
			currentValue1 = opCodeSize > 1 ? binaryData.get(currentLocation + 1) : 0;
			currentValue2 = opCodeSize > 2 ? binaryData.get(currentLocation + 2) : 0;
			try {
				if (labels.contains(currentLocation)) {
					appendToDocASM(String.format("L%04X:%n", currentLocation));
				} // if - its a label

				switch (opCodeSize) {
				case 1:
					part3 = String.format("%10s%-15s%n", "", currentOpCode.getAssemblerCode());
					doc.insertString(doc.getLength(), part3, null);
					break;
				case 2:
					part3 = String.format("%10s%-15s%n", "", currentOpCode.getAssemblerCode(currentValue1));
					doc.insertString(doc.getLength(), part3, null);
					break;
				case 3:
					part3 = String.format("%10s%-15s%n", "",
							currentOpCode.getAssemblerCode(currentValue1, currentValue2));
					doc.insertString(doc.getLength(), part3, null);
					break;
				default:
				}// switch
			} catch (BadLocationException badLocationException) {
				badLocationException.printStackTrace();
			}
			currentLocation += opCodeSize;

		} // while opcodeMap
			// txtArea.setCaretPosition(0);
	}// buildCodeFragment

	// ---------------------------------
	private JFileChooser getFileChooser(String directory, String filterDescription, String... filterExtensions) {
		Path sourcePath = Paths.get(directory);
		if (!Files.exists(sourcePath)) {
			try {
				Files.createDirectory(sourcePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // try
		} // if - make directory if not there

		JFileChooser chooser = new JFileChooser(directory);
		chooser.setMultiSelectionEnabled(false);
		chooser.addChoosableFileFilter(new FileNameExtensionFilter(filterDescription, filterExtensions));
		chooser.setAcceptAllFileFilterUsed(false);
		return chooser;
	}// getFileChooser
		// private void saveWIPas(){

	//
	// }

	private void saveWIP() {
		String fileNameParts[] = binaryFileName.split("\\.");
		// String fileName = fileNameParts[0] + FILE_SUFFIX_PERIOD;
		saveWIP(fileNameParts[0] + FILE_SUFFIX_PERIOD);

	}// saveWIP

	private void saveWIP(String fileName) {
		try {
			String relativeFileName = hostDirectory + "/" + fileName;
			System.out.printf("relativeFileName = %s%n", relativeFileName);
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(relativeFileName));
			oos.writeObject(binaryFile);
			// oos.writeObject(binaryData);
			oos.writeObject(beenThere);
			oos.writeObject(labels);
			oos.writeObject(codeFragmentModel);
			oos.close();
			log.infof("Saved WIP file %s%n", fileName);
		} catch (IOException ioe) {
			log.error("Error saving WIP %s %n %S%n", fileName, ioe.getMessage());
			ioe.printStackTrace();
		} // try - write objects
	}// saveWIP

	@SuppressWarnings("unchecked")
	private void loadWIP(String fileName) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
			binaryFile = (File) ois.readObject();
			// binaryData = (ByteBuffer) ois.readObject();
			beenThere = (Set<Integer>) ois.readObject();
			labels = (Set<Integer>) ois.readObject();
			codeFragmentModel = (CodeFragmentModel) ois.readObject();
			log.infof("Opened WIP file %s%n", fileName);
			ois.close();
		} catch (FileNotFoundException fnfe) {
			log.errorf("WIP File not found : %s%n%n", fileName);
			JOptionPane.showMessageDialog(null, fileName + "not found", "loadWIP()", JOptionPane.ERROR_MESSAGE);
			return; // exit gracefully
		} catch (IOException ie) {
			JOptionPane.showMessageDialog(null, binaryFile.getAbsolutePath() + ie.getMessage(), "IO error",
					JOptionPane.ERROR_MESSAGE);
			log.errorf("WIP IO Error : File %s%n %S%n%n", ie.getMessage(), fileName);

			return; // exit gracefully
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "ClassNotFoundException", "loadWIP()", JOptionPane.ERROR_MESSAGE);
			log.warnf("WIP file : %s ClassNotFoundException%n%n", fileName);
			return; // exit gracefully
		} // try

		if (!processBianryFile(binaryFile)) {
			JOptionPane.showMessageDialog(null, "Problem with binary Source file" + binaryFile.getAbsolutePath(),
					"openBinaryFile()", JOptionPane.ERROR_MESSAGE);
			log.errorf("Problem with binary Source file: %s%n%n", fileName);
			return;
		} // if

		btnProcessFragment.setEnabled(true);
		btnAddFragment.setEnabled(true);
		btnRemoveFragment.setEnabled(true);
		btnCombineFragments.setEnabled(true);
		btn8080Z80.setEnabled(true);
		listCodeFragments.setModel(codeFragmentModel);
		listCodeFragments.setEnabled(true);

		listCodeFragments.updateUI();
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	private void appClose() {
		Preferences myPrefs = Preferences.userNodeForPackage(ManualDisassembler.class)
				.node(this.getClass().getSimpleName());
		Dimension dim = frame.getSize();
		myPrefs.putInt("Height", dim.height);
		myPrefs.putInt("Width", dim.width);
		Point point = frame.getLocation();
		myPrefs.putInt("LocX", point.x);
		myPrefs.putInt("LocY", point.y);

		myPrefs.put("HostDirectory", hostDirectory);

		myPrefs = null;

		System.exit(0);
	}// appClose

	@SuppressWarnings("unchecked")
	private void appInit() {
		StyledDocument styledDoc = textLog.getStyledDocument();
		textLog.setFont(new Font("Arial", Font.PLAIN, 15));
		AppLogger log = AppLogger.getInstance();
		log.setDoc(styledDoc);
		log.setTextPane(textLog, "Disk Utility Log");

		Preferences myPrefs = Preferences.userNodeForPackage(ManualDisassembler.class)
				.node(this.getClass().getSimpleName());
		frame.setSize(myPrefs.getInt("Width", 761), myPrefs.getInt("Height", 693));
		frame.setLocation(myPrefs.getInt("LocX", 100), myPrefs.getInt("LocY", 100));

		hostDirectory = myPrefs.get("HostDirectory", System.getProperty(USER_HOME, THIS_DIR));

		btn8080Z80.setText(myPrefs.get("AssemblerType", "Z80"));

		myPrefs = null;
		//
		// OpCodeMap8080 opcode8080Map;

		//
		setOpCodeMap();
//		if (opcodeMap == null) {
//			opcodeMap = Opcodes8080.makeCodeMap();
//		} // if
		if (codeFragmentModel != null) {
			codeFragmentModel = null;
		} // if
		if (entryPoints != null) {
			entryPoints = null;
		} // if

		entryPoints = new Stack<Integer>();
		codeFragmentModel = new CodeFragmentModel();

		docBinary = txtBinaryFile.getDocument();
		docASM = txtASM.getDocument();
		clearDocument(txtWIPsource.getDocument());
		clearDocument(docBinary);
		clearDocument(docASM);

		listCodeFragments.setModel(codeFragmentModel);

		haveBinanryFile(false);
		binaryFileName = "<No biary file selected>";
		frame.setTitle(APP_NAME + "   " + binaryFileName);
		log.infof("Starting %s......%n", APP_NAME);
	}// appInit

	/**
	 * Create the application.
	 */
	public ManualDisassembler() {
		initialize();
		appInit();
	}

	// ------------------------------------------------------------------------------
	private File binaryFile;
	private String binaryFilePath;
	private String binaryFileName = "";
	private ByteBuffer binaryData;
	private CodeFragmentModel codeFragmentModel;
	private Opcodes8080 opcodeMap;
	private AbstractOpCodeMap OpCodeMap;//   *******
	private Document docBinary;
	// private Document docWIPbinary;
	// private Document docWIPsource;
	private Document docASM;

	private Stack<Integer> entryPoints;
	private Set<Integer> beenThere;
	private Set<Integer> labels;

	private final int OFFSET = 0X0100; // Transient Program Area starts at 256 (0x0100)
	private final int CHARACTERS_PER_LINE = 0x0010; //

	private final int TAB_WIP = 0;
	private final int TAB_BINARY_FILE = 1;
	// private final int TAB_ASM = 2;

	private final int ATTR_ADDRESS = 0;
	private final int ATTR_BINARY_CODE = 1;
	private final int ATTR_ASM_CODE = 2;
	private final int ATTR_FUNCTION = 3;

	// private final static String AC_MNU_FILE_NEW = "mnuFileNew";
	private final static String AC_MNU_FILE_OPEN = "mnuFileOpen";
	private final static String AC_MNU_FILE_LOAD_WIP = "mnuFileLoadWIP";
	private final static String AC_MNU_FILE_SAVE_WIP = "mnuFileSaveWIP";
	private final static String AC_MNU_FILE_SAVE_WIP_AS = "mnuFileSaveWIPAs";
	private final static String AC_MNU_FILE_RESET = "mnuFileReset";
	private final static String AC_MNU_FILE_EXIT = "mnuFileExit";

	private final static String AC_MNU_CODE_NEW = "mnuCodeFragmentNew";
	private final static String AC_MNU_CODE_LOAD = "mnuCodeFragmentLoad";
	private final static String AC_MNU_CODE_SAVE = "mnuCodeFragmentSave";
	private final static String AC_MNU_CODE_SAVE_AS = "mnuCodeFragmentSaveAs";

	private final static String AC_BTN_START = "btnStart";
	private final static String AC_BTN_BUILD_ASM = "btnBuildASM";
	private final static String AC_BTN_Z80_ASM = "btn8080Z80";

	private final static String AC_BTN_ADD_FRAGMENT = "btnAddFragment";
	private final static String AC_BTN_REMOVE_FRAGMENT = "btnRemoveFragment";
	private final static String AC_BTN_PROCESS_FRAGMENT = "btnProcessFragment";
	private final static String AC_BTN_COMBINE_FRAGMENTS = "btnCombineFragment";

	private final static String APP_NAME = "Manual Disassembler ";
	// private final static String SEMI = ";"; // SemiColon
	private final static String WIP = "Work In Process";
	private final static String FILE_SUFFIX = "WIP";
	private final static String FILE_SUFFIX_PERIOD = "." + FILE_SUFFIX;
	public static final String USER_HOME = "user.home";
	public static final String THIS_DIR = ".";
	public static final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");
	public final static String FILE_LOCATION = System.getenv("APPDATA") + FILE_SEPARATOR + "Disassembler";
	// public final static String FILE_LOCATION = "Disassembler";file.separator

	private Hex64KSpinner spinnerEndFragment;
	private Hex64KSpinner spinnerBeginFragment;
	private JRadioButton rbUnknown;
	private JRadioButton rbReserved;
	private JRadioButton rbLiteral;
	private JRadioButton rbConstant;
	private JRadioButton rbCode;
	@SuppressWarnings("rawtypes")
	private JList listCodeFragments;
	private JButton btnStart;
	private JButton btnAddFragment;
	private JButton btnRemoveFragment;
	private JButton btnProcessFragment;
	private final ButtonGroup groupFragment = new ButtonGroup();
	private JPanel panelFragmentTypes;
	private JTextArea txtBinaryFile;
	private JTabbedPane tabPaneDisplays;
	private JTextArea txtWIPbinary;
	private JScrollPane scrollPaneWIPbinary;
	private JTextPane txtWIPsource;
	private JButton btnCombineFragments;
	private JButton btnBuildASM;
	private JToggleButton btn8080Z80;
	private JTextArea txtASM;
	private JLabel lblSourceHeader;
	private JMenuItem mnuFileSaveWIP;
	private JMenuItem mnuFileLoadWIP;
	private JMenuItem mnuFileSaveWIPas;
	private JMenuItem mnuFileOpenBinaryFile;
	private JSeparator separator_2;
	private JMenuItem mnuFileReset;
	private JScrollPane tabLog;
	private JLabel label_2;
	private JTextPane textLog;

	// ------------------------------------------------------------------------------

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("rawtypes")
	private void initialize() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				appClose();
			}
		});
		// frame.setBounds(100, 100, 986, 836);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE, 0.0 };
		frame.getContentPane().setLayout(gridBagLayout);

		JPanel paneTop = new JPanel();
		GridBagConstraints gbc_paneTop = new GridBagConstraints();
		gbc_paneTop.insets = new Insets(0, 0, 5, 0);
		gbc_paneTop.fill = GridBagConstraints.BOTH;
		gbc_paneTop.gridx = 0;
		gbc_paneTop.gridy = 0;
		frame.getContentPane().add(paneTop, gbc_paneTop);
		GridBagLayout gbl_paneTop = new GridBagLayout();
		gbl_paneTop.columnWidths = new int[] { 0, 0, 0 };
		gbl_paneTop.rowHeights = new int[] { 0, 0, 0 };
		gbl_paneTop.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_paneTop.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		paneTop.setLayout(gbl_paneTop);

		btnStart = new JButton("Start");
		btnStart.setActionCommand(AC_BTN_START);
		btnStart.addActionListener(applicationAdapter);
		GridBagConstraints gbc_btnStart = new GridBagConstraints();
		gbc_btnStart.anchor = GridBagConstraints.WEST;
		gbc_btnStart.insets = new Insets(0, 0, 5, 5);
		gbc_btnStart.gridx = 0;
		gbc_btnStart.gridy = 0;
		paneTop.add(btnStart, gbc_btnStart);

		btnBuildASM = new JButton("Build ASM");
		btnBuildASM.setActionCommand(AC_BTN_BUILD_ASM);
		btnBuildASM.addActionListener(applicationAdapter);
		GridBagConstraints gbc_btnBuildASM = new GridBagConstraints();
		gbc_btnBuildASM.insets = new Insets(0, 0, 5, 0);
		gbc_btnBuildASM.gridx = 1;
		gbc_btnBuildASM.gridy = 0;
		paneTop.add(btnBuildASM, gbc_btnBuildASM);

		btn8080Z80 = new JToggleButton("Z80 / 8080");
		btn8080Z80.setHorizontalAlignment(SwingConstants.RIGHT);
		btn8080Z80.setActionCommand(AC_BTN_Z80_ASM);
		btn8080Z80.addActionListener(applicationAdapter);
		GridBagConstraints gbc_btn8080Z80 = new GridBagConstraints();
		gbc_btn8080Z80.insets = new Insets(0, 0, 5, 0);
		gbc_btn8080Z80.gridx = 2;
		gbc_btn8080Z80.gridy = 0;
		paneTop.add(btn8080Z80, gbc_btn8080Z80);

		JPanel panelMain = new JPanel();
		GridBagConstraints gbc_panelMain = new GridBagConstraints();
		gbc_panelMain.insets = new Insets(0, 0, 5, 0);
		gbc_panelMain.fill = GridBagConstraints.BOTH;
		gbc_panelMain.gridx = 0;
		gbc_panelMain.gridy = 1;
		frame.getContentPane().add(panelMain, gbc_panelMain);
		GridBagLayout gbl_panelMain = new GridBagLayout();
		gbl_panelMain.columnWidths = new int[] { 270, 0, 0 };
		gbl_panelMain.rowHeights = new int[] { 0, 0 };
		gbl_panelMain.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panelMain.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		panelMain.setLayout(gbl_panelMain);

		JTabbedPane tabPaneFragments = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabPaneFragments = new GridBagConstraints();
		gbc_tabPaneFragments.insets = new Insets(0, 0, 0, 5);
		gbc_tabPaneFragments.fill = GridBagConstraints.BOTH;
		gbc_tabPaneFragments.gridx = 0;
		gbc_tabPaneFragments.gridy = 0;
		panelMain.add(tabPaneFragments, gbc_tabPaneFragments);

		JPanel panelFragments = new JPanel();
		panelFragments.setMinimumSize(new Dimension(0, 0));
		panelFragments.setPreferredSize(new Dimension(0, 0));
		tabPaneFragments.addTab("Code Fragments", null, panelFragments, null);
		GridBagLayout gbl_panelFragments = new GridBagLayout();
		gbl_panelFragments.columnWidths = new int[] { 60, 0 };
		gbl_panelFragments.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_panelFragments.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_panelFragments.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		panelFragments.setLayout(gbl_panelFragments);

		JPanel panelBeginEnd = new JPanel();
		GridBagConstraints gbc_panelBeginEnd = new GridBagConstraints();
		gbc_panelBeginEnd.insets = new Insets(0, 0, 5, 0);
		gbc_panelBeginEnd.fill = GridBagConstraints.VERTICAL;
		gbc_panelBeginEnd.gridx = 0;
		gbc_panelBeginEnd.gridy = 0;
		panelFragments.add(panelBeginEnd, gbc_panelBeginEnd);
		GridBagLayout gbl_panelBeginEnd = new GridBagLayout();
		gbl_panelBeginEnd.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gbl_panelBeginEnd.rowHeights = new int[] { 0, 0 };
		gbl_panelBeginEnd.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panelBeginEnd.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panelBeginEnd.setLayout(gbl_panelBeginEnd);

		JLabel lblBegin = new JLabel("  Begin   ");
		GridBagConstraints gbc_lblBegin = new GridBagConstraints();
		gbc_lblBegin.insets = new Insets(0, 0, 0, 5);
		gbc_lblBegin.gridx = 0;
		gbc_lblBegin.gridy = 0;
		panelBeginEnd.add(lblBegin, gbc_lblBegin);

		spinnerBeginFragment = new Hex64KSpinner();
		GridBagConstraints gbc_spinnerBeginFragment = new GridBagConstraints();
		gbc_spinnerBeginFragment.insets = new Insets(0, 0, 0, 5);
		gbc_spinnerBeginFragment.gridx = 1;
		gbc_spinnerBeginFragment.gridy = 0;
		panelBeginEnd.add(spinnerBeginFragment, gbc_spinnerBeginFragment);
		spinnerBeginFragment.setMinimumSize(new Dimension(50, 20));
		spinnerBeginFragment.setPreferredSize(new Dimension(50, 20));

		JLabel lblEnd = new JLabel(" End  ");
		GridBagConstraints gbc_lblEnd = new GridBagConstraints();
		gbc_lblEnd.insets = new Insets(0, 0, 0, 5);
		gbc_lblEnd.gridx = 2;
		gbc_lblEnd.gridy = 0;
		panelBeginEnd.add(lblEnd, gbc_lblEnd);

		spinnerEndFragment = new Hex64KSpinner();
		GridBagConstraints gbc_spinnerEndFragment = new GridBagConstraints();
		gbc_spinnerEndFragment.gridx = 3;
		gbc_spinnerEndFragment.gridy = 0;
		panelBeginEnd.add(spinnerEndFragment, gbc_spinnerEndFragment);
		spinnerEndFragment.setPreferredSize(new Dimension(50, 20));
		spinnerEndFragment.setMinimumSize(new Dimension(50, 20));

		panelFragmentTypes = new JPanel();
		panelFragmentTypes.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null),
				"Fragement Type", TitledBorder.CENTER, TitledBorder.BELOW_TOP, null, null));
		GridBagConstraints gbc_panelFragmentTypes = new GridBagConstraints();
		gbc_panelFragmentTypes.insets = new Insets(0, 0, 5, 0);
		gbc_panelFragmentTypes.fill = GridBagConstraints.VERTICAL;
		gbc_panelFragmentTypes.gridx = 0;
		gbc_panelFragmentTypes.gridy = 1;
		panelFragments.add(panelFragmentTypes, gbc_panelFragmentTypes);
		GridBagLayout gbl_panelFragmentTypes = new GridBagLayout();
		gbl_panelFragmentTypes.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_panelFragmentTypes.rowHeights = new int[] { 0, 0, 0 };
		gbl_panelFragmentTypes.columnWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panelFragmentTypes.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		panelFragmentTypes.setLayout(gbl_panelFragmentTypes);

		rbCode = new JRadioButton("Code");
		rbCode.setActionCommand(CodeFragment.CODE);
		groupFragment.add(rbCode);
		GridBagConstraints gbc_rbCode = new GridBagConstraints();
		gbc_rbCode.anchor = GridBagConstraints.WEST;
		gbc_rbCode.insets = new Insets(0, 0, 5, 5);
		gbc_rbCode.gridx = 0;
		gbc_rbCode.gridy = 0;
		panelFragmentTypes.add(rbCode, gbc_rbCode);

		rbConstant = new JRadioButton("Constant");
		rbConstant.setActionCommand(CodeFragment.CONSTANT);
		groupFragment.add(rbConstant);
		GridBagConstraints gbc_rbConstant = new GridBagConstraints();
		gbc_rbConstant.anchor = GridBagConstraints.WEST;
		gbc_rbConstant.insets = new Insets(0, 0, 5, 5);
		gbc_rbConstant.gridx = 1;
		gbc_rbConstant.gridy = 0;
		panelFragmentTypes.add(rbConstant, gbc_rbConstant);

		rbLiteral = new JRadioButton("Literal");
		rbLiteral.setActionCommand(CodeFragment.LITERAL);
		groupFragment.add(rbLiteral);
		GridBagConstraints gbc_rbLiteral = new GridBagConstraints();
		gbc_rbLiteral.anchor = GridBagConstraints.WEST;
		gbc_rbLiteral.insets = new Insets(0, 0, 5, 0);
		gbc_rbLiteral.gridx = 2;
		gbc_rbLiteral.gridy = 0;
		panelFragmentTypes.add(rbLiteral, gbc_rbLiteral);

		rbReserved = new JRadioButton("Reserved");
		rbReserved.setActionCommand(CodeFragment.RESERVED);
		groupFragment.add(rbReserved);
		GridBagConstraints gbc_rbReserved = new GridBagConstraints();
		gbc_rbReserved.anchor = GridBagConstraints.WEST;
		gbc_rbReserved.insets = new Insets(0, 0, 0, 5);
		gbc_rbReserved.gridx = 0;
		gbc_rbReserved.gridy = 1;
		panelFragmentTypes.add(rbReserved, gbc_rbReserved);

		rbUnknown = new JRadioButton("Unknown");
		rbUnknown.setActionCommand(CodeFragment.UNKNOWN);
		groupFragment.add(rbUnknown);
		GridBagConstraints gbc_rbUnknown = new GridBagConstraints();
		gbc_rbUnknown.anchor = GridBagConstraints.WEST;
		gbc_rbUnknown.gridx = 2;
		gbc_rbUnknown.gridy = 1;
		panelFragmentTypes.add(rbUnknown, gbc_rbUnknown);

		JPanel panelFragmentButtons = new JPanel();
		GridBagConstraints gbc_panelFragmentButtons = new GridBagConstraints();
		gbc_panelFragmentButtons.insets = new Insets(0, 0, 5, 0);
		gbc_panelFragmentButtons.fill = GridBagConstraints.VERTICAL;
		gbc_panelFragmentButtons.gridx = 0;
		gbc_panelFragmentButtons.gridy = 2;
		panelFragments.add(panelFragmentButtons, gbc_panelFragmentButtons);
		GridBagLayout gbl_panelFragmentButtons = new GridBagLayout();
		gbl_panelFragmentButtons.columnWidths = new int[] { 0, 0, 0 };
		gbl_panelFragmentButtons.rowHeights = new int[] { 0, 0, 0 };
		gbl_panelFragmentButtons.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gbl_panelFragmentButtons.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		panelFragmentButtons.setLayout(gbl_panelFragmentButtons);

		btnAddFragment = new JButton("Add/Update");
		btnAddFragment.setActionCommand(AC_BTN_ADD_FRAGMENT);
		btnAddFragment.addActionListener(applicationAdapter);
		GridBagConstraints gbc_btnAddFragment = new GridBagConstraints();
		gbc_btnAddFragment.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddFragment.gridx = 0;
		gbc_btnAddFragment.gridy = 0;
		panelFragmentButtons.add(btnAddFragment, gbc_btnAddFragment);

		btnRemoveFragment = new JButton("Remove");
		btnRemoveFragment.setActionCommand(AC_BTN_REMOVE_FRAGMENT);
		btnRemoveFragment.addActionListener(applicationAdapter);
		GridBagConstraints gbc_btnRemoveFragment = new GridBagConstraints();
		gbc_btnRemoveFragment.insets = new Insets(0, 0, 5, 0);
		gbc_btnRemoveFragment.gridx = 1;
		gbc_btnRemoveFragment.gridy = 0;
		panelFragmentButtons.add(btnRemoveFragment, gbc_btnRemoveFragment);

		btnProcessFragment = new JButton("Process");
		btnProcessFragment.setActionCommand(AC_BTN_PROCESS_FRAGMENT);
		btnProcessFragment.addActionListener(applicationAdapter);
		GridBagConstraints gbc_btnProcessFragment = new GridBagConstraints();
		gbc_btnProcessFragment.insets = new Insets(0, 0, 0, 5);
		gbc_btnProcessFragment.gridx = 0;
		gbc_btnProcessFragment.gridy = 1;
		panelFragmentButtons.add(btnProcessFragment, gbc_btnProcessFragment);

		btnCombineFragments = new JButton("Combine");
		btnCombineFragments.setActionCommand(AC_BTN_COMBINE_FRAGMENTS);
		btnCombineFragments.addActionListener(applicationAdapter);
		GridBagConstraints gbc_btnCombineFragments = new GridBagConstraints();
		gbc_btnCombineFragments.anchor = GridBagConstraints.NORTHEAST;
		gbc_btnCombineFragments.gridx = 1;
		gbc_btnCombineFragments.gridy = 1;
		panelFragmentButtons.add(btnCombineFragments, gbc_btnCombineFragments);

		JScrollPane scrollPaneFragments = new JScrollPane();
		GridBagConstraints gbc_scrollPaneFragments = new GridBagConstraints();
		gbc_scrollPaneFragments.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneFragments.gridx = 0;
		gbc_scrollPaneFragments.gridy = 3;
		panelFragments.add(scrollPaneFragments, gbc_scrollPaneFragments);

		listCodeFragments = new JList();
		listCodeFragments.addListSelectionListener(applicationAdapter);
		listCodeFragments.setFont(new Font("Courier New", Font.PLAIN, 12));
		scrollPaneFragments.setViewportView(listCodeFragments);

		JLabel lblNewLabel = new JLabel("Start   End    Len     Type");
		lblNewLabel.setPreferredSize(new Dimension(110, 14));
		lblNewLabel.setMinimumSize(new Dimension(110, 14));
		lblNewLabel.setForeground(Color.BLUE);
		lblNewLabel.setFont(new Font("Courier New", Font.PLAIN, 12));
		scrollPaneFragments.setColumnHeaderView(lblNewLabel);

		tabPaneDisplays = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabPaneDisplays = new GridBagConstraints();
		gbc_tabPaneDisplays.fill = GridBagConstraints.BOTH;
		gbc_tabPaneDisplays.gridx = 1;
		gbc_tabPaneDisplays.gridy = 0;
		panelMain.add(tabPaneDisplays, gbc_tabPaneDisplays);

		JPanel panelWIP = new JPanel();
		tabPaneDisplays.addTab("WIP", null, panelWIP, null);
		GridBagLayout gbl_panelWIP = new GridBagLayout();
		gbl_panelWIP.columnWidths = new int[] { 0, 0 };
		gbl_panelWIP.rowHeights = new int[] { 0, 0 };
		gbl_panelWIP.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panelWIP.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		panelWIP.setLayout(gbl_panelWIP);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setOneTouchExpandable(true);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 0;
		panelWIP.add(splitPane, gbc_splitPane);

		scrollPaneWIPbinary = new JScrollPane();
		splitPane.setLeftComponent(scrollPaneWIPbinary);

		txtWIPbinary = new JTextArea();
		txtWIPbinary.setEditable(false);
		txtWIPbinary.setFont(new Font("Courier New", Font.PLAIN, 15));
		scrollPaneWIPbinary.setViewportView(txtWIPbinary);

		JLabel label_1 = new JLabel("      00 01 02 03 04 05 06 07  08 09 0A 0B 0C 0D 0E 0F");
		label_1.setForeground(Color.BLUE);
		label_1.setFont(new Font("Courier New", Font.PLAIN, 15));
		scrollPaneWIPbinary.setColumnHeaderView(label_1);

		JScrollPane scrollPaneWIPsource = new JScrollPane();
		splitPane.setRightComponent(scrollPaneWIPsource);

		txtWIPsource = new JTextPane();
		txtWIPsource.setFont(new Font("Courier New", Font.PLAIN, 16));
		scrollPaneWIPsource.setViewportView(txtWIPsource);
		splitPane.setDividerLocation(300);

		JPanel panelBinanryFile = new JPanel();
		tabPaneDisplays.addTab("Binary File", null, panelBinanryFile, null);
		GridBagLayout gbl_panelBinanryFile = new GridBagLayout();
		gbl_panelBinanryFile.columnWidths = new int[] { 680, 0 };
		gbl_panelBinanryFile.rowHeights = new int[] { 400, 0 };
		gbl_panelBinanryFile.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panelBinanryFile.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		panelBinanryFile.setLayout(gbl_panelBinanryFile);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(680, 400));
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		panelBinanryFile.add(scrollPane, gbc_scrollPane);

		txtBinaryFile = new JTextArea();
		txtBinaryFile.setEditable(false);
		txtBinaryFile.setFont(new Font("Courier New", Font.PLAIN, 15));
		scrollPane.setViewportView(txtBinaryFile);

		JLabel label = new JLabel("      00 01 02 03 04 05 06 07  08 09 0A 0B 0C 0D 0E 0F");
		label.setForeground(Color.BLUE);
		label.setFont(new Font("Courier New", Font.PLAIN, 15));
		scrollPane.setColumnHeaderView(label);

		JScrollPane panelScrollASM = new JScrollPane();
		tabPaneDisplays.addTab("Source Code", null, panelScrollASM, null);

		txtASM = new JTextArea();
		txtASM.setFont(new Font("Courier New", Font.PLAIN, 15));
		panelScrollASM.setViewportView(txtASM);

		lblSourceHeader = new JLabel("Source code");
		lblSourceHeader.setForeground(Color.BLUE);
		lblSourceHeader.setFont(new Font("Courier New", Font.ITALIC, 15));
		lblSourceHeader.setHorizontalAlignment(SwingConstants.CENTER);
		panelScrollASM.setColumnHeaderView(lblSourceHeader);

		tabLog = new JScrollPane();
		tabPaneDisplays.addTab("Log", null, tabLog, null);

		label_2 = new JLabel("Appication Log");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setForeground(Color.BLUE);
		label_2.setFont(new Font("Arial", Font.BOLD, 18));
		tabLog.setColumnHeaderView(label_2);

		textLog = new JTextPane();
		tabLog.setViewportView(textLog);

		// paneTop.add(panelMain, gbc_panelMain);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnuFile = new JMenu("File");
		menuBar.add(mnuFile);

		mnuFileOpenBinaryFile = new JMenuItem("Open Binary File...");
		mnuFileOpenBinaryFile.setActionCommand(AC_MNU_FILE_OPEN);
		mnuFileOpenBinaryFile.addActionListener(applicationAdapter);
		mnuFile.add(mnuFileOpenBinaryFile);

		mnuFileLoadWIP = new JMenuItem("Load WIP...");
		mnuFileLoadWIP.setActionCommand(AC_MNU_FILE_LOAD_WIP);
		mnuFileLoadWIP.addActionListener(applicationAdapter);
		mnuFile.add(mnuFileLoadWIP);

		JSeparator separator = new JSeparator();
		mnuFile.add(separator);

		JMenuItem mnuFileExit = new JMenuItem("Exit");
		mnuFileExit.setActionCommand(AC_MNU_FILE_EXIT);
		mnuFileExit.addActionListener(applicationAdapter);

		mnuFileSaveWIP = new JMenuItem("Save WIP");
		mnuFileSaveWIP.setActionCommand(AC_MNU_FILE_SAVE_WIP);
		mnuFileSaveWIP.addActionListener(applicationAdapter);
		mnuFile.add(mnuFileSaveWIP);

		mnuFileSaveWIPas = new JMenuItem("Save WIP as...");
		mnuFileSaveWIPas.setActionCommand(AC_MNU_FILE_SAVE_WIP_AS);
		mnuFileSaveWIPas.addActionListener(applicationAdapter);
		mnuFile.add(mnuFileSaveWIPas);

		separator_2 = new JSeparator();
		mnuFile.add(separator_2);

		mnuFileReset = new JMenuItem("Reset");
		mnuFileReset.setActionCommand(AC_MNU_FILE_RESET);
		mnuFileReset.addActionListener(applicationAdapter);
		mnuFile.add(mnuFileReset);

		JSeparator separator_1 = new JSeparator();
		mnuFile.add(separator_1);
		mnuFile.add(mnuFileExit);
	}// initialize

	// <><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>

	class ApplicationAdapter implements ActionListener, ListSelectionListener {

		@Override // ActionListener
		public void actionPerformed(ActionEvent actionEvent) {
			String actionCommand = actionEvent.getActionCommand();
			String message = null;
			switch (actionCommand) {
			case AC_MNU_FILE_OPEN:
				openBinaryFile();
				break;
			case AC_MNU_FILE_LOAD_WIP:
				JFileChooser chooserLoad = getFileChooser(hostDirectory, WIP, FILE_SUFFIX);
				if (chooserLoad.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION) {
					System.out.printf("You cancelled the Save as...%n", "");
				} else {
					// String fileNameParts[] = chooserLoad.getSelectedFile().getName().split("\\.");
					// loadWIP(fileNameParts[0] + FILE_SUFFIX_PERIOD);
					hostDirectory = chooserLoad.getSelectedFile().getParent();
					loadWIP(chooserLoad.getSelectedFile().getAbsolutePath());
				} // if - returnValue

				break;
			case AC_MNU_FILE_SAVE_WIP:
				saveWIP();
				break;
			case AC_MNU_FILE_SAVE_WIP_AS:
				JFileChooser chooserSaveAs = getFileChooser(FILE_LOCATION, WIP, FILE_SUFFIX);
				if (chooserSaveAs.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION) {
					System.out.printf("You cancelled the Save as...%n", "");
				} else {
					String fileNameParts[] = chooserSaveAs.getSelectedFile().getName().split("\\.");
					saveWIP(fileNameParts[0] + FILE_SUFFIX_PERIOD);
				} // if - returnValue
				break;
			case AC_MNU_FILE_RESET:
				appInit();
				break;
			case AC_MNU_FILE_EXIT:
				appClose();

				break;
			case AC_MNU_CODE_NEW:
				message = "mnuCodeFragmentNew";
				break;
			case AC_MNU_CODE_LOAD:
				message = "mnuCodeFragmentLoad";
				break;
			case AC_MNU_CODE_SAVE:
				message = "mnuCodeFragmentSave";
				break;
			case AC_MNU_CODE_SAVE_AS:
				message = "mnuCodeFragmentSaveAs";
				break;

			case AC_BTN_START:
				actionStart();
				break;
			case AC_BTN_BUILD_ASM:
				buildASM();
				break;
			case AC_BTN_Z80_ASM:
				setAssemblerType();
				break;

			case AC_BTN_ADD_FRAGMENT:
				addFragement();
				break;
			case AC_BTN_REMOVE_FRAGMENT:
				removeFragment();
				break;
			case AC_BTN_PROCESS_FRAGMENT:
				processFragment();
				break;
			case AC_BTN_COMBINE_FRAGMENTS:
				combineFragments();
				break;
			default:
				message = actionCommand;
			}// switch
			if (message != null) {
				System.out.printf("<actionPerformed> actionCommand = %s%n", actionCommand);
			} // if

		}// actionPerformed

		@Override // ListSelectionListener
		public void valueChanged(ListSelectionEvent lse) {
			if (lse.getValueIsAdjusting()) {
				return;
			}
			@SuppressWarnings("rawtypes")
			int index = ((JList) lse.getSource()).getSelectedIndex();
			if (index == -1) {
				return;
			}
			CodeFragment codeFragment = codeFragmentModel.getElementAt(index);
			spinnerBeginFragment.setValue(codeFragment.startLoc);
			spinnerEndFragment.setValue(codeFragment.endLoc);
			setFragmentRadioButton(codeFragment.type);
			displayFragmenBinary(txtWIPbinary, codeFragment.startLoc - OFFSET, codeFragment.endLoc - OFFSET);
			displayFragmentSource(txtWIPsource, codeFragment);
		}// valueChanged

	}// class ApplicationAdapter

}// class ManualDissambler