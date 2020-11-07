/**
 * 
 */
package fm.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	private String ORDER_FILE;
	private static final String PRODUCT_FILE = "Products.txt";
	private static final String TAX_FILE = "Taxes.txt";
	private static final String DELIMITER = "::";
	
	private Map<Integer, Order> orders = new HashMap<>();
	private Map<String, Product> products = new HashMap<>();
	private Map<String, Tax> taxes = new HashMap<>();
	
	@Override
	public Order addOrder(int orderNumber, Order order) throws FlooringMasteryPersistenceException, IOException {
		String orderFileName = orderFileNameSyntax(order.getOrderDate());
		boolean doesTheFileExist = checkIfFileForThatDateExists(orderFileName);
		if(doesTheFileExist) {
			loadOrder(orderFileName);loadProduct();loadTax();// maybe not necessary the other loads
		}
		Order newOrder = orders.put(orderNumber, order);
		writeOrder(orders, orderFileName);
		return newOrder;
	}
	
	@Override
	public List<Order> getAllOrders(LocalDate orderDate) throws FlooringMasteryPersistenceException{
		String orderFileName = orderFileNameSyntax(orderDate);
		loadOrder(orderFileName);
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
	public Order getOrder(int orderNumber, LocalDate orderDate) throws FlooringMasteryPersistenceException {
		String orderFileName = orderFileNameSyntax(orderDate);
		loadOrder(orderFileName);
		return orders.get(orderNumber);
	}
	
	@Override
	public Order removeOrder(int orderNumber, LocalDate orderDate) throws FlooringMasteryPersistenceException, IOException {
		String orderFileName = orderFileNameSyntax(orderDate);
		loadOrder(orderFileName);
		Order removedOrder = orders.remove(orderNumber);
		writeOrder(orders, orderFileName);
		return removedOrder;
	}
	@Override
	public List<Order> exportAllOrders() throws FlooringMasteryPersistenceException{
		File direct = new File("FileData/Orders/");
		
		File[] fileArr = direct.listFiles();
		
		for(File file : fileArr) {
			loadOrder(file.getPath());
		}
	}
	
	private Order unmarshallOrder(String orderAsText) {
		//<OrderNumber>::<CustomerName>::<StateAbbrev>::<TaxRate>::<ProductType>::<Area>::<CostPerSquareFoot>::<LaborCostPerSquareFoot>::<MaterialCost>::<LaborCost>::<Tax>::<Total>::<OrderDate>
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
		
		orderFromFile.setOrderDate(LocalDate.parse(orderTokens[12]));//[12]
		
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
	
	private void loadOrder(String orderFileName) throws FlooringMasteryPersistenceException{
		
		File file;
		
		// Check if the path is ready. If it is, then just assign it to ORDER_FILE
		if(orderFileName.contains("\'")) { // check if this logic actually works
			ORDER_FILE = orderFileName;
		}else {
			file = new File("FileData/Orders/"+orderFileName);
			ORDER_FILE = file.getPath();
		}
		
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
			scanner = new Scanner(new BufferedReader(new FileReader("FileData/Data/"+PRODUCT_FILE)));
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
			scanner = new Scanner(new BufferedReader(new FileReader("FileData/Data/"+TAX_FILE)));
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
		
		orderAsText += aOrder.getTotal()+DELIMITER;//11
		
		orderAsText += aOrder.getOrderDate();//12
		
		return orderAsText;
	}
	
	private void writeOrder(Map <Integer,Order> orders, String orderFileName) throws FlooringMasteryPersistenceException, IOException {
        
		File file = new File("FileData/Orders/"+orderFileName);
		
		ORDER_FILE = file.getPath();
		
		PrintWriter scanner;
        try {
            scanner = new PrintWriter(new FileWriter(ORDER_FILE));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException( "-_- Could not write orders into memory.", e);
        }
        
        file.createNewFile();
        scanner.println("OrderNumber::CustomerName::State::TaxRate::ProductType::Area::CostPerSquareFoot::LaborCostPerSquareFoot::MaterialCost::LaborCost::Tax::Total::Date");
        for(Entry<Integer, Order> thisOrder: orders.entrySet()){
            scanner.println(marshallOrder(thisOrder.getValue()));
        }
        scanner.close();
    }
	
	private String orderFileNameSyntax(LocalDate orderDate) {
		return "Orders_"+orderDate.format(DateTimeFormatter.ofPattern("MMddyyyy"))+".txt";
	}
	
	private boolean checkIfFileForThatDateExists(String orderFileName) {
		File file = new File("FileData/Orders/");
		
		File[] fileArr = file.listFiles();
		
		return Arrays.stream(fileArr).map((f) -> f.getPath()).anyMatch((f) -> f.contains(orderFileName));
	}
	
	
}
