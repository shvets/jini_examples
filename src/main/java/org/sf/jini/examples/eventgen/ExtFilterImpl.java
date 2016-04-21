package org.sf.jini.examples.eventgen;

import java.rmi.RemoteException;

import javax.swing.event.EventListenerList;

import net.jini.core.event.EventRegistration;
import net.jini.core.event.RemoteEvent;
import net.jini.core.event.RemoteEventListener;
import org.sf.jini.examples.remote.RemoteFilterImpl;

/**
 * This is the implementation of remote ext ilter.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class ExtFilterImpl extends RemoteFilterImpl implements ExtFilter {
  /** The prefix. */
  private String prefix;

  /** The list of event listeners.  */
  protected EventListenerList listenerList = new EventListenerList();

  /** The sequence number. */
  protected long seqNum = 0L;

  /**
   * Filters the input message.
   *
   * @param message original message
   * @return filtered message
   *
   * @throws RemoteException the remote exception
   */
  public String filter(String message) throws RemoteException {
    return prefix + ": " + message;
  }

  /**
   * Sets the prefix.
   *
   * @param prefix the prefix
   * @throws RemoteException the remote exception
   */
  public void setPrefix(String prefix) throws RemoteException {
    this.prefix = prefix;

    fireNotify(PrefixEvent.SET_PREFIX_ID);
  }

  /**
   * Adds remote event listener.
   *
   * @param listener the listener
   * @return the event registration
   * @throws RemoteException the remote exception
   */
  public EventRegistration addRemoteEventListener(RemoteEventListener listener)
                           throws RemoteException {
    listenerList.add(RemoteEventListener.class, listener);

    return new EventRegistration(
      PrefixEvent.SET_PREFIX_ID,
      this/*getProxy()*/,
      null, // Lease is null for simplicity only. It should be e.g. a LandlordLease
      seqNum);
  }

  /**
   * Removes remote event listener.
   *
   * @param listener the listener
   * @return the event registration
   * @throws RemoteException the remote exception
   */
  public void removeRemoteEventListener(RemoteEventListener listener)
         throws RemoteException {
    listenerList.remove(RemoteEventListener.class, listener);
  }

  /**
   * Fires the event to all registered listeners.
   *
   * @param eventID the event ID
   */
  protected void fireNotify(long eventID) {
    RemoteEvent event = null;

    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();

    // Process the listeners last to first, notifying
    // those that are interested in this event
    for(int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == RemoteEventListener.class) {
        RemoteEventListener listener = (RemoteEventListener) listeners[i+1];

        if(event == null) {
          if(eventID == PrefixEvent.SET_PREFIX_ID) {
            event = new PrefixEvent(this, seqNum++, prefix);
          }
        }

        try {
          listener.notify(event);
        }
        catch(Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

}
