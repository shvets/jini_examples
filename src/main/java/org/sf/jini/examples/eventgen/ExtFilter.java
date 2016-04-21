package org.sf.jini.examples.eventgen;

import java.rmi.RemoteException;

import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.EventRegistration;

import org.sf.jini.examples.remote.RemoteFilter;

/**
 * This interface describes the behavior of remote ext ilter.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public interface ExtFilter extends RemoteFilter {

  /**
   * Sets the prefix.
   *
   * @param prefix the prefix
   * @throws RemoteException the remote exception
   */
  public void setPrefix(String prefix) throws RemoteException;

  /**
   * Adds remote event listener.
   *
   * @param listener the listener
   * @return the event registration
   * @throws RemoteException the remote exception
   */
  public EventRegistration addRemoteEventListener(RemoteEventListener listener)
                           throws RemoteException;
   
}