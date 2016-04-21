package org.sf.jini.examples.eventclient;

import org.sf.jini.examples.common.JiniService;
import org.sf.jini.examples.common.SingleServiceLocator;
import org.sf.jini.examples.simpleservice.FilterImpl;

/**
 * Creates new "eventclient" service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class EventFilterService extends JiniService {
  
  /**
   * Creates new service.
   */
  public EventFilterService() {
    createServiceItem("eventclient");
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

    JiniService service = new EventFilterService();

    service.register(serviceLocator, 60*1000);

    System.out.println("Registered.");
  }

}
