// JiniService.java

package org.sf.jini.examples.common;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.io.*;

import net.jini.core.entry.Entry;
import net.jini.core.lookup.ServiceItem;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceRegistration;
import net.jini.core.discovery.LookupLocator;
import net.jini.lookup.entry.Name;

/**
 * The class that simplifies registration of Jini service.
 *
 * @version 1.1 11/23/2002
 * @author Alexander Shvets
 */
public abstract class JiniService {
  static {
    if(System.getSecurityManager() == null) {
      System.setSecurityManager(new RMISecurityManager());
    }
  }

  /** The service item. */
  protected ServiceItem serviceItem;

  /** The service object. */
  protected Object serviceObject;

  /** The service registration. */
  protected ServiceRegistration registration;

  /**
   * Creates new Jini service.
   */
  public JiniService() {
  }

  /**
   * Creates new Jini service and the service item by name.
   *
   * @param name the service name
   * @throws Exception the exception
   */
  public JiniService(String name) throws Exception {
    createServiceItem(name);
  }

  /**
   * Creates new Jini service and the service item by entries list.
   * @param entries the array of entries
   */
  public JiniService(Entry[] entries) {
    createServiceItem(entries);
  }

  /**
   * Gets the object that represents the service.
   *
   * @return the proxy object
   */
  protected Object getServiceObject() {
    return serviceObject;
  }

  /**
   * Creates new service item ?? the name.
   *
   * @param name the name
   */
  public void createServiceItem(String name) {
    this.serviceItem = new ServiceItem(null, getServiceObject(), new Entry[] { new Name(name) });
  }

  /**
   * Creates new service item by the array of entries.
   *
   * @param  entries the array of entries
   */
  public void createServiceItem(Entry[] entries) {
    this.serviceItem = new ServiceItem(null, getServiceObject(), entries);
  }

  /**
   * Gets the service item.
   *
   * @return the service item
   */
  public ServiceItem getServiceItem() {
    return serviceItem;
  }

  /**
   * Gets the service registration.
   *
   * @return the service registration
   */
  public ServiceRegistration getServiceRegistration() {
    return registration;
  }

  /**
   * Registers the service on the lookup system with the help of
   * registrar object.
   *
   * @param registrar  the registrar object
   * @param leaseTime  the lease time
   * @exception  RemoteException if an remote exception occurs.
   */
  public void register(ServiceRegistrar registrar, long leaseTime) throws RemoteException {
    if(serviceItem == null) {
      throw new IllegalArgumentException("Service item cannot be null.");
    }

    registration = registrar.register(getServiceItem(), leaseTime);

    if(serviceItem.serviceID == null) {
      serviceItem.serviceID = registration.getServiceID();
    }
  }

  /**
   * Registers the service with the help of the service locator.
   *
   * @param serviceLocator service locator
   * @param leaseTime lease time
   * @throws IOException IOException
   * @throws ClassNotFoundException ClassNotFoundException
   */
  public void register(SingleServiceLocator serviceLocator, int leaseTime)
         throws IOException, ClassNotFoundException {
    LookupLocator lookupLocator = serviceLocator.getLookupLocator();

    register(lookupLocator.getRegistrar(), leaseTime);
  }
  
}
