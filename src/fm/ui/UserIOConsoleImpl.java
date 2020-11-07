/**
 * 
 */
package fm.ui;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.springframework.stereotype.Component;

/**
 * @author Flavio Silva
 *
 */
@Component
public class UserIOConsoleImpl implements UserIO {
	
	private static Scanner scanner = new Scanner(System.in);
	
	@Override
	public void print(String msg) {
		System.out.print(msg);
	}
	
	@Override
	public void println(String msg) {
		System.out.println(msg);
	}
	
	@Override
	public String readString(String prompt) {
		print(prompt);
		String ans = scanner.nextLine();
		println("");
		return ans;
	}
	
	@Override
	public int readInt(String prompt) {
		print(prompt);
		int ans = Integer.parseInt(scanner.nextLine());
		println("");
		return ans;
	}
	
	@Override
	public BigDecimal readBigDecimal(String prompt, int decimals, RoundingMode roundingMode) {
		print(prompt);
		BigDecimal ans = new BigDecimal(scanner.nextLine());
		ans.setScale(decimals, roundingMode);
		println("");
		return ans;
	}
	
	@Override
	public LocalDate readLocalDate(String prompt, DateTimeFormatter format) {
		print(prompt);
		LocalDate ans = LocalDate.parse(scanner.nextLine(), format);
		return ans;
	}
}
