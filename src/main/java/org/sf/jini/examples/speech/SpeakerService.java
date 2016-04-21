package org.sf.jini.examples.speech;

import org.sf.jini.examples.common.SingleServiceLocator;
import org.sf.jini.examples.common.ProxiedJiniService;

import javax.speech.Central;
import javax.speech.EngineException;

import net.jini.export.Exporter;
import net.jini.jeri.BasicJeriExporter;
import net.jini.jeri.BasicILFactory;
import net.jini.jeri.tcp.TcpServerEndpoint;

/**
 * Creates new "speaker" service (showing how to use FreeTTS using only the Java
 * Speech API (JSAPI)).
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */

public class SpeakerService extends ProxiedJiniService {
  static {
    try {
      Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
    } catch (EngineException e) {
      System.out.println();
    }
  }

  /**
   * Creates new service.
   *
   * @param exporter the exporter
   * @param voiceName the voice name
   * @param mode the mode
   * @throws Exception the exception
   */
  public SpeakerService(Exporter exporter, String voiceName, String mode) throws Exception {
    createProxyAdapter(exporter);

    export(new SpeakerImpl(voiceName, mode));
    
    createServiceItem("speaker");
  }

  /**
   * Main entry point for the service registration.
   *
   * @param args the list of arguments
   *
   * @throws Exception the exception
   */
  public static void main(String[] args) throws Exception {
    SingleServiceLocator serviceLocator = new SingleServiceLocator("jini://localhost");

    Exporter exporter = new BasicJeriExporter(TcpServerEndpoint.getInstance(0), new BasicILFactory());
    
    SpeakerService service = new SpeakerService(exporter, "kevin", "general");

    System.out.println("Started ...");

    service.register(serviceLocator, 60*1000);

    System.out.println("Registered.");
  }

}
