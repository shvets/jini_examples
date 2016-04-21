package org.sf.jini.examples.common;

import net.jini.core.lookup.ServiceTemplate;
import net.jini.core.lookup.ServiceItem;

import java.io.IOException;

/**
 * Interface for representing service locator behavior.
 *
 * @version 1.1 11/04/2006
 * @author Alexander Shvets
 */
public interface ServiceLocator {
  /**
   * Locates the service.
   *
   * @param serviceTemplate the service template
   * @return the service
   * @throws IOException I/O exception
   * @throws ClassNotFoundException class not found exception
   * @throws InterruptedException interrupted exception
   */
  Object locate(ServiceTemplate serviceTemplate)
         throws IOException, ClassNotFoundException, InterruptedException;

  /**
   * Locates the array of services. Each service is represented as ServiceItem object.
   *
   * @param serviceTemplate the service template
   * @param waitTime the wait time
   * @return the array of service items
   * @throws IOException I/O exception
   * @throws ClassNotFoundException class not found exception
   * @throws InterruptedException interrupted exception
   */
  ServiceItem[] locate(ServiceTemplate serviceTemplate, int waitTime)
                throws IOException, ClassNotFoundException, InterruptedException;

}
