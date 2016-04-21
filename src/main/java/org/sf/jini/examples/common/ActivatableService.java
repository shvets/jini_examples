package org.sf.jini.examples.common;

import java.rmi.RemoteException;
import java.rmi.activation.ActivationException;

/**
 *
 * @version 1.1 11/23/2002
 * @author Alexander Shvets
 */
public abstract class ActivatableService extends ProxiedJiniService {
  /** The activatable factory. */
  protected ActivatableFactory activatableFactory;

  /**
   * Creates new activatable factory.
   *
   * @param policyFile the polocy file
   * @param codeBase the codeBase
   * @throws ActivationException the activation exception
   * @throws RemoteException the remote exception
   */
  public void createActivatableFactory(String policyFile, String codeBase)
         throws ActivationException, RemoteException {
    activatableFactory = new ActivatableFactory(policyFile, codeBase);
  }

}
