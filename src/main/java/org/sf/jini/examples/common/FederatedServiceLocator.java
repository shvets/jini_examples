package org.sf.jini.examples.common;

import net.jini.lookup.*;
import net.jini.core.lookup.*;
import net.jini.discovery.LookupDiscovery;
import net.jini.lease.LeaseRenewalManager;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.io.IOException;

public class FederatedServiceLocator implements ServiceDiscoveryListener/*, ServiceLocator*/ {
  public static final ServiceTemplate SERVICE_REGISTRAR_TEMPLATE =
          new ServiceTemplate( null, new Class[] { ServiceRegistrar.class }, null);

  private ArrayList<ServiceID> serviceIDList = new ArrayList<ServiceID>();

  private final ArrayList<Object> discoveredServices = new ArrayList<Object>();

  private final Locker locker = new Locker();

  private ServiceTemplate serviceTemplate;

  public FederatedServiceLocator(ServiceTemplate serviceTemplate, long waitTime)
    throws IOException, InterruptedException {
    this.serviceTemplate = serviceTemplate;

    LookupDiscovery lookupDiscovery = new LookupDiscovery(LookupDiscovery.ALL_GROUPS);

    LeaseRenewalManager leaseRenewalManager = new LeaseRenewalManager();

    ServiceDiscoveryManager serviceDiscoveryManager =
                    new ServiceDiscoveryManager(lookupDiscovery, leaseRenewalManager);

    ServiceItemFilter serviceItemFilter = new ServiceItemFilter() {
      public boolean check(ServiceItem item) {
        return true;
      }
    };

    LookupCache cache = serviceDiscoveryManager.createLookupCache(SERVICE_REGISTRAR_TEMPLATE, serviceItemFilter, this);

    locker.lock(waitTime);

    cache.terminate();
    serviceDiscoveryManager.terminate();
    lookupDiscovery.terminate();
  }

  public void serviceAdded(ServiceDiscoveryEvent event) {
    System.out.println("serviceAdded() handler executed.");

    ServiceItem serviceItem = event.getPostEventServiceItem();
    Object service = serviceItem.service;

    if (service instanceof ServiceRegistrar) {
      ServiceRegistrar registrar = (ServiceRegistrar)service;

      synchronized (discoveredServices) {
        ServiceID serviceID = serviceItem.serviceID;

        if (!serviceIDList.contains(serviceID)) {
          serviceIDList.add(serviceID);
          lookup(registrar, serviceTemplate);
        }
      }
    }
  }

  private void lookup(ServiceRegistrar registrar, ServiceTemplate serviceTemplate) {
    try {
      lookupService(registrar, serviceTemplate);
    }
    catch (RemoteException ex) {
      ex.printStackTrace();
    }

    try {
      lookupRegistrar(registrar, serviceTemplate);
    }
    catch (RemoteException ex) {
      ex.printStackTrace();
    }
  }

  private void lookupService(ServiceRegistrar registrar, ServiceTemplate serviceTemplate)
          throws RemoteException {
    ServiceMatches serviceMatches = registrar.lookup(serviceTemplate, Integer.MAX_VALUE);

    ServiceItem[] serviceItems = serviceMatches.items;

    for (ServiceItem serviceItem : serviceItems) {
      Object service = serviceItem.service;
      ServiceID serviceID = serviceItem.serviceID;

      if (service != null && !serviceIDList.contains(serviceID)) {
        System.out.println("Adding service " + service);

        discoveredServices.add(service);
        serviceIDList.add(serviceID);
      }
    }
  }

  private void lookupRegistrar(ServiceRegistrar registrar, ServiceTemplate serviceTemplate) throws RemoteException {
    ServiceMatches serviceMatches = registrar.lookup(SERVICE_REGISTRAR_TEMPLATE, Integer.MAX_VALUE);

    ServiceItem[] serviceItems = serviceMatches.items;

    for (ServiceItem serviceItem : serviceItems) {
      Object service = serviceItem.service;
      ServiceID serviceID = serviceItem.serviceID;

      if (service != null && service instanceof ServiceRegistrar) {
        ServiceRegistrar registrar2 = (ServiceRegistrar) service;

        if(!serviceIDList.contains(serviceID)) {
          System.out.println("Federated LUS discovered " + registrar2.getLocator() + " : doing recursive lookup");
          serviceIDList.add(serviceID);

          //recursive lookup
          lookup(registrar2, serviceTemplate);
        }
      }
    }
  }

  public void serviceChanged(ServiceDiscoveryEvent event) {
    System.out.println("serviceChanged() handler executed.");
  }

  public void serviceRemoved(ServiceDiscoveryEvent event) {
    System.out.println("serviceRemoved() handler executed.");
  }

  public ArrayList<Object> getDiscoveredServices() {
    return discoveredServices;
  }

}
