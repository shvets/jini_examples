package org.sf.jini.examples.norm;

import java.rmi.RemoteException;
import java.rmi.RMISecurityManager;

import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.core.lookup.ServiceMatches;
import net.jini.core.discovery.LookupLocator;
import net.jini.core.lease.Lease;
import net.jini.lease.LeaseRenewalService;
import net.jini.lease.LeaseRenewalSet;
import net.jini.event.EventMailbox;
import net.jini.event.MailboxRegistration;

public class Test {
  static {
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new RMISecurityManager());
    }
  }

  public static Object lookup(ServiceRegistrar registrar, ServiceTemplate template) 
                throws RemoteException {
    ServiceMatches matches = registrar.lookup(template, Integer.MAX_VALUE);

    if(matches.items.length > 0) {
      return matches.items[0].service;
    }

    return null;
  }

  public static void main(String args[]) throws Exception {
    System.out.println("Started ...");

    LookupLocator lookupLocator = new LookupLocator("jini://localhost");

    ServiceRegistrar registrar = lookupLocator.getRegistrar();

    ServiceTemplate normTmpl = 
      new ServiceTemplate(null, new Class[] {LeaseRenewalService.class}, null);

    ServiceTemplate mercuryTmpl = 
      new ServiceTemplate(null, new Class[] {EventMailbox.class}, null);

    EventMailbox mercury = (EventMailbox)lookup(registrar, mercuryTmpl);

    if(mercury == null ) {
      System.out.println("EventMailbox service cannot be found.");
      return;
    }

    LeaseRenewalService norm = (LeaseRenewalService)lookup(registrar, normTmpl);

    if(norm == null ) {
      System.out.println("LeaseRenewalService service cannot be found.");
      return;
    }

    System.out.println(norm);
    System.out.println(mercury);
    
    MailboxRegistration mbr = mercury.register(Lease.FOREVER);
    
    LeaseRenewalSet lrs = norm.createLeaseRenewalSet(Lease.FOREVER);
    
    lrs.renewFor(mbr.getLease(), Lease.FOREVER);
  }

}
