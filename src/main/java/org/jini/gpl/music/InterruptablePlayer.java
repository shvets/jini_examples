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

/*
 * This code released as part of:
 * 
 * Home - The Jini Home Automation Project
 *
 * author: Stephen R. Pietrowicz srp@magiclamp.org
 *
 */

package org.jini.gpl.music;

import javazoom.jl.decoder.*;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * This is a different version of a player that comes with the javalayer
 * distribution.  This version allows the player to be interrupted.  It
 * should probably be rewritten to take advantage of some of the new JDK 1.5
 * concurrency work.
 */
public class InterruptablePlayer implements Runnable {
  /**
   * The MPEG audio bitstream.
   */
  private Bitstream bitstream;

  /**
   * The MPEG audio decoder.
   */
  private Decoder decoder;

  /**
   * The AudioDevice the audio samples are written to.
   */
  private AudioDevice audio;

  /**
   * Has the player been closed?
   */
  private boolean closed = false;

  /**
   * Has the player played back all frames from the stream?
   */
  private boolean complete = false;
  private int lastPosition = 0;

  Thread playerThread = null;

  PlayerListener listener = null;

  public InterruptablePlayer() {
    //
  }

  private void setup(InputStream stream, AudioDevice device) throws JavaLayerException {
    bitstream = new Bitstream(stream);

    if (device != null) audio = device;
    else audio = FactoryRegistry.systemRegistry().createAudioDevice();
    audio.open(decoder = new Decoder());
  }

  public void load(String filename) throws Exception {
    InputStream is = null;
    is = new FileInputStream(filename);
    setup(is, null);
  }

  /**
   * Plays a number of MPEG audio frames.
   *
   * @param frames The number of frames to play.
   * @return true if the last frame was played, or false if there are
   *         more frames.
   */
  public boolean play(int frames) throws JavaLayerException {
    boolean ret = true;


    while (frames-- > 0 && ret) {
      ret = decodeFrame();
    }

    // last frame, ensure all data flushed to the audio device.
    AudioDevice out = audio;
    if (out != null) {

      out.flush();

      synchronized (this) {
        complete = (!closed);
        close();
      }

    }


    return ret;
  }

  public void start() {
    playerThread = new Thread(this);
    playerThread.start();
  }

  public void run() {
    Thread thisThread = Thread.currentThread();
    boolean ret = true;
    if (listener != null)
      listener.started();
    while (ret && (thisThread == playerThread)) {
      try {
        ret = decodeFrame();
      }
      catch (JavaLayerException ex) {
        ret = false;
      }
    }
    synchronized (this) {
      complete = (!closed);
      close();
    }
    if (listener != null) {
      listener.stopped();
    }
  }

  public void stop() {
    playerThread = null;
  }

  /**
   * Cloases this player. Any audio currently playing is stopped
   * immediately.
   */
  public synchronized void close() {
    AudioDevice out = audio;
    if (out != null) {
      closed = true;
      audio = null;
      // this may fail, so ensure object state is set up before
      // calling this method.
      out.close();
      lastPosition = out.getPosition();
      try {
        bitstream.close();
      }
      catch (BitstreamException ex) {
      }
    }
  }

  /**
   * Decodes a single frame.
   *
   * @return true if there are no more frames to decode, false otherwise.
   */
  protected boolean decodeFrame() throws JavaLayerException {
    try {
      AudioDevice out = audio;
      if (out == null) return false;

      Header h = bitstream.readFrame();
      if (h == null) return false;

      // sample buffer set when decoder constructed
      SampleBuffer output = (SampleBuffer) decoder.decodeFrame(h, bitstream);

      synchronized (this) {
        out = audio;
        if (out != null) {
          out.write(output.getBuffer(), 0, output.
            getBufferLength());
        }
      }

      bitstream.closeFrame();
    }
    catch (RuntimeException ex) {
      throw new JavaLayerException(
        "Exception decoding audio frame", ex);
    }
    return true;
  }

  /**
   * skips over a single frame
   *
   * @return false        if there are no more frames to decode, true othe
   *         rwise.
   */
  protected boolean skipFrame() throws JavaLayerException {
    Header h = bitstream.readFrame();
    if (h == null) return false;
    bitstream.closeFrame();
    return true;
  }

  /**
   * Plays a range of MPEG audio frames
   *
   * @param start The first frame to play
   * @param end   The last frame to play
   * @return true if the last frame was played, or false if there are more
   *         frames.
   */
  public boolean play(final int start, final int end) throws
    JavaLayerException {
    boolean ret = true;
    int offset = start;
    while (offset-- > 0 && ret) ret = skipFrame();
    return play(end - start);
  }

  /**
   * sets the <code>PlaybackListener</code>
   */
  public void setPlayerListener(PlayerListener listener) {
    this.listener = listener;
  }

  /**
   * gets the <code>PlaybackListener</code>
   */
  public PlayerListener getPlayerListener() {
    return listener;
  }

}
