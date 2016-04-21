package org.sf.jini.examples.common;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.server.ExportException;

import net.jini.export.Exporter;
import net.jini.export.ProxyAccessor;

/**
 * Adapter class for keeping together proxy and explorer information.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class ProxyAdapter implements ProxyAccessor, Serializable {
  /** The proxy. */
  private transient Remote proxy;

  /** The explorer. */
  private transient Exporter exporter;

  /**
   * Creates new Proxy Adapter.
   *
   * @param exporter the exporter
   */
  public ProxyAdapter(Exporter exporter) {
    this.exporter = exporter;
  }

  /**
   * Gets the proxy.
   *
   * @return the proxy
   */
  public Object getProxy() {
    return proxy;
  }

  /**
   * Exports remote obkect.
   *
   * @param remote the remote obkect
   * @return the proxy
   * @throws ExportException export exception
   */
  public Object export(Remote remote) throws ExportException {
    proxy = exporter.export(remote);

    return proxy;
  }

  /**
   * Unexports remote object.
   *
   * @return true if object was unexported; false otherwise
   *
   * @throws ExportException export exception
   */
  public boolean unexport() throws ExportException {
    return exporter.unexport(true);
  }

}
