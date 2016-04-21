package org.sf.jini.examples.simpleservice;

import java.io.Serializable;

/**
 * This is the implementation of filter.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class FilterImpl implements Filter, Serializable {

  /**
   * Default constructor.
   */
  public FilterImpl() {}

  /**
   * Filters the input message.
   *
   * @param message original message
   * @return filtered message
   */
  public String filter(String message) {
    return message;
  }

}
