package org.sf.jini.examples.remoteconf;

import org.sf.jini.examples.common.JiniClient;
import org.sf.jini.examples.common.SingleServiceLocator;
import org.sf.jini.examples.remote.RemoteFilter;

/**
 * Creates new client for "org.sf.jini.examples.remoteconf" service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class RemoteConfClient extends JiniClient {

  /**
   * Creates new client.
   */
  public RemoteConfClient() {
    super("org.sf.jini.examples.remoteconf");
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
   *
   * @param args the list of arguments
   * @throws Exception the exception
   */
  public static void main(String args[]) throws Exception {
    System.out.println("Started ...");

    SingleServiceLocator serviceLocator = new SingleServiceLocator("jini://localhost");

    JiniClient client = new RemoteConfClient();

    RemoteFilter filter = (RemoteFilter)serviceLocator.locate(client.getServiceTemplate());

    if(filter == null) {
      System.out.println("Filter service did not found.");
    }
    else {
      System.out.println("Got response: " + filter.filter("Where are you?"));
    }
  }

}
