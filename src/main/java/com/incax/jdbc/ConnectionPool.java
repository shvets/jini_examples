/*
*  Example JDBC connection pool for Inca X DBService Example
*
*  For more information please contact
*  support@incax.com
*  www.incax.com
*
*  All Inca X example code is intended for educational purposes only.
*/

package com.incax.jdbc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ConnectionPool {


  /**
   * Factory method for creating a new ConnectionPool instance
   *
   * @param jdbcUrl  JDBC Driver URL
   * @param jdbcUser Database USER
   * @param jdbcPw   Database Password
   * @param size     Number of connections to share
   * @return New ConnectionPool instance
   * @throws SQLException
   */
  public static ConnectionPool newInstance(String jdbcUrl, String jdbcUser, String jdbcPw, int size)
    throws SQLException {
    return new ConnectionPool(jdbcUrl, jdbcUser, jdbcPw, size);
  }

  private List _pool = new ArrayList();
  private Logger _logger;

  /**
   * Private constructor for creating new ConnectionPool instances
   *
   * @param jdbcUrl  JDBC Driver URL
   * @param jdbcUser Database USER
   * @param jdbcPw   Database Password
   * @param size     Number of connections to share
   * @throws SQLException
   */

  private ConnectionPool(String jdbcUrl, String jdbcUser, String jdbcPw, int size)
    throws SQLException {


    _logger = Logger.getLogger(getClass().getName());

    for (int i = 0; i < size; i++) {
      Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPw);
      //create a dynamic proxy for the connection

      Object connectionProxy =
        Proxy.newProxyInstance(connection.getClass().getClassLoader(),
          new Class[]{Connection.class},
          new ConnectionInvocationHandler(connection)
        );

      _pool.add(connectionProxy);
    }
  }

  /**
   * Blocking method to get a Connection object
   * The Connection object that is returned is a proxy object
   *
   * @param waitTime how long to wait for a connection to become available
   * @return A proxy to a Connection object
   */
  public synchronized Connection getConnection(long waitTime) {

    while (_pool.size() == 0) {
      _logger.info("Waiting for a connection to become available");
      try {
        long start = System.currentTimeMillis();
        wait(waitTime);
        //check if we've timed out
        long now = System.currentTimeMillis();
        if (start + waitTime >= now) {
          _logger.info("Wait timed out");
          return null;
        }
      } catch (InterruptedException ex) {
        _logger.info("Interrupted in wait() - returning null");
        return null;
      }
    }
    Connection connection = (Connection) _pool.remove(0);
    _logger.info("Returning connection - available connections=" + _pool.size());
    return connection;
  }

  /**
   * Invoked from ConnectionInvocationHandler when close() is invoked on the Connection proxy
   *
   * @param connection Connection proxy to put back into the connection pool
   */
  private synchronized void releaseConnection(Object connection) {
    _pool.add(connection);
    _logger.info("Connection closed, adding to pool "
      + " - available connections=" + _pool.size());
    notifyAll();
  }

  private class ConnectionInvocationHandler
    implements InvocationHandler {

    private Connection _impl;

    /**
     * Create an invocation handler for the Connection object proxy
     *
     * @param con the 'real' Connection object
     */
    ConnectionInvocationHandler(Connection con) {
      _impl = con;
    }

    /**
     * Invoked when methods are called on the Connection proxy
     * All method invocation except close() are delegated to
     * the 'real' Connection object _impl
     *
     * @param proxy  the proxy the method was invoked on
     * @param method the method object being invoked
     * @param params parameters to be passed to the method object
     * @return value from Method invocation
     * @throws Throwable
     */
    public Object invoke(Object proxy, Method method, Object[] params)
      throws Throwable {

      _logger.info(method.getName());

      //delegate all calls to the connection except close()
      //when close() is called on the connection it gets put back in the pool
      if (method.getName().equals("close")) {
        _logger.info("close() invoked on " + _impl);

        releaseConnection(proxy);
        return null;

      } else {
        return method.invoke(_impl, params);
      }
    }

  }
}
