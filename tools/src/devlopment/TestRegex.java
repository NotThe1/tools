

package devlopment;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.text.JTextComponent;
import java.awt.event.MouseAdapter;

public class TestRegex implements ActionListener {

	private JFrame frmTestRegex;
	private JTextField txtResult;
	private JTextArea txtLog;
	private JButton btnMatches;
	private JButton btnFind;
	private JButton btnLookingAt;
	private JTextField txtCode;
	private JTextField txtSource;
	private Pattern p;
	private Matcher m;
	private JButton btnActOnResult;
	private JButton btnReplace;
	private JTextField txtReplacement;

	// all printable characters(from 1 to 5characters) =
	// ^([a-zA-Z0-9!@#$%^&amp;*()-_=+;:'&quot;|~`&lt;&gt;?/{}]{1,5})$
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestRegex window = new TestRegex();
					window.frmTestRegex.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}// try
			}// run
		});
	}// main

	/**
	 * Create the application.
	 */
	public TestRegex() {
		initialize();
	}// Constructor

	@Override
	public void actionPerformed(ActionEvent ae) {
		String actionCommand = ae.getActionCommand();
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
			divideByFour();
			break;
		case "btnReplace":
			doReplace();
			break;
		default:
			System.err.printf("Unknown Action Command %s.%n",actionCommand);
			break;
		}// switch
	}// actionPerformed
	private void doReplace(){
		
		
		
		String codeString= txtCode.getText();
		String original = txtSource.getText();
		String replacement = txtReplacement.getText();
		String newString = original.replaceAll(codeString, replacement);
		txtResult.setText(newString);		
	}

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

	private void divideByFour() {
		// String regexString = "\[[\-|\+]*\d+\]";
	    String[] validTests = {"[+05620]", "[005624]", "[-05628]", "[005632]", "[555636]", "[+05640]", "[005600]",
                "the beginning [-0] the end", "~[4]", "[32]", "the beginning [0] ... [invalid] numb[3]rs ... the end",
                "...may be [+002016] will be."};
	    
	    String[] invalidTests = {"[+05621]", "[-55622]", "[005623]", "[~24]", "[8.04]", "No, [2014] isn't a multiple of 4..."};


		//String regexString = "\\[[\\-|\\+]*\\d+\\]";
		Pattern pattern = Pattern.compile(txtCode.getText());
		
		txtLog.setText("");//clear the log
		String ans;
		txtLog.append(String.format(" Valid Tests follow %n"));
		for (String v:validTests){
			Matcher matcher = pattern.matcher(v);
			ans = (matcher.find())?"VALID!":"invalid";
			txtLog.append(String.format("%s for string %s%n", ans,v));
		}//for
		txtLog.append(String.format("%n invalid Tests follow %n%n%n"));
		for (String v:invalidTests){
			Matcher matcher = pattern.matcher(v);
			ans = (matcher.find())?"VALID!":"invalid";
			txtLog.append(String.format("%s for string %s%n", ans,v));
		}//for

	}// divideByFour
	

	private void doFind() {
		String logMessage0 = "Not Found";
		txtResult.setForeground(Color.black);
		txtResult.setText("");
		String sourceText = txtSource.getText();
		
		try {
			p = Pattern.compile(txtCode.getText());
			m = p.matcher(txtSource.getText());
			if (m.find()) {
				logMessage0 = foundIt("Find");
				txtLog.append(String.format("end = %d, start = %s%n", m.end(), m.start()));
				txtLog.append(String.format("group = |%s|%n", m.group()));
				txtLog.append(String.format("Before group = |%s|%n", sourceText.substring(0, m.start())));
			} else {
				logMessage0 = foundItNot("Find");
			}// if
		} catch (Exception e) {
			String errMessage = String.format("%s - %s%n%n", "In Catch", e.getMessage());
			txtLog.append(errMessage);
		}
		postLogMessage(logMessage0);
	}// doFind

	private void doLookingAt() {
		String logMessage0 = "Not looking at";
		txtResult.setForeground(Color.black);
		txtResult.setText("");

		try {
			p = Pattern.compile(txtCode.getText());
			m = p.matcher(txtSource.getText());

			if (m.lookingAt()) {
				logMessage0 = foundIt("lookingAT");
				txtLog.append(String.format("end = %d, start = %s%n", m.end(), m.start()));
			} else {
				logMessage0 = foundItNot("lookingAT");
			}//
		} catch (Exception e) {
			String errMessage = String.format("%s - %s%n%n", "In Catch", e.getMessage());
			txtLog.append(errMessage);
		}//
		postLogMessage(logMessage0);

	}// doLookingAt

	private void doMatch() {
		String logMessage0 = "No Match";
		txtResult.setForeground(Color.black);
		txtResult.setText("");
		try {
			p = Pattern.compile(txtCode.getText());
			m = p.matcher(txtSource.getText());

			if (m.matches()) {
				logMessage0 = foundIt("Match");
				txtLog.append(String.format("end = %d, start = %s%n", m.end(), m.start()));
			} else {
				logMessage0 = foundItNot("Match");
			}//
		} catch (Exception e) {
			String errMessage = String.format("%s - %s%n%n", "In Catch", e.getMessage());
			txtLog.append(errMessage);
		}//
		postLogMessage(logMessage0);
	}// doMatch

	private String foundIt(String typeOfOperation) {
		txtResult.setForeground(Color.GREEN);
		txtResult.setText("It is " + typeOfOperation);
		return typeOfOperation;

	}//

	private String foundItNot(String typeOfOperation) {
		txtResult.setForeground(Color.red);
		txtResult.setText("NOT " + typeOfOperation);
		return "NOT " + typeOfOperation;
	}//

	private void postLogMessage(String logMessage0) {
		String logMessage = String.format("%s \t%s - \t%s%n", logMessage0, txtSource.getText(), txtCode.getText());
		txtLog.append(logMessage);
		// logMessage =
		// String.format("end = %d, hitEnd = %s%n",m.end(),m.hitEnd() );
	}// postLogMessage
	
	private void initApp(){
		
		txtCode.setText("\\A0*");
		txtReplacement.setText("");
		txtSource.setText("01110");

//		String patternMod4 = "\\[[+-]{0,1}([048]{1}|[0-9]*([02468]{1}[048]{1}|[13579]{1}[26]{1}))\\]";
//		txtCode.setText(patternMod4);
	}//initApp

	// ---------------------------------------------------------------------------------------------

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTestRegex = new JFrame();
		frmTestRegex.setTitle("Test Regex");
		frmTestRegex.setBounds(100, 100, 905, 659);
		frmTestRegex.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTestRegex.getContentPane().setLayout(null);

		txtCode = new JTextField();
		txtCode.setHorizontalAlignment(SwingConstants.RIGHT);
		txtCode.setFont(new Font("Courier New", Font.PLAIN, 17));
		txtCode.setBounds(120, 67, 350, 20);
		frmTestRegex.getContentPane().add(txtCode);
		txtCode.setColumns(10);

		txtSource = new JTextField();
		txtSource.setText("Label: MOV B,A ; comments");
		txtSource.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSource.setFont(new Font("Courier New", Font.PLAIN, 17));
		txtSource.setColumns(10);
		txtSource.setBounds(120, 100, 350, 20);
		frmTestRegex.getContentPane().add(txtSource);

		JLabel lblRegexCode = new JLabel("Regex Code");
		lblRegexCode.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblRegexCode.setBounds(10, 67, 100, 20);
		frmTestRegex.getContentPane().add(lblRegexCode);

		JLabel lblSourceString = new JLabel("Source String");
		lblSourceString.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblSourceString.setBounds(10, 100, 125, 20);
		frmTestRegex.getContentPane().add(lblSourceString);

		txtResult = new JTextField();
		txtResult.setHorizontalAlignment(SwingConstants.RIGHT);
		txtResult.setFont(new Font("Courier New", Font.PLAIN, 17));
		txtResult.setColumns(10);
		txtResult.setBounds(120, 203, 350, 20);
		frmTestRegex.getContentPane().add(txtResult);

		JLabel lblResult = new JLabel("Result");
		lblResult.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblResult.setBounds(10, 207, 125, 20);
		frmTestRegex.getContentPane().add(lblResult);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(480, 54, 398, 518);
		frmTestRegex.getContentPane().add(scrollPane);

		txtLog = new JTextArea();
		txtLog.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() >= 2) {
					if (me.getComponent().getName() == "txtLog") {
						((JTextComponent) me.getComponent()).setText("");
					}// inner if
				}// outer if
			}
		});
		txtLog.setName("txtLog");
		//txtLog.addMouseListener(this);
		// txtLog.setText("Log");
		scrollPane.setViewportView(txtLog);

		btnMatches = new JButton("Matches");
		btnMatches.setActionCommand("btnMatches");
		btnMatches.addActionListener(this);
		btnMatches.setBounds(170, 250, 89, 23);
		frmTestRegex.getContentPane().add(btnMatches);

		btnFind = new JButton("Find");
		btnFind.setActionCommand("btnFind");
		btnFind.addActionListener(this);
		btnFind.setBounds(170, 284, 89, 23);
		frmTestRegex.getContentPane().add(btnFind);

		btnLookingAt = new JButton("lookingAt");
		btnLookingAt.setActionCommand("btnLookingAt");
		btnLookingAt.addActionListener(this);
		btnLookingAt.setBounds(170, 318, 89, 23);
		frmTestRegex.getContentPane().add(btnLookingAt);

		JButton btnSaveLog = new JButton("Save Log");
		btnSaveLog.setActionCommand("btnSaveLog");
		btnSaveLog.addActionListener(this);
		btnSaveLog.setBounds(170, 473, 89, 23);
		frmTestRegex.getContentPane().add(btnSaveLog);

		btnActOnResult = new JButton("Act on Result");
		btnActOnResult.addActionListener(this);
		btnActOnResult.setActionCommand("btnActOnResult");
		btnActOnResult.setBounds(341, 250, 129, 23);
		frmTestRegex.getContentPane().add(btnActOnResult);
		
		btnReplace = new JButton("Replace");
		btnReplace.addActionListener(this);
		btnReplace.setActionCommand("btnReplace");
		btnReplace.setBounds(170, 369, 89, 23);
		frmTestRegex.getContentPane().add(btnReplace);
		
		JLabel lblReplacement = new JLabel("Replacement");
		lblReplacement.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblReplacement.setBounds(10, 141, 100, 20);
		frmTestRegex.getContentPane().add(lblReplacement);
		
		txtReplacement = new JTextField();
		txtReplacement.setText("\\[[+-]{0,1}([048]{1}|[0-9]*([02468]{1}[048]{1}|[13579]{1}[26]{1}))\\]");
		txtReplacement.setHorizontalAlignment(SwingConstants.RIGHT);
		txtReplacement.setFont(new Font("Courier New", Font.PLAIN, 17));
		txtReplacement.setColumns(10);
		txtReplacement.setBounds(120, 141, 350, 20);
		frmTestRegex.getContentPane().add(txtReplacement);
		initApp();
	}// initialize

	
}// class TestRegex
