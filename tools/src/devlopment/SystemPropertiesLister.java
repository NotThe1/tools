package devlopment;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;

public class SystemPropertiesLister implements ActionListener, MouseListener{

	private JFrame frmTestHarness;
	private JFormattedTextField ftfOne;
	private JFormattedTextField ftfTwo;
	private JFormattedTextField ftfThree;
	private JFormattedTextField ftfFour;
	private JTextArea txtLog;
	private JSpinner spinnerOne;
	private JSpinner spinnerTwo;
	private JSpinner spinnerThree;
	private JSpinner spinnerFour;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SystemPropertiesLister window = new SystemPropertiesLister();
					window.frmTestHarness.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public void listProperties(){
		Properties properties = System.getProperties();
		Enumeration<?> propertyNames = properties.propertyNames();
		String property,value;
		String entry;
		while(propertyNames.hasMoreElements()){
			property = (String) propertyNames.nextElement();
			value = properties.getProperty(property);
			entry = String.format("%-20s   %s%n", property,value);
			txtLog.append(entry);
		}//while
		
	}//listProperties
	
	public void initApp(){
		txtLog.setText("");
		listProperties();
	}//initApp
	//------------------------------------------------------------------------

	
	@Override
	public void actionPerformed(ActionEvent ae) {
		String actionCommand = ae.getActionCommand();
		
		switch (actionCommand){
		case "btnOne":
			doBtnOne();
			break;
		case "btnTwo":
			doBtnTwo();
			break;
		case "btnThree":
			doBtnThree();
			break;
		case "btnFour":
			doBtnFour();
			break;
		case "doBtnFour":
			doBtnFive();
			break;
		case "btnSix":
			doBtnSix();
			break;
		case "btnReset":
			doBtnReset();
			break;
		}		
	}//
	public void doBtnReset(){
		
	}//
	
	public void doBtnSix(){
		
	}//
	
	public void doBtnFive(){
		
	}//
	
	public void doBtnFour(){
		
	}//
	
	public void doBtnThree(){
		
	}//
	
	public void doBtnTwo(){
		
	}//
	
	public void doBtnOne(){
		
	}//
	
	
	
	@Override
	public void mouseClicked(MouseEvent me) {
		if ( (me.getClickCount() >1) && (me.getComponent().getName() == "txtLog")) {
			((JTextComponent) me.getComponent()).setText("");
		}//if
	}

	
	//------------------------------------------------------------------------

	/**
	 * Create the application.
	 */
	public SystemPropertiesLister() {
		initialize();
		initApp();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTestHarness = new JFrame();
		frmTestHarness.setTitle("Test Harness");
		frmTestHarness.setBounds(100, 100, 1135, 579);
		frmTestHarness.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTestHarness.getContentPane().setLayout(null);
		//frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 326, 506);
		frmTestHarness.getContentPane().add(panel);
		panel.setLayout(null);
		
		JButton btnOne = new JButton("btnOne");
		btnOne.setName("btnOne");
		btnOne.setActionCommand("btnOne");
		btnOne.addActionListener(this);
		btnOne.setBounds(20, 384, 91, 23);
		panel.add(btnOne);
		
		JButton btnTwo = new JButton("btnTwo");
		btnTwo.setName("btnTwo");
		btnTwo.setActionCommand("btnTwo");
		btnTwo.addActionListener(this);
		btnTwo.setBounds(20, 418, 91, 23);
		panel.add(btnTwo);
		
		JButton btnThree = new JButton("btnThree");
		btnThree.setName("btnThree");
		btnThree.setActionCommand("btnThree");
		btnThree.addActionListener(this);
		btnThree.setBounds(20, 452, 91, 23);
		panel.add(btnThree);
		
		JButton btnReset = new JButton("Reset");
		btnReset.setName("bntReset");
		btnReset.setActionCommand("btnReset");
		btnReset.setBounds(253, 472, 63, 23);
		btnReset.addActionListener(this);
		panel.add(btnReset);
		
		JPanel panelText = new JPanel();
		panelText.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelText.setBounds(10, 25, 306, 110);
		panel.add(panelText);
		panelText.setLayout(null);
		
		JLabel lblOne = new JLabel("One");
		lblOne.setBounds(10, 14, 30, 14);
		panelText.add(lblOne);
		
		JLabel lblTwo = new JLabel("Two");
		lblTwo.setBounds(10, 38, 46, 14);
		panelText.add(lblTwo);
		
		JLabel lblThree = new JLabel("Three");
		lblThree.setBounds(10, 60, 46, 14);
		panelText.add(lblThree);
		
		JLabel lblFour = new JLabel("Four");
		lblFour.setBounds(10, 83, 46, 14);
		panelText.add(lblFour);
		
		ftfOne = new JFormattedTextField();
		ftfOne.setHorizontalAlignment(SwingConstants.RIGHT);
		ftfOne.setBounds(50, 11, 234, 20);
		panelText.add(ftfOne);
		ftfOne.setActionCommand("ftfOne");
		ftfOne.setName("ftfOne");
		
		ftfTwo = new JFormattedTextField();
		ftfTwo.setHorizontalAlignment(SwingConstants.RIGHT);
		ftfTwo.setBounds(50, 35, 234, 20);
		panelText.add(ftfTwo);
		ftfTwo.setActionCommand("ftfTwo");
		ftfTwo.setName("ftfTwo");
		
		ftfThree = new JFormattedTextField();
		ftfThree.setHorizontalAlignment(SwingConstants.RIGHT);
		ftfThree.setBounds(50, 57, 234, 20);
		panelText.add(ftfThree);
		ftfThree.setActionCommand("ftfThree");
		ftfThree.setName("ftfThree");
		
		ftfFour = new JFormattedTextField();
		ftfFour.setHorizontalAlignment(SwingConstants.RIGHT);
		ftfFour.setBounds(50, 80, 234, 20);
		panelText.add(ftfFour);
		ftfFour.setActionCommand("ftfFour");
		ftfFour.setName("ftfFour");
		
		JPanel panelSpinnes = new JPanel();
		panelSpinnes.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelSpinnes.setBounds(10, 146, 306, 110);
		panel.add(panelSpinnes);
		panelSpinnes.setLayout(null);
		
		spinnerOne = new JSpinner();
		spinnerOne.setBounds(77, 6, 136, 20);
		panelSpinnes.add(spinnerOne);
		
		spinnerTwo = new JSpinner();
		spinnerTwo.setBounds(77, 32, 136, 20);
		panelSpinnes.add(spinnerTwo);
		
		spinnerThree = new JSpinner();
		spinnerThree.setBounds(77, 58, 136, 20);
		panelSpinnes.add(spinnerThree);
		
		spinnerFour = new JSpinner();
		spinnerFour.setBounds(77, 84, 136, 20);
		panelSpinnes.add(spinnerFour);
		
		JPanel panelChecksAndRadioB = new JPanel();
		panelChecksAndRadioB.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelChecksAndRadioB.setBounds(10, 267, 306, 110);
		panel.add(panelChecksAndRadioB);
		panelChecksAndRadioB.setLayout(null);
		
		JPanel panelRB = new JPanel();
		panelRB.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panelRB.setBounds(10, 11, 117, 88);
		panelChecksAndRadioB.add(panelRB);
		panelRB.setLayout(null);
		
		JRadioButton rbOne = new JRadioButton("rbOne");
		rbOne.setBounds(6, 7, 109, 23);
		panelRB.add(rbOne);
		
		JRadioButton rbTwo = new JRadioButton("rbTwo");
		rbTwo.setBounds(6, 33, 109, 23);
		panelRB.add(rbTwo);
		
		JRadioButton rbThree = new JRadioButton("rbThree");
		rbThree.setBounds(6, 60, 109, 23);
		panelRB.add(rbThree);
		
		JCheckBox cbOne = new JCheckBox("cbOne");
		cbOne.setBounds(160, 11, 97, 23);
		panelChecksAndRadioB.add(cbOne);
		
		JCheckBox cbTwo = new JCheckBox("cbTwo");
		cbTwo.setBounds(160, 41, 97, 23);
		panelChecksAndRadioB.add(cbTwo);
		
		JCheckBox cbThree = new JCheckBox("cbThree");
		cbThree.setBounds(160, 76, 97, 23);
		panelChecksAndRadioB.add(cbThree);
		
		JButton btnFour = new JButton("btnFour");
		btnFour.setName("btnFour");
		btnFour.setActionCommand("btnFour");
		btnFour.setBounds(138, 384, 91, 23);
		panel.add(btnFour);
		
		JButton btnFive = new JButton("btnFive");
		btnFive.setName("btnFive");
		btnFive.setActionCommand("btnFive");
		btnFive.setBounds(138, 418, 91, 23);
		panel.add(btnFive);
		
		JButton btnSix = new JButton("btnSix");
		btnSix.setName("btnSix");
		btnSix.setActionCommand("btnSix");
		btnSix.setBounds(138, 452, 91, 23);
		panel.add(btnSix);
		ftfFour.addActionListener(this);
		ftfThree.addActionListener(this);
		ftfTwo.addActionListener(this);
		ftfOne.addActionListener(this);
		
		JMenuBar menuBar = new JMenuBar();
		frmTestHarness.setJMenuBar(menuBar);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(336, 0, 747, 517);
		frmTestHarness.getContentPane().add(scrollPane);
		
		txtLog = new JTextArea();
		txtLog.setName("txtLog");
		txtLog.addMouseListener(this);
		txtLog.setEditable(false);
		scrollPane.setViewportView(txtLog);
		
	}
	//------------------------------------------------------------------------

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
