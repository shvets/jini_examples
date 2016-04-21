package org.sf.jini.examples.speech;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * This interface describes the behavior of the speaker.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public interface Speaker extends Remote {

  /**
   * Speaks the message.
   *
   * @param message the message
   * @throws RemoteException the remote exception
   */
  void speak(String message) throws RemoteException;

  /**
   * Lists all available voices for given mode.
   *
   * @param mode the mode
   * @return list of available voices
   * @throws RemoteException the remote exception
   */
  List listAllVoices(String mode) throws RemoteException;

  /**
   * Lists all available voices.
   *
   * @return list of available voices
   * @throws RemoteException the remote exception
   */
  List listAllVoices() throws RemoteException;
  
}
