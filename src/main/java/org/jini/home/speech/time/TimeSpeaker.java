/*
 * TimeSpeaker.java
 *
 * Beta 1.0
 *
 * This code released as part of:
 * 
 * Home - The Jini Home Automation Project
 *
 * author: Stephen R. Pietrowicz srp@magiclamp.org
 *
 * Copyright 2004 Stephen R. Pietrowicz
 */

package org.jini.home.speech.time;


import java.rmi.RemoteException;
import java.rmi.Remote;


public interface TimeSpeaker extends Remote {

  public void speakCurrentTime() throws RemoteException;

}
