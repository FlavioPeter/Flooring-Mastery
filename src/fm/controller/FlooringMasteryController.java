/**
 * 
 */
package fm.controller;

import java.io.IOException;
import java.time.LocalDate;
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
						listOrders();// add pick a date functionality and make sure it unmarshalls that date
						break;
					case 2:
						lookOrder();// add pick a date functionality and make sure it unmarshalls that date
						break;
					case 3:
						createNewOrder(); // add pick a date functionality and make sure it unmarshalls and marshalls that date
						break;
					case 4:
						removeOrder(); // add pick a date functionality and make sure it unmarshalls and marshalls that date
						break;
					case 5:
						exportAllData(); // Unmarshalls all order dates and marshalls to one file
						break;
					case 6:
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
				String[] productTypesMenu = service.getProductTypes();
				String[][] statesMenu = service.getStates();
				Order newOrder = view.getNewOrderInfo(productTypesMenu, statesMenu);
				Order successOrder= service.createNewOrder(newOrder);
				view.displayCreateSuccessBanner(newOrder.getOrderNumber(), successOrder);
			}catch(FlooringMasteryDataValidationException e) {
				hasErrors = true;
				view.displayErrorMessage(e.getMessage());
			}
		}while(hasErrors);
	}
	
	private void listOrders() throws FlooringMasteryPersistenceException {
		view.displayListAllOrdersBanner();
		LocalDate orderDate = view.getDateSpecification();
		List<Order> orderList = service.getAllOrders(orderDate);
		view.displayOrderList(orderList);
	}
	
	private void lookOrder() throws FlooringMasteryPersistenceException {
		view.displayOrderBanner();
		LocalDate orderDate = view.getDateSpecification();
		int orderNumber = view.getOrderNumberChoice();
		Order currentOrder = service.getOrder(orderNumber, orderDate);
		view.displayOrder(currentOrder);
	}
	
	private void removeOrder() throws FlooringMasteryPersistenceException, IOException {
		view.displayRemoveOrderBanner();
		LocalDate orderDate = view.getDateSpecification();
		int orderNumber = view.getOrderNumberChoice();
		Order removedOrder = service.removeOrder(orderNumber, orderDate);
		view.displayRemoveResult(removedOrder);
	}
	
	private void exportAllData() {
		view.displayExportAllDataBanner();
		service.exportAllData();
		view.displayExportSuccess();
		
	}
	
	private void unknownCommand(int menuSelection) {
		view.displayUnknownCommandBanner(menuSelection);
	}
	
	private void exitMessage() {
		view.displayExitBanner();
	}
}
