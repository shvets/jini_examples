package org.sf.jini.examples.common;

import net.jini.core.discovery.LookupLocator;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.core.lookup.ServiceItem;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * The implementation of service locator based on lookup locator.
 *
 * @version 1.1 11/04/2006
 * @author Alexander Shvets
 */
public class SingleServiceLocator implements ServiceLocator {
  /** The lookup locator. */
  private LookupLocator lookupLocator;

  /**
   * Creates new service locator.

   * @param url the url for lookup locator
   *
   * @throws MalformedURLException malformed URL exception
   */
  public SingleServiceLocator(String url) throws MalformedURLException {
    this.lookupLocator = new LookupLocator(url);
  }

  /**
   * Gets the lookup locator.
   *
   * @return the lookup locator
   */
  public LookupLocator getLookupLocator() {
    return lookupLocator;
  }

  /**
   * @see ServiceLocator
   */
  public Object locate(ServiceTemplate serviceTemplate)
    throws IOException, ClassNotFoundException, InterruptedException {
    ServiceRegistrar registrar = lookupLocator.getRegistrar();

    return registrar.lookup(serviceTemplate);
  }

  /**
   * @see ServiceLocator
   */
  public ServiceItem[] locate(ServiceTemplate serviceTemplate, int waitTime)
         throws IOException, ClassNotFoundException {
    ServiceRegistrar registrar = lookupLocator.getRegistrar();

    return registrar.lookup(serviceTemplate, waitTime).items;
  }

}
