import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;

/**
 * This class handles the data and components/GUI of the Maintenance
 */
public class Maintenance{

	// private RegularModel vendModel;
	private TransactionHandler transactionHandler;
	private ItemHandler itemHandler;

	// private SpecialModel specModel;
	
	protected JFrame maintainanceFrame;
	
	protected JPanel maintenanceOptionsPanel;
	
	protected JPanel restockItemsPnl, changePricePnl, restockChangePnl, finalizingPanel, receiptPanel;
	protected JPanel collectEarningsPnl;
	
	protected JLabel restockItemsLabel, changePriceLabel, restockChangeLabel, lockedIconLabel, collectEarningsIdleText;
	protected JTextArea receiptTA;
	
	protected JComboBox<String> restockItemsCB, changePriceCB, restockChangeCB;
	protected JPanel restockItemsCBPanel, changePriceCBPanel, restockChangeCBPanel;
	protected JLabel restockItemsCurStockLbl, changePriceCurPriceLbl, restockChangeCurStockLbl;
	
	protected JTextField changePriceTF;
	
	protected JButton backButton, restockItemsConfirm, changePriceConfirm, restockChangeConfirm, collectEarningsBtn, printRecieptBtn, receiptBackBtn;
	
	protected int restockItemsCountNumber;
	protected JPanel restockItemsCounterPnl;
	protected JButton restockItemsMinus, restockItemsPlus;
	protected JLabel restockItemsCounterDisplay;
	
	protected int restockChangeCountNumber;
	protected JPanel restockChangeCounterPnl;
	protected JButton restockChangeMinus, restockChangePlus;
	protected JLabel restockChangeCounterDisplay;
	
	private ImageIcon arrowLeft = new ImageIcon("assets/ArrowLeft.png"), lockIcon = new ImageIcon("assets/LockIcon.png");
	
	private final int StockLimit = 15;
	private int timesRestocked = 0;

	protected boolean isSpecial;
	
	private MaintenanceListeners maintenanceListeners;

	public Maintenance(TransactionHandler transactionHandler, ItemHandler itemHandler) {
		this.itemHandler = itemHandler;
		this.transactionHandler = transactionHandler;
		this.isSpecial = false;
		this.maintenanceListeners = new MaintenanceListeners(this);
		
		//attributes of the Maintenance Frame
		this.maintainanceFrame = new JFrame("Maintenance");
		this.maintainanceFrame.setSize(820,830); //sets the x and y dimensions; size of maintainanceFrame
		this.maintainanceFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //exit out of application
		this.maintainanceFrame.setResizable(false);
		this.maintainanceFrame.setLocationRelativeTo(null);
		this.maintainanceFrame.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		
		//Components of the back button to return to Main Menu
		backButton = new JButton(resizeImage(arrowLeft, 80, 60));
		backButton.setPreferredSize(new Dimension(100,60));
		backButton.setContentAreaFilled(false); 
		backButton.setFocusable(false);
		
		JPanel backPanel = new JPanel(new FlowLayout());
		backPanel.setPreferredSize(new Dimension(100, 830));
		backPanel.setBackground(Color.CYAN);
		backPanel.add(backButton);
		
		//Initializes the panels and components of the class
		initializeMaintenancePanels("Regular");
		
		//Components of the receipt panel and data
		this.lockedIconLabel = new JLabel(resizeImage(lockIcon, 700, 700));
		this.lockedIconLabel.setPreferredSize(new Dimension(700, 830));
		this.lockedIconLabel.setOpaque(true);
		this.lockedIconLabel.setVisible(false);
		
		this.receiptBackBtn = new JButton(resizeImage(arrowLeft, 80, 60));
		this.receiptBackBtn.setPreferredSize(new Dimension(100, 60));
		this.receiptBackBtn.setContentAreaFilled(false);
		this.receiptBackBtn.setFocusable(false);
		
		this.receiptTA = new JTextArea("");
		this.receiptTA.setPreferredSize(new Dimension(340, 750));
		this.receiptTA.setFont(new Font("monospaced", Font.PLAIN, 20));
		this.receiptTA.setEditable(false);
		
		this.receiptPanel = new JPanel(new FlowLayout());
		this.receiptPanel.setPreferredSize(new Dimension(340, 830));
		this.receiptPanel.add(receiptBackBtn);
		this.receiptPanel.add(receiptTA);
		
		maintenanceOptionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		this.maintenanceOptionsPanel.setPreferredSize(new Dimension(700,830));
		this.maintenanceOptionsPanel.add(lockedIconLabel);
		this.maintenanceOptionsPanel.add(restockItemsPnl);
		this.maintenanceOptionsPanel.add(changePricePnl);
		this.maintenanceOptionsPanel.add(restockChangePnl);
		this.maintenanceOptionsPanel.add(finalizingPanel);
		
		this.maintainanceFrame.add(backPanel);
		this.maintainanceFrame.add(maintenanceOptionsPanel);
		this.maintainanceFrame.add(receiptPanel);
	}

	public Maintenance(TransactionHandler transactionHandler, ItemHandler itemHandler, boolean isSpecial) {
		this.itemHandler = itemHandler;
		this.transactionHandler = transactionHandler;
		this.isSpecial = isSpecial;
		this.maintenanceListeners = new MaintenanceListeners(this);
		//attributes of the Maintenance Frame
		this.maintainanceFrame = new JFrame("Maintenance");
		this.maintainanceFrame.setSize(820,830); //sets the x and y dimensions; size of maintainanceFrame
		this.maintainanceFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //exit out of application
		this.maintainanceFrame.setResizable(false);
		this.maintainanceFrame.setLocationRelativeTo(null);
		this.maintainanceFrame.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		
		backButton = new JButton(resizeImage(arrowLeft, 80, 60));
		backButton.setPreferredSize(new Dimension(100,60));
		backButton.setContentAreaFilled(false); 
		backButton.setFocusable(false);
		
		JPanel backPanel = new JPanel(new FlowLayout());
		backPanel.setPreferredSize(new Dimension(100, 830));
		backPanel.setBackground(Color.CYAN);
		backPanel.add(backButton);
		
		initializeMaintenancePanels("Special");
		
		this.lockedIconLabel = new JLabel(resizeImage(lockIcon, 700, 700));
		this.lockedIconLabel.setPreferredSize(new Dimension(700, 830));
		this.lockedIconLabel.setOpaque(true);
		this.lockedIconLabel.setVisible(false);
		
		this.receiptBackBtn = new JButton(resizeImage(arrowLeft, 80, 60));
		this.receiptBackBtn.setPreferredSize(new Dimension(100, 60));
		this.receiptBackBtn.setContentAreaFilled(false);
		this.receiptBackBtn.setFocusable(false);
		
		this.receiptTA = new JTextArea("");
		this.receiptTA.setPreferredSize(new Dimension(340, 750));
		this.receiptTA.setFont(new Font("monospaced", Font.PLAIN, 20));
		this.receiptTA.setEditable(false);
		
		this.receiptPanel = new JPanel(new FlowLayout());
		this.receiptPanel.setPreferredSize(new Dimension(340, 830));
		this.receiptPanel.add(receiptBackBtn);
		this.receiptPanel.add(receiptTA);
		
		maintenanceOptionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		this.maintenanceOptionsPanel.setPreferredSize(new Dimension(700,830));
		this.maintenanceOptionsPanel.add(lockedIconLabel);
		this.maintenanceOptionsPanel.add(restockItemsPnl);
		this.maintenanceOptionsPanel.add(changePricePnl);
		this.maintenanceOptionsPanel.add(restockChangePnl);
		this.maintenanceOptionsPanel.add(finalizingPanel);
		
		this.maintainanceFrame.add(backPanel);
		this.maintainanceFrame.add(maintenanceOptionsPanel);
		this.maintainanceFrame.add(receiptPanel);
	}
	
	public void initiateRegMaintenanceActionListeners(RegularView vendView, MainMenu menu, ItemHandler itemHandler,
            TransactionHandler transactionHandler, Maintenance maintenance) {
				maintenanceListeners.initiateRegMaintenanceActionListeners(vendView, menu, itemHandler, transactionHandler, maintenance);
	}

	public void initiateSpecialMaintenanceActionListeners(SpecialGui vendView, MainMenu menu, ItemHandler itemHandler,
            TransactionHandler transactionHandler, Maintenance maintenance) {
				maintenanceListeners.initiateSpecialMaintenanceActionListeners(vendView, menu, itemHandler, transactionHandler, maintenance);
	}
	
	/**
	 * Resizes the image to fit components
	 * @param icon - image
	 * @param w - width
	 * @param h - height
	 * @return the new resized image
	 */
	private ImageIcon resizeImage(ImageIcon icon, int w, int h){
		Image newimg = icon.getImage().getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(newimg);
	}
	
	/**
	 * Resets the counter for restocking items
	 */
	public void resetRestockItemsCounterDisplay() {
		this.restockItemsCountNumber=0;
		restockItemsCounterDisplay.setText(String.format("%d", restockItemsCountNumber));
	}
	
	/**
	 * Resets the text field for changing price
	 */
	public void resetChangePriceTextField() {
		this.changePriceTF.setText("0");
	}
	
	/**
	 * Resets the counter for restocking change
	 */
	public void resetRestockChangeCounterDisplay() {
		this.restockChangeCountNumber=0;
		restockChangeCounterDisplay.setText(String.format("%d", restockChangeCountNumber));
	}

	/**
	 * Returns the number of times items have been restocked
	 * @return timesRestocked - number of times of restocking
	 */
	public int getTimesRestocked() {
		return this.timesRestocked;
	}
	
	/**
	 * Increments the number of times restocked
	 */
	public void incrementTimesRestocked() {
		this.timesRestocked+=1;
	}
	
	/**
	 * Reveals the area for displaying the receipt
	 */
	public void showReceiptArea() {
		this.maintainanceFrame.setSize(1160,830);
	}
	
	/**
	 * Hides the area for displaying the receipt
	 */
	public void hideReceiptArea() {
		this.maintainanceFrame.setSize(820,830);
	}
	
	/**
	 * Locks the main components of the class while receipt is revealed
	 */
	public void lockMaintenance() {
		this.lockedIconLabel.setVisible(true);
		this.backButton.setEnabled(false);
	}
	
	/**
	 * Unlocks the main components of the class after receipt is hidden
	 */
	public void unlockMaintenance() {
		this.lockedIconLabel.setVisible(false);
		this.backButton.setEnabled(true);
	}
	
	/**
	 * Initiates the components and logic counters for Restocking Items and Change
	 */
	private void initiateCounters() {
		//Components of the counter for restocking items
		this.restockItemsMinus = new JButton("-");
		this.restockItemsMinus.setPreferredSize(new Dimension(50,50));
		this.restockItemsMinus.setFocusable(false);
		this.restockItemsMinus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(restockItemsCountNumber!=0) {
					restockItemsCountNumber--;
					restockItemsCounterDisplay.setText(String.format("%d", restockItemsCountNumber));
				}
				else System.out.println("Cannot be negative");
			}
		});
		
		this.restockItemsCounterDisplay = new JLabel();
		this.restockItemsCounterDisplay.setText(String.format("%d", restockItemsCountNumber));
		this.restockItemsCounterDisplay.setFont(new Font("", Font.PLAIN, 30));
		
		this.restockItemsPlus = new JButton("+");
		this.restockItemsPlus.setPreferredSize(new Dimension(50,50));
		this.restockItemsPlus.setFocusable(false);
		this.restockItemsPlus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(restockItemsCountNumber == StockLimit - itemHandler.getSingleItemArrayList(restockItemsCB.getSelectedIndex()).size())
					System.out.println("Limit reached");
				else {
					restockItemsCountNumber++;
					restockItemsCounterDisplay.setText(String.format("%d", restockItemsCountNumber));
				}
			}
		});
		
		this.restockItemsCounterPnl = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 25));
		this.restockItemsCounterPnl.setPreferredSize(new Dimension(175, 100));
		this.restockItemsCounterPnl.setBackground(Color.green);
		this.restockItemsCounterPnl.add(this.restockItemsMinus);
		this.restockItemsCounterPnl.add(this.restockItemsCounterDisplay);
		this.restockItemsCounterPnl.add(this.restockItemsPlus);
		
		
		//Components of the counter for restocking change
		this.restockChangeMinus = new JButton("-");
		this.restockChangeMinus.setPreferredSize(new Dimension(50,50));
		this.restockChangeMinus.setFocusable(false);
		this.restockChangeMinus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(restockChangeCountNumber!=0) {
					restockChangeCountNumber--;
					restockChangeCounterDisplay.setText(String.format("%d", restockChangeCountNumber));
				}
				else System.out.println("Cannot be negative");
			}
		});
		
		this.restockChangeCounterDisplay = new JLabel();
		this.restockChangeCounterDisplay.setText(String.format("%d", restockChangeCountNumber));
		this.restockChangeCounterDisplay.setFont(new Font("", Font.PLAIN, 30));
		
		this.restockChangePlus = new JButton("+");
		this.restockChangePlus.setPreferredSize(new Dimension(50,50));
		this.restockChangePlus.setFocusable(false);
		this.restockChangePlus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				restockChangeCountNumber++;
				restockChangeCounterDisplay.setText(String.format("%d", restockChangeCountNumber));
			}
		});
		
		this.restockChangeCounterPnl = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 25));
		this.restockChangeCounterPnl.setPreferredSize(new Dimension(175, 100));
		this.restockChangeCounterPnl.setBackground(Color.green);
		this.restockChangeCounterPnl.add(this.restockChangeMinus);
		this.restockChangeCounterPnl.add(this.restockChangeCounterDisplay);
		this.restockChangeCounterPnl.add(this.restockChangePlus);
	}
	
	/**
	 * Initializes the main components of the Maintenance Class including, Restock Items, Change Price, and Restocking Change
	 */
	private void initializeMaintenancePanels(String type) {
		
		initiateCounters();
		//Different Item Records for RVM and SVM
		if(type.equals("Regular")){
			this.restockItemsCB = new JComboBox<String>(itemHandler.getStringedItemRecord());
			this.changePriceCB = new JComboBox<String>(itemHandler.getStringedItemRecord());
			this.restockChangeCB = new JComboBox<String>(transactionHandler.getStringedChangeDenominations());
		}
		else if(type.equals("Special")){
			this.restockItemsCB = new JComboBox<String>(itemHandler.getStringedSpecItemRecord());
			this.changePriceCB = new JComboBox<String>(itemHandler.getStringedSpecItemRecord());
			this.restockChangeCB = new JComboBox<String>(transactionHandler.getStringedChangeDenominations());
		}

		//Components for restock items
		this.restockItemsLabel = new JLabel("Restock Item:  ");
		this.restockItemsLabel.setFont(new Font("", Font.BOLD, 16));
		
		this.restockItemsCB.setPreferredSize(new Dimension(150,50));
		
		this.restockItemsCurStockLbl = new JLabel("");
		this.restockItemsCurStockLbl.setFont(new Font("", Font.PLAIN, 14));
		
		this.restockItemsCBPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 2));
		this.restockItemsCBPanel.setPreferredSize(new Dimension(150, 75));
		this.restockItemsCBPanel.setBackground(Color.WHITE);
		this.restockItemsCBPanel.add(restockItemsCB);
		this.restockItemsCBPanel.add(restockItemsCurStockLbl);
		
		this.restockItemsConfirm = new JButton("Restock");
		this.restockItemsConfirm.setFocusable(false);
		this.restockItemsConfirm.setPreferredSize(new Dimension(120, 50));
		
		this.restockItemsPnl = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 55));
		this.restockItemsPnl.setPreferredSize(new Dimension(700, 200));
		this.restockItemsPnl.setBackground(Color.lightGray);
		this.restockItemsPnl.add(restockItemsLabel);
		this.restockItemsPnl.add(restockItemsCBPanel);
		this.restockItemsPnl.add(restockItemsCounterPnl);
		this.restockItemsPnl.add(restockItemsConfirm);
		
		//Components for changing price
		this.changePriceLabel = new JLabel("Change Price:");
		this.changePriceLabel.setFont(new Font("", Font.BOLD, 16));
		
		this.changePriceCB.setPreferredSize(new Dimension(150,50));
		
		this.changePriceCurPriceLbl = new JLabel("");
		this.changePriceCurPriceLbl.setFont(new Font("", Font.PLAIN, 14));
		
		this.changePriceCBPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 2));
		this.changePriceCBPanel.setPreferredSize(new Dimension(150, 75));
		this.changePriceCBPanel.setBackground(Color.WHITE);
		this.changePriceCBPanel.add(changePriceCB);
		this.changePriceCBPanel.add(changePriceCurPriceLbl);
		
		JLabel dollarSign = new JLabel("$");
		dollarSign.setFont(new Font("", Font.PLAIN, 18));
		
		this.changePriceTF = new JTextField("0");
		this.changePriceTF.setPreferredSize(new Dimension(100, 30));
		this.changePriceTF.setFont(new Font("", Font.PLAIN, 14));
		
		this.changePriceConfirm = new JButton("Change");
		this.changePriceConfirm.setFocusable(false);
		this.changePriceConfirm.setPreferredSize(new Dimension(120, 50));
		
		this.changePricePnl = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 75));
		this.changePricePnl.setPreferredSize(new Dimension(700, 200));
		this.changePricePnl.setBackground(Color.ORANGE);
		this.changePricePnl.add(changePriceLabel);
		this.changePricePnl.add(changePriceCBPanel);
		this.changePricePnl.add(dollarSign);
		this.changePricePnl.add(changePriceTF);
		this.changePricePnl.add(changePriceConfirm);
		
		//Components for restocking change
		this.restockChangeLabel = new JLabel("Restock Denomination:");
		this.restockChangeLabel.setFont(new Font("", Font.BOLD, 16));
		
		this.restockChangeCB.setPreferredSize(new Dimension(150,50));
		
		this.restockChangeCurStockLbl = new JLabel("");
		this.restockChangeCurStockLbl.setFont(new Font("", Font.PLAIN, 14));
		
		this.restockChangeCBPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 2));
		this.restockChangeCBPanel.setPreferredSize(new Dimension(150, 75));
		this.restockChangeCBPanel.setBackground(Color.WHITE);
		this.restockChangeCBPanel.add(restockChangeCB);
		this.restockChangeCBPanel.add(restockChangeCurStockLbl);
		
		this.restockChangeConfirm = new JButton("Restock");
		this.restockChangeConfirm.setFocusable(false);
		this.restockChangeConfirm.setPreferredSize(new Dimension(120, 50));
		
		this.restockChangePnl = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 50));
		this.restockChangePnl.setPreferredSize(new Dimension(700, 200));
		this.restockChangePnl.setBackground(Color.MAGENTA);
		this.restockChangePnl.add(restockChangeLabel);
		this.restockChangePnl.add(restockChangeCBPanel);
		this.restockChangePnl.add(restockChangeCounterPnl);
		this.restockChangePnl.add(restockChangeConfirm);
		
		//Components for collecting earnings
		this.collectEarningsBtn = new JButton("Collect Earnings");
		this.collectEarningsBtn.setFocusable(false);
		this.collectEarningsBtn.setFont(new Font("", Font.PLAIN, 20));
		this.collectEarningsBtn.setPreferredSize(new Dimension(200, 75));
		
		this.collectEarningsIdleText = new JLabel("");
		
		this.collectEarningsPnl = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 1));
		this.collectEarningsPnl.setPreferredSize(new Dimension(200, 100));
		this.collectEarningsPnl.add(collectEarningsBtn);
		this.collectEarningsPnl.add(collectEarningsIdleText);
		
		////Components for print receipt
		this.printRecieptBtn = new JButton("Print Receipt");
		this.printRecieptBtn.setFocusable(false);
		this.printRecieptBtn.setFont(new Font("", Font.PLAIN, 20));
		this.printRecieptBtn.setPreferredSize(new Dimension(200, 75));
		
		this.finalizingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 62));
		this.finalizingPanel.setPreferredSize(new Dimension(700, 200));
		this.finalizingPanel.setBackground(Color.PINK);
		this.finalizingPanel.add(collectEarningsPnl);
		this.finalizingPanel.add(printRecieptBtn);
	}
	
	public void hideMaintenance() {
		this.getMTFrame().setVisible(false);
	}

	/**
	 * Returns the frame of Maintenance
	 * @return maintainanceFrame
	 */
	public JFrame getMTFrame() {
		return maintainanceFrame;
	}
	
	/**
	 * Returns the ComboBox of Restock Items
	 * @return restockItemsCB
	 */
	public JComboBox<String> getRestockItemsCB() {
		return restockItemsCB;
	}
	
	/**
	 * Returns the index of the item selected in the ComboBox of Restock Items
	 * @return index of selected item
	 */
	public int getRestockItemsCBSelectedIndex() {
		return restockItemsCB.getSelectedIndex();
	}
	
	/**
	 * Returns the integer form of the counter for Restock Items
	 * @return integer form of counter
	 */
	public int getRestockItemCount() {
		return Integer.parseInt(restockItemsCounterDisplay.getText());
	}
	
	/**
	 * Returns the text field of Change Price
	 * @return changePriceTF
	 */
	public JTextField getChangePriceTF() {
		return this.changePriceTF;
	}
	
	/**
	 * Returns the index of the item selected in the ComboBox of Change Price
	 * @return index of selected item
	 */
	public int getChangePriceCBSelectedIndex() {
		return this.changePriceCB.getSelectedIndex();
	}
	
	/**
	 * Returns the index of the item selected in the ComboBox of Restock Change
	 * @return index of selected item
	 */
	public int getRestockChangeCBSelectedIndex() {
		return this.restockChangeCB.getSelectedIndex();
	}
	
	/**
	 * Returns the integer form of the counter for Restock Change
	 * @return integer form of counter
	 */
	public int getRestockChangeCount() {
		return Integer.parseInt(restockChangeCounterDisplay.getText());
	}
	
	/**
	 * Changes the text of collect earnings
	 * @param text
	 */
	public void setCollectEarningsText(String text) {
		this.collectEarningsIdleText.setText(text);
	}
	
	/**
	 * Displays the receipt
	 * @param text - contents of receipt
	 */
	public void setReceiptTextArea(String text) {
		this.receiptTA.setText(text);
	}
	
	/**
	 * Sets the Current Stock of the item
	 * @param text - current stock of the item
	 */
	public void setRestockItemsCurStockLabel(String text) {
		this.restockItemsCurStockLbl.setText(text);
	}
	
	/**
	 * Sets the Current Price of the item
	 * @param text - current price of the item
	 */
	public void setChangePriceCurPriceLabel(String text) {
		this.changePriceCurPriceLbl.setText(text);
	}
	
	/**
	 * Sets the Current Stock of the denomination
	 * @param text - current stock of the denomination
	 */
	public void setRestockChangeCurStockLabel(String text) {
		this.restockChangeCurStockLbl.setText(text);
	}
}