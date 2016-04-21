package org.sf.jini.examples.mailbox;

import org.sf.jini.examples.common.JiniClient;
import org.sf.jini.examples.common.SingleServiceLocator;
import org.sf.jini.examples.eventgen.ExtFilter;

/**
 * Creates new client for "eventgen" service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class MailboxClient2 extends JiniClient {
   
  /**
   * Creates new client.
   */
  public MailboxClient2() {
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
   *
   * @param args the list of arguments
   * @throws Exception the exception
   */
  public static void main(String args[]) throws Exception {
    System.out.println("Started ...");

    SingleServiceLocator serviceLocator = new SingleServiceLocator("jini://localhost");

    MailboxClient2 client = new MailboxClient2();

    ExtFilter filter = (ExtFilter)serviceLocator.locate(client.getServiceTemplate());

    if(filter == null) {
      System.out.println("Filter service did not found.");
    }
    else {
      System.out.println("Setting prefix to prefix1");
      filter.setPrefix("prefix1");

      System.out.println("Setting prefix to prefix2");
      filter.setPrefix("prefix2");

      System.out.println("Setting prefix to prefix3");
      filter.setPrefix("prefix3");
    }

    System.out.println("Finished ...");
  }

}
