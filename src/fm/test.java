/**
 * 
 */
package fm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fm.dao.FlooringMasteryPersistenceException;
import fm.ui.UserIO;
import fm.ui.UserIOConsoleImpl;

/**
 * @author Flavio Silva
 *
 */
public class test {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws FlooringMasteryPersistenceException 
	 */
	public static void main(String[] args) throws IOException, FlooringMasteryPersistenceException {
		/*
		String validChars = "abcdefghijklmnopqrstwxyz0123456789";
		String word = "hello";
		
		for(int i=0;i<word.length();i++) {
			System.out.println(word.charAt(index))
		}
		*/
		
		Pattern p = Pattern.compile("[^a-z0-9,]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(",");
		boolean b = m.find();
		
		System.out.println(b);
	}

}
