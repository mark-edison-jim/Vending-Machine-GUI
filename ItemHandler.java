import java.util.ArrayList;

import Items.BananaItem;
import Items.HaloHaloItem;
import Items.IceItem;
import Items.Item;
import Items.LankaItem;
import Items.LecheFlanItem;
import Items.MilkItem;
import Items.MunggoItem;
import Items.NataItem;
import Items.PinipigItem;
import Items.RedSagoItem;
import Items.SabaItem;
import Items.UbeHalayaItem;
import Items.UbeIceCreamItem;

public class ItemHandler {
    protected final int numItems = 13;
    protected ArrayList<ArrayList<Item>> itemArray; 
    protected ArrayList<Item> itemRecord;
    private final int numSpecItems = 12;

    public ItemHandler(){
        itemArray = new ArrayList<ArrayList<Item>>(numItems);
		itemRecord = new ArrayList<Item>(numItems);

		addItemArrayLists();
		createVendItems();
		copyItemsToRecord();
    }


    protected void copyItemsToRecord() {
		for(int i=0; i<numItems; i++)
			this.itemRecord.add(this.itemArray.get(i).get(0));
	}
	
	/**
	 * Adds the item types to the item array with an initial stock
	 */
	public void createVendItems() {
		for(int i=0; i<numItems; i++)
			for(int j=0; j<5; j++)
				this.itemArray.get(i).add(addNewItemType(i));
	}
	
	/**
	 * Creates a new instance of an item and adds to the item array
	 * @param index of item type
	 * @param n - number of times to be added
	 */
	public void addNewItemInstance(int index, int n) {
		for(int i=0; i<n; i++)
			itemArray.get(index).add(addNewItemType(index));
		updateItemsPrice(index, this.itemRecord.get(index).getPrice());
	}
	
	/**
	 * Returns the type of item to be created
	 * @param i
	 * @return the type of item
	 */
	protected Item addNewItemType(int i) {
		Item temp = null;
		switch(i) {
		case 0: temp = new BananaItem("Banana", 110, 75, 5);
			break;
		case 1: temp = new LankaItem("Langka", 150, 30, 5);
			break;
		case 2: temp = new LecheFlanItem("Leche Flan", 180, 45, 5);
			break;
		case 3: temp = new MunggoItem("Monggo Beans", 70, 80, 5);
			break;
		case 4: temp = new NataItem("Nata", 200, 35, 5);
			break;
		case 5: temp = new PinipigItem("Pinipig", 90, 75, 5);
			break;
		case 6: temp = new RedSagoItem("Red Sago", 65, 20, 5);
			break;
		case 7: temp = new SabaItem("Saba", 120, 60, 5);
			break;
		case 8: temp = new UbeHalayaItem("Ube Halaya", 80, 5, 5);
			break;
		case 9: temp = new UbeIceCreamItem("Ube Ice Cream", 120, 100, 5);
			break;
		case 10: temp = new IceItem("Ice", 0, 5, 5);
			break;
		case 11: temp = new MilkItem("Milk", 40, 30, 5);
			break;
		case 12: temp = new HaloHaloItem("Halo Halo", 260, 160, 5);
		}
		return temp;
	}
	
	/**
	 * Initializes ArrayLists of items for the item array
	 */
	protected void addItemArrayLists() {
		for(int i=0; i<numItems; i++) {
			this.itemArray.add(new ArrayList<Item>());
		}
	}
    public void proceedTransaction(int[] buyList)
	{
		for (int i = 0; i < buyList.length; i++) {
			for (int j = 0; j < buyList[i]; j++) {
				removeItem(i);
			}
		}
	}
    public String getCookingInstruction(int[] buyList, int totalPayment, int totalPrice)
	{
		String instruction = "";
		String toppings = "";
		boolean isHaloHalo;

		for (int i = 0; i < buyList.length; i++) {
			if (buyList[i] > 0) {
				toppings += String.format("%2d%-14s%5s\n",buyList[i], itemRecord.get(i).getName(), buyList[i] * itemRecord.get(i).getPrice());
			}
		}

		if(buyList[10] > 0 || buyList[11] > 0)
		{
			instruction += String.format("%20s\n", "Vending Receipt: ");
			instruction += "1 Custom Halo Halo\n";
			instruction += toppings;
			isHaloHalo = true;
		}else
		{
			instruction += String.format("%-20s\n", "Vending Receipt: ");
			instruction += "Toppings\n";
			instruction += toppings;
			isHaloHalo = false;
		}

		instruction += String.format("%-20s%5d\n", "Total Price: ", totalPrice);
		instruction += String.format("%-20s%5d\n", "Total Payment: ", totalPayment);
		instruction += String.format("%-15s%5d\n\n", "Total Change: ", totalPayment - totalPrice);

		if (isHaloHalo) {
			if (buyList[10] > 0) {
				instruction += String.format("%-30s\n","Shaving the " + itemRecord.get(10).getName());
			}
			if (buyList[11] > 0) {
				instruction += String.format("%-30s\n","Pouring the " + itemRecord.get(11).getName());
			}

			for (int i = 0; i < 10; i++) {
				if (buyList[i] > 0) {
					instruction += String.format("%-30s\n","Adding the " + itemRecord.get(i).getName());
				}
			}
		}else
		{
			for (int i = 0; i < 10; i++) {
				if (buyList[i] > 0) {
					instruction += String.format("%-30s\n","Dispensing the " + itemRecord.get(i).getName());
				}
			}
		}
		return instruction;
	}
    public boolean checkStandAlone(int[] buyList)
	{
		boolean valid = true;
		int hasStandAlone = 0;

		if (buyList[10] > 0 || buyList[11] > 0) {
			for (int i = 0; i < 10; i++) {
				if(buyList[i] > 0){
					hasStandAlone++;
				}
			}

			if (hasStandAlone > 0) {
				valid = true;
			}else{
				valid = false;
			}
		}
			
		return valid;
	}
	public int computeTotalPrice(int[] buyList)
	{
		int totalPrice = 0;
		for (int i = 0; i < buyList.length; i++) {
			totalPrice += (buyList[i] * getItemRecord().get(i).getPrice());
		}
		return totalPrice;
	}
	/**
	 * Returns a single ArrayList of items provided the index
	 * @param index of item
	 * @return item arrayList of specific type
	 */
	public ArrayList<Item> getSingleItemArrayList(int index){
		return itemArray.get(index);
	}
	
	/**
	 * Returns the entire item array holding all the types of items
	 * @return itemArray
	 */
	public ArrayList<ArrayList<Item>> getWholeItemArrayList(){
		return itemArray;
	}
	
	/**
	 * Returns the itemRecord holding an instance of each item
	 * @return itemRecord
	 */
	public ArrayList<Item> getItemRecord(){
		return itemRecord;
	}

    public boolean sufficientStock(int buyItem, int i)
	{
		boolean valid = true;
		if (buyItem > getWholeItemArrayList().get(i).size()-1) {
			valid = false;
		}
		return valid;
	}
    
    public String[] getStringedSpecItemRecord() {
		String[] temp = new String[numSpecItems];
		for(int i=0; i<numSpecItems; i++)
			temp[i] = itemRecord.get(i).getName();
		return temp;
	}

    public String[] getStringedItemRecord() {
		String[] temp = new String[numItems];
		for(int i=0; i<numItems; i++)
			temp[i] = itemRecord.get(i).getName();
		return temp;
	}

    public int getNumItems() {
		return this.numItems;
	}
	
	/**
	 * Checks to see if any item is at 0 stock
	 * @return boolean value whether any item has 0 stock or not
	 */
	public boolean checkItemStock() {
		boolean check = true;
		for(ArrayList<Item> items : this.itemArray) {
			if(items.size()==0)
				check=false;
		}
		return check;
	}
	
	/**
	 * Removes an item from the item array
	 * @param i - index of item to be removed
	 */
	public void removeItem(int i) {
		this.itemArray.get(i).remove(0);
	}
	
	/**
	 * Locates the index of an item
	 * @param name - name of an item
	 * @return index of the item to be located
	 */
	public int locateItem(String name) {
		int i;
		for(i=0; i<numItems; i++)
			if(name.equals(itemRecord.get(i).getName()))
				break;
		return i;
	}
	
	/**
	 * Updates the prices of an item
	 * @param index of item's prices to be updated
	 * @param newPrice
	 */
	public void updateItemsPrice(int index, int newPrice) {
		for(int i=0; i<this.itemArray.get(index).size(); i++) {
			itemArray.get(index).get(i).setPrice(newPrice);
		}
		itemRecord.get(index).setPrice(newPrice);
	}
}
