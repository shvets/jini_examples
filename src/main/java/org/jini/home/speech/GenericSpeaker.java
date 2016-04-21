/*
 * GenericSpeaker.java
 *
 * Created on June 5, 2004, 10:23 AM
 */

package org.jini.home.speech;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author srp
 */
public interface GenericSpeaker extends Remote {

  public void speak(String text) throws RemoteException;

}
