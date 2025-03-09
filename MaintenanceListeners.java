import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Timer;

public class MaintenanceListeners {
    private Maintenance maintenance;

    public MaintenanceListeners(Maintenance maintenance) {
        this.maintenance = maintenance;
    }

    public void initiateRegMaintenanceActionListeners(RegularView vendView, MainMenu menu, ItemHandler itemHandler,
            TransactionHandler transactionHandler, Maintenance maintenance) {
        this.setMaintenanceBackBtnActionListenener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maintenance.hideMaintenance();
                menu.revealMainMenu();
            }
        });

        setRestockItemsConfirmBtnActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transactionHandler.printReceipt(itemHandler, maintenance);
                int index = maintenance.getRestockItemsCBSelectedIndex();
                if (index == 12) {
                    for (int i = 0; i < itemHandler.getNumItems(); i++) {
                        itemHandler.addNewItemInstance(i, maintenance.getRestockItemCount());
                        itemHandler.getItemRecord().get(i).addTotalRestock(maintenance.getRestockItemCount());
                        if (i != 10 && i != 11) {
                            if (itemHandler.getSingleItemArrayList(i).size() > 0)
                                vendView.enableBtn(i);
                        }
                        maintenance.setRestockItemsCurStockLabel(
                                String.format("Current Stock: %d", itemHandler.getSingleItemArrayList(i).size()));
                    }
                } else {
                    itemHandler.addNewItemInstance(index, maintenance.getRestockItemCount());
                    itemHandler.getItemRecord().get(index).addTotalRestock(maintenance.getRestockItemCount());
                    if (index != 10 && index != 11)
                        if (itemHandler.getSingleItemArrayList(index).size() > 0)
                            vendView.enableBtn(index);
                    maintenance.setRestockItemsCurStockLabel(
                            String.format("Current Stock: %d", itemHandler.getSingleItemArrayList(index).size()));
                }
                for (int k = 0; k < itemHandler.getNumItems(); k++) {
                    itemHandler.getItemRecord().get(k).setStartingStock(itemHandler.getSingleItemArrayList(k).size());
                }
                maintenance.incrementTimesRestocked();
                maintenance.resetRestockItemsCounterDisplay();
                System.out.println(itemHandler.getSingleItemArrayList(index));
            }
        });

        setRestockItemsCBActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maintenance.setRestockItemsCurStockLabel(String.format("Current Stock: %d",
                        itemHandler.getSingleItemArrayList(maintenance.getRestockItemsCBSelectedIndex()).size()));
            }
        });

        setTFKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                // String value = getChangePriceTF().getText();
                if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9' || ke.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                    maintenance.getChangePriceTF().setEditable(true);
                } else {
                    maintenance.getChangePriceTF().setEditable(false);
                    System.out.println("* Enter only numeric digits(0-9)");
                }
            }
        });

        setChangePriceConfirmBtnActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                itemHandler.updateItemsPrice(maintenance.getChangePriceCBSelectedIndex(),
                        Integer.parseInt(maintenance.getChangePriceTF().getText()));
                maintenance.resetChangePriceTextField();
                maintenance.setChangePriceCurPriceLabel(String.format("Current Price: $%d",
                        itemHandler.getItemRecord().get(maintenance.getChangePriceCBSelectedIndex()).getPrice()));
                System.out.println(maintenance.getChangePriceCBSelectedIndex());
                System.out.println(maintenance.getChangePriceTF().getText());
            }
        });

        setChangePriceCBActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maintenance.setChangePriceCurPriceLabel(String.format("Current Price: $%d",
                        itemHandler.getItemRecord().get(maintenance.getChangePriceCBSelectedIndex()).getPrice()));
            }
        });

        setRestockChangeConfirmBtnActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = maintenance.getRestockChangeCBSelectedIndex();
                transactionHandler.setChangeStockIndex(index,
                        transactionHandler.getChangeStock()[index] + maintenance.getRestockChangeCount());
                        maintenance.resetRestockChangeCounterDisplay();
                        maintenance.setRestockChangeCurStockLabel(String.format("Current Stock: %d",
                        transactionHandler.getChangeStock()[maintenance.getRestockChangeCBSelectedIndex()]));
            }
        });

        setRestockChangeCBActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maintenance.setRestockChangeCurStockLabel(String.format("Current Stock: %d",
                        transactionHandler.getChangeStock()[maintenance.getRestockChangeCBSelectedIndex()]));
            }
        });

        setCollectEarningsBtnActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maintenance.setCollectEarningsText(String.format("$%d was collected", transactionHandler.collectEarnings()));
                ActionListener listener = new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        maintenance.setCollectEarningsText("");
                    }
                };
                Timer timer = new Timer(2000, listener);
                timer.setRepeats(false);
                timer.start();
            }
        });

        setPrintRecieptBtnActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maintenance.showReceiptArea();
                maintenance.lockMaintenance();
                maintenance.setReceiptTextArea(transactionHandler.printReceipt(itemHandler, maintenance));
            }
        });

        setReceiptBackBtnActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maintenance.hideReceiptArea();
                maintenance.unlockMaintenance();
                maintenance.setReceiptTextArea("");
            }
        });
    }

    public void initiateSpecialMaintenanceActionListeners(SpecialGui vendView, MainMenu menu, ItemHandler itemHandler,
            TransactionHandler transactionHandler, Maintenance maintenance) {
        this.setMaintenanceBackBtnActionListenener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maintenance.hideMaintenance();
                vendView.updateInfoLabel(itemHandler);
                menu.revealMainMenu();
            }
        });

        this.setRestockItemsConfirmBtnActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transactionHandler.printReceipt(itemHandler, maintenance, maintenance.isSpecial);
                int index = maintenance.getRestockItemsCBSelectedIndex();

                itemHandler.addNewItemInstance(index, maintenance.getRestockItemCount());
                itemHandler.getItemRecord().get(index).addTotalRestock(maintenance.getRestockItemCount());
                maintenance.setRestockItemsCurStockLabel(
                        String.format("Current Stock: %d", itemHandler.getSingleItemArrayList(index).size()));

                for (int k = 0; k < 12; k++) {
                    itemHandler.getItemRecord().get(k).setStartingStock(itemHandler.getSingleItemArrayList(k).size());
                }
                maintenance.incrementTimesRestocked();
                maintenance.resetRestockItemsCounterDisplay();
                System.out.println(itemHandler.getSingleItemArrayList(index));
            }
        });

        this.setRestockItemsCBActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maintenance.setRestockItemsCurStockLabel(String.format("Current Stock: %d",
                        itemHandler.getSingleItemArrayList(maintenance.getRestockItemsCBSelectedIndex()).size()));
            }
        });

        this.setTFKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                // String value = maintenance.getChangePriceTF().getText();
                if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9' || ke.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                    maintenance.getChangePriceTF().setEditable(true);
                } else {
                    maintenance.getChangePriceTF().setEditable(false);
                    System.out.println("* Enter only numeric digits(0-9)");
                }
            }
        });

        this.setChangePriceConfirmBtnActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                itemHandler.updateItemsPrice(maintenance.getChangePriceCBSelectedIndex(),
                        Integer.parseInt(maintenance.getChangePriceTF().getText()));
                maintenance.resetChangePriceTextField();
                maintenance.setChangePriceCurPriceLabel(String.format("Current Price: $%d",
                        itemHandler.getItemRecord().get(maintenance.getChangePriceCBSelectedIndex()).getPrice()));
                System.out.println(maintenance.getChangePriceCBSelectedIndex());
                System.out.println(maintenance.getChangePriceTF().getText());

            }
        });

        this.setChangePriceCBActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maintenance.setChangePriceCurPriceLabel(String.format("Current Price: $%d",
                        itemHandler.getItemRecord().get(maintenance.getChangePriceCBSelectedIndex()).getPrice()));
            }
        });

        this.setRestockChangeConfirmBtnActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = maintenance.getRestockChangeCBSelectedIndex();
                transactionHandler.setChangeStockIndex(index,
                        transactionHandler.getChangeStock()[index] + maintenance.getRestockChangeCount());
                maintenance.resetRestockChangeCounterDisplay();
                maintenance.setRestockChangeCurStockLabel(String.format("Current Stock: %d",
                        transactionHandler.getChangeStock()[maintenance.getRestockChangeCBSelectedIndex()]));
            }
        });

        this.setRestockChangeCBActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maintenance.setRestockChangeCurStockLabel(String.format("Current Stock: %d",
                        transactionHandler.getChangeStock()[maintenance.getRestockChangeCBSelectedIndex()]));
            }
        });

        this.setCollectEarningsBtnActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maintenance.setCollectEarningsText(
                        String.format("$%d was collected", transactionHandler.collectEarnings()));
                ActionListener listener = new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        maintenance.setCollectEarningsText("");
                    }
                };
                Timer timer = new Timer(2000, listener);
                timer.setRepeats(false);
                timer.start();
            }
        });

        this.setPrintRecieptBtnActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maintenance.showReceiptArea();
                maintenance.lockMaintenance();
                maintenance.setReceiptTextArea(transactionHandler.printReceipt(itemHandler, maintenance, maintenance.isSpecial));
            }
        });

        this.setReceiptBackBtnActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maintenance.hideReceiptArea();
                maintenance.unlockMaintenance();
                maintenance.setReceiptTextArea("");
            }
        });
    }

    /**
     * Assigns an action to the button for returning to the Main Menu
     * 
     * @param actionListener
     */

    public void setMaintenanceBackBtnActionListenener(ActionListener actionListener) {
        maintenance.backButton.addActionListener(actionListener);
    }

    /**
     * Assigns an action/s to the ComboBox of the restocking of items
     * 
     * @param actionListener
     */
    public void setRestockItemsCBActionListener(ActionListener actionListener) {
        maintenance.restockItemsCB.addActionListener(actionListener);
    }

    /**
     * Assigns an action to the confirming of restocking an item button
     * 
     * @param actionListener
     */
    public void setRestockItemsConfirmBtnActionListener(ActionListener actionListener) {
        maintenance.restockItemsConfirm.addActionListener(actionListener);
    }

    /**
     * Assigns an action to the ComboBox of changing the price of an item
     * 
     * @param actionListener
     */
    public void setChangePriceCBActionListener(ActionListener actionListener) {
        maintenance.changePriceCB.addActionListener(actionListener);
    }

    /**
     * Assigns an action to the confirming of changing the price of an item button
     * 
     * @param actionListener
     */
    public void setChangePriceConfirmBtnActionListener(ActionListener actionListener) {
        maintenance.changePriceConfirm.addActionListener(actionListener);
    }

    /**
     * Assigns an action to the ComboBox of changing the stock of change
     * 
     * @param actionListener
     */
    public void setRestockChangeCBActionListener(ActionListener actionListener) {
        maintenance.restockChangeCB.addActionListener(actionListener);
    }

    /**
     * Assigns an action to the confirming of changing the stock of change button
     * 
     * @param actionListener
     */
    public void setRestockChangeConfirmBtnActionListener(ActionListener actionListener) {
        maintenance.restockChangeConfirm.addActionListener(actionListener);
    }

    /**
     * Assigns an action to the button to collect earnings
     * 
     * @param actionListener
     */
    public void setCollectEarningsBtnActionListener(ActionListener actionListener) {
        maintenance.collectEarningsBtn.addActionListener(actionListener);
    }

    /**
     * Assigns an action to the button to print the receipt
     * 
     * @param actionListener
     */
    public void setPrintRecieptBtnActionListener(ActionListener actionListener) {
        maintenance.printRecieptBtn.addActionListener(actionListener);
    }

    /**
     * Assigns an action to the text field of the change price
     * 
     * @param keyAdapter
     */
    public void setTFKeyListener(KeyAdapter keyAdapter) {
        maintenance.changePriceTF.addKeyListener(keyAdapter);
    }

    /**
     * Assigns an action to the button for returning to the Maintenance after
     * printing the receipt
     * 
     * @param actionListener
     */
    public void setReceiptBackBtnActionListener(ActionListener actionListener) {
        maintenance.receiptBackBtn.addActionListener(actionListener);
    }

}
