package org.sf.jini.examples.norm;

import org.sf.jini.examples.common.ActivatableService;
import org.sf.jini.examples.common.Util;
import org.sf.jini.examples.common.SingleServiceLocator;

import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationID;
import java.rmi.RemoteException;
import java.rmi.Remote;

import net.jini.config.ConfigurationException;
import net.jini.core.discovery.LookupLocator;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceRegistration;
import net.jini.core.lease.Lease;
import net.jini.lease.LeaseRenewalService;
import net.jini.lease.LeaseRenewalSet;
import net.jini.export.Exporter;
import net.jini.jeri.BasicJeriExporter;
import net.jini.jeri.BasicILFactory;
import net.jini.jeri.tcp.TcpServerEndpoint;
import net.jini.activation.ActivationExporter;

/**
 * Creates new "normservice" service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class NormService1 extends ActivatableService {

  /**
   * Creates new service.
   *
   * @param exporter the exporter
   * @param policyFile the policy file
   * @param codeBase the code base
   * @throws ActivationException the activation exception
   * @throws RemoteException the remote exception
   * @throws ConfigurationException the configuration exception
   */
  public NormService1(Exporter exporter, String policyFile, String codeBase)
         throws ActivationException, RemoteException, ConfigurationException {
    createActivatableFactory(policyFile, codeBase);

//    Remote remote = register("org.sf.jini.examples.activation.ActFilterImpl", null);
//    createProxyAdapter(exporter);

    ActivationID activationID = activatableFactory.register("org.sf.jini.examples.activation.ActFilterImpl", null);
    Remote remote = activationID.activate(true);

    createProxyAdapter(new ActivationExporter(activationID, exporter));

    export(remote);

    createServiceItem("normservice");
  }

  /**
   * Main entry point for the service registration.
   *
   * @param args the list of arguments
   *
   * @throws Exception the exception
   */
  public static void main(String[] args) throws Exception {
    System.out.println("Started ...");

    LookupLocator lookupLocator = new LookupLocator("jini://localhost");

    ServiceRegistrar registrar = lookupLocator.getRegistrar();

    LeaseRenewalService leaseRenewalService = Util.getLeaseRenewalService(registrar);

    if (leaseRenewalService == null) {
      System.out.println("LeaseRenewalService cannot be found.");
      return;
    }

    String policyFile = System.getProperty("java.security.policy");
    String codeBase = System.getProperty("java.rmi.activation.codebase");
    String codeBase2 = System.getProperty("java.rmi.activation2.codebase");

    SingleServiceLocator serviceLocator = new SingleServiceLocator("jini://localhost");

    Exporter exporter = new BasicJeriExporter(TcpServerEndpoint.getInstance(0), new BasicILFactory());

    NormService1 service1 = new NormService1(exporter, policyFile, codeBase);
    service1.register(serviceLocator, 60*1000);

    ServiceRegistration serviceRegistration1 = service1.getServiceRegistration();
    Lease lease1 = serviceRegistration1.getLease();

    NormService2 service2 = new NormService2(exporter, policyFile, codeBase2, leaseRenewalService, 10*60*1000);

    LeaseRenewalSet leaseRenewalSet2 = service2.getLeaseRenewalSet();
    Lease renewalSetLease2 = leaseRenewalSet2.getRenewalSetLease();

    leaseRenewalSet2.renewFor(lease1, 30*1000);

    System.out.println("Lease for set expires in " +
            (renewalSetLease2.getExpiration() - System.currentTimeMillis()));

    System.out.println("Lease for service1 expires in " +
            (lease1.getExpiration() - System.currentTimeMillis()));

    System.out.println("Registered.");
  }

}
