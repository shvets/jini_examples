package org.sf.jini.examples.mailbox;

import java.io.File;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.rmi.MarshalledObject;

import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.discovery.LookupLocator;
import net.jini.core.lease.Lease;

import net.jini.event.EventMailbox;
import net.jini.event.MailboxRegistration;

import org.sf.jini.examples.common.JiniClient;
import org.sf.jini.examples.common.Util;
import org.sf.jini.examples.eventgen.ExtFilter;

/**
 * Creates new client for "eventgen" service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class MailboxClient1 extends JiniClient {

  /**
   * Creates new client.
   */
  public MailboxClient1() {
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

    LookupLocator lookupLocator = new LookupLocator("jini://localhost");

    ServiceRegistrar registrar = lookupLocator.getRegistrar();

    MailboxClient1 client = new MailboxClient1();

    ExtFilter filter = (ExtFilter)client.lookup(registrar);

    if(filter == null) {
      System.out.println("Filter service did not found.");
    }
    else {
      EventMailbox mailbox = Util.getEventMailbox(registrar);

      if(mailbox == null ) {
        System.out.println("EventMailbox service cannot be found.");
        return;
      }

      MailboxRegistration mailboxRegistration = 
                          mailbox.register(Lease.FOREVER);

      filter.addRemoteEventListener(mailboxRegistration.getListener());

      File file = new File("mymailbox.mb");

      ObjectOutputStream out = 
                         new ObjectOutputStream(new FileOutputStream(file));
      
      MarshalledObject marshalledObject = 
                       new MarshalledObject(mailboxRegistration);

      out.writeObject(marshalledObject);

      out.close();
    }

    System.out.println("Finished ...");    
  }

}
