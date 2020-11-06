/**
 * 
 */
package fm.ui;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Flavio Silva
 *
 */
public interface UserIO { // Here we need to be able to handle String, Integer and BigDecimal based on fields used

	void print(String msg);
	
	void println(String msg);
	
	String readString(String prompt);
	
	int readInt(String prompt);
	
	BigDecimal readBigDecimal(String prompt, int decimals, RoundingMode roundingMode);
	
}
