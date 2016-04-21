package org.sf.jini.examples.failover;

import org.sf.jini.examples.common.JiniClient;
import org.sf.jini.examples.remote.RemoteFilter;
import net.jini.core.discovery.LookupLocator;
import net.jini.core.lookup.ServiceRegistrar;

/**
 * Creates new client for "failover" service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class FailoverClient extends JiniClient {

  /**
   * Creates new client.
   */
  public FailoverClient() {
    super("failover");
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

    JiniClient client = new FailoverClient();

    RemoteFilter filter = (RemoteFilter)client.lookup(registrar);

    if(filter == null) {
      System.out.println("Filter service did not found.");
    }
    else {
      while(true) {
        System.out.println("Got response: " + filter.filter("Where are you?"));

        Thread.sleep(3000);
      }
    }
  }

}
