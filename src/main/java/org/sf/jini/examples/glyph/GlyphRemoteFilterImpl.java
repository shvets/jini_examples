package org.sf.jini.examples.glyph;

import org.jini.glyph.Exportable;
import org.jini.glyph.Service;

import java.rmi.RemoteException;

@Service
@Exportable
//(parentInterfaces = "net.jini.admin.Administrable")
public class GlyphRemoteFilterImpl implements GlyphRemoteFilter {
  //private Object adminObject;

  public GlyphRemoteFilterImpl(String[] args) {}

/*  public void setAdmin(Object adminObject) {
    System.out.println("Set admin to: " + adminObject.getClass().getName());

    //this.adminObject = adminObject;
  }
*/
  public String filter(String message) throws RemoteException {
    return message;
  }
  
}

