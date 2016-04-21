package org.sf.jini.examples.remote;

import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.RemoteEvent;
import net.jini.core.event.UnknownEventException;
import net.jini.core.lease.Lease;
import net.jini.core.lookup.ServiceRegistration;
import net.jini.lease.ExpirationWarningEvent;

import java.rmi.RemoteException;
import java.rmi.MarshalledObject;

/**
 * This is the implementation of remote filter interface.
 * This class is also able to listen for events.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class RemoteFilterImpl implements RemoteFilter, RemoteEventListener {

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
   * Implements RemoteEventListener interface. This method is invoked when new event happened.
   *
   * @param event the event
   * @throws UnknownEventException the unknown event exception
   * @throws RemoteException the remote exception
   */
  public void notify(RemoteEvent event) throws UnknownEventException, RemoteException {
    System.out.println("In notify() " + event);

    if(event instanceof ExpirationWarningEvent) {
      ExpirationWarningEvent expirationWarningEvent = (ExpirationWarningEvent) event;
      System.out.println("expiring... " + event.toString());

      Lease lease = expirationWarningEvent.getRenewalSetLease();

      System.out.println("lease1 " + lease);
      try {
        // This is short, for testing. Try 2+ hours
        lease.renew(30*1000);
      }
      catch (Exception e) {
        e.printStackTrace();
      }

      System.out.println("Lease renewed for " + (lease.getExpiration() - System.currentTimeMillis()));

      MarshalledObject marshalledObject = expirationWarningEvent.getRegistrationObject();

      try {
        ServiceRegistration serviceRegistration = (ServiceRegistration)marshalledObject.get();

        Lease lease2 = serviceRegistration.getLease();
      System.out.println("lease2 " + lease2);        

        lease2.renew(30*1000);

        System.out.println("Lease renewed for " + (lease2.getExpiration() - System.currentTimeMillis()));
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

}
