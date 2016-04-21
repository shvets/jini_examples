package org.sf.jini.examples.common;

import net.jini.core.lookup.ServiceID;
import net.jini.id.Uuid;
import net.jini.id.UuidFactory;

import java.io.*;

/**
 * This is helper class for loading/saving service ID object.
 *
 * @version 1.1 11/04/2006
 * @author Alexander Shvets
 */
public class ServiceIDHelper {
  
  /**
   * Try to load the service ID from file.
   *
   * @param serviceIdFile service ID file name
   * @throws IOException I/O exception
   * @return service ID object 
   */
  public ServiceID loadServiceId(File serviceIdFile) throws IOException {
    ServiceID serviceID = null;

    if(serviceIdFile.exists()) {
      //DataInputStream din = null;

      FileInputStream fis = null;
      ObjectInputStream ois = null;

      try {
        // din = new DataInputStream(new FileInputStream(serviceIdFile));
        // serviceID = new ServiceID(din);

        fis = new FileInputStream(serviceIdFile);
        ois = new ObjectInputStream(fis);

        serviceID =(ServiceID)ois.readObject();
      }
      catch (ClassNotFoundException e) {
        throw new IOException(e.getMessage());
      }
      finally {
        if(ois != null) {
          try {
            ois.close();
          }
          catch (IOException e) {
            //ignore
          }
        }
        if(fis != null) {
          try {
            fis.close();
          }
          catch (IOException e) {
            //ignore
          }
        }
      }
    }
    else {
      // Each service ought to have a unique id - this can be assigned
      // us by reggie but we'll generate one ourselves
      /*Uuid myUuid = UuidFactory.generate();

      serviceID = new ServiceID(myUuid.getMostSignificantBits(), myUuid.getLeastSignificantBits());
      */
      Uuid myUuid = UuidFactory.generate();

      serviceID = new ServiceID(myUuid.getMostSignificantBits(), myUuid.getLeastSignificantBits());
    }

    return serviceID;
  }

  /**
   * Try to save the service ID in a file.
   *
   * @param serviceIdFile service ID file
   * @param serviceID the service ID
   * @throws IOException I/O exception
   */
  public void storeServiceId(ServiceID serviceID, File serviceIdFile) throws IOException {
    FileOutputStream fos = null;
    ObjectOutputStream oos = null;

    try {
      //dout = new DataOutputStream(new FileOutputStream(serviceIdFile));

      //serviceID.writeBytes(dout);
      //dout.flush();

      fos = new FileOutputStream(serviceIdFile);
      oos = new ObjectOutputStream(fos);

      oos.writeObject(serviceID);
      oos.flush();
    }
    finally {
      if(oos != null) {
        try {
          oos.close();
        }
        catch (IOException e) {
          // ignore
        }
      }
      if(fos != null) {
        try {
          fos.close();
        }
        catch (IOException e) {
          // ignore
        }
      }
    }
  }

}
