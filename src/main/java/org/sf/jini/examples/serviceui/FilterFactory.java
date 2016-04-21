// FilterFactory.java

package org.sf.jini.examples.serviceui;

import javax.swing.JFrame;

import net.jini.lookup.ui.factory.JFrameFactory;
import net.jini.lookup.ui.MainUI;
import net.jini.lookup.ui.AboutUI;
import net.jini.lookup.ui.AdminUI;
import net.jini.lookup.entry.UIDescriptor;
import net.jini.core.entry.Entry;
import net.jini.core.lookup.ServiceItem;

/**
 * Creates the frame factory for the filter frame.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class FilterFactory implements JFrameFactory {

  /**
   * Gets the frame.
   *
   * @param roleObj the role
   * @return the frame
   */
  public JFrame getJFrame(Object roleObj) {
    // we should check to see what role we have to return
    if(!(roleObj instanceof ServiceItem)) {
      return null;
    }

    ServiceItem item = (ServiceItem) roleObj;

    Entry[] entries = item.attributeSets;
    System.out.println("Entries = " + entries);

    // Find the entry for the Interface filling the MainUIRole
    for (Entry entry : entries) {
      if (entry instanceof UIDescriptor) {
        UIDescriptor uiDescriptor = (UIDescriptor) entry;
        String role = uiDescriptor.role;

        if (role.equals(MainUI.ROLE)) {
          return new FilterFrame(item, "Filter Main Frame");
        }
        else if (role.equals(AdminUI.ROLE)) {
          return new FilterFrame(item, "Filter Admin Frame");
        }
        else if (role.equals(AboutUI.ROLE)) {
          return new FilterFrame(item, "Filter About Frame");
        }
      }
    }

    return null;
  }

}
