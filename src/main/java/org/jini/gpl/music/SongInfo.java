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

import java.io.Serializable;

public class SongInfo implements Serializable {
  String location;
  ArtistInfo artist;
  AlbumInfo album;
  String song;

  public SongInfo(String location, String artist, String album, String song) {
    this.song = song;
    this.artist = new ArtistInfo(artist);
    this.album = new AlbumInfo(album);
    this.location = location;
  }

  public String getLocation() {
    return location;
  }

  public ArtistInfo getArtist() {
    return artist;
  }

  public AlbumInfo getAlbum() {
    return album;
  }

  public String getSong() {
    return song;
  }

  public String toString() {
    return song;
  }
}
