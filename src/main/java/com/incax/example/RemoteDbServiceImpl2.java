package com.incax.example;

import com.incax.jdbc.ConnectionPool;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.rmi.RemoteException;
import java.sql.*;


public class RemoteDbServiceImpl2 implements RemoteDbService {
  //DbService Specific
  private ConnectionPool _dbConnectionPool;
  //example part #3 Prepared SQL statements held in the service's config file
  private Map _statements = new HashMap();
  private Logger _logger = Logger.getLogger(getClass().getName());



  //DbService methods
  //DbService methods
  public SQLResult executeQuery(String sql)
    throws RemoteException {
    //create a wrapper around part #2 pass thru SQL
    SQLRequest request = SQLRequest.makeSQLRequest(sql);
    return executeImpl(request);
  }

  //new in example part #3
  public SQLResult execute(SQLRequest request) throws RemoteException {

    return executeImpl(request);
  }

  //returns an array of prepared statements
  //used for testing the example Service UI
  public Object[] getPreparedStatementKeys() throws RemoteException {
    return _statements.keySet().toArray();
  }

  //returns the number of parameters expected for the preparedStatement
  //used for testing the example Service UI
  public int getNumParameters(Object preparedStatementKey) throws RemoteException {
    int numParams = 0;
    String statement = (String) _statements.get(preparedStatementKey);
    if (statement == null) {
      throw new DbServiceException("Unknown preparedStatementKey " + preparedStatementKey);
    }
    //cound the number of declared parameters
    int index = statement.indexOf("?");
    while (index != -1) {
      numParams++;
      if (index + 1 == statement.length()) {
        break;
      }
      index = statement.indexOf("?", index + 1);
    }
    return numParams;
  }

  private SQLResult executeQueryImpl(String sql, Statement statement)
    throws RemoteException, SQLException {
    if (sql == null) {
      throw new DbServiceException("Can't execute a null query");
    }

    if (statement.execute(sql) == false) {
      return null;
    }
    ResultSet rs = null;
    try {
      rs = statement.getResultSet();
      return createResult(rs);
    } finally {
      if (rs != null) {
        try {
          rs.close();
        } catch (Exception ex) {
        }
      }
    }

  }

  private SQLResult executePreparedStatement(PreparedStatement statement, SQLRequest request)
    throws RemoteException, SQLException {

    String[] params = request.getParams();
    for (int i = 0; i < params.length; i++) {
      //set the parameters for the prepared statement
      statement.setString(i + 1, params[i]);
    }

    ResultSet rs = null;
    try {

      if (request.isUpdate()) {
        int rowUpdateCount = statement.executeUpdate();
        return new SQLResult(rowUpdateCount);
      } else {
        rs = statement.executeQuery();
        return createResult(rs);
      }
    } finally {
      if (rs != null) {
        try {
          rs.close();
        } catch (Exception ex) {
        }
      }
    }

  }

  private SQLResult executeImpl(SQLRequest request)
    throws RemoteException {

    _logger.info("Executing SQLRequest " + request);

    Statement statement = null;
    //get a connection from the pool & wait 5 seconds for it
    Connection connection = _dbConnectionPool.getConnection(5000L);
    //check if we get a connection or we timed out & got null
    if (connection == null) {
      throw new DbServiceException("Unable to get connection - request timed out");
    }

    try {

      switch (request.getType()) {
        case SQLRequest.SQL:
          statement = connection.createStatement();
          return executeQueryImpl(request.getQuery(), statement);
        case SQLRequest.PREPARED_STATEMENT:
          //retrieve the prepared statement that matches the query Key
          String prepSql = (String) _statements.get(request.getQuery());
          if (prepSql == null) {
            throw new DbServiceException("Unknown prepared statement key " + request.getQuery());
          }
          statement = connection.prepareStatement(prepSql);

          return executePreparedStatement((PreparedStatement) statement, request);

        default:
          throw new DbServiceException("Unsupported SQLRequest type");
      }

    } catch (SQLException ex) {
      throw new DbServiceException("" + ex);
    }
    finally {
      _logger.info("Tidying up");
      //MAKE SURE THE CONNECTION IS CLOSED - this puts it back in the pool
      if (connection != null) {

        try {
          connection.close();

        } catch (Exception ex) {
          ex.printStackTrace();
          _logger.severe("" + ex);
        }
      }
      if (statement != null) {
        try {
          statement.close();
        } catch (Exception ex) {
        }
      }

    }
  }

  //Create a result object from the ResultSet
  private SQLResult createResult(ResultSet rs)
    throws SQLException {
    ResultSetMetaData metaData = rs.getMetaData();

    int ncols = metaData.getColumnCount();
    String[] cols = new String[ncols];
    for (int i = 0; i < ncols; i++) {
      cols[i] = metaData.getColumnName(i + 1);
    }
    ArrayList results = new ArrayList();
    while (rs.next()) {

      Object[] data = new Object[ncols];
      for (int i = 0; i < ncols; i++) {
        data[i] = rs.getObject(i + 1);
      }
      results.add(data);
    }

    Object[] res = results.toArray();
    Object[][] resultArray = new Object[res.length][ncols];

    for (int row = 0; row < res.length; row++) {
      Object[] rowData = (Object[]) res[row];
      for (int col = 0; col < ncols; col++) {
        resultArray[row][col] = rowData[col];
      }
    }
    return new SQLResult(resultArray, cols);
  }

  public void setDBConnectionPool(ConnectionPool dbConnectionPool) {
    _dbConnectionPool = dbConnectionPool;
  }

  public void setStatements(Map statements) {
    _statements = statements;
  }
}
