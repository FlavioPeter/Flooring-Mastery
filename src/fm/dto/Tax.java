/**
 * 
 */
package fm.dto;

import java.math.BigDecimal;

/**
 * @author Flavio Silva
 *
 */
public class Tax {
	
	private String stateAbbreviation;
	private String stateName;
	private BigDecimal taxRate;
	
	public Tax(String stateAbbreviation) {
		this.stateAbbreviation = stateAbbreviation;
	}
	
	public String getStateAbbreviation() {
		return stateAbbreviation;
	}
	
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	
	public String getStateName() {
		return stateName;
	}
	
	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}
	
	public BigDecimal getTaxRate() {
		return taxRate;
	}
}
