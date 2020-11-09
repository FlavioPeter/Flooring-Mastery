/**
 * 
 */
package fm.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fm.dao.FlooringMasteryDao;
import fm.dao.FlooringMasteryPersistenceException;
import fm.dto.Order;
import fm.dto.Product;
import fm.dto.Tax;

/**
 * @author Flavio Silva
 *
 */
@Component
public class FlooringMasteryServiceLayerImpl implements FlooringMasteryServiceLayer {

	FlooringMasteryDao dao;
	
	@Autowired
	public FlooringMasteryServiceLayerImpl(FlooringMasteryDao dao) {
		this.dao = dao;
	}
	
	@Override
	public Order createNewOrder(Order order)
			throws FlooringMasteryDataValidationException, FlooringMasteryPersistenceException, IOException {
		//maybe check if the order number already exists
		
		validateOrderData(order); // If it passes this point, then we can start calculations
		
		setStateTaxAndProductTypeCost(order);
		
		setCalculatedFields(order);
		
		dao.addOrder(order.getOrderNumber(), order);
		
		setUniqueNumber(order);
		
		removeOrder(0, order.getOrderDate());
		
		return dao.addOrder(order.getOrderNumber(), order);
		
	}

	private void setStateTaxAndProductTypeCost(Order order) throws FlooringMasteryPersistenceException {
		BigDecimal taxRate = getAllTaxes().stream().filter((t) -> t.getStateAbbreviation().equalsIgnoreCase(order.getStateAbbrev())).map((t) -> t.getTaxRate()).reduce(BigDecimal.ZERO.setScale(2), BigDecimal::add);
		BigDecimal costPerSquareFoot = getAllProducts().stream().filter((p) -> p.getProductType().equalsIgnoreCase(order.getProductType())).map((p) -> p.getCostPerSquareFoot()).reduce(BigDecimal.ZERO.setScale(2), BigDecimal::add);
		BigDecimal laborCostPerSquareFoot = getAllProducts().stream().filter((p) -> p.getProductType().equalsIgnoreCase(order.getProductType())).map((p) -> p.getLaborCostPerSquareFoot()).reduce(BigDecimal.ZERO.setScale(2), BigDecimal::add);
		
		order.setTaxRate(taxRate.setScale(2,RoundingMode.HALF_UP));
		order.setCostPerSquareFoot(costPerSquareFoot.setScale(2,RoundingMode.HALF_UP));
		order.setLaborCostPerSquareFoot(laborCostPerSquareFoot.setScale(2,RoundingMode.HALF_UP));
	}

	private void setCalculatedFields(Order order) {
		
		BigDecimal materialCost = calculateMaterialCost(order);
		order.setMaterialCost(materialCost.setScale(2,RoundingMode.HALF_UP));
		BigDecimal laborCost = calculateLaborCost(order);
		order.setLaborCost(laborCost.setScale(2,RoundingMode.HALF_UP));
		BigDecimal tax = calculateTax(order);
		order.setTax(tax.setScale(2,RoundingMode.HALF_UP));
		BigDecimal total = calculateTotal(order);
		order.setTotal(total.setScale(2,RoundingMode.HALF_UP));
		
	}
	
	private void setUniqueNumber(Order order) throws FlooringMasteryPersistenceException {
		OptionalInt num = getAllOrders(order.getOrderDate()).stream().mapToInt((o) -> o.getOrderNumber()).reduce(Integer::max);
		
		int orderNumber = num.getAsInt();
		
		order.setOrderNumber(orderNumber+1);
	}

	private BigDecimal calculateMaterialCost(Order order) {
		return order.getArea().multiply(order.getCostPerSquareFoot());
	}
	
	private BigDecimal calculateLaborCost(Order order) {
		return order.getArea().multiply(order.getLaborCostPerSquareFoot());
	}
	
	private BigDecimal calculateTax(Order order) {
		return order.getMaterialCost().add(order.getLaborCost()).multiply(order.getTaxRate()).divide(new BigDecimal("100.00"),2,RoundingMode.HALF_UP);
	}
	
	private BigDecimal calculateTotal(Order order) {
		return order.getMaterialCost().add(order.getLaborCost()).add(order.getTax());
	}

	@Override
	public List<Order> getAllOrders(LocalDate orderDate) throws FlooringMasteryPersistenceException {
		return dao.getAllOrders(orderDate);
	}
	
	@Override
	public List<Product> getAllProducts() throws FlooringMasteryPersistenceException {
		return dao.getAllProducts();
	}
	
	@Override
	public List<Tax> getAllTaxes() throws FlooringMasteryPersistenceException {
		return dao.getAllTaxes();
	}

	@Override
	public Order getOrder(int orderNumber, LocalDate orderDate) throws FlooringMasteryPersistenceException {
		return dao.getOrder(orderNumber, orderDate);
	}

	@Override
	public Order removeOrder(int orderNumber, LocalDate orderDate) throws FlooringMasteryPersistenceException, IOException {
		return dao.removeOrder(orderNumber, orderDate);
	}
	
	@Override
	public void changeOrderData(Order orderToModify) throws FlooringMasteryDataValidationException, FlooringMasteryPersistenceException {
		
		validateOrderData(orderToModify); // If it passes this point, then we can start calculations
		
		setStateTaxAndProductTypeCost(orderToModify);
		
		setCalculatedFields(orderToModify);
		
	}
	
	@Override
	public Order addOrder(Order order) throws FlooringMasteryPersistenceException, IOException {
		return dao.addOrder(order.getOrderNumber(), order);
		
	}
	
	@Override
	public void validateOrderData(Order order) throws FlooringMasteryDataValidationException, FlooringMasteryPersistenceException {
		
		// Checking that no values entered are null
		if(order.getCustomerName() == null
				|| order.getStateAbbrev() == null
				|| order.getProductType() == null
				|| order.getArea() == null) {
			
			throw new FlooringMasteryDataValidationException("ERROR: All fields are required. They must not be null");
		}
		
		// Checking for valid date
		if(order.getOrderDate().compareTo(LocalDate.now())<0) {
			throw new FlooringMasteryDataValidationException("ERROR: The date of the order must be in the future.");
		}
		
		// Checking for valid characters
		boolean checkInvalidChars = Pattern.compile("[^a-z0-9, ]", Pattern.CASE_INSENSITIVE).matcher(order.getCustomerName()).find();
		if(checkInvalidChars) {
			throw new FlooringMasteryDataValidationException("ERROR: The Customer Name contains other characters than a-z, A-Z and 0-9.");
		}
		
		// Checking for valid state abbreviation
		boolean validStateAbbreviation = Arrays.stream(getStates()[0]).anyMatch(order.getStateAbbrev()::equalsIgnoreCase);
		if(validStateAbbreviation==false) {
			throw new FlooringMasteryDataValidationException("ERROR: There was an invalid state abbreviation");
		}
		
		// Checking for valid product type
		boolean validProductType = Arrays.stream(getProductTypes()).anyMatch(order.getProductType()::equalsIgnoreCase);
		if(validProductType==false) {
			throw new FlooringMasteryDataValidationException("ERROR: There was an invalid product type.");
		}
		
		// Checking for area being greater than 0
		if(order.getArea().compareTo(BigDecimal.ZERO)<=0) {
			throw new FlooringMasteryDataValidationException("ERROR: The Area must be greater than 0.");
		}
	}
	
	@Override
	public String[] getProductTypes() throws FlooringMasteryPersistenceException {
		return getAllProducts().stream().map((p) -> p.getProductType()).toArray(String[]::new);
	}
	
	@Override
	public String[][] getStates() throws FlooringMasteryPersistenceException {
		String[] abbreviations = getAllTaxes().stream().map((t) -> t.getStateAbbreviation()).toArray(String[]::new);
		String[] states = getAllTaxes().stream().map((t) -> t.getStateName()).toArray(String[]::new);
		String[][] combined = {abbreviations, states};
		return combined;
	}
	
	@Override
	public void exportAllOrders() throws FlooringMasteryPersistenceException, IOException{
		dao.exportAllOrders();
	}
	
}
