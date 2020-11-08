/**
 * 
 */
package fm.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import fm.dao.FlooringMasteryPersistenceException;
import fm.dto.Order;
import fm.dto.Product;
import fm.dto.Tax;

/**
 * @author Flavio Silva
 *
 */
public interface FlooringMasteryServiceLayer {

	Order createNewOrder(Order order) throws FlooringMasteryDataValidationException, FlooringMasteryPersistenceException, IOException;
	
	List<Order> getAllOrders(LocalDate orderDate) throws FlooringMasteryPersistenceException;
	
	List<Product> getAllProducts() throws FlooringMasteryPersistenceException;

	List<Tax> getAllTaxes() throws FlooringMasteryPersistenceException;
	
	Order getOrder(int orderNumber, LocalDate orderDate) throws FlooringMasteryPersistenceException;
	
	Order removeOrder(int orderNumber, LocalDate orderDate) throws FlooringMasteryPersistenceException, IOException;
	
	String[] getProductTypes() throws FlooringMasteryPersistenceException;
	
	String[][] getStates() throws FlooringMasteryPersistenceException;
	
	void exportAllOrders() throws FlooringMasteryPersistenceException, IOException;

	void changeOrderData(Order orderToModify) throws FlooringMasteryDataValidationException, FlooringMasteryPersistenceException;

	Order addOrder(Order order) throws FlooringMasteryPersistenceException, IOException;
	
}
