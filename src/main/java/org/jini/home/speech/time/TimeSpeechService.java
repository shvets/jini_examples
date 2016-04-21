/*
 * TimeSpeechService - speaks the current time in a human voice
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
import org.jini.home.util.ServiceIDManager;

import javax.speech.Central;
import javax.speech.EngineException;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;

public class TimeSpeechService {
  static {
    try {
      Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
    } catch (EngineException e) {
      System.out.println();
    }
  }

  private static final String SERVICE = "TimeSpeechService";

  private Remote proxy;
  private Remote impl;
  private Exporter exporter;

  private String codebase;
  String voice;

  LeaseRenewalManager leaseManager = new LeaseRenewalManager();

  public static void main(String args[]) {
    new TimeSpeechService(args);

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

  public TimeSpeechService(String[] args) {

    if (args.length == 0) {
      System.err.println("No configuration specified");
      System.exit(1);
    }
    String[] configArgs = new String[]{
      args[0]};

    getConfiguration(configArgs);

    try {
      impl = new TimeSpeechImpl(voice, "time");
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

    Name name = new Name("Time");
    Entry[] entries = null;
    try {
      entries = new Entry[]{name, TimeSpeechUI.getUIDescriptor()};
    } catch (Exception e) {
      System.out.println("main: enteries: " + e);
    }
    ServiceIDManager storage = new ServiceIDManager("TimeSpeechServiceID");
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
      voice = (String) config.getEntry(SERVICE,
        "voice",
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
