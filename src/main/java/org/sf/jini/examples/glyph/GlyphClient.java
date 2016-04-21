package org.sf.jini.examples.glyph;

import org.sf.jini.examples.common.JiniClient;
import org.sf.jini.examples.common.SingleServiceLocator;
import org.sf.jini.examples.simpleservice.Filter;

public class GlyphClient extends JiniClient {

  public GlyphClient() {
    super("glyphservice");
  }

  protected Class getServiceClass() {
    return Filter.class;
  }

  public static void main(String args[]) throws Exception {
    System.out.println("Started ...");

    SingleServiceLocator serviceLocator = new SingleServiceLocator("jini://localhost");

    JiniClient client = new GlyphClient();

    Filter filter = (Filter)serviceLocator.locate(client.getServiceTemplate());

    if(filter == null) {
      System.out.println("Filter service did not found.");
    }
    else {
      System.out.println("Got response: " + filter.filter("Where are you?"));
    }
  }

}
