package org.sf.jini.examples.federate;

import org.sf.jini.examples.common.ProxiedJiniService;
import org.sf.jini.examples.common.SingleServiceLocator;
import org.sf.jini.examples.remote.RemoteFilterImpl;
import net.jini.config.ConfigurationException;
import net.jini.export.Exporter;
import net.jini.jeri.BasicJeriExporter;
import net.jini.jeri.BasicILFactory;
import net.jini.jeri.tcp.TcpServerEndpoint;

import java.rmi.RemoteException;

/**
 * Creates new "federateservice" service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class FederateService extends ProxiedJiniService {

  /**
   * Creates new service.
   *
   * @param exporter the exporter
   * @throws ConfigurationException the configuration exception
   * @throws RemoteException the remote exception
   */
  public FederateService(Exporter exporter) throws ConfigurationException, RemoteException {
    createProxyAdapter(exporter);

    //setRemote(new RemoteFilterImpl());

    export(new RemoteFilterImpl());

    createServiceItem("federateservice");
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

    SingleServiceLocator serviceLocator1 = new SingleServiceLocator(args[0]);
    SingleServiceLocator serviceLocator2 = new SingleServiceLocator(args[1]);

    Exporter exporter1 = new BasicJeriExporter(TcpServerEndpoint.getInstance(0), new BasicILFactory());
    Exporter exporter2 = new BasicJeriExporter(TcpServerEndpoint.getInstance(0), new BasicILFactory());

    FederateService service1 = new FederateService(exporter1);
    FederateService service2 = new FederateService(exporter2);

    service1.register(serviceLocator1, 5*60*1000);
    
    System.out.println("Registered service1 on registrar1.");

    service2.register(serviceLocator2, 5*60*1000);

    System.out.println("Registered service2 on registrar2.");
  }

}
