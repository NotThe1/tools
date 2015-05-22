package devlopment;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.text.JTextComponent;

public class WindowOneSimple implements ActionListener, MouseListener{

	private JFrame frmTestHarness;
	private JFormattedTextField ftfOne;
	private JFormattedTextField ftfTwo;
	private JFormattedTextField ftfThree;
	private JFormattedTextField ftfFour;
	private JTextArea txtLog;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WindowOneSimple window = new WindowOneSimple();
					window.frmTestHarness.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		String actionCommand = ae.getActionCommand();
		
		switch (actionCommand){
		case "btnOne":
			break;
		case "btnTwo":
			break;
		case "btnThree":
			break;
		case "btnReset":
			break;
		}
		
	}
	
	@Override
	public void mouseClicked(MouseEvent me) {
		if ( (me.getClickCount() >1) && (me.getComponent().getName() == "txtLog")) {
			((JTextComponent) me.getComponent()).setText("");
		}//if
	}

	
	public void initApp(){
		
	}
	//------------------------------------------------------------------------

	/**
	 * Create the application.
	 */
	public WindowOneSimple() {
		initialize();
		initApp();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTestHarness = new JFrame();
		frmTestHarness.setTitle("Test Harness");
		frmTestHarness.setBounds(100, 100, 662, 579);
		frmTestHarness.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTestHarness.getContentPane().setLayout(null);
		//frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 312, 506);
		frmTestHarness.getContentPane().add(panel);
		panel.setLayout(null);
		
		ftfOne = new JFormattedTextField();
		ftfOne.setActionCommand("ftfOne");
		ftfOne.setName("ftfOne");
		ftfOne.addActionListener(this);
		ftfOne.setBounds(68, 40, 234, 20);
		panel.add(ftfOne);
		
		ftfTwo = new JFormattedTextField();
		ftfTwo.setActionCommand("ftfTwo");
		ftfTwo.setName("ftfTwo");
		ftfTwo.addActionListener(this);
		ftfTwo.setBounds(68, 82, 234, 20);
		panel.add(ftfTwo);
		
		ftfThree = new JFormattedTextField();
		ftfThree.setActionCommand("ftfThree");
		ftfThree.setName("ftfThree");
		ftfThree.addActionListener(this);
		ftfThree.setBounds(68, 126, 234, 20);
		panel.add(ftfThree);
		
		ftfFour = new JFormattedTextField();
		ftfFour.setActionCommand("ftfFour");
		ftfFour.setName("ftfFour");
		ftfFour.addActionListener(this);
		ftfFour.setBounds(68, 168, 234, 20);
		panel.add(ftfFour);
		
		JLabel lblOne = new JLabel("lblOne");
		lblOne.setBounds(12, 43, 46, 14);
		panel.add(lblOne);
		
		JLabel lblTwo = new JLabel("lblTwo");
		lblTwo.setBounds(12, 85, 46, 14);
		panel.add(lblTwo);
		
		JLabel lblThree = new JLabel("lblThree");
		lblThree.setBounds(12, 129, 46, 14);
		panel.add(lblThree);
		
		JLabel lblFour = new JLabel("lblFour");
		lblFour.setBounds(12, 171, 46, 14);
		panel.add(lblFour);
		
		JButton btnOne = new JButton("btnOne");
		btnOne.setName("btnOne");
		btnOne.setActionCommand("btnOne");
		btnOne.addActionListener(this);
		btnOne.setBounds(128, 231, 91, 23);
		panel.add(btnOne);
		
		JButton btnTwo = new JButton("btnTwo");
		btnTwo.setName("btnTwo");
		btnTwo.setActionCommand("btnTwo");
		btnTwo.addActionListener(this);
		btnTwo.setBounds(128, 280, 91, 23);
		panel.add(btnTwo);
		
		JButton btnThree = new JButton("btnThree");
		btnThree.setName("btnThree");
		btnThree.setActionCommand("btnThree");
		btnThree.addActionListener(this);
		btnThree.setBounds(128, 325, 91, 23);
		panel.add(btnThree);
		
		JButton btnReset = new JButton("Reset");
		btnReset.setName("bntReset");
		btnReset.setActionCommand("btnReset");
		btnReset.setBounds(211, 472, 91, 23);
		btnReset.addActionListener(this);
		panel.add(btnReset);
		
		JMenuBar menuBar = new JMenuBar();
		frmTestHarness.setJMenuBar(menuBar);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(313, 0, 333, 517);
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
