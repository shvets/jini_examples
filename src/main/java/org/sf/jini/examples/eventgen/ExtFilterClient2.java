package org.sf.jini.examples.eventgen;

import net.jini.core.discovery.LookupLocator;
import net.jini.core.lookup.ServiceRegistrar;
import org.sf.jini.examples.common.JiniClient;

/**
 * Creates new client for "eventgen" service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class ExtFilterClient2 extends JiniClient {

  /**
   * Creates new client.
   */
  public ExtFilterClient2() {
    super("eventgen");
  }

  /**
   * Gets the class for looking service
   *
   * @return the class for looking service
   */
  protected Class getServiceClass() {
    return ExtFilter.class;
  }

  /**
   * Main entry point for the client.
   * Here we are trying to find the service, If it exists we invoke setPrefix() method,
   * then filter() method,
   *
   * @param args the list of arguments
   *
   * @throws Exception the exception
   */
  public static void main(String args[]) throws Exception {
    System.out.println("Started ...");

    LookupLocator lookupLocator = new LookupLocator("jini://localhost");

    ServiceRegistrar registrar = lookupLocator.getRegistrar();

    ExtFilterClient2 client = new ExtFilterClient2();

    ExtFilter filter = (ExtFilter)client.lookup(registrar);

    if(filter == null) {
      System.out.println("Filter service did not found.");
    }
    else {
      filter.setPrefix("prefix");

      System.out.println("Get response: " + filter.filter("Where are you?"));
    }
  }

}
