package org.sf.jini.examples.eventclient;

import java.rmi.RemoteException;

import net.jini.core.discovery.LookupLocator;
import net.jini.core.event.RemoteEvent;
import net.jini.core.event.UnknownEventException;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.export.Exporter;
import net.jini.jeri.tcp.TcpServerEndpoint;
import net.jini.jeri.BasicJeriExporter;
import net.jini.jeri.BasicILFactory;
import org.sf.jini.examples.common.NotifiedClient;
import org.sf.jini.examples.common.NotifiedRemoteEventListener;
import org.sf.jini.examples.simpleservice.Filter;

/**
 * Creates new client for "eventclient" service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class EventFilterClient extends NotifiedClient {

  /**
   * Creates new client.
   *
   * @param exporter the exporter
   * @throws RemoteException the remote exception
   */
  public EventFilterClient(Exporter exporter) throws RemoteException {
    setRemoteEventListener(
      new NotifiedRemoteEventListener(exporter) {
      public void notify(RemoteEvent event) throws UnknownEventException, RemoteException {
        System.out.println("Got an event:" + event.getSource());

        super.notify(event);

        notified();
      }
    });

    remoteEventListener.export();

    createServiceTemplate("eventclient");
  }

  /**
   * Gets the class for looking service
   *
   * @return the class for looking service
   */
  protected Class getServiceClass() {
    return Filter.class;
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

    Exporter exporter = new BasicJeriExporter(TcpServerEndpoint.getInstance(0), new BasicILFactory());

    EventFilterClient client = new EventFilterClient(exporter);

    Filter filter = (Filter)client.lookup(registrar);

    if(filter == null) {
      System.out.println("Filter doesn't exist. Trying to register for events. Waiting...");

      filter = (Filter)client.register(registrar, 10*60*1000);
    }

    System.out.println("Got response: " + filter.filter("Where are you?"));
  }

}
