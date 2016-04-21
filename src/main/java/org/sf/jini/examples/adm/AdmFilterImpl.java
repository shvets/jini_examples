package org.sf.jini.examples.adm;

import net.jini.core.lookup.ServiceRegistration;
import net.jini.core.lease.UnknownLeaseException;

import java.rmi.RemoteException;

/**
 * This is the implementation of adm filter.
 *
 * @version 1.1 11/23/2002
 * @author Alexander Shvets
 */
public class AdmFilterImpl implements AdmFilter {
  /** The admin object. */
  private Object admin;

  /** The service registration. */
  private ServiceRegistration serviceRegistration;

  /**
   * Filters the input message.
   *
   * @param message original message
   * @return filtered message
   *
   * @throws RemoteException the remote exception
   */
  public String filter(String message) throws RemoteException {
    return message;
  }

  /**
   * Gets the admin object.
   *
   * @return the admin object
   * @throws RemoteException the remote exception
   */
  public Object getAdmin() throws RemoteException {
    return admin;
  }

  /**
   * Sets the admin object.
   *
   * @param admin the admin object
   * @throws RemoteException the remote exception
   */
  public void setAdmin(Object admin) throws RemoteException {
    this.admin = admin;
  }

  /**
   * Sets the service registration.
   *
   * @param serviceRegistration the service registration
   *
   * @throws RemoteException the remote exception
   */
  public void setServiceRegistration(ServiceRegistration serviceRegistration)
         throws RemoteException {
    this.serviceRegistration = serviceRegistration;
  }

  /**
   * Destroys this service implementation.
   *
   * @throws RemoteException the remote exception
   */
  public void destroy() throws RemoteException {
    try {
      serviceRegistration.getLease().cancel();
    }
    catch (UnknownLeaseException e) {
      System.out.println(e);
    }
  }
  
}
