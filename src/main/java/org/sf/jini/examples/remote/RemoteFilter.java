package org.sf.jini.examples.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.io.Serializable;

/**
 * This interface describes the behavior of remote filter.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public interface RemoteFilter extends Remote, Serializable {

  /**
   * Filters the input message.
   *
   * @param message original message
   * @return filtered message
   *
   * @throws RemoteException the remote exception
   */
  String filter(String message) throws RemoteException;

}
