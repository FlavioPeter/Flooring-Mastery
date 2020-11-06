/**
 * 
 */
package fm.dao;

import java.io.IOException;
import java.util.List;

import fm.dto.Order;
import fm.dto.Product;
import fm.dto.Tax;

/**
 * @author Flavio Silva
 *
 */
public interface FlooringMasteryDao {

	/**
	 * Adds a given Order 
	 * 
	 * 
	 */
	Order addOrder(int orderNumber, Order order) throws FlooringMasteryPersistenceException, IOException;
	
	/**
	 * Return a list of all orders
	 * 
	 * 
	 */
	List<Order> getAllOrders() throws FlooringMasteryPersistenceException;
	
	List<Product> getAllProducts() throws FlooringMasteryPersistenceException;
	
	List<Tax> getAllTaxes() throws FlooringMasteryPersistenceException;
	
	/**
	 * Returns a specific order
	 * 
	 * 
	 */
	Order getOrder(int orderNumber) throws FlooringMasteryPersistenceException;
	
	/**
	 * Removes order associated with a given id
	 * Returns Order being removed or null if it doesn't exist
	 * 
	 */
	Order removeOrder(int orderNumber) throws FlooringMasteryPersistenceException, IOException;	
	
}