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
		boolean hasError = false;
		do {
			try {
				print(prompt);
				int ans = Integer.parseInt(scanner.nextLine());
				hasError = false;
				println("");
				return ans;
			}catch(Exception e) {
				println("Please enter an integer.");
				hasError = true;
			}
		}while(hasError);
		return 0;
	}
	
	@Override
	public BigDecimal readBigDecimal(String prompt, int decimals, RoundingMode roundingMode) {
		boolean hasError = false;
		do {
			try {
				print(prompt);
				BigDecimal ans = new BigDecimal(scanner.nextLine());
				ans.setScale(decimals, roundingMode);
				hasError = false;
				println("");
				return ans;
			}catch(Exception e) {
				println("Please enter a number.");
				hasError = true;
			}
		}while(hasError);
		return BigDecimal.ZERO;
	}
	
	@Override
	public LocalDate readLocalDate(String prompt, DateTimeFormatter format) {
		boolean hasError = false;
		do {
			try {
				print(prompt);
				LocalDate ans = LocalDate.parse(scanner.nextLine(), format);
				hasError = false;
				println("");
				return ans;
			}catch(Exception e) {
				println("Please enter a valid pattern.");
				hasError = true;
			}
		}while(hasError);
		return LocalDate.now();
	}
	
	@Override
	public boolean readBoolean(String prompt, String trueString) {
		print(prompt);
		String ans = scanner.nextLine();
		println("");
		if(ans.equals(trueString)) {
			return true;
		}
		return false;
	}
}
