package org.sf.jini.examples.federate;

import org.sf.jini.examples.common.JiniClient;
import org.sf.jini.examples.common.FederatedServiceLocator;
import org.sf.jini.examples.remote.RemoteFilter;

import java.io.IOException;
import java.util.List;

/**
 * Creates new client for "federateservice" service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class FederateClient extends JiniClient {

  /**
   * Creates new client.
   */
  public FederateClient() {
    super("federateservice");
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
   * Looks up for the service inside the interval of time.
   *
   * @param waitTime the wait time
   * @return The list of discovered services.
   *
   * @throws IOException I/O exception
   * @throws InterruptedException interrupted exception
   */
  public List lookup(long waitTime) throws IOException, InterruptedException {
    FederatedServiceLocator discoveryListener =
      new FederatedServiceLocator(getServiceTemplate(), waitTime);

    List discoveredServices = discoveryListener.getDiscoveredServices();

    if (discoveredServices.size() == 0) {
      throw new InterruptedException("Services not found within wait time");
    }

    return discoveredServices;
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

    FederateClient client = new FederateClient();

    List services = client.lookup(10*1000);

    System.out.println("Found " + services.size() + " filter service(s).");

    for (Object service : services) {
      RemoteFilter filter = (RemoteFilter) service;

      if (filter == null) {
        System.out.println("Filter service did not found.");
      } else {
        System.out.println("Got response: " + filter.filter("Where are you?"));
      }
    }
  }

}
