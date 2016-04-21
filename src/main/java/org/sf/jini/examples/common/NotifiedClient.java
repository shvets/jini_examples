// NotifiedClient.java

package org.sf.jini.examples.common;

import java.rmi.RemoteException;

import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.event.RemoteEventListener;

/**
 * This is extension of ListenerClient with the support for synchronous access
 * and registration.
 *
 * @version 1.1 11/23/2002
 * @author Alexander Shvets
 */
public abstract class NotifiedClient extends ListenerClient {
  private final transient Locker locker = new Locker();

  /**
   * Registers for event listening for the service, defined in service template.
   *
   * @param serviceRegistrar  the serviceRegistrar object
   * @param leaseTime the lease time for which the registration will
   *                  be valid
   * @exception  RemoteException if an remote exception occurs.
   * @return the service object
   * @throws InterruptedException interrupted exception
   */
  public synchronized Object register(ServiceRegistrar serviceRegistrar, long leaseTime)
    throws RemoteException, InterruptedException {
    serviceRegistrar.notify(serviceTemplate,
                     ServiceRegistrar.TRANSITION_NOMATCH_MATCH |
                     ServiceRegistrar.TRANSITION_MATCH_NOMATCH |
                     ServiceRegistrar.TRANSITION_MATCH_MATCH,
                     (RemoteEventListener)remoteEventListener.getProxyAdapter().getProxy(),
                     null,  // marshalled object
                     leaseTime);
    locker.lock();

    return ((NotifiedRemoteEventListener)remoteEventListener).getService();
  }

  /**
   * Unlocks the resource.
   */
  public synchronized void notified() {
    locker.unlock();
  }

}
