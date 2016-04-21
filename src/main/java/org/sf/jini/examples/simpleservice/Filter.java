package org.sf.jini.examples.simpleservice;

/**
 * This interface describes the behavior of filter.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public interface Filter {

  /**
   * Filters the input message.
   *
   * @param message original message
   * @return filtered message
   */
  public String filter(String message);
   
}