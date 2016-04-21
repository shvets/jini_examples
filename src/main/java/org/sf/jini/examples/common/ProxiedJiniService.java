package org.sf.jini.examples.common;

import java.rmi.Remote;
import java.rmi.server.ExportException;

import net.jini.export.Exporter;

/**
 * This is the jini service with the proxy.
 *
 * @version 1.1 10/21/2006
 * @author Alexander Shvets
 */
public class ProxiedJiniService extends JiniService {
  /** The proxy adapter object. */
  protected ProxyAdapter proxyAdapter;

  /** The remote object. */
  protected Remote remote;

  /**
   * Creates new proxy adapter.
   *
   * @param exporter the exporter
   */
  public void createProxyAdapter(Exporter exporter) {
    proxyAdapter = new ProxyAdapter(exporter);
  }

  /**
   * Exports the remote object.
   *
   * @param remote the remote object
   * @throws ExportException the export exception
   */
  public void export(Remote remote) throws ExportException {
    this.remote = remote;

    proxyAdapter.export(remote);

    serviceObject = proxyAdapter.getProxy();
  }

  /**
   * Gets the remote object.
   *
   * @return the remote object
   */
  public Remote getRemote() {
    return remote;
  }

  /**
   * Gets the proxy object.
   *
   * @return the proxy object
   */
  public Object getProxy() {
    return proxyAdapter.getProxy();
  }

}
