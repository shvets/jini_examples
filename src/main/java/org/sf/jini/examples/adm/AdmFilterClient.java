package org.sf.jini.examples.adm;

import org.sf.jini.examples.common.JiniClient;
import org.sf.jini.examples.common.SingleServiceLocator;
import org.sf.jini.examples.remote.RemoteFilter;
import net.jini.admin.Administrable;
import com.sun.jini.admin.DestroyAdmin;

/**
 * Creates new client for "adm" service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class AdmFilterClient extends JiniClient {

  /**
   * Creates new client.
   */
  public AdmFilterClient() {
    super("adm");
  }

  /**
   * Gets the class for looking service
   *
   * @return the class for looking service
   */
  protected Class getServiceClass() {
    return RemoteFilter.class;
  }

  /**
   * Main entry point for the client.
   * Here we are trying to find the service, then invoke it. We also trying to delete the service
   * if it's of Administrable/Destroyable type. 
   *
   * @param args the list of arguments
   *
   * @throws Exception the exception
   */
  public static void main(String args[]) throws Exception {
    System.out.println("Started ...");

    JiniClient client = new AdmFilterClient();

    SingleServiceLocator serviceLocator = new SingleServiceLocator("jini://localhost");

    RemoteFilter filter = (RemoteFilter)serviceLocator.locate(client.getServiceTemplate());

    if(filter == null) {
      System.out.println("Filter service did not found.");
    }
    else {
      System.out.println("Got response: " + filter.filter("Where are you?"));

      if(filter instanceof Administrable) {
        Object admin = ((Administrable)filter).getAdmin();

        System.out.println("We have Administrable object.");

        if(admin instanceof DestroyAdmin) {
          System.out.println("We have DestroyAdmin object. Trying to delete...");

          ((DestroyAdmin)admin).destroy();

          System.out.println("Filter service destroyed.");
        }
      }
    }
  }

}
