package org.sf.jini.examples.speech;

import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.speech.synthesis.Voice;
import javax.speech.EngineException;
import javax.speech.AudioException;
import javax.speech.Central;
import javax.speech.EngineList;
import java.io.Serializable;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;
import java.rmi.RemoteException;

/**
 * This is the implementation of the speaker behavior.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class SpeakerImpl implements Speaker, Serializable {
  /** The synthesizer. */
  private Synthesizer synthesizer;

  /** The voice name. */
  private String voiceName;

  /**
   * Creates the speaker implementation.
   *
   * @param voiceName the voice name
   * @param mode the mode
   * @throws EngineException the engine exception
   * @throws AudioException the audio exception
   * @throws RemoteException the remote exception
   */
  public SpeakerImpl(String voiceName, String mode) throws EngineException, AudioException, RemoteException {
    this.voiceName = voiceName;

    boolean created = createSynthesizer(mode);

    if(!created) {
      throw new RemoteException("Cannot create synthesizer.");
    }
  }

  /**
   * Creates new synthesizer.
   *
   * @param mode the mode
   * @return new synthesizer
   * @throws EngineException the engine exception
   * @throws AudioException the audio exception
   */
  private boolean createSynthesizer(String mode) throws EngineException, AudioException {
    SynthesizerModeDesc desc = new SynthesizerModeDesc(
      null,          // engine name
      mode,     // mode name
      Locale.US,     // locale
      null,          // running
      null);         // voice

    synthesizer = Central.createSynthesizer(desc);

    if (synthesizer == null) {
      //System.err.println("Cannot find synthesizer.");
      //System.exit(1);
      return false;
    }

    synthesizer.allocate();
    synthesizer.resume();

    return true;
  }

  /**
   * Destroys the synthesizer.
   *
   * @throws EngineException the engine exception
   */
  private void destroySynthesizer() throws EngineException {
    synthesizer.deallocate();
  }

  /**
   * Gets the voice.
   *
   * @param voiceName the voice name
   * @return the voice
   */
  private Voice getVoice(String voiceName) {
    SynthesizerModeDesc desc = (SynthesizerModeDesc) synthesizer.getEngineModeDesc();

    Voice[] voices = desc.getVoices();

    Voice voice = null;

    for (int i = 0; i < voices.length && voice == null; i++) {
      if (voices[i].getName().equals(voiceName)) {
        voice = voices[i];
      }
    }

    return voice;
  }

  /**
   * Speaks the message.
   *
   * @param message the message
   * @throws RemoteException the remote exception
   */
  public void speak(String message) throws RemoteException {
    try {
      synthesizer.getSynthesizerProperties().setVoice(getVoice(voiceName));

      synthesizer.speakPlainText(message, null);

      synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);

    } catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException(e.getMessage());
    }

//          destroySynthesizer();
  }

  /**
   * Lists all available voices for given mode.
   *
   * @param mode the mode
   * @return list of available voices
   * @throws RemoteException the remote exception
   */
  public List listAllVoices(String mode) throws RemoteException {
    List<String> list = new ArrayList<String>();

    //System.out.println();
    //System.out.println("All " + mode + " Mode JSAPI Synthesizers and Voices:");

    SynthesizerModeDesc required = new SynthesizerModeDesc(
      null,      // engine name
      mode,  // mode name
      Locale.US, // locale
      null,      // running
      null);     // voices

    EngineList engineList = Central.availableSynthesizers(required);
    for (Object anEngineList : engineList) {

      SynthesizerModeDesc desc = (SynthesizerModeDesc) anEngineList;
//      System.out.println("    " + desc.getEngineName()
//        + " (mode=" + desc.getModeName() + ", locale=" + desc.getLocale() + "):");

      Voice[] voices = desc.getVoices();

      for (Voice voice : voices) {
        //System.out.println("        " + voice.getName());
        list.add(voice.getName());
      }
    }

    return list;
  }

  /**
   * Lists all available voices.
   *
   * @return list of available voices
   * @throws RemoteException the remote exception
   */
  public List listAllVoices() throws RemoteException {
    List<String> list = new ArrayList<String>();

    //System.out.println();
    //System.out.println("All voices available:");

    com.sun.speech.freetts.VoiceManager voiceManager = com.sun.speech.freetts.VoiceManager.getInstance();
    com.sun.speech.freetts.Voice[] voices = voiceManager.getVoices();

    for (com.sun.speech.freetts.Voice voice : voices) {
      //System.out.println("    " + voice.getName() + " (" + voice.getDomain() + " domain)");
      list.add(voice.getName() + " (" + voice.getDomain() + " domain)");
    }

    return list;
  }

}
