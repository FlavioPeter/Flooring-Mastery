/**
 * 
 */
package fm.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.OptionalInt;

/**
 * @author Flavio Silva
 *
 */
public class Order {

	private int orderNumber; // 1
	private String customerName; // 2
	private String stateAbbrev; // 3
	private BigDecimal taxRate; // 4
	private String productType; // 5
	private BigDecimal area; // 6
	private BigDecimal costPerSquareFoot; // 7
	private BigDecimal laborCostPerSquareFoot; // 8
	private BigDecimal materialCost; // total material cost
	private BigDecimal laborCost; // total labor cost
	private BigDecimal tax; // total tax
	private BigDecimal total; // total of totals
	
	LocalDate orderDate;
	
	public Order(int orderNumber) { // 1
		this.orderNumber = orderNumber;
	}
	
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	public int getOrderNumber() {
		return orderNumber;
	}
	
	public void setCustomerName(String customerName) { // 2
		this.customerName = customerName;
	}
	
	public String getCustomerName() { // 2
		return customerName;
	}
	
	public void setStateAbbrev(String stateAbbrev) { // 3
		this.stateAbbrev = stateAbbrev;
	}
	
	public String getStateAbbrev() { // 3
		return stateAbbrev;
	}
	
	public void setTaxRate(BigDecimal taxRate) { // 4 
		this.taxRate = taxRate;
	}
	
	public BigDecimal getTaxRate() { // 4
		return taxRate;
	}
	
	public void setProductType(String productType) { // 5
		this.productType = productType;
	}
	
	public String getProductType() { // 5
		return productType;
	}
	
	public void setArea(BigDecimal area) { // 6
		this.area = area;
	}
	
	public BigDecimal getArea() { // 6
		return area;
	}
	
	public void setCostPerSquareFoot(BigDecimal costPerSquareFoot) { // 7
		this.costPerSquareFoot = costPerSquareFoot;
	}
	
	public BigDecimal getCostPerSquareFoot() { // 7
		return costPerSquareFoot;
	}
	
	public void setLaborCostPerSquareFoot(BigDecimal laborCostPerSquareFoot) { // 8
		this.laborCostPerSquareFoot = laborCostPerSquareFoot;
	}
	
	public BigDecimal getLaborCostPerSquareFoot() { // 8
		return laborCostPerSquareFoot;
	}
	
	public void setMaterialCost(BigDecimal materialCost) { // 9 calculate
		this.materialCost = materialCost;//this.materialCost = area.multiply(costPerSquareFoot); // Move this calculation to service
	}
	
	public BigDecimal getMaterialCost() { // 9
		return materialCost;
	}
	
	public void setLaborCost(BigDecimal laborCost) { // 10 calculate
		this.laborCost = laborCost;//this.laborCost = area.multiply(laborCostPerSquareFoot); // Move this calculation to service
	}
	
	public BigDecimal getLaborCost() { // 10
		return laborCost;
	}
	
	public void setTax(BigDecimal tax) { // 11 calculate
		this.tax = tax;//this.tax = materialCost.add(laborCost).multiply(taxRate).divide(new BigDecimal("100.00"),2,RoundingMode.HALF_UP); // Move this calculation to service
	}
	
	public BigDecimal getTax() { // 11
		return tax;
	}
	
	public void setTotal(BigDecimal total) { // 12 calculate
		this.total = total;//this.total = materialCost.add(laborCost).add(tax);// Move this calculation to service
	}
	
	public BigDecimal getTotal() { // 12
		return total;
	}
	
	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}
	
	public LocalDate getOrderDate() {
		return orderDate;
	}
	
}
