package org.jini.home.remotecontrol;

import net.jini.core.entry.Entry;
import net.jini.core.lookup.ServiceItem;
import net.jini.lookup.entry.Name;
import net.jini.lookup.entry.UIDescriptor;
import net.jini.lookup.ui.factory.JComponentFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

/**
 * <p>Title: UIFrame</p>
 * <p>Description: Simple Window the handles registration & display of Jini
 * services are passed to it</p>
 * <p>Copyright: Copyright 2004</p>
 *
 * @author Stephen R. Pietrowicz (srp@magiclamp.org)
 * @version 1.0
 */
public class UIFrame {

  static String NAME = "Service";
  JFrame frame = null;
  JTabbedPane pane = null;
  HashMap map = new HashMap();

  public UIFrame() {
    frame = new JFrame("Home Project Remote Control");
    frame.getContentPane().setLayout(new GridLayout(1, 1));
    pane = new JTabbedPane();
    frame.setSize(350, 550);
    frame.getContentPane().add(pane);
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    frame.setVisible(true);
  }

  public void add(ServiceItem item) {
    String serviceName;
    JComponent comp = null;
    int len = item.attributeSets.length;
    serviceName = NAME;
    for (int i = 0; i < len; i++) {

      Entry entry = item.attributeSets[i];
      if (entry instanceof Name) {
        Name name = (Name) entry;
        serviceName = name.name;
      }
      if ((entry instanceof UIDescriptor) == false)
        continue;

      try {
        UIDescriptor desc = (UIDescriptor) entry;
        JComponentFactory factory = (JComponentFactory) desc.factory.get();

        comp = factory.getJComponent(item);

        if (comp == null)
          return;
        // add the service Name and component
        // to the remote control interface
        pane.add(serviceName, comp);

        frame.repaint();
        frame.validate();

        // register this service with the
        // internally, so we know which
        // component to remove
        map.put(item.serviceID, comp);
      } catch (Exception e) {
        // something bad happened...try and
        // remove the service we just put into
        // the cache
        remove(item);
        System.out.println("Error: " + e);
      }

    }
  }

  public void remove(ServiceItem item) {

    // look up the previously registered component, and remove
    // it from our internal cache.
    JComponent jcomp = (JComponent) map.remove(item.serviceID);

    if (jcomp == null)  // didn't find it?  Weird...ok, just leave.
      return;

    // remove it from the interface
    pane.remove(jcomp);
    frame.repaint();
    frame.validate();
  }
}
