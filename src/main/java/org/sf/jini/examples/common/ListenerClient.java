// ListenerClient.java

package org.sf.jini.examples.common;

/**
 * The class helps in creation of Jini client that could listen to events.
 *
 * @version 1.1 10/21/2006
 * @author Alexander Shvets
 */
public abstract class ListenerClient extends JiniClient {
  /** Remote event listener. */
  protected ProxiedRemoteEventListener remoteEventListener;

  /**
   * Sets the remote event listener.
   *
   * @param remoteEventListener the remote event listener
   */
  public void setRemoteEventListener(ProxiedRemoteEventListener remoteEventListener) {
    this.remoteEventListener = remoteEventListener;
  }

  /**
   * Gets the remote event listener.
   * @return the remote event listener
   */
  public ProxiedRemoteEventListener getRemoteEventListener() {
    return remoteEventListener;
  }

}
