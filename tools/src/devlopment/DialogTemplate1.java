package devlopment;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class DialogTemplate1 extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private int dialogResultValue;

	public int showDialog() {
		dialogResultValue = JOptionPane.CANCEL_OPTION;
		this.setLocationRelativeTo(this.getOwner());

		this.setVisible(true);
		this.dispose();
		return dialogResultValue;
	}// showDialog

	private void doBtnOK() {
		System.err.printf("%s not yet implemented%n", "doBtnOK");
		dialogResultValue = JOptionPane.OK_OPTION;
		dispose();
	}// doBtnOk

	private void doBtnCancel() {
		System.err.printf("%s not yet implemented%n", "doBtnCancel");
		dialogResultValue = JOptionPane.CANCEL_OPTION;
		dispose();
	}// doBtnCancel
		// -------------------------------------------

	private void appClose() {

	}// appClose

	private void appInit() {

	}// appInit

	/**
	 * Create the dialog.
	 */
	// public Dialog1() {
	// initialize();
	// appInit();
	// }//Constructor

	public DialogTemplate1(Window w) {
		 super(w,"Dialog Template 1",Dialog.DEFAULT_MODALITY_TYPE);

		initialize();
		appInit();
	}// Constructor

	private void initialize() {
	//	setModalityType(java.awt.Dialog.DEFAULT_MODALITY_TYPE);
	//	this.setTitle("Dialog 1");
	//	this.setLocationRelativeTo(this.getOwner());
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				appClose();
			}
		});
		this.setSize(450, 300);
		// this.setLocation(10, 10);
		// this.setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnOk = new JButton("OK");
		btnOk.setName(BTN_OK);
		btnOk.addActionListener(this);
		btnOk.setActionCommand("OK");
		buttonPane.add(btnOk);
		getRootPane().setDefaultButton(btnOk);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setName(BTN_CANCEL);
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("Cancel");
		buttonPane.add(btnCancel);

	}// initialize

	private static final String BTN_OK = "btnOk";
	private static final String BTN_CANCEL = "btnCancel";

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		switch (((Component) actionEvent.getSource()).getName()) {
		case BTN_OK:
			doBtnOK();
			break;
		case BTN_CANCEL:
			doBtnCancel();
			break;

		default:
			System.err.printf("[ListDevicePropertyDialog] - actionPerformed %n Unknown Action %s%n%n",
					((Component) actionEvent.getSource()).getName());
		}// switch

	}// actionPerformed

	private final JPanel contentPanel = new JPanel();

}// class Dialog1
