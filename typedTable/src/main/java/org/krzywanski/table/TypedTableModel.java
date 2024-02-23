package org.krzywanski.table;

import javax.swing.table.DefaultTableModel;

public class TypedTableModel extends DefaultTableModel {
    final ColumnCreator columnCreator;
    boolean updateAdapterInstalled;
    TypedTableModel(ColumnCreator columnCreator){
        super(columnCreator.getColumnsNames(), 0);
        this.columnCreator = columnCreator;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnCreator.getTableColumns().get(columnIndex).getType();
    }

    public ColumnCreator getColumnCreator() {
        return columnCreator;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return columnCreator.getTableColumns().get(column).getEditable() && updateAdapterInstalled;
    }

    @Override
    public void fireTableCellUpdated(int row, int column) {
        super.fireTableCellUpdated(row, column);
    }

    public void setUpdateAdapterInstalled(boolean updateAdapterInstalled) {
        this.updateAdapterInstalled = updateAdapterInstalled;
    }
}
