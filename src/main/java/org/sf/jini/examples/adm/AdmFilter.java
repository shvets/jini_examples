package org.sf.jini.examples.adm;

import net.jini.admin.Administrable;
import net.jini.core.lookup.ServiceRegistration;
import com.sun.jini.admin.DestroyAdmin;

import java.rmi.RemoteException;

import org.sf.jini.examples.remote.RemoteFilter;

/**
 * This interface describes the behavior of remote adm filter.
 *
 * @version 1.1 11/23/2002
 * @author Alexander Shvets
 */
public interface AdmFilter extends
  RemoteFilter, Administrable, /*JoinAdmin, */DestroyAdmin {

  /**
   * Sets the service registration.
   *
   * @param serviceRegistration the service registration
   *
   * @throws RemoteException the remote exception
   */
   public void setServiceRegistration(ServiceRegistration serviceRegistration)
          throws RemoteException;

}
