// NetlibServer.java

package org.sf.jini.examples.netlib;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.discovery.LookupLocator;
import net.jini.lease.LeaseRenewalEvent;

import org.javalobby.net.Servant;
import org.javalobby.net.ServantFactory;
import org.javalobby.net.PooledServantManager;
import org.javalobby.net.DefaultServer;
import org.javalobby.net.infoworm.InfoWormServant;
import org.javalobby.net.infoworm.InfoWormConnectionFactory;
import org.sf.jini.examples.common.JiniService;

/**
 * Creates new server class for "wrapping" "netlib" service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class NetlibServer extends JiniServer {

  /**
   * Creates new server.
   *
   * @param server the default server
   * @param registrar the registrar
   * @param service the service
   */
  public NetlibServer(DefaultServer server, ServiceRegistrar registrar, JiniService service) {
    super(server, registrar, service);
  }

  /**
   * This method is executed on lease expiration.
   *
   * @param event the event
   */
  public void notify(LeaseRenewalEvent event) {
    System.out.println("Lease expired " + event.toString());
  }

  /**
   * Main entry point for the server.
   *
   * @param args the list of arguments
   *
   * @throws Exception the exception
   */
  static public void main(String args[]) throws Exception {
    String hostName = "localhost";
    int port = 3128;

    final DefaultServer server = new DefaultServer();

    InfoWormConnectionFactory connectionFactory = new InfoWormConnectionFactory(server);

    connectionFactory.setHost(hostName);
    connectionFactory.setPort(port);

    server.setConnectionFactory(connectionFactory);

    ServantFactory factory = new ServantFactory() {
      public Servant create() {
        return new InfoWormServant(server) {
          public Object[] service(Object request) throws IOException {
            return new Object[] { request };
          }
        };
      }
    };

    server.setServantManager(new PooledServantManager(factory));

    NetlibService service = new NetlibService(hostName, port);

    LookupLocator lookupLocator = new LookupLocator("jini://localhost");

    ServiceRegistrar registrar = lookupLocator.getRegistrar();

    JiniServer jiniServer = new NetlibServer(server, registrar, service);

    jiniServer.setLeaseTime(60*1000);

    jiniServer.start();

    System.out.println("Service registered. Type 'exit' to finish.");
    
    do {
      try {
        while(true) {
          String line = (new BufferedReader(new InputStreamReader(System.in))).readLine();

          if(line == null) {
            continue;
          }
          if(line.equalsIgnoreCase("exit")) {
            break;
          }
        }
        
        jiniServer.stop();
        System.out.println("Server stopped. Service unregistered.");
        System.out.println("Press any key...");
        
        (new BufferedReader(new InputStreamReader(System.in))).readLine();
        System.exit(0);
      }
      catch(IOException e) {
        e.printStackTrace();
      }
    } while(true);
  }

}
