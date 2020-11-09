/**
 * 
 */
package fm;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import fm.dao.FlooringMasteryDao;
import fm.dao.FlooringMasteryDaoFileImpl;
import fm.dao.FlooringMasteryPersistenceException;
import fm.dto.Order;
import fm.service.FlooringMasteryDataValidationException;
import fm.service.FlooringMasteryServiceLayer;
import fm.service.FlooringMasteryServiceLayerImpl;

/**
 * @author Flavio Silva
 *
 */
public class test {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws FlooringMasteryPersistenceException 
	 * @throws FlooringMasteryDataValidationException 
	 */
	public static void main(String[] args) throws FlooringMasteryDataValidationException, FlooringMasteryPersistenceException, IOException {
		FlooringMasteryDao dao = new FlooringMasteryDaoFileImpl();
		FlooringMasteryServiceLayer service = new FlooringMasteryServiceLayerImpl(dao);
		
		Order order1 = new Order(0);
		order1.setCustomerName("Shouldbeinthefile,Instance 1");
		System.out.println(order1.getCustomerName());
		order1.setStateAbbrev("TX");
		System.out.println(order1.getStateAbbrev());
		order1.setProductType("Tile");
		System.out.println(order1.getProductType());
		order1.setOrderDate(LocalDate.parse("01-01-3001",DateTimeFormatter.ofPattern("MM-dd-yyyy")));
		System.out.println(order1.getOrderDate());
		order1.setArea((new BigDecimal("2.115")).setScale(2,RoundingMode.HALF_UP));
		
		System.out.println(service.createNewOrder(order1));
		System.out.println(service.createNewOrder(order1));
	}

}
