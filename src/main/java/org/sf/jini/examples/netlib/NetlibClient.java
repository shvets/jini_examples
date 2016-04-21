// NetlibClient.java

package org.sf.jini.examples.netlib;

import net.jini.core.discovery.LookupLocator;
import net.jini.core.lookup.ServiceRegistrar;
import org.javalobby.net.Client;
import org.javalobby.net.infoworm.InfoWorm;
import org.sf.jini.examples.common.NotifiedClient;

/**
 * Creates new client for "netlib" service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class NetlibClient extends NotifiedClient {

  /**
   * Creates new client.
   */
  public NetlibClient() {
    createServiceTemplate("netlib");
  }

  /**
   * Gets the class for looking service
   *
   * @return the class for looking service
   */
  protected Class getServiceClass() {
    return Client.class;
  }

  /**
   * Prepares the request.
   *
   * @return the request
   */
  private InfoWorm prepareRequest() {
    InfoWorm request = new InfoWorm();

    request.setField("aaa", "bbb");
    request.setField(InfoWorm.CONTENT_LENGTH_FIELD, Integer.toString("Hello!".getBytes().length));
    request.setBody("Hello!".getBytes());

    return request;
  }

  /**
   * Main entry point for the client.
   * Here we are trying to find the service, then invoke it.
   *
   * @param args the list of arguments
   * @throws Exception the exception
   */
  static public void main(String args[]) throws Exception {
    LookupLocator lookupLocator = new LookupLocator("jini://localhost");

    ServiceRegistrar registrar = lookupLocator.getRegistrar();

    NetlibClient jiniClient = new NetlibClient();

    Client client = (Client)jiniClient.lookup(registrar);

    if(client == null) {
      System.out.println("Client doesn't exist. Waiting...");

      client = (Client)jiniClient.register(registrar, 10*60*1000);
    }

    System.out.println("Client: " + client);

    InfoWorm request = jiniClient.prepareRequest();

    System.out.println("The request is: " + request);

    Object[] responses;

    try {
      responses = client.pipe(request);
    }
    catch(Exception e) {
      System.out.println("Client doesn't exist. Waiting...");

      client = (Client)jiniClient.register(registrar, 10*60*1000);

      responses = client.pipe(request);
    }

    System.out.println("The responses are: ");

    for (Object response : responses) {
      System.out.println(response.toString());
    }
  }
   
}
