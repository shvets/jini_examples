/*
 * TimeSpeechImpl.java
 *
 * Beta 1.0
 *
 * This code released as part of:
 * 
 * Home - The Jini Home Automation Project
 *
 * author: Stephen R. Pietrowicz srp@magiclamp.org
 *
 * Copyright 2004 Stephen R. Pietrowicz
 *
 */
package org.jini.home.speech.time;

import javax.speech.EngineException;
import javax.speech.AudioException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.sf.jini.examples.speech.SpeakerImpl;

public class TimeSpeechImpl extends SpeakerImpl {
  //Voice voice = null;

  // english strings set up here so we can change them easily, if we ever
  // change the FreeTTS grammar.
  static String START = "The time is, ";
  static String PAST = "past";
  static String MINUTES_PAST = "minutes past";
  static String MINUTE_PAST = "minute past";
  static String TO = "to";
  static String MINUTES_TO = "minutes to";
  static String MINUTE_TO = "minute to";
  static String MORNING = "in the morning";
  static String AFTERNOON = "in the afternoon";
  static String EVENING = "in the evening";
  static String[] numbers = {
    "",
    "one",
    "two",
    "three",
    "four",
    "five",
    "six",
    "seven",
    "eight",
    "nine",
    "ten",
    "eleven",
    "twelve",
    "thirteen",
    "fourteen",
    "quarter",
    "sixteen",
    "seventeen",
    "eighteen",
    "nineteen",
    "twenty",
    "twenty-one",
    "twenty-two",
    "twenty-three",
    "twenty-four",
    "twenty-five",
    "twenty-six",
    "twenty-seven",
    "twenty-eight",
    "twenty-nine",
    "half"
  };

  public TimeSpeechImpl(String name, String mode) throws RemoteException, AudioException, EngineException {
    super(name, mode);
     //Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
    /* The VoiceManager manages all the voices for FreeTTS.
    */
/*    VoiceManager voiceManager = VoiceManager.getInstance();
    voice = voiceManager.getVoice(name);
    if (voice == null) {
      throw new RemoteException("Can't allocate voice");
    }

    voice.setAudioPlayer(new JavaClipAudioPlayer());

    voice.allocate();
*/
  }

  public void speak(String s) throws RemoteException {
    Date now;
    String sHour, sMinute, sPeriod;
    int hour, minute;
    SimpleDateFormat formatter;

    formatter = new SimpleDateFormat("h");
    now = new Date();
    sHour = formatter.format(now);
    hour = Integer.parseInt(sHour);

    formatter = new SimpleDateFormat("mm");
    sMinute = formatter.format(now);
    minute = Integer.parseInt(sMinute);

    formatter = new SimpleDateFormat("a");
    sPeriod = formatter.format(now);

    String sTimeOfDay;
    if (sPeriod.equals("AM")) {
      sTimeOfDay = "in the morning";
    } else {
      if ((hour < 6) || (hour == 12))
        sTimeOfDay = "in the afternoon";
      else
        sTimeOfDay = "in the evening";
    }


    String sMinutesPast;
    if (minute == 0) {
      sMinutesPast = "";
    } else if (minute < 30) {
      if (minute == 1)
        sMinutesPast = MINUTE_PAST;
      else if (minute == 15)
        sMinutesPast = PAST;
      else
        sMinutesPast = MINUTES_PAST;
    } else {
      minute = 60 - minute;
      hour += 1;
      if (minute == 1)
        sMinutesPast = MINUTE_TO;
      else if (minute == 15)
        sMinutesPast = TO;
      else
        sMinutesPast = MINUTES_TO;

    }

    String sayThis = "The time is, " + numbers[minute] + " " + sMinutesPast + " " + numbers[hour] + ", " + sTimeOfDay;
    System.out.println(sayThis);
    //voice.speak(sayThis);
    super.speak(sayThis);
  }

}
