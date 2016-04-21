package org.sf.jini.examples.activation;

import java.rmi.MarshalledObject;
import java.rmi.activation.ActivationID;

import org.sf.jini.examples.common.ActivationAdapter;
import org.sf.jini.examples.remote.RemoteFilterImpl;

/**
 * This is the implementation of remote filter interface for "activation" service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class ActFilterImpl extends RemoteFilterImpl {
  /** The activation adapter. */
  private ActivationAdapter activationAdapter;

  /**
   * Creates new filter implementation.
   *
   * @param activationID the activation ID
   * @param marshalledObject the marshalled object
   */
  public ActFilterImpl(ActivationID activationID,
                       MarshalledObject marshalledObject) {
    activationAdapter = new ActivationAdapter(activationID, marshalledObject);

    System.out.println("*****ActFilterImpl object have created.");    
  }

  /**
   * Gets the activation adapter
   *
   * @return the activation adapter
   */
  public ActivationAdapter getActivationAdapter() {
    return activationAdapter;
  }

}
