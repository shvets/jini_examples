// JiniServer.java

package org.sf.jini.examples.netlib;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RMISecurityManager;

import net.jini.core.lease.Lease;
import net.jini.core.lookup.ServiceID;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.lease.LeaseListener;
import net.jini.lease.LeaseRenewalEvent;
import net.jini.lease.LeaseRenewalManager;
import org.javalobby.net.ConnectionFactory;
import org.javalobby.net.ContextManager;
import org.javalobby.net.DefaultServer;
import org.javalobby.net.ServantManager;
import org.javalobby.net.Server;
import org.javalobby.net.Stateful;
import org.javalobby.util.Logger;
import org.sf.jini.examples.common.JiniService;


/**
 * The class-wrapper around the default server. It allows to start/stop
 * this server as Jini service.
 *
 * @version 1.1 08/14/2001
 * @author Alexander Shvets
 */
public class JiniServer implements Stateful, Server, LeaseListener {
  // Setting up the RMI security manager 
  static {
    if(System.getSecurityManager() == null) {
      System.setSecurityManager(new RMISecurityManager());
    }
  }

  private long leaseTime = 10 * 60 * 1000;

  /** Is server finished a work? */
  private boolean done;

  /** The lease manager */
  private LeaseRenewalManager leaseManager = new LeaseRenewalManager();

  /** The service ID */
  private ServiceID serviceID;

  //private ServiceRegistration registration;

  private DefaultServer server;
  private ServiceRegistrar registrar;
  private JiniService service;

  /**
   * Creates new Jini server with specified parameters
   *
   * @param server the default server
   * @param registrar the service registrar
   * @param service the Jini service
   */
  public JiniServer(DefaultServer server, ServiceRegistrar registrar,
                    JiniService service) {
    this.server = server;
    this.registrar = registrar;
    this.service = service;
  }

  /**
   * Sets the lease time
   *
   * @param leaseTime the lease time
   */
  public void setLeaseTime(long leaseTime) {
    this.leaseTime = leaseTime;
  }

  /**
   * The lease cannot be renewed
   *
   * @param event the lease renewal event
   */
  public void notify(LeaseRenewalEvent event) {
    System.out.println("Lease cannot be renewed.");
  }

  /**
   * Starts the server
   *
   * @exception  Exception if exception occurs.
   */
  public void start() throws Exception {
    done = false;

    load();

    service.register(registrar, leaseTime);

    save();

    leaseManager.renewUntil(
            service.getServiceRegistration().getLease(),
            Lease.FOREVER, this);

    server.start();
  }

  /**
   * Stops the server
   *
   * @exception  Exception if exception occurs.
   */
  public void stop() throws Exception {
    done = true;

    service.getServiceRegistration().getLease().cancel();

    server.stop();
  }

  /**
   * Loads persistent information
   *
   * @exception  IOException  if an I/O error occurs.
   */
  public void load() throws IOException {
    File file = new File(getFileName());

    if(file.exists()) {
      try {
        FileInputStream fis = new FileInputStream(file);
        DataInputStream is = new DataInputStream(fis);

        serviceID = new ServiceID(is);

        service.getServiceItem().serviceID = serviceID;

        is.close();
      }
      catch(Exception e) {
        server.getLogger().logMessage(e.getMessage());
      }
    }
  }

  /**
   * Saves persistent information
   *
   * @exception  IOException  if an I/O error occurs.
   */
  public void save() throws IOException {
    if(serviceID == null) {
      serviceID = service.getServiceRegistration().getServiceID();

      try {
        FileOutputStream fos = new FileOutputStream(getFileName());
        DataOutputStream os = new DataOutputStream(fos);

        serviceID.writeBytes(os);

        os.close();
      }
      catch(Exception e) {
        server.getLogger().logMessage(e.getMessage());
      }
    }
  }

  /**
   * Calculates the name of the file for saving Jini id
   *
   * @return the file name for saving Jini id
   */
  private String getFileName() {
    String fileName = this.getClass().getName();
   
    int index = fileName.lastIndexOf(".");

    return fileName.substring(index+1) + ".id";
  }

  /**
   * Performs some final actions before this manager will be destroyed.
   *
   * @throws Throwable the <code>Exception</code> raised by this method
   */
  public void finalize() throws Throwable {
    if(!done) {
      stop();
    }
  }

  /**
   * Gets the connection activatableFactory
   *
   * @return the connection activatableFactory
   */
  public ConnectionFactory getConnectionFactory() {
    return server.getConnectionFactory();
  }

  /**
   * Sets the connection activatableFactory
   *
   * @param connectionFactory the connection activatableFactory
   */
  public void setConnectionFactory(ConnectionFactory connectionFactory) {
    server.setConnectionFactory(connectionFactory);
  }

  /**
   * Gets an object that will manipulate servants to perform a request
   *
   * @return an object that will manipulate servants to perform a request
   */
  public ServantManager getServantManager() {
    return server.getServantManager();
  }

  /**
   * Sets up an object that will manipulate servants to perform a request
   *
   * @param servantManager  an object that will manipulate servants to perform a request
   */
  public void setServantManager(ServantManager servantManager) {
    server.setServantManager(servantManager);
  }

  /**
   * Gets the context manager
   *
   * @return the context manager
   */
  public ContextManager getContextManager() {
    return server.getContextManager();
  }

  /**
   * Sets the context manager
   *
   * @param contextManager the context manager
   */
  public void setContextManager(ContextManager contextManager) {
    server.setContextManager(contextManager);
  }

  /**
   * Gets the logger object
   *
   * @return  the logger object
   */
  public Logger getLogger() {
    return server.getLogger();
  }

  /**
   * Sets the logger object
   *
   * @param logger  the logger object
   */
  public void setLogger(Logger logger) {
    server.setLogger(logger);
  }

}
