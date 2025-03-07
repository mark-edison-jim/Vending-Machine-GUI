import java.util.ArrayList;

import Items.Item;

public class TransactionHandler {
    protected int[] changeDenominations = {100, 50, 20, 10, 5, 1};
	protected int[] changeStock;
	protected int[] tempChangeStock;
	protected ArrayList<Transactions> transactions;
	protected int earnings;
    protected RegularView vendView;
    private ArrayList<TransactionList> transactionList;
    int payment[];

    public TransactionHandler(RegularView vendView){
        changeStock = new int[6];
		tempChangeStock = new int[6];
		//tempNewHoldingChangeStock = new int[6];
		transactions = new ArrayList<Transactions>();
		earnings = 0;
        this.vendView = vendView;
        initializeChangeStock();
    }

    public TransactionHandler(SpecialGui vendView){
		changeStock = new int[6];
		tempChangeStock = new int[6];
		transactions = new ArrayList<Transactions>();
		earnings = 0;
        this.vendView = vendView;
        initializeChangeStock();
        this.transactionList = new ArrayList<TransactionList>();
		this.payment = new int[6];
		resetPayment();
    }

    protected void initializeChangeStock() {
		for(int i=0; i<changeStock.length; i++) {
			changeStock[i]=5;
		}
	}
	public void updatePayment(int i)
	{
		this.payment[i] += 1;
		for (int j = 0; j < 6; j++) {
			System.out.println(this.payment[j]);
		}
	}
    public int getPayment(int i)
	{
		return this.payment[i];
	}
	/**
	 * This resets the cash inserted in the machine
	 */
	public void resetPayment()
	{
		for (int i = 0; i < 6; i++) {
			this.payment[i] = 0;
		}
	}
	/**
	 * Increments the stock of a denomination
	 * @param index of stock of change to increment
	 */
	public void addChangeStock(int index) {
		changeStock[index]++;
		//tempNewHoldingChangeStock[index]++;
	}
    public ArrayList<TransactionList> getTransactionList() {
		return transactionList;
	}
    public void saveTransaction(int[] buyList, int timesRestocked, ItemHandler itemHandler)
	{
		ArrayList<Item> temp = new ArrayList<Item>();
		
		for (int i = 0; i < buyList.length; i++) {
			for (int index = 0; index < buyList[i]; index++) {
				temp.add(itemHandler.itemRecord.get(i));
				//transactionList.get(i).add(new Transactions(itemRecord.get(i), timesRestocked));
			}
		}
		transactionList.add(new TransactionList(temp, timesRestocked));
	}
	public boolean checkChange(int userTotalCash, int totalPrice)
	{
		int excess = userTotalCash - totalPrice; //temporary user's change
        boolean valid = true;
        
		for(int i=0; i<6; i++)
            System.out.println(changeDenominations[i] + " " + changeStock[i]);

        for(int i=0; i<6; i++)
            tempChangeStock[i]=changeStock[i];
        
        if(excess > 0){
            for(int i = 0; i < 6; i++){
                //subtracts available change of vending machine from user's change
                while(excess>=changeDenominations[i] && tempChangeStock[i]>0 && excess>0){
                    excess -= changeDenominations[i];
                    tempChangeStock[i]-=1;
                }
            }
        }
        //if there is still change after all the machine runs out of change, transaction will not proceed
        if(excess > 0){
            valid = false;
            System.out.println("No Change");
        }else
		{
			updateChangeStock();
			for (int index = 0; index < 6; index++) {
				System.out.println(changeDenominations[index] + " " + changeStock[index]);
			}
		}
        
        return valid;
	}
	/**
	 * Testing purposes
	 */
	public void displayChangeStock() {
		for(int i=0; i<changeStock.length; i++)
			System.out.println(String.format("%d: %d", changeDenominations[i], changeStock[i]));
	}

    public ArrayList<Transactions> getTransactions(){
		return this.transactions;
	}
	
	/**
	 * Adds a new Transaction
	 * @param item
	 * @param timesRestocked - number of times restocked
	 */
	public void addTransaction(Item item, int timesRestocked) {
		this.transactions.add(new Transactions(item, timesRestocked));
	}

    public String[] getStringedChangeDenominations() {
		String[] temp = new String[changeDenominations.length];
		for(int i=0; i<changeDenominations.length; i++)
			temp[i] = String.valueOf(changeDenominations[i]);
		return temp;
	}
	
	/**
	 * Returns the stock of change
	 * @return changeStock
	 */
	public int[] getChangeStock() {
		return this.changeStock;
	}

    public boolean checkChange(int userTotalCash, int itemIndex, ItemHandler itemHandler) {
		int excess = userTotalCash - itemHandler.getItemRecord().get(itemIndex).getPrice(); //temporary user's change
        boolean valid = true;
        
        //copies change stock for temporary usage
        for(int i=0; i<changeStock.length; i++)
            tempChangeStock[i]=this.changeStock[i];
        
        //subtracts all denominations from highest to lowest until excess is 0
        if(excess > 0){
            for(int i = 0; i < changeStock.length; i++){
                //subtracts available change of RVM from user's change
                while(excess>=changeDenominations[i] && tempChangeStock[i]>0 && excess>0){
                    excess -= changeDenominations[i];
                    tempChangeStock[i]-=1;
                }
            }
        }
        //if there is still change after all the machine runs out of change, transaction will not proceed
        if(excess > 0){
            valid = false;
            System.out.println("No Change...");
        }
        
        return valid;
	}

	public String printReceipt(ItemHandler itemHandler, Maintenance maintenance, Boolean isSpecial)
    {
        String text = "";
        boolean isCurrentTransaction = false;
        int totalSales[] = new int[12];
        int endStock[] = new int[12];
        for(int i=0; i<12; i++) {
        	totalSales[i]=0;
        	endStock[i]=0;
        }
        
        for (int i = 0; i < this.getTransactionList().size(); i++) 
        {
            isCurrentTransaction = false;
            if (this.getTransactionList().get(i).getTimesRestocked() == maintenance.getTimesRestocked()) {
                isCurrentTransaction = true;
            }

            if (isCurrentTransaction) {
                int k = 0;
                for (int j = 0; j < this.getTransactionList().get(i).getItem().size(); j++) {
                    while(!this.getTransactionList().get(i).getItem().get(j).getName().equals(itemHandler.getItemRecord().get(k).getName()))
                    {
                        k++;
                    }
                    totalSales[k]++;
                    
                }
            }
            
        }
        //formatted receipt
        for (int i = 0; i < 12; i++) {
            endStock[i] = itemHandler.getItemRecord().get(i).getStartingStock() - totalSales[i];
        }
        text += String.format("%22s\n", "Vending Receipt:");
        text += "S-StartingStock\nE-EndingStock\nT-TotalSales\nR-TotalRestocked\n";
        text += String.format("%-16s %-2s %-2s %-2s %-2s\n", "Name:", "S", "E", "T", "R");
        for(int i = 0; i < 12; i++)
        {
        	text += String.format("%-16s %-2d %-2d %-2d %-2d\n", itemHandler.getItemRecord().get(i).getName(),
            		itemHandler.getItemRecord().get(i).getStartingStock(),endStock[i], totalSales[i], itemHandler.getItemRecord().get(i).getTotalRestocked());
        }
        System.out.println(text);
        return text;
    }
	
    public String printReceipt(ItemHandler itemHandler, Maintenance maintenance)
    {
		String text = "";
        boolean isCurrentTransaction = false;
        int numItems = itemHandler.getNumItems();
        int totalSales[] = new int[numItems];
        int endStock[] = new int[numItems];
        for(int i=0; i<numItems; i++) {
        	totalSales[i]=0;
        	endStock[i]=0;
        }
        
        for (int i = 0; i < getTransactions().size(); i++) 
        {
            //System.out.println(transactions.get(i).getItem().getName()); //Testing
            isCurrentTransaction = false;
            if (getTransactions().get(i).getTimesRestocked() == maintenance.getTimesRestocked()) {
                isCurrentTransaction = true;
            }
            if (isCurrentTransaction == true) {
                int k = 0;
                while(!this.getTransactions().get(i).getItem().getName().equals(itemHandler.getItemRecord().get(k).getName())){
                    k++;
                }
                if(getTransactions().get(i).getItem().getName().equals(itemHandler.getItemRecord().get(k).getName())){
                        totalSales[k]++;
                } 
            }
        }
        
        //formatted receipt
        for (int i = 0; i < numItems; i++) {
            endStock[i] = itemHandler.getItemRecord().get(i).getStartingStock() - totalSales[i];
        }
        text += String.format("%22s\n", "Vending Receipt:");
        text += "S-StartingStock\nE-EndingStock\nT-TotalSales\nR-TotalRestocked\n";
        text += String.format("%-16s %-2s %-2s %-2s %-2s\n", "Name:", "S", "E", "T", "R");
        //System.out.println("S-StartingStock\nE-EndingStock\nT-TotalSales\nR-TotalRestocked");
        //System.out.println(String.format("%-16s %-2s %-2s %-2s %-2s", "Name:", "S", "E", "T", "R"));
        for(int i = 0; i < numItems; i++)
        {
        	text += String.format("%-16s %-2d %-2d %-2d %-2d\n", itemHandler.getItemRecord().get(i).getName(),
            		itemHandler.getItemRecord().get(i).getStartingStock(),endStock[i], totalSales[i], itemHandler.getItemRecord().get(i).getTotalRestocked());
            //System.out.println(String.format("%-16s %-2d %-2d %-2d %-2d", this.vendModel.getItemRecord().get(i).getName(),
            //		this.vendModel.getItemRecord().get(i).getStartingStock(),endStock[i], totalSales[i], this.vendModel.getItemRecord().get(i).getTotalRestocked()));
        }
        System.out.println(text);
        return text;
    }

	/**
	 * Updates the changeStock if a transaction is made
	 */
	public void updateChangeStock() {
		for(int i=0; i<changeStock.length; i++) 
        	this.changeStock[i]=tempChangeStock[i];
	}
	
	/**
	 * Adds the earnings based on item bought
	 * @param itemIndex	
	 */
	public void addEarnings(int itemIndex, ItemHandler itemHandler) {
		this.earnings += itemHandler.getItemRecord().get(itemIndex).getPrice();
	}
	
	/**
	 * Adds the earnings based on amount provided
	 * @param money - to be added
	 */
	public void addDirectEarnings(int amount) {
		this.earnings += amount;
	}
	
	/**
	 * Collects and resets current earnings
	 * @return total
	 */
	public int collectEarnings() {
		int total = this.earnings;
		this.earnings=0;
				
		return total;
	}

    public void setChangeStockIndex(int i, int newChangeStock) {
		this.changeStock[i] = newChangeStock;
	}

    public boolean checkValidTransac(int i, ItemHandler itemHandler) {
		boolean valid = true;
		
		valid = checkChange(vendView.getTotalCashInserted(), i, itemHandler);
		if(!itemHandler.checkItemStock() && i==12) {
			System.out.println("Not enough ingredients...");
			valid = false;
		}
		
		if(itemHandler.getSingleItemArrayList(i).get(0).getPrice()>vendView.getTotalCashInserted() && valid) {
			System.out.println("You do not have enough money for that...");
			valid = false;
		}
		return valid;
	}

}
