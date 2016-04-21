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


import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;

//
// TODO: When this shifts to JDK 1.5, remove the sleeps (eech!) and replace them
// with a locking mechanism.

//
public class JukeboxImpl implements JukeboxController, PlayerListener {
  static int PLAY_STOPPED = -1;
  static int PLAY_ALL = 1;
  static int PLAY_ALBUM = 2;
  static int PLAY_ARTIST = 3;
  int playMode = PLAY_STOPPED;

  InterruptablePlayer player = null;

  ArrayList songList = new ArrayList();

  String sep = File.separator;
  int currentSong = -1;

  String currentArtist = null;
  String currentAlbum = null;

  public JukeboxImpl(String musicDirectory) throws RemoteException {
    try {
      player = new InterruptablePlayer();
      player.setPlayerListener(this);

    }
    catch (Exception e) {
      System.out.println(e);
    }

    load(musicDirectory);
  }

  public void load(String directory) {
    File dir = new File(directory);
    if (dir.isDirectory() == false)
      return;
    String[] groups = dir.list();
    for (int i = 0; i < groups.length; i++) {
      String groupDir = directory + sep + groups[i];
      File group = new File(groupDir);
      if (group.isFile() == true)
        continue;
      String[] albums = group.list();
      for (int j = 0; j < albums.length; j++) {
        String albumDir = groupDir + sep + albums[j];
        File songDir = new File(albumDir);
        if (songDir.isFile() == true)
          continue;
        String[] songs = songDir.list();
        for (int k = 0; k < songs.length; k++) {
          String loc = albumDir + sep + songs[k];
          SongInfo songInfo = new SongInfo(loc, groups[i], albums[j], songs[k]);
          songList.add(songInfo);
        }
      }
    }
  }

  public void play(String song) {
    player.stop();
    try {
      Thread.sleep(2000L); // fix after move to JDK1.5
    } catch (Exception e) {
    }
    for (int i = 0; i < songList.size(); i++) {
      SongInfo info = (SongInfo) songList.get(i);
      String file = info.song;
      if (file.equals(song)) {
        String loc = info.location;
        try {
          player.load(loc);
          player.start();
        } catch (Exception e) {
          System.out.println(e);
        }
        return;
      }
    }


  }

  public void playAlbum(String album) {
    player.stop();
    try {
      Thread.sleep(2000L); // fix after move to JDK1.5
    } catch (Exception e) {
    }

    playMode = PLAY_ALBUM;
    currentAlbum = album;
    for (int i = 0; i < songList.size(); i++) {
      SongInfo info = (SongInfo) songList.get(i);
      AlbumInfo ai = info.getAlbum();
      String s = ai.getName();
      if (s.equals(album)) {
        currentSong = i - 1;
        break;
      }
    }
    playNext();
  }

  public void playArtist(String artist) {
    player.stop();
    try {
      Thread.sleep(2000L); // fix after move to JDK1.5
    } catch (Exception e) {
    }

    playMode = PLAY_ARTIST;
    currentArtist = artist;
    for (int i = 0; i < songList.size(); i++) {
      SongInfo info = (SongInfo) songList.get(i);
      ArtistInfo ai = info.getArtist();
      String s = ai.getName();
      if (s.equals(artist)) {
        currentSong = i - 1;
        break;
      }
    }
    playNext();
  }

  public void playAll() {
    player.stop();
    try {
      Thread.sleep(2000L); // fix after move to JDK1.5
    }
    catch (Exception e) {
    }
    playMode = PLAY_ALL;
    currentSong = -1;
    playNext();
  }

  private void playNext() {

    currentSong++;

    if (currentSong >= songList.size()) {
      currentSong = -1;
      playMode = PLAY_STOPPED;
      return;
    }

    SongInfo songInfo = (SongInfo) songList.get(currentSong);
    AlbumInfo albumInfo = songInfo.getAlbum();
    String album = albumInfo.getName();

    ArtistInfo artistInfo = songInfo.getArtist();
    String artist = artistInfo.getName();

    if ((playMode == PLAY_ALBUM) && (album.equals(currentAlbum) == false)) {
      currentSong = -1;
      playMode = PLAY_STOPPED;
      return;
    } else if ((playMode == PLAY_ARTIST) && (artist.equals(currentArtist) == false)) {
      currentSong = -1;
      playMode = PLAY_STOPPED;
      return;
    }

    String file = songInfo.getSong();
    String loc = songInfo.getLocation();
    try {
      player.load(loc);
      player.start();
    } catch (Exception e) {
      System.out.println(e);
    }
    return;

  }

  public void stop() {
    playMode = PLAY_STOPPED;
    currentSong = -1;
    player.stop();
  }

  public SongInfo[] getSongs() {
    return (SongInfo[]) songList.toArray(new SongInfo[songList.size()]);
  }

  public void started() {
    //		System.out.println("Started");
  }

  public void stopped() {
    //		System.out.println("Stopped");
    if (playMode != PLAY_STOPPED) {
      playNext();
    }
  }

}
