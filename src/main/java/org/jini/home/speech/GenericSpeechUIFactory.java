/*
 * GenericSpeechUIFactory.java
 *
 * Created on June 5, 2004, 5:15 PM
 */

package org.jini.home.speech;

import net.jini.core.lookup.ServiceItem;
import net.jini.lookup.ui.factory.JComponentFactory;

import javax.swing.*;

/**
 * @author srp
 */
public class GenericSpeechUIFactory implements JComponentFactory {

  public JComponent getJComponent(Object roleObject) {
    System.out.println("In getJComponent " + roleObject.getClass().getName());

    try {
      return new GenericSpeechUI((ServiceItem) roleObject);
    } catch (ClassCastException e) {
      System.out.println("? " + roleObject.getClass().getName());
      e.printStackTrace();
      throw new IllegalArgumentException("ServiceItem required");
    } catch (Exception re) {
      return null;
    }
  }
}
