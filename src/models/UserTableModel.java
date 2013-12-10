package models;
/*
 * Table Model to represent connect users. One model for board.
 */

import javax.swing.table.AbstractTableModel;

public class UserTableModel extends AbstractTableModel{
    private static final long serialVersionUID = 8305718477610260762L;
    private String[] columnNames = {"Users"};      
    private Object[][] data = new Object[0][0];
    
    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }
  
    public void updateData(Object[][] newData) {
        data = newData;
        fireTableDataChanged();
    }

}