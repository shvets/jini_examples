package org.sf.jini.examples.norm;

import org.sf.jini.examples.common.ActivatableService;
import org.sf.jini.examples.common.ProxiedRemoteEventListener;

import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationID;
import java.rmi.RemoteException;
import java.rmi.Remote;

import net.jini.config.ConfigurationException;
import net.jini.core.lease.Lease;
import net.jini.core.event.RemoteEvent;
import net.jini.core.event.UnknownEventException;
import net.jini.lease.LeaseRenewalSet;
import net.jini.lease.LeaseRenewalService;
import net.jini.lease.ExpirationWarningEvent;
import net.jini.lease.RenewalFailureEvent;
import net.jini.export.Exporter;
import net.jini.activation.ActivationExporter;

/**
 * Creates new "admact" service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class NormService2 extends ActivatableService {
  /** The lease duration. */
  protected long leaseDuration;

  /** The remote event listener. */
  protected ProxiedRemoteEventListener remoteEventListener;

  /** The lease renewal set. */
  private LeaseRenewalSet leaseRenewalSet;

  /**
   * Creates new service.
   *
   * @param exporter the exporter
   * @param policyFile the policy file
   * @param codeBase the code base
   * @param leaseRenewalService lease renewal service
   * @param leaseDuration lease duration
   * @throws ActivationException the activation exception
   * @throws RemoteException the remote exception
   * @throws ConfigurationException the configuration exception
   */
  public NormService2(Exporter exporter, String policyFile, String codeBase,
                      LeaseRenewalService leaseRenewalService, long leaseDuration)
         throws ActivationException, RemoteException, ConfigurationException {
    createActivatableFactory(policyFile, codeBase);

    this.leaseDuration = leaseDuration;

//    Remote remote = register("org.sf.jini.examples.norm.LeaseRenewer", null);
//    createProxyAdapter(exporter);
    ActivationID activationID = activatableFactory.register("org.sf.jini.examples.admact.AdmActFilterImpl", null);
    Remote remote = activationID.activate(true);

    createProxyAdapter(new ActivationExporter(activationID, exporter));

    export(remote);

    //createServiceItem("leaserenewer");

    remoteEventListener = new ProxiedRemoteEventListener(exporter) {
      public void notify(RemoteEvent event) throws UnknownEventException, RemoteException {
        NormService2.this.notify(event);
      }
    };

    remoteEventListener.export();

    leaseRenewalSet = leaseRenewalService.createLeaseRenewalSet(leaseDuration);

    //LeaseRenewer leaseRenewer = (LeaseRenewer)getRemote();
    //leaseRenewer.setLeaseDuration(time);


    //EventRegistration eventRegistration1 =
    //leaseRenewalSet.setExpirationWarningListener(leaseRenewer, 5*1000, null);

    //EventRegistration eventRegistration2 =
    //leaseRenewalSet.setRenewalFailureListener(leaseRenewer, null);
  }

/*  public long getLeaseDuration() {
    return leaseDuration;
  }

  public void setLeaseDuration(long leaseDuration) {
    this.leaseDuration = leaseDuration;
  }
*/

  /**
   * Gets the lease renewal set.
   *
   * @return the lease renewal set
   */
  public LeaseRenewalSet getLeaseRenewalSet() {
    return leaseRenewalSet;
  }

/*  public void renew(ServiceRegistration serviceRegistration, long time)
          throws RemoteException {
    leaseRenewalSet.renewFor(serviceRegistration.getLease(), time);

    System.out.println("Lease for service1 expires in " +
            (serviceRegistration.getLease().getExpiration() -
                    System.currentTimeMillis()));
  }
*/

  /**
   * This method is executed if some remote event happened.
   *
   * @param event the event
   * @throws UnknownEventException the unknown event exception
   * @throws RemoteException the remote exception
   */
  public void notify(RemoteEvent event)
              throws UnknownEventException, RemoteException {
    System.out.println("In notify() handler.");
    System.out.println("Notified: " + event);

    if(event instanceof ExpirationWarningEvent) {
      ExpirationWarningEvent expirationWarningEvent =
                             (ExpirationWarningEvent)event;
      try {
        Lease lease = expirationWarningEvent.getRenewalSetLease();

        try {
          lease.renew(leaseDuration);
        }
        catch(Exception e) {
          e.printStackTrace();
        }

        System.out.println("*****Lease renewed.");
      }
      catch(Exception e) {
        e.printStackTrace();
      }
    }
    else if(event instanceof RenewalFailureEvent) {
      RenewalFailureEvent renewalFailureEvent = (RenewalFailureEvent)event;

      try {
        System.out.println("*****Renewal failed: " + renewalFailureEvent.getThrowable());
      }
      catch(Exception e) {
        e.printStackTrace();
      }
    }

    // Make it inactive again
    //try {
      //Activatable.inactive(activationID);
      //proxyAdapter.unexport();
    //}
    //catch (Exception e) {
    //  System.out.println ("error making inactive " + e);
    //}
  }

}
