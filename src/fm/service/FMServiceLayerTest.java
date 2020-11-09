/**
 * 
 */
package fm.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fm.dao.FlooringMasteryDao;
import fm.dao.FlooringMasteryDaoFileImpl;
import fm.dao.FlooringMasteryPersistenceException;
import fm.dto.Order;

/**
 * @author Flavio Silva
 *
 */
class FMServiceLayerTest {

	// Initiate dao and service
	public FlooringMasteryDao dao = new FlooringMasteryDaoFileImpl();
	public FlooringMasteryServiceLayer service = new FlooringMasteryServiceLayerImpl(dao);
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
		// Instance 1 (Everything good) test 1
		Order order1 = new Order(0);
		order1.setCustomerName("Should be in the file, Instance 1.");
		order1.setStateAbbrev("TX");
		order1.setProductType("Tile");
		order1.setOrderDate(LocalDate.parse("01-01-3001",DateTimeFormatter.ofPattern("MM-dd-yyyy")));
		
		// Instance 2 (Bad product type) test 1
		Order order2 = new Order(0);
		order2.setCustomerName("Not going to make it, Instance 2");
		order2.setStateAbbrev("TX");
		order2.setProductType("This product type does not exist.");
		order2.setOrderDate(LocalDate.parse("01-01-3001",DateTimeFormatter.ofPattern("MM-dd-yyyy")));
		
		// Instance 3
		Order order3 = new Order(0);
		order3.setCustomerName("John");
		order3.setStateAbbrev("TX");
		order3.setProductType("Tile");
		order3.setOrderDate(LocalDate.parse("01-01-3000",DateTimeFormatter.ofPattern("MM-dd-yyyy")));
		
		// Instance 4
		Order order4 = new Order(0);
		order4.setCustomerName("John");
		order4.setStateAbbrev("TX");
		order4.setProductType("Tile");
		order4.setOrderDate(LocalDate.parse("01-01-3000",DateTimeFormatter.ofPattern("MM-dd-yyyy")));
		
		// Instance 5
		Order order5 = new Order(0);
		order5.setCustomerName("John");
		order5.setStateAbbrev("TX");
		order5.setProductType("Tile");
		order5.setOrderDate(LocalDate.parse("01-01-3000",DateTimeFormatter.ofPattern("MM-dd-yyyy")));
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test1() throws FlooringMasteryDataValidationException, FlooringMasteryPersistenceException, IOException {
		// Instance 1 (Everything good) test 1
		Order order1 = new Order(0);
		order1.setCustomerName("Should be in the file, Instance 1.");
		order1.setStateAbbrev("TX");
		order1.setProductType("Tile");
		order1.setOrderDate(LocalDate.parse("01-01-3001",DateTimeFormatter.ofPattern("MM-dd-yyyy")));
				
		// Instance 2 (Bad product type) test 1
		Order order2 = new Order(0);
		order2.setCustomerName("Not going to make it, Instance 2");
		order2.setStateAbbrev("TX");
		order2.setProductType("This product type does not exist.");
		order2.setOrderDate(LocalDate.parse("01-01-3001",DateTimeFormatter.ofPattern("MM-dd-yyyy")));
		
		// Using order1 and order2
		assertNull(service.createNewOrder(order1));
		assertNotNull(service.createNewOrder(order1));
		Assertions.assertThrows(FlooringMasteryDataValidationException.class, () -> service.validateOrderData(order2));
	}
	
	@Test
	void test2() {
		fail("Not yet implemented");
	}
	
	@Test
	void test3() {
		fail("Not yet implemented");
	}
	
	@Test
	void test4() {
		fail("Not yet implemented");
	}

}
