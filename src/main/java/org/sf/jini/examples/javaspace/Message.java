package org.sf.jini.examples.javaspace;

import net.jini.core.entry.Entry;

/**
 * This class is used for storing information into Javaspace Jini service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class Message implements Entry {
  /** The message */
  public String message;

  /** The "from" property. */
  public String from;

  /** The "to" property. */
  public String to;

  /**
   * Creates new message (default constructor).
   */
  public Message() {}

  /**
   * Creates new message.
   *
   * @param message the message
   * @param from the from property
   * @param to the to property
   */
  public Message(String message, String from, String to) {
    this.message = message;
    this.from = from;
    this.to = to;
  }

  /**
   * The string representation of the message object.
   *
   * @return string representation of the message object
   */
  public String toString() {
    return "(" + from + ", " + to + "): \"" + message + "\"";
  }  

}
