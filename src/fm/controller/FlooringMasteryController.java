/**
 * 
 */
package fm.controller;

import java.io.IOException;
import java.util.List;
import fm.dao.FlooringMasteryPersistenceException;
import fm.dto.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fm.service.FlooringMasteryDataValidationException;
import fm.service.FlooringMasteryServiceLayer;
import fm.ui.FlooringMasteryView;

/**
 * @author Flavio Silva
 *
 */

@Component
public class FlooringMasteryController {
	
	private FlooringMasteryServiceLayer service;
	private FlooringMasteryView view;
	
	@Autowired
	public FlooringMasteryController(FlooringMasteryServiceLayer service, FlooringMasteryView view) {
		this.service = service;
		this.view = view;
	}
	
	public void run() {
		boolean keepGoing = true;
		int menuSelection = 0;
		
		try {
			while(keepGoing) {
				
				menuSelection = getMenuSelection();
				
				switch(menuSelection) {
					case 1:
						listOrders();
						break;
					case 2:
						lookOrder();
						break;
					case 3:
						createNewOrder();
						break;
					case 4:
						removeOrder();
						break;
					case 5:
						keepGoing = false;
						break;
					default:
						unknownCommand(menuSelection);	
				}
			}
			exitMessage();
		}catch(FlooringMasteryPersistenceException | IOException e) {
			view.displayErrorMessage(e.getMessage());
		}
	}
	
	private int getMenuSelection() {
		return view.printMenuAndGetSelection();
	}
	
	private void createNewOrder() throws FlooringMasteryPersistenceException, IOException {
		view.displayCreateNewOrderBanner();
		boolean hasErrors = false;
		do {
			try {
				Order newOrder = view.getNewOrderInfo();
				service.createNewOrder(newOrder);
				view.displayCreateSuccessBanner(newOrder.getOrderNumber());
			}catch(FlooringMasteryDataValidationException e) {
				hasErrors = true;
				view.displayErrorMessage(e.getMessage());
			}
		}while(hasErrors);
	}
	
	private void listOrders() throws FlooringMasteryPersistenceException {
		view.displayListAllOrdersBanner();
		List<Order> orderList = service.getAllOrders();
		view.displayOrderList(orderList);
	}
	
	private void lookOrder() throws FlooringMasteryPersistenceException {
		view.displayOrderBanner();
		int orderNumber = view.getOrderNumberChoice();
		Order currentOrder = service.getOrder(orderNumber);
		view.displayOrder(currentOrder);
	}
	
	private void removeOrder() throws FlooringMasteryPersistenceException, IOException {
		view.displayRemoveOrderBanner();
		int orderNumber = view.getOrderNumberChoice();
		Order removedOrder = service.removeOrder(orderNumber);
		view.displayRemoveResult(removedOrder);
	}
	
	private void unknownCommand(int menuSelection) {
		view.displayUnknownCommandBanner(menuSelection);
	}
	
	private void exitMessage() {
		view.displayExitBanner();
	}
}
