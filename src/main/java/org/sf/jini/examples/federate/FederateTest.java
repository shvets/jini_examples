package org.sf.jini.examples.federate;

import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.discovery.LookupLocator;
import net.jini.admin.JoinAdmin;
import net.jini.admin.Administrable;

import java.rmi.RMISecurityManager;

/**
 * Creates the federation of 2 lookup locators.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class FederateTest {

  /**
   * Main entry point.
   *
   * @param args the list of arguments
   * @throws Exception the exception
   */
  public static void main(String[] args) throws Exception {
    System.setSecurityManager(new RMISecurityManager());

    LookupLocator lookupLocator1 = new LookupLocator(args[0]);
    ServiceRegistrar serviceRegistrar1 = lookupLocator1.getRegistrar();
    Administrable admin1 = (Administrable) serviceRegistrar1;
    JoinAdmin joinAdmin1 = (JoinAdmin) admin1.getAdmin();

    LookupLocator lookupLocator2 = new LookupLocator(args[1]);
    ServiceRegistrar serviceRegistrar2 = lookupLocator2.getRegistrar();
    Administrable admin2 = (Administrable) serviceRegistrar2;
    JoinAdmin joinAdmin2 = (JoinAdmin) admin2.getAdmin();

    joinAdmin1.addLookupLocators(new LookupLocator[]{lookupLocator2});
    joinAdmin2.addLookupLocators(new LookupLocator[]{lookupLocator1});

    System.out.println("Lookup services federated");
  }

}
