package org.sf.jini.examples.eventgen;

import net.jini.core.event.RemoteEvent;

/**
 * The remote event that support "prefix" property.
 */
public class PrefixEvent extends RemoteEvent {
  /** The sequence number for this type of event. */ 
  public static final long SET_PREFIX_ID = 0;

  /** The prefix. */
  private String prefix;

  /**
   * Creates new remote event.
   *
   * @param source the source
   * @param seqNo the sequence number
   * @param prefix the prefix
   */
  public PrefixEvent(Object source, long seqNo, String prefix) {
    super(source, SET_PREFIX_ID, seqNo, null);

    this.prefix = prefix;
  }

  /**
   * Gets the prefix.
   *
   * @return the prefix
   */
  public String getPrefix() {
    return prefix;
  }

  /**
   * Sets the prefix.
   *
   * @param prefix the prefix
   */
  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }
  
}

