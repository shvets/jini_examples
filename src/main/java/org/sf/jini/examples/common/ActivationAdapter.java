package org.sf.jini.examples.common;

import java.rmi.activation.ActivationID;
import java.rmi.MarshalledObject;
import java.io.Serializable;

/**
 * Class for keeping activation related data together.
 *
 * @version 1.1 11/23/2006
 * @author Alexander Shvets
 */
public class ActivationAdapter implements Serializable {
  /** The activation ID. */
  private ActivationID activationID;

  /** The marshalled object. */
  private MarshalledObject marshalledObject;

  /**
   * Creates new activatable adapter.
   *
   * @param activationID the activation ID
   * @param marshalledObject marshalled object
   */
  public ActivationAdapter(ActivationID activationID, MarshalledObject marshalledObject) {
    this.activationID = activationID;
    this.marshalledObject = marshalledObject;
  }

  /**
   * Gets the activation ID.
   * @return the activation ID
   */
  public ActivationID getActivationID() {
    return activationID;
  }

  /**
   * Gets  the marshalled object.
   *
   * @return the marshalled object
   */
  public MarshalledObject getMarshalledObject() {
    return marshalledObject;
  }

}
