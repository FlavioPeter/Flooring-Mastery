/**
 * 
 */
package fm;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import fm.ui.UserIO;
import fm.ui.UserIOConsoleImpl;

/**
 * @author Flavio Silva
 *
 */
public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		BigDecimal a = new BigDecimal("-2");
		
		if(a.compareTo(BigDecimal.ZERO) < 0) {
			System.out.println(a.compareTo(BigDecimal.ZERO));
		}
		
		LocalDate ld = LocalDate.now();
		
		System.out.println(ld);

		System.out.println(LocalDate.parse("2015-05-05"));
		
		String[] b = {"b","b"};
		String[] c = {"d","d"};
		String[][] d = {b,c};
		System.out.println(d[1][1]);
		System.out.println(d.length);
		*/
		File file = new File("FileData/Orders/");
		
		File[] fileArr = file.listFiles();
		
		boolean alreadyExists = Arrays.stream(fileArr).map((f) -> f.getPath()).anyMatch((f) -> f.contains("08082021"));
		String[] arr = Arrays.stream(fileArr).map((f) -> f.getPath()).toArray(String[]::new);
		//Arrays.stream(fileArr).map((f) -> f.getPath()).forEach((f) -> {System.out.println(f);});
		//Arrays.stream(fileArr).forEach((f) -> {System.out.println(f.getPath());});
		for(String ar : arr) {
			System.out.println(ar);
		}
		System.out.println(alreadyExists);
		
		for(File f : fileArr) {
			System.out.println(f.getPath());
		}
	}

}
