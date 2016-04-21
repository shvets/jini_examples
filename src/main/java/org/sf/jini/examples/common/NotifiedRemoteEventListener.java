package org.sf.jini.examples.common;

import net.jini.core.event.RemoteEvent;
import net.jini.core.event.UnknownEventException;
import net.jini.core.lookup.ServiceEvent;
import net.jini.core.lookup.ServiceItem;
import net.jini.export.Exporter;

import java.rmi.RemoteException;

/**
 * This is extension of ProxiedRemoteEventListener behavior to support locling.
 *
 * @version 1.1 11/04/2006
 * @author Alexander Shvets
 */
public class NotifiedRemoteEventListener extends ProxiedRemoteEventListener {
  protected final transient Locker locker = new Locker();

  /** The service object */
  protected transient Object service;

  public NotifiedRemoteEventListener(Exporter exporter) {
    super(exporter);
  }

  /**
   * Callback method - called by the Jini system when the service
   * appears-disapears or modified.
   *
   * @param event  the remote event
   * @throws RemoteException remote exception
   * @throws UnknownEventException unknown event exception
   */
  public void notify(RemoteEvent event) throws UnknownEventException, RemoteException {
    if(event instanceof ServiceEvent) {
      ServiceEvent serviceEvent = (ServiceEvent)event;

      ServiceItem item = serviceEvent.getServiceItem();

      service = item.service;

      locker.unlock();
    }
  }

  public synchronized void lock() throws InterruptedException {
    locker.lock();
  }

  public Object getService() {
    return service;
  }
  
}
