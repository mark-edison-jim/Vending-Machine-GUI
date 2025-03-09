import javax.swing.ButtonModel;

/*
 * This class acts as a parent controller for the regular and special VM controllers
 */
public class VendingMachineController {
	private MainMenu menu;
	private RegularView vendView;
	private SpecialGui specialVendView;
	// private SpecialModel specModel;
	private Maintenance maintenance;
	private RegularFacade regControl;
	private SpecialFacade specControl;
	private TransactionHandler transacHandler;
	private ItemHandler itemHandler;
	
	public VendingMachineController(MainMenu menu) {
		this.menu = menu;
	}
	public void createTypeBtnActions() {
		//if selected RadioButton is the regular VM
		ButtonModel selection = this.menu.getCreateRBGroup().getSelection();
		ButtonModel regular = this.menu.getRegularRB().getModel();
		ButtonModel special = this.menu.getSpecialButton().getModel();
		
		if(selection==regular) {
			//creates instances of the regular MVC and its maintenance
			this.deleteSpecialVend();
			this.menu.setVendSelection("Regular");
			this.menu.setVendExists(true);

			this.vendView = new RegularView();
			this.itemHandler = new ItemHandler();
			this.transacHandler = new TransactionHandler(vendView);
			this.maintenance = new Maintenance(transacHandler, itemHandler);

			regControl = new RegularFacade(menu, vendView, transacHandler, itemHandler, maintenance);

			System.out.println(this.menu.getVendSelection());
		}
		// if selected RadioButton is the special VM
		else if(selection==special) {
			//creates instances of the special MVC and its maintenance
			this.deleteRegularVend();
			this.menu.setVendSelection("Special");
			this.menu.setVendExists(true);
			
			this.specialVendView = new SpecialGui();
			this.itemHandler = new ItemHandler();
			this.transacHandler = new TransactionHandler(specialVendView);
			this.maintenance = new Maintenance(transacHandler, itemHandler);

			this.specControl = new SpecialFacade(menu, specialVendView, itemHandler, transacHandler, maintenance);

			System.out.println(this.menu.getVendSelection());
		}
			
		else if(this.menu.getCreateRBGroup().getSelection()==null)
			System.out.println("Please Choose an Option");
		System.err.println(itemHandler + " and " + transacHandler);
	}
	
	public void confirmTestBtnActions() {
		//Decide on whether to allow each type to coexist or be mutually exclusive
		if(this.menu.getVendExists()) {
			//if the regular VM is selected the user decides to run its features
			if(this.menu.getTestRBGroup().getSelection()==this.menu.getFeaturesRB().getModel() && this.menu.getCreateRBGroup().getSelection()==this.menu.getRegularRB().getModel()) {
				if(this.menu.getVendSelection().equals("Special")){
					System.out.println("You have not created a Regular Vending Machine yet");
				}
				else{
					//hides main menu, initializes item labels, and reveals the RVM Gui
					this.menu.setTestSelection("Regular Features");
					System.out.println(this.menu.getTestSelection());
					this.menu.hideMainMenu();
					this.vendView.initiateItemLabels(this.itemHandler.getWholeItemArrayList(), this.itemHandler.getItemRecord());
					this.regControl.revealRegVend();
				}
			}
			//if the regular VM is selected and the user decides to run its maintenance
			else if(this.menu.getTestRBGroup().getSelection()==this.menu.getMainTRB().getModel() &&  this.menu.getCreateRBGroup().getSelection()==this.menu.getRegularRB().getModel()) {
				if(this.menu.getVendSelection().equals("Special")){
					System.out.println("You have not created a Regular Vending Machine yet");
				}
				else{
					this.menu.setTestSelection("Regular Maintenance");
					System.out.println(this.menu.getTestSelection());
					this.maintenance.setRestockItemsCurStockLabel(String.format("Current Stock: %d", itemHandler.getSingleItemArrayList(maintenance.getRestockItemsCBSelectedIndex()).size()));
					this.maintenance.setChangePriceCurPriceLabel(String.format("Current Price: $%d", itemHandler.getItemRecord().get(maintenance.getChangePriceCBSelectedIndex()).getPrice()));
					this.maintenance.setRestockChangeCurStockLabel(String.format("Current Stock: %d", transacHandler.getChangeStock()[maintenance.getRestockChangeCBSelectedIndex()]));
					this.menu.hideMainMenu();
					this.regControl.revealMaintenance();
				}
			}
			//if the special VM is selected the user decides to run its features
			else if(this.menu.getTestRBGroup().getSelection()==this.menu.getFeaturesRB().getModel() && this.menu.getCreateRBGroup().getSelection()==this.menu.getSpecialButton().getModel()) {
				if(this.menu.getVendSelection().equals("Regular")){
					System.out.println("You have not created a Special Vending Machine yet");
				}
				else{
					this.menu.hideMainMenu();
					this.specialVendView.showSpecialGUI();
					this.menu.setTestSelection("Special Features");
					//specControl.initiateMaintenanceActionListeners();
					System.out.println(this.menu.getTestSelection());
				}
			}
				//if the special VM is selected and the user decides to run its maintenance	
			else if(this.menu.getTestRBGroup().getSelection()==this.menu.getMainTRB().getModel() &&  this.menu.getCreateRBGroup().getSelection()==this.menu.getSpecialButton().getModel()) {
				if(this.menu.getVendSelection().equals("Regular")){
					System.out.println("You have not created a Special Vending Machine yet");
				}
				else{
					this.maintenance.setRestockItemsCurStockLabel(String.format("Current Stock: %d", itemHandler.getSingleItemArrayList(maintenance.getRestockItemsCBSelectedIndex()).size()));
					this.maintenance.setChangePriceCurPriceLabel(String.format("Current Price: $%d", itemHandler.getItemRecord().get(maintenance.getChangePriceCBSelectedIndex()).getPrice()));
					this.maintenance.setRestockChangeCurStockLabel(String.format("Current Stock: %d", transacHandler.getChangeStock()[maintenance.getRestockChangeCBSelectedIndex()]));
					this.menu.hideMainMenu();
					this.specControl.revealMaintenance();
					this.menu.setTestSelection("Special Maintenance");
					System.out.println(this.menu.getTestSelection());
				}
			}

			else if(this.menu.getTestRBGroup().getSelection()==null)
				System.out.println("Please Choose an Option");
		}
		else System.out.println("Please Create a Vending Machine");
	}
	
	private void deleteRegularVend(){
		this.vendView = null;
		this.itemHandler = null;
		this.transacHandler= null;
		this.regControl = null;
	}

	private void deleteSpecialVend(){
		this.specialVendView = null;
		this.itemHandler = null;
		this.transacHandler= null;
		this.specControl = null;
	}
}
