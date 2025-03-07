import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegularFacade {
    private TransactionHandler transacHandler;
    private ItemHandler itemHandler;
    private MainMenu menu;
	private RegularView vendView;
	private Maintenance maintenance;

    public RegularFacade(MainMenu menu, RegularView vendView, TransactionHandler transacHandler, ItemHandler itemHandler, Maintenance maintenance){
        this.menu = menu;
		this.vendView = vendView;
		this.transacHandler = transacHandler;
		this.itemHandler = itemHandler;
		this.maintenance = maintenance;
        
        this.initateRegularVendActions();
    }

    private ActionListener doItemActions(int i) {
		ActionListener al = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(transacHandler.checkValidTransac(i, itemHandler)) {
					vendView.assignItemBtnFunctions(itemHandler, transacHandler, maintenance, i);
				}
				else{
					System.out.println("Invalid Transaction");
				}
				transacHandler.displayChangeStock();
			}
		};
		return al;
	}

	private void initateRegularVendActions(){
		maintenance.initiateRegMaintenanceActionListeners(vendView, menu, itemHandler, transacHandler, maintenance);
        vendView.initiateRegVendViewActionListeners(menu, transacHandler, itemHandler);
		
		for(int i=0; i<itemHandler.getNumItems(); i++){
			if(i!=10 && i!=11)
				this.vendView.setAddButtonActionListener(i, doItemActions(i));
		}
		
		for(int i=0; i<6; i++) {
			this.vendView.setDenominationsBtnActionListener(i, doDenominationActions(i));
		}
	}
	
    private ActionListener doDenominationActions(int i) {
		ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				vendView.setTotalCashInserted(vendView.getTotalCashInserted() + Integer.parseInt(vendView.getDenominationButtons()[i].getText()));
				//vendModel.addChangeStock(i);
				vendView.displayAmountLabelText();
			}
		};
		return al;
	}

    public void revealMaintenance() {
		this.maintenance.getMTFrame().setVisible(true);
	}

    public void revealRegVend() {
		this.vendView.getRegVendFrame().setVisible(true);
	}
}