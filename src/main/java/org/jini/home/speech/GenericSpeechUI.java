/*
 * This code released as part of:
 *
 * Home - The Jini Home Automation Project
 *
 * author: Stephen R. Pietrowicz srp@magiclamp.org
 *
 */
package org.jini.home.speech;

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

public class GenericSpeechUI extends JPanel {

  Speaker speaker = null;
  JTextField textField;

  public GenericSpeechUI(ServiceItem serviceItem) {
    speaker = (Speaker) serviceItem.service;

    setLayout(new FlowLayout());

    textField = new JTextField("Hello there", 20);
    add(textField);

    JButton talkButton = new JButton("Say This");
    talkButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        try {
          String s = textField.getText();
          if (s == null)
            return;
          speaker.speak(s);
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
    desc.factory = new MarshalledObject(new GenericSpeechUIFactory());
    return desc;
  }
}
