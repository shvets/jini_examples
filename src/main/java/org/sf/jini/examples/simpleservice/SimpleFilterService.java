package org.sf.jini.examples.simpleservice;

import org.sf.jini.examples.common.JiniService;
import org.sf.jini.examples.common.SingleServiceLocator;

/**
 * Creates new "simpleservice" service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class SimpleFilterService extends JiniService {

  /**
   * Creates new service.
   * @throws Exception the exception
   */
  public SimpleFilterService() throws Exception {
    super("simpleservice");
  }

  /**
   * Gets the object that represents the service.
   *
   * @return the proxy object
   */
  protected Object getServiceObject() {
    return new FilterImpl();
  }

  /**
   * Main entry point for the service registration.
   *
   * @param args the list of arguments
   *
   * @throws Exception the exception
   */
  public static void main(String args[]) throws Exception {
    System.out.println("Started ...");

    SingleServiceLocator serviceLocator = new SingleServiceLocator("jini://localhost");

    JiniService service = new SimpleFilterService();

    service.register(serviceLocator, 60*1000);

    System.out.println("Registered.");
  }

}
