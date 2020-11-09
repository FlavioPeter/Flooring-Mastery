package fm.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

class ServiceLayerTest {

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
			order1.setCustomerName("Should be in the file, Instance 1");
			order1.setStateAbbrev("TX");
			order1.setProductType("Tile");
			order1.setOrderDate(LocalDate.parse("01-01-3001",DateTimeFormatter.ofPattern("MM-dd-yyyy")));
			order1.setArea((new BigDecimal("2.115")).setScale(2,RoundingMode.HALF_UP));
					
			
			// Using order1
			assertNull(service.createNewOrder(order1)); // pass if order1 was added correctly
			assertTrue(order1.getOrderNumber()==1); // pass if the OrderNumber was incremented to the nearest highest number existent in file
			Order order = service.removeOrder(1, order1.getOrderDate()); //  remove the order just added, and return the order removed
			assertNotNull(order); // pass if the order returned from removeOrder is not null
			assertEquals(order1.getOrderNumber(), order.getOrderNumber()); // pass if the orderNumber is the same as the one added as the one removed
			
		}
		
		@Test
		void test2() throws FlooringMasteryDataValidationException, FlooringMasteryPersistenceException, IOException {
					
			// Instance 2 (Bad product type) test 1
			Order order2 = new Order(0);
			order2.setCustomerName("test");
			order2.setStateAbbrev("TX");
			order2.setProductType("Tile");
			order2.setOrderDate(LocalDate.parse("01-01-3001",DateTimeFormatter.ofPattern("MM-dd-yyyy")));
			order2.setArea((new BigDecimal("2.115")).setScale(2,RoundingMode.HALF_UP));
			
			// Using order2
			
			// ERROR: The Customer Name contains other characters than a-z, A-Z and 0-9 .
			order2.setCustomerName("Invalid character @");// null
			Exception e = assertThrows(FlooringMasteryDataValidationException.class, () -> service.validateOrderData(order2));
			System.out.println(e.getMessage());
			order2.setCustomerName("test");
			// ERROR: All fields are required. They must not be null
			order2.setCustomerName(null);// null
			Exception e1 = assertThrows(FlooringMasteryDataValidationException.class, () -> service.validateOrderData(order2));
			System.out.println(e1.getMessage());
			order2.setCustomerName("test");
			// ERROR: There was an invalid state abbreviation
			order2.setStateAbbrev("Not a valid state abbreviation");// null
			Exception e2 = assertThrows(FlooringMasteryDataValidationException.class, () -> service.validateOrderData(order2));
			System.out.println(e2.getMessage());
			order2.setStateAbbrev("TX");
			// ERROR: There was an invalid product type.
			order2.setProductType("Invalid product type.");// null
			Exception e3 = assertThrows(FlooringMasteryDataValidationException.class, () -> service.validateOrderData(order2));
			System.out.println(e3.getMessage());
			order2.setProductType("Tile");
			// ERROR: The date of the order must be in the future.
			order2.setOrderDate(LocalDate.parse("01-01-1001",DateTimeFormatter.ofPattern("MM-dd-yyyy")));
			Exception e4 = assertThrows(FlooringMasteryDataValidationException.class, () -> service.validateOrderData(order2));
			System.out.println(e4.getMessage());
			order2.setOrderDate(LocalDate.parse("01-01-3001",DateTimeFormatter.ofPattern("MM-dd-yyyy")));
			// ERROR: The Area must be greater than 0.
			order2.setArea((new BigDecimal("0")).setScale(2,RoundingMode.HALF_UP));
			Exception e5 = assertThrows(FlooringMasteryDataValidationException.class, () -> service.validateOrderData(order2));
			System.out.println(e5.getMessage());
			order2.setArea((new BigDecimal("2.115")).setScale(2,RoundingMode.HALF_UP));
			
		}
		
		@Test
		void test3() throws FlooringMasteryDataValidationException, FlooringMasteryPersistenceException, IOException {
					
			// Instance 3
			Order order3 = new Order(0);
			order3.setCustomerName("John");
			order3.setStateAbbrev("TX");
			order3.setProductType("Tile");
			order3.setArea((new BigDecimal("2.115")).setScale(2,RoundingMode.HALF_UP));
			order3.setOrderDate(LocalDate.parse("01-01-3000",DateTimeFormatter.ofPattern("MM-dd-yyyy")));
			
			
			assertNull(service.createNewOrder(order3)); // pass if order1 was added correctly
			assertTrue(order3.getOrderNumber()==1); // pass if the OrderNumber was incremented to the nearest highest number existent in file
			Order order = service.getOrder(order3.getOrderNumber(), order3.getOrderDate());
			
			assertEquals(order3.getOrderNumber(), order.getOrderNumber());
			assertEquals(order3.getCustomerName(), order.getCustomerName());
			assertEquals(order3.getStateAbbrev(), order.getStateAbbrev());
			assertEquals(order3.getProductType(), order.getProductType());
			assertEquals(order3.getArea(), order.getArea());
			assertEquals(order3.getOrderDate(), order.getOrderDate());
			
			Order orderRemoved = service.removeOrder(order3.getOrderNumber(), order3.getOrderDate()); //  remove the order just added, and return the order removed
		}

}
