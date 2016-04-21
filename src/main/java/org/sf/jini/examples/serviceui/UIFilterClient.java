package org.sf.jini.examples.serviceui;

import java.util.Set;
import java.rmi.RemoteException;
import java.io.Serializable;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import net.jini.core.lookup.ServiceMatches;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceItem;
import net.jini.core.discovery.LookupLocator;
import net.jini.core.entry.Entry;
import net.jini.lookup.ui.MainUI;
import net.jini.lookup.ui.factory.JFrameFactory;
import net.jini.lookup.ui.factory.JComponentFactory;
import net.jini.lookup.ui.attribute.UIFactoryTypes;
import net.jini.lookup.entry.Name;
import net.jini.lookup.entry.UIDescriptor;

import org.sf.jini.examples.common.JiniClient;
import org.sf.jini.examples.simpleservice.Filter;

/**
 * Creates new client for "ui" service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class UIFilterClient extends JiniClient {

  /**
   * Creates new client.
   */
  public UIFilterClient() {
    super(
      new Entry[] {
        new Name("ui"),
        new UIDescriptor(MainUI.ROLE, /*JFrameFactory*/JComponentFactory.TOOLKIT, null, null)
      });
  }

  /**
   * Gets the class for looking service
   *
   * @return the class for looking service
   */
  protected Class getServiceClass() {
    return Filter.class;
  }

  /**
   * Looks up for appropriate services.
   *
   * @param registrar the registrar
   * @return discovered services
   * @throws RemoteException the remote exception
   */
  public ServiceMatches lookupMatches(ServiceRegistrar registrar) throws RemoteException {
    return registrar.lookup(serviceTemplate, Integer.MAX_VALUE);
  }

  /**
   * Gets the factory.
   *
   * @param serviceItem the service item
   * @return the factory
   * @throws Exception the exception
   */
  public Serializable getFactory(ServiceItem serviceItem) throws Exception {
    Serializable factory = null;

    Entry[] attributes = serviceItem.attributeSets;

    for (Entry attribute : attributes) {
      if (attribute instanceof UIDescriptor) {
        UIDescriptor uiDescriptor = (UIDescriptor) attribute;

        Set uiAttributes = uiDescriptor.attributes;

        for (Object a : uiAttributes) {
          if (a instanceof UIFactoryTypes) {
            UIFactoryTypes uiFactoryTypes = (UIFactoryTypes) a;

            if (uiFactoryTypes.isAssignableTo(JFrameFactory.class)) {
              ClassLoader classLoader = this.getClass().getClassLoader();

              factory = (Serializable)uiDescriptor.getUIFactory(classLoader);

              break;
            }
            else if (uiFactoryTypes.isAssignableTo(JComponentFactory.class)) {
              ClassLoader classLoader = this.getClass().getClassLoader();

              factory = (Serializable)uiDescriptor.getUIFactory(classLoader);

              break;
            }
          }
        }
      }
    }

    return factory;
  }

  /**
   * Main entry point for the client.
   *
   * @param args the list of arguments
   * @throws Exception the exception
   */
  public static void main(String args[]) throws Exception {
    System.out.println("Started ...");

    LookupLocator lookupLocator = new LookupLocator("jini://localhost");

    ServiceRegistrar registrar = lookupLocator.getRegistrar();

    UIFilterClient client = new UIFilterClient();

    ServiceMatches serviceMatches = client.lookupMatches(registrar);

    if(serviceMatches.items.length == 0) {
      System.out.println("Filter doesn't exist.");
    }
    else {
      ServiceItem serviceItem = serviceMatches.items[0];

      Serializable serializable = client.getFactory(serviceItem);

      if(serializable != null) {
        if(serializable instanceof JFrameFactory) {
          JFrameFactory frameFactory = (JFrameFactory)serializable;

          JFrame frame = frameFactory.getJFrame(serviceItem);

          if(frame == null) {
            System.out.println("Null Frame");
          }
          else {
            frame.setVisible(true);
          }
        }
        else if(serializable instanceof JComponentFactory) {
          JComponentFactory componentFactory = (JComponentFactory)serializable;

          JComponent component = componentFactory.getJComponent(serviceItem);

          if(component instanceof FilterPanel) {
            FilterPanel filterPanel = (FilterPanel)component;

            JFrame frame = new JFrame("Test frame");

            frame.addWindowListener(new WindowAdapter() {
              public void windowClosing(WindowEvent e) {
                System.exit(0);
              }
            });

            frame.setContentPane(filterPanel);
            frame.pack();

            frame.setVisible(true);            
          }
        }
      }
    }
  }

}
