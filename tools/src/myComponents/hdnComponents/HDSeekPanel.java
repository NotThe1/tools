package myComponents.hdnComponents;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.UIManager;

public class HDSeekPanel extends HDNumberBox {

	private static final long serialVersionUID = 1L;

	private int stepValue;
	private AdapterForPanel adapterForPanel = new AdapterForPanel();

	private void stepValue(int direction) {
		int changeAmount = stepValue * direction;

		long newValue = currentValue;
		newValue += changeAmount;

		if (newValue > rangeModel.getMaximum()) {
			setNewValue(rangeModel.getMaximum());
		} else if (newValue < rangeModel.getMinimum()) {
			setNewValue(rangeModel.getMinimum());
		} else {
			setNewValue((int) newValue);
		} // if
	}//

	// -------------------------------------------------------

	public HDSeekPanel() {
		this(true);
	}// Constructor

	public HDSeekPanel(boolean decimalDisplay) {
		super(decimalDisplay);
		initialize();
		appInit();
	}// Constructor

	///////////////////////////////////////////////////////////////////////////
	private void setStepValue(int step) {
		this.stepValue = Math.max(1, step); // must be 1 or greater
	}//setStepValue
	
	private void doFirst() {
		setNewValue( rangeModel.getMinimum());
	}// doFirst

	private void doLast() {
		setNewValue( rangeModel.getMaximum());
	}// doLast

	private void doNext() {
		stepValue(UP);
	}// doNext

	private void doPrevious() {
		stepValue(DOWN);
	}// doPrevious

	///////////////////////////////////////////////////////////////////////////
	
//	public void close() {
//		
//	}
	
	private void appClose() {
		
	}//appClose
	
	private void appInit() {
		this.addMouseListener(adapterForPanel);
		this.setStepValue(1);
	}//appInit

	public void initialize() {

		setPreferredSize(new Dimension(390, 30));

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 65, 65, 100, 65, 65, 0 };
		gridBagLayout.rowHeights = new int[] { 23, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JButton btnFirst = new JButton("<<");
		btnFirst.setName(FIRST);
		btnFirst.addActionListener(adapterForPanel);
		GridBagConstraints gbc_btnFirst = new GridBagConstraints();
		gbc_btnFirst.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnFirst.insets = new Insets(5, 1, 5, 5);
		gbc_btnFirst.gridx = 0;
		gbc_btnFirst.gridy = 0;
		add(btnFirst, gbc_btnFirst);

		JButton btnPrior = new JButton("<");
		btnPrior.setName(PREVIOUS);
		btnPrior.addActionListener(adapterForPanel);
//		btnPrior.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				stepValue(DOWN);
//			}
//		});
		GridBagConstraints gbc_btnPrior = new GridBagConstraints();
		gbc_btnPrior.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnPrior.insets = new Insets(5, 0, 5, 0);
		gbc_btnPrior.gridx = 1;
		gbc_btnPrior.gridy = 0;
		add(btnPrior, gbc_btnPrior);

//		txtValueDisplay.setFont(new Font("Courier New", Font.PLAIN, 13));
//		txtValueDisplay.addFocusListener(new FocusAdapter() {
//			@Override
//			public void focusLost(FocusEvent arg0) {
//				if (txtValueDisplay.getText().equals("")) {
//					return;
//				} // if null
//				int radix = showDecimal ? 10 : 16;
//				setNewValue(Integer.valueOf(txtValueDisplay.getText(), radix));
//			}
//		});
		GridBagConstraints gbc_txtValueDisplay = new GridBagConstraints();
		gbc_txtValueDisplay.anchor = GridBagConstraints.CENTER;
		gbc_txtValueDisplay.insets = new Insets(5, 2, 5, 2);
		gbc_txtValueDisplay.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtValueDisplay.gridx = 2;
		gbc_txtValueDisplay.gridy = 0;
		add(txtValueDisplay, gbc_txtValueDisplay);

		JButton btnNext = new JButton(">");
		btnNext.setName(NEXT);
		btnNext.addActionListener(adapterForPanel);
//		btnNext.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				stepValue(UP);
//			}
//		});
		GridBagConstraints gbc_btnNext = new GridBagConstraints();
		gbc_btnNext.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNext.insets = new Insets(5, 0, 5, 0);
		gbc_btnNext.gridx = 3;
		gbc_btnNext.gridy = 0;
		add(btnNext, gbc_btnNext);

		JButton btnLast = new JButton(">>");
		btnLast.setName(LAST);
		btnLast.addActionListener(adapterForPanel);
		GridBagConstraints gbc_btnLast = new GridBagConstraints();
		gbc_btnLast.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnLast.insets = new Insets(5, 5, 5, 2);
		gbc_btnLast.gridx = 4;
		gbc_btnLast.gridy = 0;
		add(btnLast, gbc_btnLast);

		setBorder(UIManager.getBorder("Spinner.border"));
	}// Constructor

	// ---------------------------

	class AdapterForPanel implements ActionListener, MouseListener {

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			String name = ((JComponent) actionEvent.getSource()).getName();
			switch (name) {
			case FIRST:
				doFirst();
				break;
			case LAST:
				doLast();
				break;
			case NEXT:
				doNext();
				break;
			case PREVIOUS:
				doPrevious();
				break;
			}// switch

		}// actionPerformed

		@Override
		public void mouseClicked(MouseEvent mouseEvent) {
			if (mouseEvent.getClickCount() > 1) {
				if (showDecimal) {
					setHexDisplay();
				} else {
					setDecimalDisplay();
				} // if inner
			} // if click count
		}// mouseClicked

		@Override
		public void mouseEntered(MouseEvent mouseEvent) {
			/* Not Used */
		}// mouseEntered

		@Override
		public void mouseExited(MouseEvent mouseEvent) {
			/* Not Used */
		}// mouseExited

		@Override
		public void mousePressed(MouseEvent mouseEvent) {
			// TODO Auto-generated method stub

		}// mousePressed

		@Override
		public void mouseReleased(MouseEvent arg0) {
			/* Not Used */
		}// mouseReleased

	}// class AdapterForPanel

	private static final int UP = 1;
	private static final int DOWN = -1;

	private static final String FIRST = "First";
	private static final String LAST = "Last";
	private static final String NEXT = "Next";
	private static final String PREVIOUS = "Previous";
}// class HDSeekPanel
