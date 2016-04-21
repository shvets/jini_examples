/*
 * TimeSpeechUIFactory.java
 *
 * Beta 1.0
 *
 * This code released as part of:
 * 
 * Home - The Jini Home Automation Project
 *
 * author: Stephen R. Pietrowicz srp@magiclamp.org
 *
 * Copyright 2004 Stephen R. Pietrowicz
 */

package org.jini.home.speech.time;

import net.jini.core.lookup.ServiceItem;
import net.jini.lookup.ui.factory.JComponentFactory;

import javax.swing.*;

public class TimeSpeechUIFactory implements JComponentFactory {

  public JComponent getJComponent(Object roleObject) {
    try {
      return new TimeSpeechUI((ServiceItem) roleObject);
    } catch (ClassCastException e) {
      e.printStackTrace();
      throw new IllegalArgumentException("ServiceItem required");
    } catch (Exception re) {
      return null;
    }
  }
}
