/**
 * 
 */
package fm.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

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
	public void createNewOrder(Order order)
			throws FlooringMasteryDataValidationException, FlooringMasteryPersistenceException, IOException {
		//maybe check if the order number already exists
		
		validateOrderData(order); // If it passes this point, then we can start calculations
		
		setStateTaxAndProductTypeCost(order);
		
		setCalculatedFields(order);
		
		dao.addOrder(order.getOrderNumber(), order);
		
	}

	private void setStateTaxAndProductTypeCost(Order order) throws FlooringMasteryPersistenceException {
		BigDecimal taxRate = getAllTaxes().stream().filter((t) -> t.getStateAbbreviation().equals(order.getStateAbbrev())).map((t) -> t.getTaxRate()).reduce(BigDecimal.ZERO.setScale(2), BigDecimal::add);
		BigDecimal costPerSquareFoot = getAllProducts().stream().filter((p) -> p.getProductType().equals(order.getProductType())).map((p) -> p.getCostPerSquareFoot()).reduce(BigDecimal.ZERO.setScale(2), BigDecimal::add);
		BigDecimal laborCostPerSquareFoot = getAllProducts().stream().filter((p) -> p.getProductType().equals(order.getProductType())).map((p) -> p.getLaborCostPerSquareFoot()).reduce(BigDecimal.ZERO.setScale(2), BigDecimal::add);
		
		order.setTaxRate(taxRate);
		order.setCostPerSquareFoot(costPerSquareFoot);
		order.setLaborCostPerSquareFoot(laborCostPerSquareFoot);
	}

	private void setCalculatedFields(Order order) {
		
		BigDecimal materialCost = calculateMaterialCost(order);
		order.setMaterialCost(materialCost);
		BigDecimal laborCost = calculateLaborCost(order);
		order.setLaborCost(laborCost);
		BigDecimal tax = calculateTax(order);
		order.setTax(tax);
		BigDecimal total = calculateTotal(order);
		order.setTotal(total);
		
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
	public List<Order> getAllOrders() throws FlooringMasteryPersistenceException {
		return dao.getAllOrders();
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
	public Order getOrder(int orderNumber) throws FlooringMasteryPersistenceException {
		return dao.getOrder(orderNumber);
	}

	@Override
	public Order removeOrder(int orderNumber) throws FlooringMasteryPersistenceException, IOException {
		return dao.removeOrder(orderNumber);
	}
	
	private void validateOrderData(Order order) throws FlooringMasteryDataValidationException, FlooringMasteryPersistenceException {
		
		// Checking that no values entered are null
		if(order.getCustomerName() == null
				|| order.getStateAbbrev() == null
				|| order.getProductType() == null
				|| order.getArea() == null) {
			
			throw new FlooringMasteryDataValidationException("ERROR: All fiels are required. They must not be null");
		}
		
		// Checking for valid state abbreviation
		boolean validStateAbbreviation = getAllTaxes().stream().map((t) -> t.getStateAbbreviation()).anyMatch(order.getStateAbbrev()::equals);
		if(validStateAbbreviation==false) {
			throw new FlooringMasteryDataValidationException("ERROR: There was an invalid state abbreviation");
		}
		
		// Checking for valid product type
		boolean validProductType = getAllProducts().stream().map((p) -> p.getProductType()).anyMatch(order.getProductType()::equals);
		if(validProductType==false) {
			throw new FlooringMasteryDataValidationException("ERROR: There was an invalid product type.");
		}
	}
	
	
	
}
