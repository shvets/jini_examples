package org.sf.jini.examples.adm;

import org.sf.jini.examples.common.ProxiedJiniService;
import org.sf.jini.examples.common.SingleServiceLocator;
import net.jini.lease.LeaseListener;
import net.jini.lease.LeaseRenewalEvent;
import net.jini.config.ConfigurationException;
import net.jini.export.Exporter;
import net.jini.jeri.BasicJeriExporter;
import net.jini.jeri.BasicILFactory;
import net.jini.jeri.tcp.TcpServerEndpoint;

import java.rmi.RemoteException;
import java.io.IOException;

/**
 * Creates new "adm" service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class AdmFilterService extends ProxiedJiniService implements LeaseListener {
  /** The service implementation. */
  private AdmFilterImpl impl = new AdmFilterImpl();

  /**
   * Creates new service.
   *
   * @param exporter the exporter
   * @throws ConfigurationException the configuration exception
   * @throws RemoteException the remote exception
   */
  public AdmFilterService(Exporter exporter) throws ConfigurationException, RemoteException {
    createProxyAdapter(exporter);

    export(impl);

    createServiceItem("adm");

    impl.setAdmin(getServiceObject());
  }

  /**
   * Registers the service with the help of the service locator.
   *
   * @param serviceLocator service locator
   * @param leaseTime lease time
   * @throws IOException IOException
   * @throws ClassNotFoundException ClassNotFoundException
   */
  public void register(SingleServiceLocator serviceLocator, int leaseTime)
         throws IOException, ClassNotFoundException {
    super.register(serviceLocator, leaseTime);

    impl.setServiceRegistration(registration);
  }

  /**
   * Implementation of LeaseListener interface.
   *
   * @param event the event
   */
  public void notify(LeaseRenewalEvent event) {
    System.out.println("Lease renewed.");
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

    SingleServiceLocator serviceLocator = new SingleServiceLocator("jini://localhost");    

    Exporter exporter = new BasicJeriExporter(TcpServerEndpoint.getInstance(0), new BasicILFactory());

    AdmFilterService service = new AdmFilterService(exporter);

    service.register(serviceLocator, 60*1000);

    System.out.println("Registered.");
  }

}
