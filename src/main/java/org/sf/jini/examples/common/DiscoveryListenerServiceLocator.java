package org.sf.jini.examples.common;

import net.jini.core.lookup.ServiceTemplate;
import net.jini.core.lookup.ServiceItem;
import net.jini.lookup.*;
import net.jini.discovery.LookupDiscovery;
import net.jini.lease.LeaseRenewalManager;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Service locator implementation of ServiceDiscoveryListener interface.
 *
 * @version 1.1 11/23/2006
 * @author Alexander Shvets
 */
public class DiscoveryListenerServiceLocator implements ServiceDiscoveryListener {
  /** The locker object. */
  private final Locker locker = new Locker();

  /** The list of discovered services. */
  private final ArrayList<Object> discoveredServices = new ArrayList<Object>();

  /**
   * Locates the first matching service via multicast discovery.
   * @param serviceTemplate service template
   * @param waitTime wait time
   * @throws IOException IOException
   * @throws InterruptedException InterruptedException
   */
  public DiscoveryListenerServiceLocator(ServiceTemplate serviceTemplate, long waitTime)
         throws IOException, InterruptedException {
     LookupDiscovery lookupDiscovery = new LookupDiscovery(LookupDiscovery.ALL_GROUPS);

    LeaseRenewalManager leaseRenewalManager = new LeaseRenewalManager();

    ServiceDiscoveryManager serviceDiscoveryManager =
                    new ServiceDiscoveryManager(lookupDiscovery, leaseRenewalManager);

    ServiceItemFilter serviceItemFilter = new ServiceItemFilter() {
      public boolean check(ServiceItem item) {
        return true;
      }
    };

    LookupCache cache = serviceDiscoveryManager.createLookupCache(serviceTemplate, serviceItemFilter, this);

    locker.lock(waitTime);

    cache.terminate();
    serviceDiscoveryManager.terminate();
    lookupDiscovery.terminate();

    if (discoveredServices.size() == 0) {
      throw new InterruptedException("Services not found within wait time");
    }
  }

  /**
   * Implementation of ServiceDiscoveryListener interface.
   *
   * @param event the service discovery event
   */
  public void serviceAdded(ServiceDiscoveryEvent event) {
    //System.out.println("serviceAdded " + event);

    ServiceItem serviceItem = event.getPostEventServiceItem();

    //System.out.println("serviceItem= " + serviceItem);

    if (serviceItem.service != null) {
      synchronized (discoveredServices) {
        discoveredServices.add(serviceItem.service);
      }
    }
  }

  /**
   * Implementation of ServiceDiscoveryListener interface.
   *
   * @param event the service discovery event
   */
  public void serviceChanged(ServiceDiscoveryEvent event) {
    //System.out.println("serviceChanged " + event);
  }

  /**
   * Implementation of ServiceDiscoveryListener interface.
   *
   * @param event the service discovery event
   */
  public void serviceRemoved(ServiceDiscoveryEvent event) {
    //System.out.println("serviceRemoved " + event);
  }

  /**
   * Gets the list of discovered services.
   *
   * @return the list of discovered services
   */
  public ArrayList<Object> getDiscoveredServices() {
    return discoveredServices;
  }

}
