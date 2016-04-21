// FilterPanel.java

package org.sf.jini.examples.serviceui;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.sf.jini.examples.simpleservice.Filter;

/**
 * This is the implementation of panel for the filter service.
 *
 * @version 1.1 12/09/2006
 * @author Alexander Shvets
 */
public class FilterPanel extends JPanel {
  /**
   * Creates new filter panel.
   *
   * @param filter the filter
   * @param name the name
   */
  public FilterPanel(final Filter filter, final String name) {
    setLayout(new BorderLayout());
    setPreferredSize(new Dimension(400, 300));

    final JTextField inputField = new JTextField();

    final JTextField outputField = new JTextField();

    JButton okButton = new JButton("OK");

    okButton.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent event) {
        String answer = filter.filter(name + ": " + inputField.getText());

        outputField.setText(answer);
      }
    });

    add(inputField, BorderLayout.NORTH);
    add(okButton, BorderLayout.CENTER);
    add(outputField, BorderLayout.SOUTH);
  }

}
