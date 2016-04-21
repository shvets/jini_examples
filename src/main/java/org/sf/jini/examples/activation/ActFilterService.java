package org.sf.jini.examples.activation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import java.rmi.Remote;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationID;

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
 * Creates new "activation" service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class ActFilterService extends ActivatableService {

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
  public ActFilterService(Exporter exporter, String policyFile, String codeBase)
    throws ActivationException, RemoteException, ConfigurationException {
    this(exporter, policyFile, codeBase, "activation");
  }

  /**
   * Creates new service.
   *
   * @param exporter the exporter
   * @param policyFile the policy file
   * @param codeBase the code base
   * @param name the name
   * @throws ActivationException the activation exception
   * @throws RemoteException the remote exception
   * @throws ConfigurationException the configuration exception
   */
  public ActFilterService(Exporter exporter, String policyFile, String codeBase, String name)
    throws ActivationException, RemoteException, ConfigurationException {
    createActivatableFactory(policyFile, codeBase);
    
    //Remote remote = register("org.sf.jini.examples.activation.ActFilterImpl", null);
    ActivationID activationID = activatableFactory.register("org.sf.jini.examples.activation.ActFilterImpl", null);
    Remote remote = activationID.activate(true);

    createProxyAdapter(new ActivationExporter(activationID, exporter));

    export(remote);

    createServiceItem(name);
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

    String policyFile = System.getProperty("java.security.policy");
    String codeBase = System.getProperty("java.rmi.activation.codebase");

    Exporter exporter = new BasicJeriExporter(TcpServerEndpoint.getInstance(0), new BasicILFactory());

    JiniService service = new ActFilterService(exporter, policyFile, codeBase);

    service.register(serviceLocator, 3*60*1000);

    System.out.println("Registered.");

    System.out.println("Serializing service registration .... ");

    File file = new File("activation.reg");

    ObjectOutputStream out =
            new ObjectOutputStream(new FileOutputStream(file));

    MarshalledObject marshalledObject =
            new MarshalledObject(service.getServiceRegistration());

    out.writeObject(marshalledObject);

    out.close();

    System.out.println("Serialized. ");
  }

}
