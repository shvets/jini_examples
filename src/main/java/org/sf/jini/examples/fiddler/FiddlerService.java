package org.sf.jini.examples.fiddler;

import java.rmi.RemoteException;

import net.jini.core.discovery.LookupLocator;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.RemoteEvent;
import net.jini.core.event.UnknownEventException;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.discovery.*;
import net.jini.export.Exporter;
import net.jini.jeri.BasicJeriExporter;
import net.jini.jeri.BasicILFactory;
import net.jini.jeri.tcp.TcpServerEndpoint;
import org.sf.jini.examples.common.JiniService;
import org.sf.jini.examples.common.Util;
import org.sf.jini.examples.common.ProxiedRemoteEventListener;
import org.sf.jini.examples.simpleservice.FilterImpl;

/**
 * Creates new "fiddler" service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class FiddlerService extends JiniService {
  /** the remote event listener. */
  protected ProxiedRemoteEventListener remoteEventListener;

  /**
   * Creates new service.
   *
   * @param exporter the exporter
   * @throws Exception the exception
   */
  public FiddlerService(Exporter exporter) throws Exception {
    super("fiddler");

    remoteEventListener = new ProxiedRemoteEventListener(exporter) {
      public void notify(RemoteEvent event) throws UnknownEventException, RemoteException {
        System.out.println("Notified: " + event);

        if(event instanceof RemoteDiscoveryEvent) {
          RemoteDiscoveryEvent remoteDiscoveryEvent = (RemoteDiscoveryEvent)event;

          if(!remoteDiscoveryEvent.isDiscarded()) {
            try {
              ServiceRegistrar [] registrars = remoteDiscoveryEvent.getRegistrars();

              FiddlerService.this.register(registrars[0], 60*1000);

              System.out.println("Registered.");
            }
            catch (LookupUnmarshalException e) {
              ;
            }
          }
        }
      }
    };

    remoteEventListener.export();
  }

  /**
   * Gets the object that represents the service.
   *
   * @return the proxy object
   */
  protected Object getServiceObject() {
    return new FilterImpl();
  }

  /**
   * Gets the remote event listener.
   *
   * @return the remote event listener
   */
  public ProxiedRemoteEventListener getRemoteEventListener() {
    return remoteEventListener;
  }

  /**
   * Main entry point for the service registration.
   *
   * @param args the list of arguments
   *
   * @throws Exception the exception
   */
  public static void main(String args[]) throws Exception {
    System.out.println("Started ...");

    LookupLocator lookupLocator = new LookupLocator("jini://localhost");

    ServiceRegistrar registrar = lookupLocator.getRegistrar();

    LookupDiscoveryService lookupDiscoveryService =
                           Util.getLookupDiscoveryService(registrar);

    // fiddler
    if(lookupDiscoveryService == null ) {
      System.out.println("LookupDiscoveryService cannot be found.");
      return;
    }

    Exporter exporter = new BasicJeriExporter(TcpServerEndpoint.getInstance(0), new BasicILFactory());

    FiddlerService service = new FiddlerService(exporter);

    /*LookupDiscoveryRegistration registration = */lookupDiscoveryService.register(
             LookupDiscovery.ALL_GROUPS,
             null,
             (RemoteEventListener)service.getRemoteEventListener().getProxyAdapter().getProxy(),
             null,
             60*1000);

    //as there is no guarentee that our request for Lease.FOREVER will
    //be granted we will use the LeaseRenewalManager to do renewals for us
/* example 1 todo: does not work
   LeaseRenewalManager leaseRenewalManager = new LeaseRenewalManager();

    leaseRenewalManager.renewFor(
      registration.getLease(), Lease.FOREVER, new LeaseListener() {
        public void notify(LeaseRenewalEvent lre){
          System.out.println("LeaseRenewalEvent "+lre);
        }
      });
*/
  }

}

