package org.sf.jini.examples.common;

import net.jini.core.event.RemoteEventListener;
import net.jini.export.Exporter;

import java.io.Serializable;
import java.rmi.server.ExportException;

/**
 * This is adapter class for RemoteEventListener that supports proxies.
 *
 * @version 1.1 11/04/2006
 * @author Alexander Shvets
 */
public abstract class ProxiedRemoteEventListener implements RemoteEventListener, Serializable {
  /** The proxy adapter object. */
  private ProxyAdapter proxyAdapter;

  /**
   * Creates new proxied remote event listener.

   * @param exporter the exporter
   */
  public ProxiedRemoteEventListener(Exporter exporter) {
    proxyAdapter = new ProxyAdapter(exporter);
  }

  /**
   * Gets the proxy adapter.
   *
   * @return the proxy adapter
   */
  public ProxyAdapter getProxyAdapter() {
    return proxyAdapter;
  }

  /**
   * Exports this listener as remote object.
   *
   * @throws ExportException the export exception
   */
  public void export() throws ExportException {
    proxyAdapter.export(this);
  }

}
