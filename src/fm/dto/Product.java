/**
 * 
 */
package fm.dto;

import java.math.BigDecimal;

/**
 * @author Flavio Silva
 *
 */
public class Product {
	
	private String productType;
	private BigDecimal costPerSquareFoot;
	private BigDecimal laborCostPerSquareFoot;
	
	public Product(String productType) {
		this.productType = productType;
	}
	
	public String getProductType() {
		return productType;
	}
	
	public void setCostPerSquareFoot(BigDecimal costPerSquareFoot) {
		this.costPerSquareFoot = costPerSquareFoot;
	}
	
	public BigDecimal getCostPerSquareFoot() {
		return costPerSquareFoot;
	}
	
	public void setLaborCostPerSquareFoot(BigDecimal laborCostPerSquareFoot) {
		this.laborCostPerSquareFoot = laborCostPerSquareFoot;
	}
	
	public BigDecimal getLaborCostPerSquareFoot() {
		return laborCostPerSquareFoot;
	}
	
}

