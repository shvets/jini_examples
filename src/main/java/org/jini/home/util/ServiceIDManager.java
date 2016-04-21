/*
 * ServiceIDManager.java
 */

package org.jini.home.util;

import net.jini.core.lookup.ServiceID;
import net.jini.lookup.ServiceIDListener;

import java.io.*;

public class ServiceIDManager implements ServiceIDListener {
  String filename = null;

  public ServiceIDManager(String serviceIDFile) {
    filename = serviceIDFile;
  }

  public ServiceID getServiceID() {
    try {
      File file = new File(filename);
      if (file.exists() == false)
        return null;
      DataInputStream dis = new DataInputStream(new FileInputStream(file));
      ServiceID id = new ServiceID(dis);
      return id;
    } catch (Exception e) {
      return null;
    }
  }

  public void serviceIDNotify(ServiceID serviceID) {
    File file = null;
    try {
      file = new File(filename);
      DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
      serviceID.writeBytes(dos);
      dos.close();
    } catch (Exception e) {
      if (file != null)
        file.delete();
    }
  }
}

