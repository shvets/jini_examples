/*
 * This code released as part of:
 * 
 * Home - The Jini Home Automation Project
 *
 * author: Stephen R. Pietrowicz srp@magiclamp.org
 *
 */
package org.jini.home.speech;

import com.sun.speech.freetts.*;
import com.sun.speech.freetts.audio.JavaClipAudioPlayer;

import javax.speech.EngineException;
import java.rmi.RemoteException;

public class GenericSpeechImpl implements GenericSpeaker {
  Voice voice = null;

  public GenericSpeechImpl(String name) throws RemoteException, EngineException {
    /* The VoiceManager manages all the voices for FreeTTS.
    */
    VoiceManager voiceManager = VoiceManager.getInstance();

    voice = voiceManager.getVoice(name);

    if (voice == null) {
      throw new RemoteException("Can't allocate voice");
    }

    voice.setAudioPlayer(new JavaClipAudioPlayer());

    voice.allocate();
  }

  public void speak(String text) throws RemoteException {
    voice.speak(text);
  }

}
