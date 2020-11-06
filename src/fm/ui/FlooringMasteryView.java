/**
 * 
 */
package fm.ui;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fm.dto.Order;

/**
 * @author Flavio Silva
 *
 */
@Component
public class FlooringMasteryView {
	
	private UserIO io;
	
	@Autowired
	public FlooringMasteryView(UserIO io) {
		this.io = io;
	}
	
	public int printMenuAndGetSelection(){
		io.println("Main Menu");
		io.println("1. List Orders");
		io.println("2. Look for an order");
		io.println("3. Add an order");
		io.println("4. Remove an order");
		io.println("5. Exit");
		
		return io.readInt("Please, select from the menu above: ");
	}
	
	public Order getNewOrderInfo() {
		int orderNumber = io.readInt("Please, enter order ID: "); // 1 //Should be assigned automatically
		String customerName = io.readString("Please enter the customer's name: "); // 2
		
		// View stateAbbrevs available
		String stateAbbrev = io.readString("Please, enter the state abbreviation: "); // 3
		
		// 4 determine taxRate in service in function of stateAbbrev
		
		// View productTypes available
		String productType = io.readString("Please, enter product type: "); // 5
		
		BigDecimal area = io.readBigDecimal("Please inser area in ft^2: ", 2, RoundingMode.HALF_UP); // 6
		
		// 7 determine costPerSquareFoot in service in function of productType
		
		// 8 determine laborCostPerSquareFoot in service in function of productType
		
		// 9 total material cost, let service layer calculate and set
		// 10 total labor cost, let service layer calculate and set
		// 11 total tax, let service layer calculate and set
		// 12 total of totals, let service layer calculate and set
		
		// Set values taken
		Order newOrder = new Order(orderNumber); // 1
		newOrder.setCustomerName(customerName); // 2
		newOrder.setStateAbbrev(stateAbbrev); // 3
		newOrder.setProductType(productType); // 5
		newOrder.setArea(area); // 6
		
		return newOrder;
	}
	
	public void displayCreateNewOrderBanner() {
		io.println("=== CREATE NEW ORDER ===");
	}
	
	public void displayCreateSuccessBanner(int orderNumber) {
		io.readString("Order "+orderNumber+" created with success. Please hit enter to continue...");
	}
	
	private void orderDisplayer(Order currentOrder) { // used in displayOrderList and displayOrder
		String orderInfo = String.format("#%s : %s :: %s :: %s :: %s :: %s :: %s :: %s :: %s :: %s :: %s :: %s",
				String.valueOf(currentOrder.getOrderNumber()),//1
				currentOrder.getCustomerName(),//2
				currentOrder.getStateAbbrev(),//3
				currentOrder.getTaxRate().toString(),//4
				currentOrder.getProductType(),//5
				currentOrder.getArea().toString(),//6
				currentOrder.getCostPerSquareFoot().toString(),//7
				currentOrder.getLaborCostPerSquareFoot().toString(),//8
				currentOrder.getMaterialCost().toString(),//9
				currentOrder.getLaborCost().toString(),//10
				currentOrder.getTax().toString(),//11
				currentOrder.getTotal().toString());//12
				
		io.println(orderInfo);
	}
	
	public void displayOrderList(List<Order> orderList) {
		for(Order currentOrder : orderList) {
			orderDisplayer(currentOrder);
		}
		io.readString("Please hit enter to continue.");
	}
	
	public void displayListAllOrdersBanner() {
		io.println("=== DISPLAY ALL ORDERS ===");
	}
	
	public void displayOrderBanner() {
		io.println("=== DISPLAY ORDER ===");
	}
	
	public int getOrderNumberChoice() {
		return io.readInt("Please enter the order number (or ID): ");
	}
	
	public void displayOrder(Order currentOrder) {
		if(currentOrder != null) {
			orderDisplayer(currentOrder);
		}else {
			io.print("No order with this number.");
		}
		io.readString("Please hit enter to continue...");
	}
	
	public void displayRemoveOrderBanner() {
		io.println("=== REMOVE STUDENT ===");
	}
	
	public void displayRemoveResult(Order removedOrder) {
		if(removedOrder != null) {
			orderDisplayer(removedOrder);
			io.println("Was removed with success.");
		}else {
			io.println("No such order exists.");
		}
		io.readString("Please hit enter to continue...");
	}
	
	public void displayExitBanner() {
		io.println("=== EXIT ===");
		io.println("Good Bye...");
	}
	
	public void displayUnknownCommandBanner(int menuSelection) {
		io.println(menuSelection+" does not exist.");
		io.readString("Please hit enter to continue...");
	}
	
	public void displayErrorMessage(String errorMsg) {
		io.println("=== ERROR ===");
		io.print(errorMsg);
	}
	
}
