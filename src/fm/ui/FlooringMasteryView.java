/**
 * 
 */
package fm.ui;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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
		io.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * ");
		io.println("* <<Flooring Program>>");
		io.println("* 1. List Orders");
		io.println("* 2. Look for an order");
		io.println("* 3. Add an order");
		io.println("* 4. Modify an order");
		io.println("* 5. Remove an order");
		io.println("* 6. Export all orders data.");
		io.println("* 7. Exit");
		io.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * ");
		return io.readInt("Please, select from the menu above: ");
	}
	
	public Order getNewOrderInfo(String[] productTypesMenu, String[][] statesMenu) {
		
		Order newOrder = new Order(0); // 1 instatiate with 0 for now, and re-assign once it passes validation
		
		LocalDate orderDate = io.readLocalDate("For when is the order? (e.g: mm-dd-yyyy): ", DateTimeFormatter.ofPattern("MM-dd-yyyy"));
		
		String customerName = io.readString("Please enter the customer's name: "); // 2
		
		// View stateAbbrevs available
		for(int i = 0; i<statesMenu[0].length; i++) {
			io.println(statesMenu[0][i]+" : "+statesMenu[1][i]);
		}
		String stateAbbrev = io.readString("Please, enter the state abbreviation: "); // 3
		
		// 4 determine taxRate in service in function of stateAbbrev
		
		Arrays.stream(productTypesMenu).forEach((p) -> {io.println(p);});// View productTypes available
		String productType = io.readString("Please, enter product type: "); // 5
		
		BigDecimal area = io.readBigDecimal("Please inser area in ft^2: ", 2, RoundingMode.HALF_UP); // 6
		
		// 7 determine costPerSquareFoot in service in function of productType
		
		// 8 determine laborCostPerSquareFoot in service in function of productType
		
		// 9 total material cost, let service layer calculate and set
		// 10 total labor cost, let service layer calculate and set
		// 11 total tax, let service layer calculate and set
		// 12 total of totals, let service layer calculate and set
		
		// Set values taken
		newOrder.setOrderDate(orderDate);//13
		newOrder.setCustomerName(customerName); // 2
		newOrder.setStateAbbrev(stateAbbrev); // 3
		newOrder.setProductType(productType); // 5
		newOrder.setArea(area); // 6
		
		return newOrder;
	}
	
	public void displayCreateNewOrderBanner() {
		io.println("=== CREATE NEW ORDER ===");
	}
	
	public void displayCreateSuccessBanner(int orderNumber,Order successOrder) {
		if(successOrder==null) {
			io.readString("Order "+orderNumber+" created with success. Please hit enter to continue...");
		}else {
			io.readString("Order "+orderNumber+" was overwritten. Please hit enter to continue...");
		}
	}
	
	private void orderDisplayer(Order currentOrder) { // used in displayOrderList and displayOrder
		String orderInfo = String.format("#%s : %s :: %s :: %s %% :: %s :: %s ft^2 :: $ %s :: $ %s :: $ %s :: $ %s :: $ %s :: $ %s",
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
		io.println("#OrderNumber:CustomerName::State::TaxRate::ProductType::Area::CostPerSquareFoot::LaborCostPerSquareFoot::MaterialCost::LaborCost::Tax::Total");
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
	
	public void displayModifyOrderBanner() {
		io.println("=== MODIFY ORDER DATA ===");
	}
	
	public void displayRemoveOrderBanner() {
		io.println("=== DELETE ORDER ===");
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

	public void displayExportAllDataBanner() {
		io.println("=== Export All Data ===");
	}

	public void displayExportSuccess() {
		io.println("All data was exported with success.");
		io.readString("Press enter to continue...");
	}
	
	public LocalDate getDateSpecification() {
		return io.readLocalDate("Which date? (e.g: mm-dd-yyyy): ", DateTimeFormatter.ofPattern("MM-dd-yyyy"));
	}

	public boolean updateData() {
		return io.readBoolean("Save modified order? (Y/N)", "Y");
	}

	public void notUpdateDataMessage() {
		io.println("The data was not updated.");
	}

	public void getModifiedOrderInfo(Order orderToModify, String[] productTypesMenu, String[][] statesMenu) {

		// Modify Customer Name //
		io.println("Customer Name is currently "+orderToModify.getCustomerName()+".");
		String customerName = io.readString("Write the new name, otherwise, just press enter to skip.:"); // 2
		
		if(!customerName.equals("")) {
			orderToModify.setCustomerName(customerName);
		}
		
		// Modify State Abbreviation //
		io.println("State Abbreviation is currently "+orderToModify.getStateAbbrev()+".");
		// View stateAbbrevs available
		for(int i = 0; i<statesMenu[0].length; i++) {
			io.println(statesMenu[0][i]+" : "+statesMenu[1][i]);
		}
		String stateAbbrev = io.readString("Please, enter the new state abbreviation, otherwise, just press enter to skip.: "); // 3
		
		if(!stateAbbrev.equals("")) {
			orderToModify.setStateAbbrev(stateAbbrev);
		}
		
		// Modify Product Type //
		io.println("Product Type is currently "+orderToModify.getProductType()+".");
		
		Arrays.stream(productTypesMenu).forEach((p) -> {io.println(p);});// View productTypes available
		String productType = io.readString("Please, enter new product type, otherwise, just press enter to skip.: "); // 5
		
		if(!productType.equals("")) {
			orderToModify.setProductType(productType);
		}
		
		// Modify Area //
		io.println("Area is currently "+orderToModify.getArea()+".");
		
		BigDecimal area = io.readBigDecimal("Please insert a new area in ft^2, otherwise, just press enter to skip.: ",2,RoundingMode.HALF_UP); // 6
		
		orderToModify.setArea(area);
		
	}
}
