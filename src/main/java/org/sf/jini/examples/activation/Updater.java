package org.sf.jini.examples.activation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.rmi.MarshalledObject;

import net.jini.core.lease.Lease;
import net.jini.core.lookup.ServiceRegistration;

/**
 * Creates updater class for the registration. The registration for the service was
 * serialized by the service right after the registration.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class Updater implements Runnable {
  private Thread thread = new Thread(this);

  private ServiceRegistration registration;

  private long duration;

  /**
   * Creates new updater of the registration.
   * @param duration the duration
   * @throws IOException I/O exception
   * @throws ClassNotFoundException class not found exception
   */
  public Updater(long duration) throws IOException, ClassNotFoundException {
    this.duration = duration;

    File file = new File("activation.reg");

    if(!file.exists()) {
      System.out.println("File activation.reg doesn't exist");
    }
    else {
      ObjectInputStream in =
                 new ObjectInputStream(
                     new FileInputStream(file));

      MarshalledObject marshalledObject =
                       (MarshalledObject)in.readObject();

      registration = (ServiceRegistration)marshalledObject.get();

      in.close();
    }

    thread.start();
  }

  /**
   * Main thread lifecycle method.
   */
  public void run() {
    while(true) {
      Lease lease = registration.getLease();

      try {
        System.out.println("Lease expired in " + 
                           (lease.getExpiration()  - System.currentTimeMillis()) +
                           ". Updating lease.");

        lease.renew(duration);

        Thread.sleep(duration-1000);
      }
      catch(Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Main entry point for running updater program.
   *
   * @param args the list of arguments
   *
   * @throws Exception the exception
   */
  public static void main(String args[]) throws Exception {
    new Updater(15*1000);
  }

}
