package org.sf.jini.examples.norm;

import java.io.Serializable;
import java.rmi.MarshalledObject;
import java.rmi.Remote;
import java.rmi.activation.ActivationID;

import org.sf.jini.examples.common.ActivationAdapter;

/**
 * Creates new lease renewer object.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class LeaseRenewer implements Remote, Serializable {
  private ActivationAdapter activationAdapter;

//  protected long leaseDuration = 20*1000;

  /**
   * Creates new lease renewer.
   *
   * @param activationID the activation ID
   * @param marshalledObject the marshalled object
   */
  public LeaseRenewer(ActivationID activationID, MarshalledObject marshalledObject) {
    activationAdapter = new ActivationAdapter(activationID, marshalledObject);

    System.out.println("*****LeaseRenewer object have created.");
  }

  /**
   * Gets the activation adapter
   * @return the activation adapter
   */
  public ActivationAdapter getActivationAdapter() {
    return activationAdapter;
  }

/*  public long getLeaseDuration() {
    return leaseDuration;
  }

  public void setLeaseDuration(long leaseDuration) {
    this.leaseDuration = leaseDuration;
  }
*/

}
