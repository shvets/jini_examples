package com.incax.example;

import java.rmi.RemoteException;

public class DbServiceException extends RemoteException {
  public DbServiceException(String why, Exception cause) {
    super(why, cause);
  }

  public DbServiceException(String why) {
    super(why);
  }
}
