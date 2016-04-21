package org.sf.jini.examples.common;

import java.io.Serializable;

/**
 * The purpouse of this class is to provide synchronous access to shared resource.
 *
 * @version 1.1 11/23/2006
 * @author Alexander Shvets
 */
public class Locker implements Serializable {
  /** Is locked flag. */
  private boolean isLocked = false;

  /**
   * Locks the shared resource forever.
   *
   * @throws InterruptedException interrupted exception
   */
  public void lock() throws InterruptedException {
    synchronized (this) {
      isLocked = true;
      wait();
      isLocked = false;
    }
  }

  /**
   * Locks the shared resource for specified amount of time.
   *
   * @param time the time
   * @throws InterruptedException interrupted exception
   */
  public void lock(long time) throws InterruptedException {
    synchronized (this) {
      isLocked = true;
      wait(time);
      isLocked = false;
    }
  }

  /**
   * Unlocks the shared resource.
   */
  public void unlock() {
    synchronized(this) {
      notifyAll();
      isLocked = false;
    }
  }

  /**
   * Checks if the shared resource is locked.
   *
   * @return true if the shared resource is locked; false otherwise
   */
  public boolean isLocked() {
    return isLocked;
  }
  
}
