/**
 * 
 */
package fm.service;

import java.io.IOException;
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

	void createNewOrder(Order order) throws FlooringMasteryDataValidationException, FlooringMasteryPersistenceException, IOException;
	
	List<Order> getAllOrders() throws FlooringMasteryPersistenceException;
	
	List<Product> getAllProducts() throws FlooringMasteryPersistenceException;

	List<Tax> getAllTaxes() throws FlooringMasteryPersistenceException;
	
	Order getOrder(int orderNumber) throws FlooringMasteryPersistenceException;
	
	Order removeOrder(int orderNumber) throws FlooringMasteryPersistenceException, IOException;
	
}
