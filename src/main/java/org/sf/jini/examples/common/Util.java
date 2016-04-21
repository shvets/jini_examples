// Util.java

package org.sf.jini.examples.common;

import java.rmi.*;

import net.jini.core.lookup.ServiceTemplate;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceMatches;

import net.jini.event.EventMailbox;

import net.jini.core.transaction.server.TransactionManager;

import net.jini.space.JavaSpace;
import net.jini.lease.LeaseRenewalService;
import net.jini.discovery.LookupDiscoveryService;

/**
 * The class that simplifies creation of Jini client, lookup ang
 * registration for events.
 *
 * @version 1.1 11/23/2002
 * @author Alexander Shvets
 */
public class Util {

  private Util() {}

  public static Object lookup(ServiceRegistrar registrar, ServiceTemplate template) 
                throws RemoteException {
    ServiceMatches matches = registrar.lookup(template, Integer.MAX_VALUE);

    if(matches.items.length > 0) {
      return matches.items[0].service;
    }

    return null;
  }

  public static EventMailbox getEventMailbox(ServiceRegistrar registrar) 
                throws RemoteException {
    ServiceTemplate template = 
                    new ServiceTemplate(null, 
                                        new Class[] { EventMailbox.class }, 
                                        null);

    return (EventMailbox)lookup(registrar, template);
  }

  public static TransactionManager getTransactionManager(ServiceRegistrar registrar) 
                throws RemoteException {
    ServiceTemplate template = 
                    new ServiceTemplate(null, 
                                        new Class[] { TransactionManager.class }, 
                                        null);
    return (TransactionManager)lookup(registrar, template);
  }

  public static JavaSpace getJavaSpace(ServiceRegistrar registrar) 
                throws RemoteException {
    ServiceTemplate javaSpaceTemplate = 
                    new ServiceTemplate(null, 
                                        new Class[] { JavaSpace.class }, 
                                        null);
    return (JavaSpace)lookup(registrar, javaSpaceTemplate);
  }

  public static LeaseRenewalService getLeaseRenewalService(ServiceRegistrar registrar) 
                throws RemoteException {
    ServiceTemplate template = 
                    new ServiceTemplate(null, 
                                        new Class[] { LeaseRenewalService.class }, 
                                        null);
    return (LeaseRenewalService)lookup(registrar, template);
  }

  public static LookupDiscoveryService getLookupDiscoveryService(ServiceRegistrar registrar) 
                throws RemoteException {
    ServiceTemplate template = 
                    new ServiceTemplate(null, 
                                        new Class[] { LookupDiscoveryService.class }, 
                                        null);
    return (LookupDiscoveryService)Util.lookup(registrar, template);
  }

}
