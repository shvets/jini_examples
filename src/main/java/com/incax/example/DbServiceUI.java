/*
*  This source code was generated by Inca X
*
*  For more information please contact
*  support@incax.com
*  www.incax.com
*
*/
package com.incax.example;

import net.jini.core.lookup.ServiceItem;
import net.jini.lookup.entry.UIDescriptor;
import net.jini.lookup.ui.MainUI;
import net.jini.lookup.ui.attribute.UIFactoryTypes;
import net.jini.lookup.ui.factory.JComponentFactory;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

public class DbServiceUI {

  public static UIDescriptor getUIDescriptor()
    throws java.io.IOException {

    UIDescriptor desc = new UIDescriptor();
    desc.role = MainUI.ROLE;
    desc.toolkit = JComponentFactory.TOOLKIT;
    desc.attributes = new HashSet();

    Set types = new HashSet();
    types.add(JComponentFactory.TYPE_NAME);
    UIFactoryTypes factoryTypes = new UIFactoryTypes(types);

    desc.attributes.add(factoryTypes);

    desc.factory = new MarshalledObject(new Factory());
    return desc;
  }


  private static class Factory
    implements JComponentFactory {

    //activatableFactory methods
    //JComponent Factory
    public JComponent getJComponent(Object roleObject) {

      ServiceItem item = (ServiceItem) roleObject;

      DbService dbService = (DbService) item.service;
      //create a tabbed ui to contain the orginal pass thru SQL view
      //and a new view for testing PreparedStatements
      JTabbedPane tp = new JTabbedPane();
      tp.add("SQL", createSQLView(dbService));
      tp.add("Prepared statements", createPreparedStatementView(dbService));
      tp.setSize(500, 400);
      return tp;
    }
  }

  //new in example part #3
  private static JComponent createPreparedStatementView(final DbService dbService) {
    //first get the array of prepared statement keys
    Object[] keyArray = null;
    try {
      keyArray = dbService.getPreparedStatementKeys();
    } catch (RemoteException ex) {
      //for testing just return a label with the exception test
      return new JLabel(ex.toString());
    }
    // create prepared statement UI
    final JPanel topUI = new JPanel();
    topUI.setLayout(new BorderLayout());

    final JComboBox keyCombo = new JComboBox(keyArray);
    final JCheckBox isUpdate = new JCheckBox("Update statement");
    final ParamsTableModel paramsModel = new ParamsTableModel();
    final JTable paramTable = new JTable(paramsModel);
    topUI.add(new JScrollPane(paramTable), BorderLayout.CENTER);

    //add a listener for the combo box so when a new key is selected
    //the table displays the correct number of parameters required

    keyCombo.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent evt) {
        try {
          Object key = keyCombo.getSelectedItem();
          int nParams = dbService.getNumParameters(key);

          System.out.println(key + " has " + nParams + " parameters");

          paramsModel.update(nParams);
        } catch (RemoteException ex) {
          JOptionPane.showMessageDialog(topUI, ex);
        }
      }
    });

    final JPanel ui = new JPanel();
    ui.setLayout(new BorderLayout());

    final ResultsTableModel model = new ResultsTableModel();
    final JTable table = new JTable();

    JPanel ctrls = new JPanel();
    JButton execute = new JButton("Execute");
    ctrls.add(new JLabel("Select query key"));
    ctrls.add(keyCombo);
    ctrls.add(isUpdate);
    ctrls.add(execute);

    //add an actionListener to execute the query
    execute.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        try {
          //make the request object from the table data
          CellEditor editor = paramTable.getCellEditor();
          if (editor != null) {
            //make sure the edited cell data is saved
            editor.stopCellEditing();
          }
          String key = keyCombo.getSelectedItem().toString();
          String[] params = paramsModel.getParams();
          boolean update = isUpdate.isSelected();

          SQLRequest request =
            SQLRequest.makePreparedRequest(key, params, update);

          SQLResult result = dbService.execute(request);
          int nRowsUpdated = result.getRowUpdateCount();
          //if this is not -1 then an update statement has been executed
          if (nRowsUpdated != -1) {
            JOptionPane.showMessageDialog(ui, "Number of rows updated=" + nRowsUpdated);
          } else {
            model.update(result);
            table.setModel(model);
          }

        } catch (RemoteException ex) {
          JOptionPane.showMessageDialog(ui, ex);
        }
      }
    });
    keyCombo.setSelectedIndex(0);

    //add the components to the gui
    JSplitPane splitter = new JSplitPane(
      JSplitPane.VERTICAL_SPLIT,
      new JScrollPane(table),
      new JScrollPane(topUI)

    );
    splitter.setDividerLocation(180);

    ui.add(splitter, BorderLayout.CENTER);
    ui.add(ctrls, BorderLayout.SOUTH);

    return ui;
  }

  private static JComponent createSQLView(final DbService dbService) {
    // create the pass thru SQL UI
    JPanel ui = new JPanel();
    ui.setLayout(new BorderLayout());
    //the ui has a splitpane with an editor
    //and a JTable to show the results
    final ResultsTableModel model = new ResultsTableModel();
    final JTable table = new JTable();

    final JEditorPane sqlEditor = new JEditorPane();

    sqlEditor.setText("Enter your SQL query here");
    JPanel ctrls = new JPanel();
    JButton execute = new JButton("Execute");
    ctrls.add(execute);
    //add an actionListener to execute the query
    execute.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        try {
          SQLResult result = dbService.executeQuery(sqlEditor.getText());
          model.update(result);
          table.setModel(model);
          //System.out.println("Number of rows="+data.length);
        } catch (RemoteException ex) {
          sqlEditor.setText("" + ex);
        }
      }
    });

    //add the components to the gui
    JSplitPane splitter = new JSplitPane(
      JSplitPane.VERTICAL_SPLIT,

      new JScrollPane(table),
      new JScrollPane(sqlEditor)

    );
    splitter.setDividerLocation(180);

    ui.add(splitter, BorderLayout.CENTER);
    ui.add(ctrls, BorderLayout.SOUTH);

    return ui;
  }

  private static class ParamsTableModel extends AbstractTableModel {

    String[] _cols = {"Parameter", "Value"};
    Object[] _data = new Object[]{};

    void update(int nRows) {
      _data = new Object[nRows];
      fireTableDataChanged();
    }

    String[] getParams() {
      String[] params = new String[_data.length];
      for (int i = 0; i < _data.length; i++) {
        params[i] = _data[i].toString();
      }
      return params;
    }

    public int getRowCount() {
      return _data.length;
    }

    public int getColumnCount() {
      return _cols.length;
    }

    public String getColumnName(int col) {
      return _cols[col];
    }

    public Object getValueAt(int r, int c) {
      if (c == 0) {
        return "parameter " + c;
      } else {
        return _data[r];
      }
    }

    public boolean isCellEditable(int r, int c) {
      return c == 1;
    }

    public void setValueAt(Object value, int r, int c) {
      _data[r] = value;
    }


  }

  private static class ResultsTableModel extends AbstractTableModel {

    Object[][] _data;
    String[] _cols;

    public int getRowCount() {
      if (_data == null) {
        return 0;
      }
      return _data.length;
    }

    public int getColumnCount() {
      if (_cols == null) {
        return 0;
      }
      return _cols.length;
    }

    public String getColumnName(int col) {
      return _cols[col];
    }

    public Object getValueAt(int r, int c) {
      Object[] rowData = (Object[]) _data[r];
      return rowData[c];
    }

    public void update(SQLResult result) {
      _data = result.getData();
      _cols = result.getCols();
      fireTableDataChanged();
    }

  }
}
