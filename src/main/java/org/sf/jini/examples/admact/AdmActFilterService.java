package org.sf.jini.examples.admact;

import java.rmi.RemoteException;
import java.rmi.Remote;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationID;
import java.io.IOException;

import com.sun.jini.lookup.entry.BasicServiceType;
import net.jini.core.entry.Entry;
import net.jini.lookup.entry.ServiceInfo;
import net.jini.config.ConfigurationException;
import net.jini.export.Exporter;
import net.jini.jeri.BasicJeriExporter;
import net.jini.jeri.BasicILFactory;
import net.jini.jeri.tcp.TcpServerEndpoint;
import net.jini.activation.ActivationExporter;
import org.sf.jini.examples.common.ActivatableService;
import org.sf.jini.examples.common.JiniService;
import org.sf.jini.examples.common.SingleServiceLocator;

/**
 * Creates new "admact" service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class AdmActFilterService extends ActivatableService {
  /**
   * Creates new service.
   *
   * @param exporter the exporter
   * @param policyFile the policy file
   * @param codeBase the code base
   * @throws RemoteException the remote exception
   * @throws ActivationException the activation exception
   * @throws ConfigurationException the configuration exception
   */
  public AdmActFilterService(Exporter exporter, String policyFile, String codeBase)
    throws RemoteException, ActivationException, ConfigurationException {
    this(exporter, policyFile, codeBase, "admact");
  }

  /**
   * Creates new service.
   *
   * @param exporter the exporter
   * @param policyFile the policy file
   * @param codeBase the code base
   * @param name the name
   * @throws RemoteException the remote exception
   * @throws ActivationException the activation exception
   * @throws ConfigurationException the configuration exception
   */
  public AdmActFilterService(Exporter exporter, String policyFile, String codeBase, String name)
    throws RemoteException, ActivationException, ConfigurationException {
    createActivatableFactory(policyFile, codeBase);

    //Remote remote = register("org.sf.jini.examples.admact.AdmActFilterImpl", null);
    //createProxyAdapter(exporter);

    ActivationID activationID = activatableFactory.register("org.sf.jini.examples.admact.AdmActFilterImpl", null);
    Remote remote = activationID.activate(true);

    createProxyAdapter(new ActivationExporter(activationID, exporter));

    export(remote);

    ServiceInfo serviceInfo =
            new ServiceInfo(name, // name
                    "Alexander Shvets", // manufacturer
                    "Alexander Shvets", // vendor
                    "Jini 2.1", // version
                    "A", // model
                    "001" // serial number
            );
    BasicServiceType basicServiceType = new BasicServiceType("Service");

    createServiceItem(new Entry[]{serviceInfo, basicServiceType});

    AdmActFilterImpl impl = (AdmActFilterImpl)/*getRemote()*/remote;

    impl.setActivatableFactory(activatableFactory);

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

    AdmActFilterImpl impl = (AdmActFilterImpl)getRemote();

    impl.setServiceRegistration(registration);
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

    SingleServiceLocator serviceLocator = new SingleServiceLocator("jini://localhost");    

    String policyFile = System.getProperty("java.security.policy");
    String codeBase = System.getProperty("java.rmi.activation.codebase");

    Exporter exporter = new BasicJeriExporter(TcpServerEndpoint.getInstance(0), new BasicILFactory());

    JiniService service = new AdmActFilterService(exporter, policyFile, codeBase);

    service.register(serviceLocator, 3 * 60 * 1000);

    System.out.println("Registered.");
  }

}
