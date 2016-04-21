package org.sf.jini.examples.admact;

import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import java.rmi.activation.ActivationID;

import org.sf.jini.examples.common.ActivationAdapter;
import org.sf.jini.examples.common.ActivatableFactory;
import org.sf.jini.examples.adm.AdmFilterImpl;

/**
 * This is the implementation of adm act filter.
 *
 * @version 1.1 11/23/2002
 * @author Alexander Shvets
 */
public class AdmActFilterImpl extends AdmFilterImpl {
  /** The activation adapter. */
  private ActivationAdapter activationAdapter;

  /** The activatable factory. */
  private ActivatableFactory activatableFactory;

  /**
   * Creates new service implementation.
   *
   * @param activationID the activation ID
   * @param marshalledObject the marshalled object
   */
  public AdmActFilterImpl(ActivationID activationID, MarshalledObject marshalledObject) {
    activationAdapter = new ActivationAdapter(activationID, marshalledObject);
  }

  /**
   * Gets the activation adapter.
   *
   * @return the activation adapter
   */
  public ActivationAdapter getActivationAdapter() {
    return activationAdapter;
  }

  /**
   * Sets the activatable factory.
   *
   * @param activatableFactory the activatable factory
   */
  public void setActivatableFactory(ActivatableFactory activatableFactory) {
    this.activatableFactory = activatableFactory;
  }

  /**
   * Destroys this service implementation.
   *
   * @throws RemoteException the remote exception
   */
  public void destroy() throws RemoteException {
    Thread thread = new Thread() {
      public void run() {
        //System.setProperty("terminatingHello", "true");
        //System.err.println("Shutting down service");
        //System.err.println("Terminating - checking activation ID");

        try {
          AdmActFilterImpl.super.destroy();

          activatableFactory.unregister(activationAdapter.getActivationID());

          System.out.println("destroyed!");

/*      while (!Activatable.inactive(id)) {
        Thread.yield();
      }

      ActivationSystem activationSystem = ActivationGroup.getSystem();

      ActivationDesc activationDesc = activationSystem.getActivationDesc(id);

      ActivationGroupID gid = activationDesc.getGroupID();

      activationSystem.unregisterGroup(gid);
*/
        }
        catch(Exception e) {
          e.printStackTrace();
        }
      }
    };

    thread.setDaemon(false);
    thread.start();
  }

}

