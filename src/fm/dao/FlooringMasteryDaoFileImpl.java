/**
 * 
 */
package fm.dao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import fm.dto.Order;
import fm.dto.Product;
import fm.dto.Tax;

/**
 * @author Flavio Silva
 *
 */

@Component
public class FlooringMasteryDaoFileImpl implements FlooringMasteryDao {

	//@Autowired
	public FlooringMasteryDaoFileImpl(){
		
		LocalDate date = LocalDate.now();
		this.ORDER_FILE = "orders_"+date.getYear()+date.getMonth()+date.getDayOfWeek()+".txt";
	}
	
	public final String ORDER_FILE;
	public final String PRODUCT_FILE = "Products.txt";
	public final String TAX_FILE = "Taxes.txt";
	public static final String DELIMITER = "::";
	
	private Map<Integer, Order> orders = new HashMap<>();
	private Map<String, Product> products = new HashMap<>();
	private Map<String, Tax> taxes = new HashMap<>();
	
	@Override
	public Order addOrder(int orderNumber, Order order) throws FlooringMasteryPersistenceException, IOException {
		loadOrder();loadProduct();loadTax();
		Order newOrder = orders.put(orderNumber, order);
		writeOrder(orders);
		return newOrder;
	}
	
	@Override
	public List<Order> getAllOrders() throws FlooringMasteryPersistenceException{
		loadOrder();
		return new ArrayList<Order>(orders.values());
	}
	
	@Override
	public List<Product> getAllProducts() throws FlooringMasteryPersistenceException{
		loadProduct();
		return new ArrayList<Product>(products.values());
	}
	
	@Override
	public List<Tax> getAllTaxes() throws FlooringMasteryPersistenceException{
		loadTax();
		return new ArrayList<Tax>(taxes.values());
	}
	
	@Override
	public Order getOrder(int orderNumber) throws FlooringMasteryPersistenceException {
		loadOrder();
		return orders.get(orderNumber);
	}
	
	@Override
	public Order removeOrder(int orderNumber) throws FlooringMasteryPersistenceException, IOException {
		loadOrder();
		Order removedOrder = orders.remove(orderNumber);
		writeOrder(orders);
		return removedOrder;
	}
	
	private Order unmarshallOrder(String orderAsText) {
		//<OrderNumber>::<CustomerName>::<StateAbbrev>::<TaxRate>::<ProductType>::<Area>::<CostPerSquareFoot>::<LaborCostPerSquareFoot>::<MaterialCost>::<LaborCost>::<Tax>::<Total>
		//     [0]            [1]             [2]          [3]          [4]        [5]           [6]                     [7]                   [8]           [9]       [10]   [11]
		
		String[] orderTokens = orderAsText.split(DELIMITER);
		
		String orderNumber = orderTokens[0]; //[0]
		
		Order orderFromFile = new Order(Integer.parseInt(orderNumber));
		
		orderFromFile.setCustomerName(orderTokens[1]);//[1]
		
		orderFromFile.setStateAbbrev(orderTokens[2]);//[2]
		
		orderFromFile.setTaxRate(new BigDecimal(orderTokens[3]));//[3]
		
		orderFromFile.setProductType(orderTokens[4]);//[4]
		
		orderFromFile.setArea(new BigDecimal(orderTokens[5]));//[5]
		
		orderFromFile.setCostPerSquareFoot(new BigDecimal(orderTokens[6]));//[6]
		
		orderFromFile.setLaborCostPerSquareFoot(new BigDecimal(orderTokens[7]));//[7]
		
		orderFromFile.setMaterialCost(new BigDecimal(orderTokens[8]));//[8]
		
		orderFromFile.setLaborCost(new BigDecimal(orderTokens[9]));//[9]
		
		orderFromFile.setTax(new BigDecimal(orderTokens[10]));//[10]
		
		orderFromFile.setTotal(new BigDecimal(orderTokens[11]));//[11]
		
		return orderFromFile;
	}
	
	private Product unmarshallProduct(String productAsText) {
		//<ProductType>::<CostPerSquareFoot>::<LaborCostPerSquareFoot>
		//     [0]              [1]                      [2]          
		
		String[] productTokens = productAsText.split(DELIMITER);
		
		String productType = productTokens[0]; //[0]
		
		Product productFromFile = new Product(productType);
		
		productFromFile.setCostPerSquareFoot(new BigDecimal(productTokens[1]));//[1]
		
		productFromFile.setLaborCostPerSquareFoot(new BigDecimal(productTokens[2]));//[2]
		
		return productFromFile;
	}
	
	private Tax unmarshallTax(String taxAsText) {
		//<StateAbbreviation>::<StateName>::<TaxRate>
		//       [0]               [1]         [2]          
		
		String[] taxTokens = taxAsText.split(DELIMITER);
		
		String stateAbbreviation = taxTokens[0]; //[0]
		
		Tax taxFromFile = new Tax(stateAbbreviation);
		
		taxFromFile.setStateName(taxTokens[1]);//[1]
		
		taxFromFile.setTaxRate(new BigDecimal(taxTokens[2]));//[2]
		
		return taxFromFile;
	}
	
	private void loadOrder() throws FlooringMasteryPersistenceException{
		
		Scanner scanner;
		
		try {
			scanner = new Scanner(new BufferedReader(new FileReader(ORDER_FILE)));
		}catch(FileNotFoundException e) {
			throw new FlooringMasteryPersistenceException("-_- Could not load order data into memory.",e);
		}
		
		String currentLine;
		
		Order currentOrder;
		
		currentLine = scanner.nextLine();//get rid of the header, maybe use it later for displayList
		
		while(scanner.hasNextLine()) {
			
			currentLine = scanner.nextLine();
			
			currentOrder = unmarshallOrder(currentLine);
			
			orders.put(currentOrder.getOrderNumber(), currentOrder);
		}
		scanner.close();
	}
	
	private void loadProduct() throws FlooringMasteryPersistenceException{
		
		Scanner scanner;
		
		try {
			scanner = new Scanner(new BufferedReader(new FileReader(PRODUCT_FILE)));
		}catch(FileNotFoundException e) {
			throw new FlooringMasteryPersistenceException("-_- Could not load product data into memory.",e);
		}
		
		String currentLine;
		
		Product currentProduct;
		
		currentLine = scanner.nextLine();//get rid of the header, maybe use it later for displayList
		
		while(scanner.hasNextLine()) {
			
			currentLine = scanner.nextLine();
			
			currentProduct = unmarshallProduct(currentLine);
			
			products.put(currentProduct.getProductType(), currentProduct);
		}
		scanner.close();
	}
	
	private void loadTax() throws FlooringMasteryPersistenceException{
		
		Scanner scanner;
		
		try {
			scanner = new Scanner(new BufferedReader(new FileReader(TAX_FILE)));
		}catch(FileNotFoundException e) {
			throw new FlooringMasteryPersistenceException("-_- Could not load tax data into memory.",e);
		}
		
		String currentLine;
		
		Tax currentTax;
		
		currentLine = scanner.nextLine();//get rid of the header, maybe use it later for displayList
		
		while(scanner.hasNextLine()) {
			
			currentLine = scanner.nextLine();
			
			currentTax = unmarshallTax(currentLine);
			
			taxes.put(currentTax.getStateAbbreviation(), currentTax);
		}
		scanner.close();
	}
	
	private String marshallOrder(Order aOrder) {
		// turn object into text
		
		//<OrderNumber>0
		//<CustomerName>1
		//<StateAbbrev>2
		//<TaxRate>3
		//<ProductType>4
		//<Area>5
		//<CostPerSquareFoot>6
		//<LaborCostPerSquareFoot>7
		//<MaterialCost>8
		//<LaborCost>9
		//<Tax>10
		//<Total>11
		
		String orderAsText = aOrder.getOrderNumber()+DELIMITER; //0
		
		orderAsText += aOrder.getCustomerName()+DELIMITER;//1
		
		orderAsText += aOrder.getStateAbbrev()+DELIMITER;//2
		
		orderAsText += aOrder.getTaxRate()+DELIMITER;//3
		
		orderAsText += aOrder.getProductType()+DELIMITER;//4
		
		orderAsText += aOrder.getArea()+DELIMITER;//5
		
		orderAsText += aOrder.getCostPerSquareFoot()+DELIMITER;//6
		
		orderAsText += aOrder.getLaborCostPerSquareFoot()+DELIMITER;//7
		
		orderAsText += aOrder.getMaterialCost()+DELIMITER;//8
		
		orderAsText += aOrder.getLaborCost()+DELIMITER;//9
		
		orderAsText += aOrder.getTax()+DELIMITER;//10
		
		orderAsText += aOrder.getTotal();//11
		
		return orderAsText;
	}
	
	private void writeOrder(Map <Integer,Order> orders) throws FlooringMasteryPersistenceException, IOException {
        PrintWriter scanner;
        try {
            scanner = new PrintWriter(new FileWriter(ORDER_FILE));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException( "-_- Could not write orders into memory.", e);
        }
        
        for(Entry<Integer, Order> thisOrder: orders.entrySet()){
            scanner.println(marshallOrder(thisOrder.getValue()));
        }
        scanner.close();
    }
}
