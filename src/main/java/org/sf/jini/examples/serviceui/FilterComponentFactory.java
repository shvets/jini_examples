package org.sf.jini.examples.serviceui;

import net.jini.lookup.ui.factory.JComponentFactory;
import net.jini.lookup.ui.MainUI;
import net.jini.lookup.ui.AdminUI;
import net.jini.lookup.ui.AboutUI;
import net.jini.lookup.entry.UIDescriptor;
import net.jini.core.lookup.ServiceItem;
import net.jini.core.entry.Entry;

import javax.swing.*;

import org.sf.jini.examples.simpleservice.Filter;

/**
 * Creates the component factory for the filter component.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class FilterComponentFactory implements JComponentFactory {

  /**
   * Gets the component.
   *
   * @param roleObj the role
   * @return the component
   */
  public JComponent getJComponent(Object roleObj) {
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
          return  new FilterPanel((Filter)item.service, "Filter Main Component");
        }
        else if (role.equals(AdminUI.ROLE)) {
          return  new FilterPanel((Filter)item.service, "Filter Admin Component");
        }
        else if (role.equals(AboutUI.ROLE)) {
          return  new FilterPanel((Filter)item.service, "Filter About Component");
        }
      }
    }

    return null;
  }

}
