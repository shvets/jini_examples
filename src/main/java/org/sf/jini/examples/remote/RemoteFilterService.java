// RemoteFilterService.java

package org.sf.jini.examples.remote;

import java.rmi.RemoteException;
import java.rmi.MarshalledObject;
import java.io.IOException;

import net.jini.config.ConfigurationException;
import net.jini.lease.*;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceRegistration;
import net.jini.core.lease.Lease;
import net.jini.core.event.RemoteEventListener;
import net.jini.export.Exporter;
import net.jini.jeri.BasicJeriExporter;
import net.jini.jeri.BasicILFactory;
import net.jini.jeri.tcp.TcpServerEndpoint;
import org.sf.jini.examples.common.ProxiedJiniService;
import org.sf.jini.examples.common.SingleServiceLocator;
import org.sf.jini.examples.common.Util;

/**
 * Creates new "remoteservice" service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class RemoteFilterService extends ProxiedJiniService {


  /**
   * Creates new service.
   *
   * @param exporter the exporter
   * @throws ConfigurationException the configuration exception
   * @throws RemoteException the remote exception
   */
  public RemoteFilterService(Exporter exporter) throws ConfigurationException, RemoteException {
    createProxyAdapter(exporter);
    
    export(new RemoteFilterImpl());

    createServiceItem("remoteservice");
  }

  /**
   * Renews the lease.
   *
   * @param leaseRenewalService the lease renewal service
   * @throws IOException I/O exception
   */
  private void leaseRenew(LeaseRenewalService leaseRenewalService) throws IOException {
    ServiceRegistration serviceRegistration = getServiceRegistration();
    Lease lease = serviceRegistration.getLease();

    MarshalledObject marshalledObject = new MarshalledObject(serviceRegistration);

    LeaseRenewalSet leaseRenewalSet = leaseRenewalService.createLeaseRenewalSet(30*1000);

    leaseRenewalSet.setExpirationWarningListener((RemoteEventListener)getProxy(), 5*1000, marshalledObject);
    leaseRenewalSet.setRenewalFailureListener((RemoteEventListener)getProxy(), null);

    leaseRenewalSet.renewFor(lease, 30*1000);

    Lease renewalSetLease = leaseRenewalSet.getRenewalSetLease();

    System.out.println("Lease for set expires in " +
            (renewalSetLease.getExpiration() - System.currentTimeMillis()));

    System.out.println("Lease for service expires in " +
            (lease.getExpiration() - System.currentTimeMillis()));
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

    ServiceRegistrar serviceRegistrar = serviceLocator.getLookupLocator().getRegistrar();

    LeaseRenewalService leaseRenewalService = Util.getLeaseRenewalService(serviceRegistrar);

    if (leaseRenewalService == null) {
      System.out.println("LeaseRenewalService cannot be found.");
      return;
    }

    Exporter exporter = new BasicJeriExporter(TcpServerEndpoint.getInstance(0), new BasicILFactory());

    RemoteFilterService service = new RemoteFilterService(exporter);
    
    service.register(serviceLocator, 40*1000);

    System.out.println("Registered.");

    service.leaseRenew(leaseRenewalService);
  }

}
