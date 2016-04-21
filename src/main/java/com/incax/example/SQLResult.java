package com.incax.example;

import java.io.Serializable;

public class SQLResult
  implements Serializable {

  private Object[][] _data;
  private String[] _cols;
  private int _rowUpdateCount = -1;

  //constructro for prepared statement update result
  public SQLResult(int rowUpdateCount) {
    _rowUpdateCount = rowUpdateCount;
  }

  public SQLResult(Object[][] data, String[] cols) {
    _data = data;
    _cols = cols;
  }

  public Object[][] getData() {
    return _data;
  }

  public String[] getCols() {
    return _cols;
  }

  public int getRowUpdateCount() {
    return _rowUpdateCount;
  }
}
