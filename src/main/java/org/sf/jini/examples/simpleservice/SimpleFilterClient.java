package org.sf.jini.examples.simpleservice;

import org.sf.jini.examples.common.JiniClient;
import org.sf.jini.examples.common.SingleServiceLocator;

/**
 * Creates new client for "simpleservice" service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class SimpleFilterClient extends JiniClient {

  /**
   * Creates new client.
   */
  public SimpleFilterClient() {
    super("simpleservice");
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
   *
   * @param args the list of arguments
   * @throws Exception the exception
   */
  public static void main(String args[]) throws Exception {
    System.out.println("Started ...");

    SingleServiceLocator serviceLocator = new SingleServiceLocator("jini://localhost");

    JiniClient client = new SimpleFilterClient();

    Filter filter = (Filter)serviceLocator.locate(client.getServiceTemplate());

    if(filter == null) {
      System.out.println("Filter service did not found.");
    }
    else {
      System.out.println("Got response: " + filter.filter("Where are you?"));
    }
  }

}
