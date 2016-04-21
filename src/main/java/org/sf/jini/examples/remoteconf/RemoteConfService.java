package org.sf.jini.examples.remoteconf;

import java.io.IOException;

import com.sun.jini.start.LifeCycle;
import net.jini.config.ConfigurationException;
import net.jini.config.Configuration;
import net.jini.config.ConfigurationProvider;
import net.jini.export.Exporter;
import org.sf.jini.examples.common.ProxiedJiniService;
import org.sf.jini.examples.common.SingleServiceLocator;
import org.sf.jini.examples.remote.RemoteFilterImpl;

/**
 * Creates new "remoteservice" service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class RemoteConfService extends ProxiedJiniService {
  /** The service name. */
  private static final String MODULE = "org.sf.jini.examples.remoteconf";

  /**
   * Creates new service.
   *
   * @param configArgs the configuration arguments
   * @param lifeCycle the life cycle object
   * @throws ConfigurationException the configuration exception
   * @throws IOException I/O exception
   * @throws ClassNotFoundException class not found exception
   */
  public RemoteConfService(final String[] configArgs, final LifeCycle lifeCycle)
    throws ConfigurationException, IOException, ClassNotFoundException {
      Exporter exporter = createExporter(configArgs);

      createProxyAdapter(exporter);

/*    new Thread() {
      public void run() {
        try {
          init(configArgs);
        }
        catch (Throwable t) {
          t.printStackTrace();
        }
        finally {
          lifeCycle.unregister(RemoteConfService.this);
        }
      }
    }.start();*/

    init(configArgs);
  }

  /**
   * Creates new exporter.
   *
   * @param args the array of arguments
   * @return exporter
   * @throws ConfigurationException the configuration exception
   */
  protected Exporter createExporter(String[] args) throws ConfigurationException {
    Configuration config = ConfigurationProvider.getInstance(args);

    return (Exporter) config.getEntry(MODULE, "serverExporter", Exporter.class);
  }

  /**
   * Inits the service.
   *
   * @param configArgs the configuration arguments
   * @throws IOException I/O exception
   * @throws ConfigurationException the configuration exception
   * @throws ClassNotFoundException class not found exception
   */
  private void init(String[] configArgs) throws IOException, ConfigurationException, ClassNotFoundException {
    System.out.println("Started ...");

    //LookupLocator lookupLocator = new LookupLocator("jini://localhost");

    //ServiceRegistrar registrar = lookupLocator.getRegistrar();

//    Configuration config = ConfigurationProvider.getInstance(configArgs);

//    Exporter exporter = (Exporter) config.getEntry(MODULE, "serverExporter", Exporter.class);

    //setRemote(new RemoteFilterImpl());

    export(new RemoteFilterImpl());

    createServiceItem(MODULE);

/*   File serviceIdFile = (File) config.getEntry(MODULE, "serviceIdFile", File.class);

    ServiceID serviceID = loadServiceId(serviceIdFile);

    if(serviceID != null) {
      ServiceItem serviceItem = getServiceItem();

      serviceItem.serviceID = serviceID;
    }

    System.out.println("Found service ID in file " + serviceIdFile);
*/

    SingleServiceLocator serviceLocator = new SingleServiceLocator("jini://localhost");

    register(serviceLocator, 3*60*1000);

    System.out.println("Registered.");

//    storeServiceId(serviceIdFile);
//    System.out.println("Service id saved in " +  serviceIdFile);
  }

/*  protected Exporter createExporter() throws ConfigurationException {
    return new BasicJeriExporter(TcpServerEndpoint.getInstance(0),
                                 new BasicILFactory());
  }
*/

/*  protected Object getServiceObject() {
    return new RemoteFilterImpl();
  }
*/
}
