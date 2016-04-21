package org.sf.jini.examples.glyph;

import java.rmi.Remote;
import java.rmi.RemoteException;

//import net.jini.admin.Administrable;

public interface GlyphRemoteFilter extends /*Administrable, */Remote {

    public String filter(String message) throws RemoteException;

}