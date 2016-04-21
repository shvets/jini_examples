/*-----------------------------------------------------------------------
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *----------------------------------------------------------------------
 */

/*
 * This code released as part of:
 * 
 * Home - The Jini Home Automation Project
 *
 * author: Stephen R. Pietrowicz srp@magiclamp.org
 *
 */
package org.jini.gpl.music;

import net.jini.config.Configuration;
import net.jini.config.ConfigurationException;
import net.jini.config.ConfigurationProvider;
import net.jini.config.NoSuchEntryException;
import net.jini.core.entry.Entry;
import net.jini.core.lookup.ServiceID;
import net.jini.discovery.LookupDiscovery;
import net.jini.discovery.LookupDiscoveryManager;
import net.jini.export.Exporter;
import net.jini.lease.LeaseRenewalManager;
import net.jini.lookup.JoinManager;
import net.jini.lookup.entry.Name;

import java.rmi.RMISecurityManager;
import java.rmi.Remote;

public class MusicService {

  private static final String SERVICE = "MusicService";

  private Remote proxy;
  private Remote impl;
  private Exporter exporter;

  private String codebase;
  String musicDirectory;

  LeaseRenewalManager leaseManager = new LeaseRenewalManager();

  public static void main(String args[]) {
    new MusicService(args);

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

  public MusicService(String[] args) {

    if (args.length == 0) {
      System.err.println("No configuration specified");
      System.exit(1);
    }
    String[] configArgs = new String[]{
      args[0]};

    getConfiguration(configArgs);

    try {
      impl = new JukeboxImpl(musicDirectory);
    } catch (Exception e) {
      System.out.println(e);
      System.exit(1);
    }

    try {
      proxy = exporter.export(impl);
    }
    catch (java.rmi.server.ExportException e) {
      e.printStackTrace();
      System.exit(1);
    }

    System.setSecurityManager(new RMISecurityManager());

    Name name = new Name("Music");
    Entry[] entries = null;
    try {
      entries = new Entry[]{name, JukeboxUI.getUIDescriptor()};
    } catch (Exception e) {
      System.out.println("main: enteries: " + e);
    }
    ServiceIDStorage storage = new ServiceIDStorage("MusicServiceID");
    ServiceID id = storage.getServiceID();

    JoinManager joinManager = null;
    try {
      LookupDiscoveryManager mgr =
        new LookupDiscoveryManager(LookupDiscovery.
          ALL_GROUPS,
          null, // unicast locators
          null); // DiscoveryListener

      if (id == null) {
        joinManager = new JoinManager(proxy, entries,
          storage, mgr,
          new LeaseRenewalManager());
      } else {
        joinManager = new JoinManager(proxy, entries,
          id, mgr

          , new LeaseRenewalManager());
      }
    } catch (Exception e) {
      System.out.println("main: " + e);
    }
  }


  private void getConfiguration(String[] configArgs) {
    Configuration config = null;

    try {
      config = ConfigurationProvider.getInstance(configArgs);
    }
    catch (ConfigurationException e) {
      System.err.println(e.toString());
      e.printStackTrace();
      System.exit(100);
    }

    try {
      exporter = (Exporter) config.getEntry(SERVICE,
        "exporter",
        Exporter.class);

      codebase = (String) config.getEntry(SERVICE,
        "codebase",
        String.class);
      if (codebase != null) {
        System.setProperty("java.rmi.server.codebase", codebase);
      }
      musicDirectory = (String) config.getEntry(SERVICE,
        "musicDirectory",
        String.class);
    }
    catch (NoSuchEntryException e) {
      System.err.println("No config entry for " + e);
      System.exit(150);
    }
    catch (Exception e) {
      System.err.println(e.toString());
      e.printStackTrace();
      System.exit(200);
    }

  }
}
