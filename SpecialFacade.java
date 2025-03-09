import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * This class is the controller for the special vending machine
 */
public class SpecialFacade {
    private MainMenu menu;
    private SpecialGui specialGui;
    // private SpecialModel specialModel;
    private ItemHandler itemHandler;
    private TransactionHandler transactionHandler;
	private Maintenance maintenance;

    private int totalUserMoney;
    private int totalPrice;

    /**
     * This is the constructor for special vending machine controller
     * @param menu Main menu view
     * @param specialGui Special vending machine view
     * @param specialModel Special vending machine model
     * @param maintenance Maintenance view
     */
    public SpecialFacade(MainMenu menu, SpecialGui specialGui, ItemHandler itemHandler, TransactionHandler transactionHandler, Maintenance maintenance) {
        this.menu = menu;
        this.specialGui = specialGui;
        this.itemHandler = itemHandler;
        this.transactionHandler = transactionHandler;
        this.maintenance = maintenance;

        //adds all buttons visible in the buy menu
        setBuyButton();
        maintenance.initiateSpecialMaintenanceActionListeners(specialGui, menu, itemHandler, transactionHandler, maintenance);
    }
    /**
     * This initiates action listener for the + buttons in special vending machine view
     * @param i index of the button in the button arraylist
     * @return action listener of specific + button
     */
    private ActionListener addToBuyList(int i)
    {
        ActionListener action = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(itemHandler.sufficientStock(specialGui.getBuyList(i), i)) {
                    specialGui.updateBuyList(i, specialGui.buyList[i]+1);
                    specialGui.updateItemLabel(i, specialGui.buyList[i]);
                    specialGui.setTotalPrice(itemHandler.computeTotalPrice(specialGui.buyList));
                    
                    totalPrice = itemHandler.computeTotalPrice(specialGui.buyList);
                    //checking
                    System.out.println(i + "= " + specialGui.buyList[i]);
                }else{
                    System.out.println("Over the current stock");
                }
            }
        };
        return action;
    }

    /**
     * This initiates action listener for the - buttons in special vending machine view
     * @param i index of the button in the arraylist
     * @return action listener of specific - button
     */
    private ActionListener removeToBuyList(int i)
    {
        ActionListener action = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (specialGui.buyList[i] > 0) {
                    specialGui.updateBuyList(i, specialGui.buyList[i]-1);
                    specialGui.updateItemLabel(i, specialGui.buyList[i]);
                    specialGui.setTotalPrice(itemHandler.computeTotalPrice(specialGui.buyList));
                    
                    totalPrice = itemHandler.computeTotalPrice(specialGui.buyList);
                    //checking
                    System.out.println(i + "= " + specialGui.buyList[i]);
                }else{
                    System.out.println("invalid button click");
                }
            }
        };
        return action;
    }

    /**
     * This initiates action listeners for the cash buttons in the special vending machine view
     * @param i index of the cash button in the arraylist
     * @return action listener for the specific cash button
     */
    private ActionListener insertCash(int i)
    {
        ActionListener action = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                transactionHandler.updatePayment(i);

                totalUserMoney = 0;
                for (int j = 0; j < 6; j++) {
                    totalUserMoney += (transactionHandler.getPayment(j) * transactionHandler.changeDenominations[j]);
                }
                specialGui.setTotalPayment(totalUserMoney);
            }
        };
        return action;
    }

    /**
     * This initiates action listeners for the proceed button
     * @return action listener for proceed button
     */
    private ActionListener proceedTransaction()
    {
        ActionListener action = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                boolean valid = true;
                
                System.out.println(totalUserMoney + " " + totalPrice);
                if (totalUserMoney < totalPrice) 
                {
                    valid = false;
                    System.out.println("Insufficient Cash");
                    specialGui.setIdleText("Insufficient Cash");
                }else if(!transactionHandler.checkChange(totalUserMoney, totalPrice))
                {
                    valid = false;
                    System.out.println("Insufficient Change");
                    specialGui.setIdleText("Insufficeint Change Please try other bills");
                }else if(!itemHandler.checkStandAlone(specialGui.buyList))
                {
                    valid = false;
                    System.out.println("No stand alone items included");
                    specialGui.setIdleText("Transaction needs toppings");
                }

                if (valid) {
                    for (int i = 0; i < 12; i++) {
                        for (int j = 0; j < specialGui.buyList[i]; j++) {
                            itemHandler.removeItem(i);//removes item one by one
                        }
                    }

                    specialGui.setIdleText(itemHandler.getCookingInstruction(specialGui.buyList, totalUserMoney, totalPrice));
                    transactionHandler.saveTransaction(specialGui.buyList, maintenance.getTimesRestocked(), itemHandler);
                    transactionHandler.addDirectEarnings(totalPrice);
                 
                    for (int i = 0; i < 12; i++) {
                        System.out.println(itemHandler.getItemRecord().get(i).getName() + itemHandler.getWholeItemArrayList().get(i).size());
                    }
                }
                
                //resets the values for transaction
                totalPrice = 0;
                resetTransacValues();
            }
        };
        return action;
    }


    private void resetTransacValues(){
        specialGui.resetBuyList();//resets basket
        specialGui.resetItemLabel();//resets Gui basket
        specialGui.setTotalPayment(0);//reset label
        transactionHandler.resetPayment();//reset model payment
        specialGui.setTotalPrice(0);//reset label
        totalUserMoney = 0;
        specialGui.updateInfoLabel(itemHandler);
    }
    /**
     * This sets action listeners to all the buttons and Jlabels related to special vending machine
     */
    private void setBuyButton()
    {
        specialGui.updateInfoLabel(itemHandler);
        // Set action listeners for plus buttons
        for (int i = 0; i < 12; i++) {
            specialGui.setPButtonActionListener(i, addToBuyList(i));
        }

        // Set action listeners for minus buttons
        for (int i = 0; i < 12; i++) {
            specialGui.setMButtonActionListener(i, removeToBuyList(i));
        }

        for (int i = 0; i < 6; i++) {
            specialGui.setCashButtonListener(i, insertCash(i));
        }
        
        specialGui.setBackButtonListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                specialGui.hideSpecialGUI();
                resetTransacValues();
                menu.revealMainMenu();
            }

        });

        specialGui.setProceedButtonListener(proceedTransaction());
        
    }

    /**
     * This sets the maintenance frame visible
     */
    public void revealMaintenance() {
		this.maintenance.getMTFrame().setVisible(true);
	}

    /**
     * This hides the maintenance frame
     */
    public void hideMaintenance()
    {
        this.maintenance.getMTFrame().setVisible(false);
    }
}