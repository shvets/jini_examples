/*
 * TimeSpeechUI.java
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
 *
 */
package org.jini.home.speech.time;


import net.jini.core.lookup.ServiceItem;
import net.jini.lookup.entry.UIDescriptor;
import net.jini.lookup.ui.MainUI;
import net.jini.lookup.ui.attribute.UIFactoryTypes;
import net.jini.lookup.ui.factory.JComponentFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.MarshalledObject;
import java.util.Collections;

import org.sf.jini.examples.speech.Speaker;

public class TimeSpeechUI extends JPanel {
  Speaker speaker = null;
  JTextField textField;

  public TimeSpeechUI(ServiceItem serviceItem) {
    speaker = (Speaker) serviceItem.service;

    setLayout(new FlowLayout());

    JButton talkButton = new JButton("Speak Current Time");
    talkButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        try {
          speaker.speak("");
        } catch (Exception e) {
          System.err.println(e);
        }
      }
    });
    add(talkButton);
  }

  static UIDescriptor getUIDescriptor() throws IOException {
    UIDescriptor desc = new UIDescriptor();
    desc.role = MainUI.ROLE;
    desc.toolkit = JComponentFactory.TOOLKIT;
    desc.attributes = Collections.singleton(new UIFactoryTypes(Collections.singleton(JComponentFactory.TYPE_NAME)));
    desc.factory = new MarshalledObject(new TimeSpeechUIFactory());
    return desc;
  }
}
