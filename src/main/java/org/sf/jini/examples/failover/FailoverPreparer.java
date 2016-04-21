package org.sf.jini.examples.failover;

import net.jini.security.ProxyPreparer;

import java.rmi.RemoteException;
import java.rmi.ConnectException;
import java.rmi.NoSuchObjectException;

import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import java.util.logging.Logger;

import net.jini.core.lookup.ServiceTemplate;
import net.jini.core.lookup.ServiceItem;

import org.sf.jini.examples.common.ServiceLocator;

public class FailoverPreparer implements ProxyPreparer, Serializable, InvocationHandler {

  /**
   * Object reference to the real proxy
   */
  private Object proxy;
  private transient Logger logger = Logger.getLogger(getClass().getName());

  /**
   * ServiceTemplate used by the InvocationHandler to lookup a new instance of a
   * failed service
   */
  private ServiceTemplate serviceTemplate;
  private ServiceLocator serviceLocator;

  //how long to wait for a new service instance
  //you could add this as a parameter
  /**
   * How long to wait for service discovery
   */
  private static final int WAIT_TIME = 20000; //20 seconds

  static long serialVersionUID = 1;

  /**
   * Create a new FailoverPreparer that uses the tmpl
   * parameter on failover to lookup a different instance of the service
   *
   * @param tmpl service template
   * @param serviceLocator the service locator
   */
  public FailoverPreparer(ServiceTemplate tmpl, ServiceLocator serviceLocator) {
    serviceTemplate = tmpl;
    this.serviceLocator = serviceLocator;
  }

  /**
   * Creates a dynamic proxy
   *
   * @param proxy the proxy to prepare
   * @return the prepared proxy
   * @throws RemoteException if a communication-related exception occurs
   */
  public Object prepareProxy(Object proxy) throws RemoteException {
    this.proxy = proxy;

    logger.info("Preparing Proxy");

    Class proxyClass = proxy.getClass();

    return Proxy.newProxyInstance(proxyClass.getClassLoader(), proxyClass.getInterfaces(), this);
  }

  /**
   * InvocationHandler for dynanic proxy
   * delegates method invocations to the realProxy
   * When a ConnectException or a NoSuchObjectException is caught
   * the handler then tries to use the service template to lookup a
   * new instance of the service
   *
   * @param proxy the proxy
   * @param method the method
   * @param args arguments array
   * @return the result of invokation
   * @throws Throwable
   */
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    try {
      return method.invoke(this.proxy, args);
    }
    catch (InvocationTargetException e) {
      Throwable targetException = e.getTargetException();

      if (targetException instanceof ConnectException || targetException instanceof NoSuchObjectException) {
        if (logger == null) {
          logger = Logger.getLogger(getClass().getName()); //create a new logger
        }

        logger.info("Exception caught in proxy " + e);

        try {
          //service failover, maybe network problem so lookup another instance of the service
          //MultipleServiceLocator serviceLocator = new MultipleServiceLocator();

          ServiceItem[] items = serviceLocator.locate(serviceTemplate, WAIT_TIME);

          this.proxy = items[0].service;
          
          //try again
          return invoke(this.proxy, method, args);
        }
        catch (RemoteException rex) {
          throw rex;
        }
      }

      throw targetException;
    }
  }

}
