package com.incax.example;

import java.io.Serializable;

public class SQLRequest
  implements Serializable {

  public static final int SQL = 0;
  public static final int PREPARED_STATEMENT = 1;

  private int _type;
  //_query is either an SQL statemnt of a key to a prepared statement
  private String _query;
  //parameters for PreparedStatement
  private String[] _params;
  //isUpdate flag
  private boolean _isUpdate;

  private SQLRequest(int type, String query) {
    this(type, query, null, false);
  }

  private SQLRequest(int type, String query, String[] params, boolean isUpdate) {
    _type = type;
    _query = query;
    _params = params;
    _isUpdate = isUpdate;
  }

  public static SQLRequest makeSQLRequest(String query) {
    return new SQLRequest(SQL, query);
  }

  public static SQLRequest makePreparedRequest(String key, String[] params, boolean isUpdate) {
    return new SQLRequest(PREPARED_STATEMENT, key, params, isUpdate);
  }

  public int getType() {
    return _type;
  }

  public String getQuery() {
    return _query;
  }

  public String[] getParams() {
    return _params;
  }

  public boolean isUpdate() {
    return _isUpdate;
  }

}
