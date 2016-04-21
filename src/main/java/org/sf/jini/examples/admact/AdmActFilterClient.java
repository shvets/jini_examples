package org.sf.jini.examples.admact;

import com.sun.jini.admin.DestroyAdmin;
import net.jini.admin.Administrable;
import net.jini.core.discovery.LookupLocator;
import net.jini.core.entry.Entry;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.lookup.entry.ServiceInfo;
import org.sf.jini.examples.common.JiniClient;
import org.sf.jini.examples.remote.RemoteFilter;

/**
 * Creates new client for "admact" service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */public class AdmActFilterClient extends JiniClient {

  /**
   * Creates new client.
   */
  public AdmActFilterClient() {
    super(new Entry[] {
            new ServiceInfo(
                    "admact", // name
                    null, // manufacturer
                    null, // vendor
                    null, // version
                    null, // model
                    null // serial number
                  ) });
  }

  /**
   * Gets the class for looking service
   *
   * @return the class for looking service
   */
  protected Class getServiceClass() {
    return RemoteFilter.class;
  }

  /**
   * Main entry point for the client.
   * Here we are trying to find the service, then invoke it. We also trying to delete the service
   * if it's of Administrable/Destroyable type.
   *
   * @param args the list of arguments
   *
   * @throws Exception the exception
   */
  public static void main(String args[]) throws Exception {
    System.out.println("Started ...");

    LookupLocator lookupLocator = new LookupLocator("jini://localhost");

    ServiceRegistrar registrar = lookupLocator.getRegistrar();

    JiniClient client = new AdmActFilterClient();

    RemoteFilter filter = (RemoteFilter)client.lookup(registrar);

    if(filter == null) {
      System.out.println("Filter service did not found.");
    }
    else {
      System.out.println("Got response: " + filter.filter("Where are you?"));

      if(filter instanceof Administrable) {
        System.out.println("We have Administrable object.");

        Object admin = ((Administrable)filter).getAdmin();

        if(admin instanceof DestroyAdmin) {
          System.out.println("We have DestroyAdmin object. Trying to delete...");
          
          ((DestroyAdmin)admin).destroy();

          System.out.println("Filter service destroyed.");
        }
      }
    }
  }

}
