package org.sf.jini.examples.eventgen;

import java.rmi.RemoteException;

import net.jini.core.discovery.LookupLocator;
import net.jini.core.event.RemoteEvent;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.UnknownEventException;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.export.Exporter;
import net.jini.jeri.BasicJeriExporter;
import net.jini.jeri.BasicILFactory;
import net.jini.jeri.tcp.TcpServerEndpoint;
import org.sf.jini.examples.common.ListenerClient;
import org.sf.jini.examples.common.ProxiedRemoteEventListener;
import org.sf.jini.examples.common.Locker;

/**
 * Creates new client for "eventgen" service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class ExtFilterClient1 extends ListenerClient {

  /**
   * Creates new client-listener.
   *
   * @param exporter the exporter
   * @throws RemoteException the remote exception
   */
  public ExtFilterClient1(Exporter exporter) throws RemoteException {
    setRemoteEventListener(new ProxiedRemoteEventListener(exporter) {
      public void notify(RemoteEvent event) throws UnknownEventException, RemoteException {
        System.out.println("notified: " + event);

        if (event instanceof PrefixEvent) {
          PrefixEvent prefixEvent = (PrefixEvent) event;

          System.out.println("The prefix has changed: " + prefixEvent.getPrefix());
        }
      }
    });

    remoteEventListener.export();

    createServiceTemplate("eventgen");
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
   * Here we are trying to find the service, then invoke it. If the service is not registered yet,
   * the client will wait for it.
   *
   * @param args the list of arguments
   *
   * @throws Exception the exception
   */
  public static void main(String args[]) throws Exception {
    System.out.println("Started ...");

    LookupLocator lookupLocator = new LookupLocator("jini://localhost");

    ServiceRegistrar registrar = lookupLocator.getRegistrar();

    Exporter exporter = new BasicJeriExporter(TcpServerEndpoint.getInstance(0), new BasicILFactory());

    ExtFilterClient1 client = new ExtFilterClient1(exporter);

    ExtFilter filter = (ExtFilter) client.lookup(registrar);

    if (filter == null) {
      System.out.println("Filter service did not found.");
    }
    else {
      System.out.println("Adding remote event listener.");

      ProxiedRemoteEventListener remoteEventListener = client.getRemoteEventListener();

      filter.addRemoteEventListener((RemoteEventListener) remoteEventListener.getProxyAdapter().getProxy());

      Locker locker = new Locker();

      locker.lock();
    }
  }

}
