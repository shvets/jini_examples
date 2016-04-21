package org.sf.jini.examples.mailbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.rmi.RemoteException;
import java.rmi.RMISecurityManager;
import java.rmi.MarshalledObject;

import net.jini.core.event.RemoteEvent;
import net.jini.core.event.UnknownEventException;
import net.jini.core.event.RemoteEventListener;
import net.jini.event.MailboxRegistration;
import net.jini.jeri.BasicJeriExporter;
import net.jini.jeri.BasicILFactory;
import net.jini.jeri.tcp.TcpServerEndpoint;
import net.jini.export.Exporter;

import org.sf.jini.examples.eventgen.PrefixEvent;
import org.sf.jini.examples.common.ProxiedRemoteEventListener;

/**
 * Reads the information from serialized mailbox.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class MailboxReader extends ProxiedRemoteEventListener {

  static {
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new RMISecurityManager());
    }
  }

  /**
   * Creates new mailbox reader.
   *
   * @param exporter the exporter
   * @throws RemoteException the remote exception
   */
  public MailboxReader(Exporter exporter) throws RemoteException {
    super(exporter);

    export();
  }

  /**
   * This method is executed when new event gets created.
   *
   * @param event the event
   * @throws UnknownEventException the unknown event exception
   * @throws RemoteException the remote exception
   */
  public void notify(RemoteEvent event)
              throws UnknownEventException, RemoteException {
    System.out.println("In notify() method.");

    if(event instanceof PrefixEvent) {
      PrefixEvent prefixEvent = (PrefixEvent)event;

      System.out.println("The prefix has changed: " + prefixEvent.getPrefix());
    }
  }

  /**
   * Main entry point for the mailbox reader.
   *
   * @param args the list of arguments
   * @throws Exception the exception
   */
  public static void main(String args[]) throws Exception {
    Exporter exporter = new BasicJeriExporter(TcpServerEndpoint.getInstance(0), new BasicILFactory());

    MailboxReader reader = new MailboxReader(exporter);

    File file = new File("mymailbox.mb");

    if(!file.exists()) {
      System.out.println("File mymailbox.mb doesn't exist");
      return;
    }

    ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));

    MarshalledObject marshalledObject = (MarshalledObject)in.readObject();

    MailboxRegistration mailboxRegistration =
                        (MailboxRegistration)marshalledObject.get();

    mailboxRegistration.enableDelivery((RemoteEventListener)reader.getProxyAdapter().getProxy());

    in.close();
  }

}
