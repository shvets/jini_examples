package org.sf.jini.examples.common;

import net.jini.core.lookup.ServiceTemplate;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceItem;
import net.jini.core.lookup.ServiceMatches;
import net.jini.discovery.DiscoveryListener;
import net.jini.discovery.DiscoveryEvent;
import net.jini.discovery.LookupDiscovery;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * The implementation of service locator based on DiscoveryListener implementation.
 *
 * @version 1.1 11/04/2006
 * @author Alexander Shvets
 */
public class MultipleServiceLocator implements ServiceLocator, DiscoveryListener, Serializable {

  private final Locker locker = new Locker();

  private ServiceMatches serviceMatches;

  private ServiceTemplate serviceTemplate;
  private int waitTime;

  /**
   * DiscoveryListener interface implementation.
   *
   * @param event the event
   */
  public void discovered(DiscoveryEvent event) {
    //System.out.println("discovered: " + event);

    ServiceRegistrar[] serviceRegistrars = event.getRegistrars();

    for (int i = 0; i < serviceRegistrars.length && serviceMatches == null; i++) {
      findService(serviceRegistrars[i]);
    }
  }

  /**
   * DiscoveryListener interface implementation.
   *
   * @param event the event
   */
  public void discarded(DiscoveryEvent event) {
    //System.out.println("discarded: " + event);
  }

  public void findService(ServiceRegistrar serviceRegistrar) {
    Thread thread = Thread.currentThread();

    ClassLoader classLoader = getClass().getClassLoader();
    ClassLoader savedClassLoader = thread.getContextClassLoader();

    try {
      thread.setContextClassLoader(classLoader);

      synchronized (locker) {
        serviceMatches = serviceRegistrar.lookup(serviceTemplate, waitTime);

        if (serviceMatches != null) {
          locker.unlock();
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
    finally {
      thread.setContextClassLoader(savedClassLoader);
    }
  }

  /**
   * @see ServiceLocator
   */
  public Object locate(ServiceTemplate serviceTemplate)
    throws IOException, ClassNotFoundException, InterruptedException {
    ServiceItem[] items = locate(serviceTemplate, 0);

    return items[0].service;
  }

  /**
   * @see ServiceLocator
   */
  public ServiceItem[] locate(ServiceTemplate serviceTemplate, int waitTime) throws IOException {
    this.serviceTemplate = serviceTemplate;
    this.waitTime = waitTime;

//    System.out.println("Looking up new instance of service...");

    LookupDiscovery lookupDiscovery = new LookupDiscovery(LookupDiscovery.ALL_GROUPS);

    lookupDiscovery.addDiscoveryListener(this);

    //reset service to null before starting discovery
    serviceMatches = null;

    try {
      synchronized (locker) {
        locker.lock(waitTime);
      }

      if (serviceMatches == null) {
        throw new RemoteException("Service not found within timeout " + waitTime);
      }

      System.out.println("new service instance discovered");
    }
    catch (InterruptedException ex) {
      throw new RemoteException("Interrupted", ex);
    }
    finally {
      lookupDiscovery.terminate();
    }

    return serviceMatches.items;
  }

}
