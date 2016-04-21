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

import net.jini.core.lookup.ServiceItem;
import net.jini.lookup.entry.UIDescriptor;
import net.jini.lookup.ui.MainUI;
import net.jini.lookup.ui.attribute.UIFactoryTypes;
import net.jini.lookup.ui.factory.JComponentFactory;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import java.util.Collections;

public class JukeboxUI extends JPanel {
  JukeboxController player = null;
  int currentSong = -1;
  JScrollPane songPane = null;
  JTree tree = null;
  DefaultMutableTreeNode top = null;

  boolean playerGUICreated = false;

  public JukeboxUI(ServiceItem serviceItem) throws RemoteException {
    player = (JukeboxController) serviceItem.service;
    setLayout(new BorderLayout());
    SongInfo[] list = player.getSongs();
    songPane = createSongPane(list);

    add(songPane, BorderLayout.NORTH);

    JPanel buttonPanel = new JPanel();
    JButton playButton = new JButton("Play");
    playButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {

        TreePath path = tree.getSelectionPath();
        Object comp = path.getLastPathComponent();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) comp;
        Object obj = node.getUserObject();

        if (node == top) {
          try {
            player.playAll();
          } catch (Exception e) {
          }
        } else if (obj instanceof SongInfo) {
          SongInfo info = (SongInfo) obj;
          try {
            player.play(info.getSong());
          } catch (Exception e) {
          }
        } else if (obj instanceof AlbumInfo) {
          try {
            AlbumInfo info = (AlbumInfo) obj;
            player.playAlbum(info.getName());
          } catch (Exception e) {
          }
        } else if (obj instanceof ArtistInfo) {
          try {
            ArtistInfo info = (ArtistInfo) obj;
            player.playArtist(info.getName());
          } catch (Exception e) {
          }
        }
      }
    });
    buttonPanel.add(playButton);

    JButton stopButton = new JButton("Stop");
    stopButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {

        try {
          player.stop();
        } catch (Exception e) {

        }

      }
    });
    buttonPanel.add(stopButton);

    add(buttonPanel, BorderLayout.SOUTH);
  }

  private JScrollPane createSongPane(SongInfo[] list) {
    top = new DefaultMutableTreeNode("Music");
    createNodes(top, list);
    tree = new JTree(top);
    JScrollPane treeView = new JScrollPane(tree);
    return treeView;
  }

  private void createNodes(DefaultMutableTreeNode top, SongInfo[] list) {
    DefaultMutableTreeNode song = null;

    ArtistInfo curArtist = new ArtistInfo("");
    DefaultMutableTreeNode artistNode = null;
    AlbumInfo curAlbum = new AlbumInfo("");
    DefaultMutableTreeNode albumNode = null;

    for (int i = 0; i < list.length; i++) {
      ArtistInfo artist = list[i].getArtist();
      AlbumInfo album = list[i].getAlbum();
      if (curArtist.equals(artist) == false) {
        curArtist = artist;
        artistNode = new DefaultMutableTreeNode(curArtist.getName());
        artistNode.setUserObject(artist);
        top.add(artistNode);
      }

      if (curAlbum.equals(album) == false) {
        curAlbum = album;
        albumNode = new DefaultMutableTreeNode(curAlbum.getName());
        albumNode.setUserObject(curAlbum);
        artistNode.add(albumNode);
      }

      song = new DefaultMutableTreeNode(list[i].getSong());
      song.setUserObject(list[i]);
      albumNode.add(song);
    }

  }

  static UIDescriptor getUIDescriptor() throws IOException {
    UIDescriptor desc = new UIDescriptor();
    desc.role = MainUI.ROLE;
    desc.toolkit = JComponentFactory.TOOLKIT;
    desc.attributes = Collections.singleton(new UIFactoryTypes(Collections.singleton(JComponentFactory.TYPE_NAME)));
    desc.factory = new MarshalledObject(new JukeboxUIFactory());
    return desc;
  }
}
