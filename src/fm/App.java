/**
 * 
 */
package fm;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import fm.controller.FlooringMasteryController;

/**
 * @author Flavio Silva
 *
 */
public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		UserIO myIo = new UserIOConsoleImpl();
		FlooringMasteryView myView = new FlooringMasteryView(myIo);
		FlooringMasteryDao myDao = new FlooringMasteryDaoFileImpl();
		FlooringMasteryServiceLayer myService = new FlooringMasteryServiceLayerImpl(myDao);
		FlooringMasteryController controller = new FlooringMasteryController(myService, myView);
		controller.run();
		*/
		
		// Spring DI
		AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
		appContext.scan("fm");
		appContext.refresh();
		
		FlooringMasteryController controller = appContext.getBean("flooringMasteryController", FlooringMasteryController.class);
		controller.run();
		
	}

}
