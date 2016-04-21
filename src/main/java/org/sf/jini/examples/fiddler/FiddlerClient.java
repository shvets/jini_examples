package org.sf.jini.examples.fiddler;

import org.sf.jini.examples.common.JiniClient;
import org.sf.jini.examples.common.SingleServiceLocator;
import org.sf.jini.examples.simpleservice.Filter;

/**
 * Creates new client for "fiddler" service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class FiddlerClient extends JiniClient {

  /**
   * Creates new client.
   */
  public FiddlerClient() {
    super("fiddler");
  }

  /**
   * Gets the class for looking service
   *
   * @return the class for looking service
   */
  protected Class getServiceClass() {
    return Filter.class;
  }
    
  /**
   * Main entry point for the client.
   * Here we are trying to find the service, then invoke it.
   *
   * @param args the list of arguments
   * @throws Exception the exception
   */
  public static void main(String args[]) throws Exception {
    System.out.println("Started ...");

    SingleServiceLocator serviceLocator = new SingleServiceLocator("jini://localhost");

    JiniClient client = new FiddlerClient();

    Filter filter = (Filter)serviceLocator.locate(client.getServiceTemplate());

    if(filter == null) {
      System.out.println("Filter service did not found.");
    }
    else {
      System.out.println("Got response: " + filter.filter("Where are you?"));
    }
  }

}
