// NetlibService.java

package org.sf.jini.examples.netlib;

import org.javalobby.net.infoworm.InfoWormClient;
import org.sf.jini.examples.common.JiniService;

/**
 * Creates new "netlib" service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class NetlibService extends JiniService {
  /** The infoworm client. */
  private InfoWormClient client;

  /**
   * Creates new service.
   *
   * @param hostName the host name
   * @param port the port number
   * @throws Exception the exception
   */
  public NetlibService(String hostName, int port) throws Exception {
    client = new InfoWormClient();

    client.setHost(hostName);
    client.setPort(port);

    createServiceItem("netlib");    
  }

  /**
   * Gets the object that represents the service.
   *
   * @return the proxy object
   */
  protected Object getServiceObject() {
   return client;
  }

}
