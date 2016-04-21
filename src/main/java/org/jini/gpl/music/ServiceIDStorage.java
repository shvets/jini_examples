/*-----------------------------------------------------------------------
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *----------------------------------------------------------------------
 */
package org.jini.gpl.music;

import net.jini.core.lookup.ServiceID;
import net.jini.lookup.ServiceIDListener;

import java.io.*;

/**
 * This class implements a simple service ID storage and retrieval
 * mechanism.
 */
public class ServiceIDStorage implements ServiceIDListener {
  String filename = null;

  public ServiceIDStorage(String serviceIDFile) {
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
