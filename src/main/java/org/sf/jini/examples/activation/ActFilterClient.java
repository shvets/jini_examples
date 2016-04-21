package org.sf.jini.examples.activation;

import net.jini.core.discovery.LookupLocator;
import net.jini.core.lookup.ServiceRegistrar;

import org.sf.jini.examples.common.JiniClient;
import org.sf.jini.examples.remote.RemoteFilter;

/**
 * Creates new client for "activation" service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class ActFilterClient extends JiniClient {

  /**
   * Creates new client.
   */
  public ActFilterClient() {
    super("activation");
  }

  /**
   * Gets the class for looking service
   *
   * @return the class for looking service
   */
  protected Class getServiceClass() {
    return RemoteFilter.class;
  }

  /**
   * Main entry point for the client.
   * Here we are trying to find the service, then invoke it.
   *
   * @param args the list of arguments
   *
   * @throws Exception the exception
   */
  public static void main(String args[]) throws Exception {
    System.out.println("Started ...");

    LookupLocator lookupLocator = new LookupLocator("jini://localhost");

    ServiceRegistrar registrar = lookupLocator.getRegistrar();

    JiniClient client = new ActFilterClient();

    RemoteFilter filter = (RemoteFilter)client.lookup(registrar);

    if(filter == null) {
      System.out.println("Filter service did not found.");
    }
    else {
      System.out.println("Got response: " + filter.filter("Where are you?"));
    }
  }

}
