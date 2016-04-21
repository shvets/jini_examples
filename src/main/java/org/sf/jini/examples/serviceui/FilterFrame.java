// FilterFactory.java

package org.sf.jini.examples.serviceui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

import net.jini.lookup.ui.MainUI;
import net.jini.core.lookup.ServiceItem;

import org.sf.jini.examples.simpleservice.Filter;

public class FilterFrame extends JFrame implements MainUI {

  public FilterFrame(ServiceItem item, String name) {
    super(name);

    // Create the browser and place it in this  frame
    FilterPanel contentPane = new FilterPanel((Filter)item.service, name);

    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

    this.setContentPane(contentPane);
    this.pack();
  }

}
