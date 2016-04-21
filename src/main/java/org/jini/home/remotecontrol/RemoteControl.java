package org.jini.home.remotecontrol;

import net.jini.config.Configuration;
import net.jini.core.entry.Entry;
import net.jini.core.lookup.ServiceItem;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.discovery.LookupDiscovery;
import net.jini.discovery.LookupDiscoveryManager;
import net.jini.lease.LeaseRenewalManager;
import net.jini.lookup.LookupCache;
import net.jini.lookup.ServiceDiscoveryEvent;
import net.jini.lookup.ServiceDiscoveryListener;
import net.jini.lookup.ServiceDiscoveryManager;
import net.jini.lookup.entry.UIDescriptor;
import net.jini.lookup.ui.MainUI;
import net.jini.lookup.ui.factory.JComponentFactory;

import javax.speech.EngineException;
import javax.speech.Central;
import java.rmi.RMISecurityManager;

/**
 * <p>Title: Remote Control</p>
 * <p>Description: Jini Home Project Remote Control</p>
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * @author Stephen R. Pietrowicz (srp@magiclamp.org)
 * @version 1.0
 */
public class RemoteControl implements ServiceDiscoveryListener {
  UIFrame frame = null;

  Configuration config = null;
  ServiceDiscoveryManager clientManager = null;
  LookupCache cache = null;

  ServiceTemplate template = null;

  public static void main(String[] args) throws EngineException {
    RemoteControl browser = new RemoteControl(args);

    // stay around forever
    Object keepAlive = new Object();
    synchronized (keepAlive) {
      try {
        keepAlive.wait();
      }
      catch (InterruptedException e) {
      }
    }

  }

  public RemoteControl(String[] args) {
    frame = new UIFrame();

    try {
      System.setSecurityManager(new RMISecurityManager());

      // create the template of what we're looking for
      UIDescriptor desc = new UIDescriptor(MainUI.ROLE,
        JComponentFactory.TOOLKIT, null, null);
      Entry[] entries = new Entry[]{
        desc};

      template = new ServiceTemplate(null, null, entries);

      LookupDiscoveryManager lookupManager =
        new LookupDiscoveryManager(
          LookupDiscovery.ALL_GROUPS,
          null,
          null);
      clientManager = new ServiceDiscoveryManager(lookupManager,
        new LeaseRenewalManager());
      cache = clientManager.createLookupCache(template, null,
        this);

    } catch (Exception e) {
      System.out.println(e);
    }

  }

  public void serviceAdded(ServiceDiscoveryEvent event) {
    ServiceItem item = event.getPostEventServiceItem();
    frame.add(item);
  }

  public void serviceRemoved(ServiceDiscoveryEvent event) {
    ServiceItem item = event.getPreEventServiceItem();
    frame.remove(item);
  }

  public void serviceChanged(ServiceDiscoveryEvent event) {
    ServiceItem item = event.getPreEventServiceItem();
    frame.remove(item);
    item = event.getPostEventServiceItem();
    frame.add(item);
  }
}