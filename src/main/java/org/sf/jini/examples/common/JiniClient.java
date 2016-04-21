// JiniClient.java

package org.sf.jini.examples.common;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

import net.jini.core.entry.Entry;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.lookup.entry.Name;

/**
 * The class that simplifies creation of Jini client, lookup and registration for events.
 *
 * @version 1.1 11/23/2002
 * @author Alexander Shvets
 */
public abstract class JiniClient {
  // Setting up the RMI security manager 
  static {
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new RMISecurityManager());
    }
  }

  /** The service template */
  protected ServiceTemplate serviceTemplate;

  /**
   * Default constructor.
   */
  public JiniClient() {
  }

  /**
   * Creates new Jini client
   * @param name the service name
   */
  public JiniClient(String name) {
    createServiceTemplate(name);
  }

  /**
   * Creates new Jini client
   * @param entries the array of entries
   */
  public JiniClient(Entry[] entries) {
    createServiceTemplate(entries);
  }

  /**
   * Gets the class for looking service
   *
   * @return the class for looking service
   */
  protected abstract Class getServiceClass();

  /**
   * Creates new service template based on name.
   *
   * @param name the name
   */
  protected void createServiceTemplate(String name) {
    this.serviceTemplate =
            new ServiceTemplate(null, new Class[] { getServiceClass() }, new Entry[] { new Name(name) });
  }

  /**
   * Creates new service template based on the array of entries.
   *
   * @param entries the array of entries
   */
  protected void createServiceTemplate(Entry[] entries) {
    this.serviceTemplate = new ServiceTemplate(null, new Class[] { getServiceClass() }, entries);
  }

  /**
   * Gets the service template.
   *
   * @return the service template
   */
  public ServiceTemplate getServiceTemplate() {
    return serviceTemplate;
  }

  /**
   * Looks for the service by using registrar object
   *
   * @param registrar  the registrar object
   * @exception  RemoteException if an remote exception occurs.
   * @return the service object
   */
  public Object lookup(ServiceRegistrar registrar) throws RemoteException {
    return registrar.lookup(getServiceTemplate());
  }

}
