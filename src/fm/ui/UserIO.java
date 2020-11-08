/**
 * 
 */
package fm.ui;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
	
	LocalDate readLocalDate(String prompt, DateTimeFormatter format);
	
	boolean readBoolean(String prompt, String trueString);
	
}
