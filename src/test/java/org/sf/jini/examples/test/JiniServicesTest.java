package org.sf.jini.examples.test;

import net.jini.discovery.DiscoveryListener;
import net.jini.discovery.LookupDiscovery;
import net.jini.discovery.LookupDiscoveryManager;
import net.jini.discovery.DiscoveryEvent;
import net.jini.core.discovery.LookupLocator;
import net.jini.core.lookup.*;

import java.io.IOException;
import java.rmi.RMISecurityManager;

import org.testng.annotations.Test;
import org.sf.jini.examples.common.SingleServiceLocator;
import org.sf.jini.examples.common.Locker;

/**
 * Test class for small Jini examples.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class JiniServicesTest {
  static {
    System.setSecurityManager(new RMISecurityManager());
  }

  /**
   * Test TestNG system.
   *
   * @throws Exception the exception
   */
  @Test
  public void testTestNG() throws Exception {
    System.out.println("In testTestNG");
  }

  /**
   * Tests the availability of single lookup service.
   *
   * @throws Exception the exception
   */
  @Test
  public void testSingleLookupLocator() throws Exception {
    System.out.println("In testSingleLookupLocator");
    
    SingleServiceLocator serviceLocator = new SingleServiceLocator("jini://localhost");

    LookupLocator lookupLocator = serviceLocator.getLookupLocator();
    ServiceRegistrar registrar = lookupLocator.getRegistrar();

    System.out.println("Registrar found " + registrar);
  }

  /**
   * Calculates the list of services registered on single lookup service.
   *
   * @throws Exception the exception
   */
  @Test
  public void testServicesList() throws Exception {
    System.out.println("In testServicesList");

    LookupLocator lookupLocator = new LookupLocator("jini://localhost");

    ServiceRegistrar registrar = lookupLocator.getRegistrar();

    ServiceTemplate serviceTemplate = new ServiceTemplate(null, null, null);

    ServiceMatches serviceMatches = registrar.lookup(serviceTemplate, Integer.MAX_VALUE);

    if (serviceMatches == null) {
      System.out.println("No service found.");
    }
    else {
      ServiceItem[] items = serviceMatches.items;

      for (ServiceItem serviceItem : items) {
        System.out.println("service " + serviceItem.service);
      }
    }
  }

  /**
   * Tests discoveries of registrars dynamically.
   *
   * @throws IOException I/O Exception
   * @throws InterruptedException Interrupted Exception
   */
  @Test
  public void testLookupDiscovery() throws IOException, InterruptedException  {
    System.out.println("In testLookupDiscovery");

    class MyDiscoveryListener implements DiscoveryListener {
      final Locker locker = new Locker();

      MyDiscoveryListener() throws IOException, InterruptedException {
        synchronized(locker) {
          LookupDiscovery lookup = new LookupDiscovery(LookupDiscovery.ALL_GROUPS);
          lookup.addDiscoveryListener(this);

          locker.lock();
        }
      }

      public void discovered(DiscoveryEvent event) {
        ServiceRegistrar[] registrars = event.getRegistrars();

        for (ServiceRegistrar registrar : registrars) {
          System.out.println("Registrar found " + registrar);
        }

        locker.unlock();
      }

      public void discarded(DiscoveryEvent event) {
        ServiceRegistrar[] registrars = event.getRegistrars();

        for (ServiceRegistrar registrar : registrars) {
          System.out.println("Registrar discarded " + registrar);
        }
      }
    }

    new MyDiscoveryListener();
  }

  /**
   * Tests discoveries of registrars dynamically.
   * The list of registrars is limited to specified one.
   *
   * @throws IOException I/O Exception
   * @throws InterruptedException Interrupted Exception
   */
  @Test
  public void testLookupDiscoveryManager() throws IOException, InterruptedException {
    System.out.println("In testLookupDiscoveryManager");

    class MyDiscoveryListener implements DiscoveryListener {
      final Locker locker = new Locker();

      MyDiscoveryListener(LookupLocator lookupLocator) throws IOException, InterruptedException {
        synchronized(locker) {
          new LookupDiscoveryManager(
            LookupDiscovery.ALL_GROUPS,
            new LookupLocator[]{ lookupLocator },
            this  // discovery listener
          );

          locker.lock();
        }
      }

      public void discovered(DiscoveryEvent event) {
        ServiceRegistrar[] registrars = event.getRegistrars();

        for (ServiceRegistrar registrar : registrars) {
          System.out.println("Registrar found " + registrar);
        }

        locker.unlock();
      }

      public void discarded(DiscoveryEvent event) {
        ServiceRegistrar[] registrars = event.getRegistrars();

        for (ServiceRegistrar registrar : registrars) {
          System.out.println("Registrar discarded " + registrar);
        }
      }
    }

    LookupLocator lookupLocator = new LookupLocator("jini://localhost");

    new MyDiscoveryListener(lookupLocator);
  }

}
