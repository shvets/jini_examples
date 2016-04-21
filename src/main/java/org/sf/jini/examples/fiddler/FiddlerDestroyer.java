package org.sf.jini.examples.fiddler;

import com.sun.jini.admin.DestroyAdmin;

import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.discovery.LookupLocator;
import net.jini.discovery.LookupDiscoveryService;
import net.jini.admin.Administrable;

import org.sf.jini.examples.common.Util;

/**
 * Destroys the "fiddler" Jini Service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class FiddlerDestroyer {

  /**
   * Main entry point.
   *
   * @param args the list of arguments
   * @throws Exception the exception
   */
  public static void main(String args[]) throws Exception {
    System.out.println("Started ...");

    LookupLocator lookupLocator = new LookupLocator("jini://localhost");

    ServiceRegistrar registrar = lookupLocator.getRegistrar();

    LookupDiscoveryService lookupDiscoveryService = 
                           Util.getLookupDiscoveryService(registrar);

    if(lookupDiscoveryService instanceof Administrable) {
      Object admin = ((Administrable)lookupDiscoveryService).getAdmin();

      if(admin instanceof DestroyAdmin) {
        ((DestroyAdmin)admin).destroy();

        System.out.println("Fiddler service destroyed.");
      }
    }
    else {
      System.out.println("Fiddler service doesn't implement Administrable interface.");
    }
  }

}
