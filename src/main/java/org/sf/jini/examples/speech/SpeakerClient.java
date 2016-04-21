package org.sf.jini.examples.speech;

import org.sf.jini.examples.common.JiniClient;
import org.sf.jini.examples.common.SingleServiceLocator;

import java.util.List;

/**
 * Creates new client for "speaker" service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class SpeakerClient extends JiniClient {

  /**
   * Creates new client.
   */
  public SpeakerClient() {
    super("speaker");
  }

  /**
   * Gets the class for looking service
   *
   * @return the class for looking service
   */
  protected Class getServiceClass() {
    return Speaker.class;
  }

  /**
   * Main entry point for the client.
   *
   * @param args the list of arguments
   * @throws Exception the exception
   */
  public static void main(String args[]) throws Exception {
    System.out.println("Started ...");

    SingleServiceLocator serviceLocator = new SingleServiceLocator("jini://localhost");

    JiniClient client = new SpeakerClient();

    Speaker speaker = (Speaker)serviceLocator.locate(client.getServiceTemplate());

    if(speaker == null) {
      System.out.println("speaker service did not found.");
    }
    else {
      //if(speaker instanceof SpeakerImpl) {
        //Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
        
        //SpeakerImpl speakerImpl = (SpeakerImpl)speaker;

      List list = speaker.listAllVoices();

      System.out.println("Voices:");
      
      for (Object s : list) {
        System.out.println(s);
      }

      //}

      speaker.speak("Hello, Jini World.");
    }
  }

}
