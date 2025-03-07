import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/*
 * This class acts as a parent controller for the regular and special VM controllers
 */
public class VMFacade {
	private MainMenu menu;
	private VendingMachineController vendingMachineController;

	/*
	 * VendingMachineController constructor instantiating the main meny as well as its actions
	 */
	public VMFacade() {
		this.menu = new MainMenu();
		this.vendingMachineController = new VendingMachineController(menu);
		assignAllBtnActions();
	}
	private void assignAllBtnActions(){
		this.menu.setCreateTypeConfirmBtnActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				vendingMachineController.createTypeBtnActions();
			}
		});
		
		this.menu.setConfirmTestBtnActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				vendingMachineController.confirmTestBtnActions();
			}
		});
	}

}
