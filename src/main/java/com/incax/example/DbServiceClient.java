/*
*  This source code was generated by Inca X
*
*  For more information please contact
*  support@incax.com
*  www.incax.com
*
*/

package com.incax.example;

public class DbServiceClient {

  public static void main(String[] arg) {
    try {

      System.out.println("Locating service...");
      long timeout = 5000;

      DbService service =
        (DbService) ServiceLocator.getService(DbService.class, timeout);

      System.out.println("Service proxy=" + service);

      //SAMPLE CODE TO DUMP OUT THE RESULTS

      String yourSQL = "select * from user_test";
      SQLResult sqlResult = service.executeQuery(yourSQL);
      showResult(sqlResult);

      //part #3 example using a prepared statement in the service
      //the key value maps to the 'preparedStatement' value in your service's config file
      //for example
      // filterEmps=select * from emp where name like ?
      String key = "filterUsers";
      String[] params = {"last%"};
      boolean isUpdate = false;
      SQLRequest request = SQLRequest.makePreparedRequest(key, params, isUpdate);

      sqlResult = service.execute(request);
      showResult(sqlResult);

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private static void showResult(SQLResult sqlResult) {
    System.out.println("-- results ---");
    //if the rowUpdateCount !=-1 then the result object
    //is from a preparedStatement that has updated records
    int rowUpdateCount = sqlResult.getRowUpdateCount();
    if (rowUpdateCount != -1) {
      System.out.println("Number of rows updated=" + rowUpdateCount);
      return;
    }
    //dump out the results to the console
    String[] cols = sqlResult.getCols();
    Object[][] data = sqlResult.getData();
    StringBuffer buf = new StringBuffer();
    for (int c = 0; c < cols.length; c++) {
      buf.append(cols[c]);
      if (c < cols.length - 1) {
        buf.append("\t");
      }
    }
    //dump the column headers
    System.out.println(buf.toString());
    //process the row data
    for (int r = 0; r < data.length; r++) {
      buf = new StringBuffer();
      for (int c = 0; c < data[r].length; c++) {
        buf.append(data[r][c]);
        if (c < cols.length - 1) {
          buf.append("\t");
        }
      }
      //print the row out
      System.out.println(buf.toString());
    }
  }
}