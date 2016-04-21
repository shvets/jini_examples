package org.sf.jini.examples.common;

import java.io.Serializable;
import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import java.rmi.activation.ActivationDesc;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationGroup;
import java.rmi.activation.ActivationGroupDesc;
import java.rmi.activation.ActivationGroupID;
import java.rmi.activation.ActivationID;
import java.rmi.activation.ActivationSystem;
import java.util.Properties;

/**
 * Class for manipulating with ActivationID and ActivationGroupID objects.
 *
 * @version 1.1 11/23/2002
 * @author Alexander Shvets
 */
public class ActivatableFactory implements Serializable {
  /** The activation group ID. */
  private ActivationGroupID activationGroupID;

  /** The codebase. */
  private String codeBase;

  /**
   * Creates new activatable activatableFactory.
   *
   * @param policyFile the polocy file
   * @param codeBase the codeBase
   * @throws ActivationException the activation exception
   * @throws RemoteException the remote exception
   */
  public ActivatableFactory(String policyFile, String codeBase)
         throws ActivationException, RemoteException {
    this.codeBase = codeBase;

    createActivationGroup(policyFile);
  }

  /**
   * Creates Activation group.
   *
   * @param policyFile the polocy file
   * @throws ActivationException the activation exception
   * @throws RemoteException the remote exception
   */
  protected void createActivationGroup(String policyFile)
                 throws ActivationException, RemoteException {
    Properties props = new Properties();

    props.put("java.security.policy", policyFile);
    //props.put("java.remote.server.codebase", codeBase);

    String importClasspath = System.getProperty("java.class.path");

    String[] options = new String[]{"-classpath", importClasspath};

    // Create a descriptor for a new activation group
    ActivationGroupDesc.CommandEnvironment cmdEnv =
       new ActivationGroupDesc.CommandEnvironment(null, options);

    ActivationGroupDesc groupDesc =
                        new ActivationGroupDesc(props, cmdEnv);

    ActivationSystem activationSystem = ActivationGroup.getSystem();

    // Register the group and get the ID.
    activationGroupID = activationSystem.registerGroup(groupDesc);

/*    try {
      ActivationGroup.createGroup(activationGroupID, groupDesc, 0);
    }
    catch(ActivationException e) {} 
*/
  }

  /**
   * Registers new activatable on activation system.
   *
   * @param className the class name of the object to be registered.
   * @param data the marshalled data
   * @return the activation ID
   * @throws ActivationException the activation exception
   * @throws RemoteException the remote exception
   */
  public ActivationID register(String className, MarshalledObject data)
                   throws ActivationException, RemoteException {
    ActivationSystem activationSystem = ActivationGroup.getSystem();

    ActivationDesc desc =
         new ActivationDesc(activationGroupID, className, codeBase, data, true);

    return activationSystem.registerObject(desc);
  }

  /**
   * Unregisters new activatable from activation system.
   *
   * @param activationID the activation ID
   * @throws ActivationException the activation exception
   * @throws RemoteException the remote exception
   */
  public void unregister(ActivationID activationID) throws ActivationException, RemoteException {
    ActivationSystem sys = ActivationGroup.getSystem();

    sys.unregisterObject(activationID);
  }

}
