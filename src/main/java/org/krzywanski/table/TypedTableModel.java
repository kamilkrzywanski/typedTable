package org.krzywanski.table;

import javax.swing.table.DefaultTableModel;

public class TypedTableModel extends DefaultTableModel {
    ColumnCreator columnCreator;
    TypedTableModel(ColumnCreator columnCreator){
        super(columnCreator.getColumnsNames(), 0);
        this.columnCreator = columnCreator;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnCreator.getFieldMocks().get(columnIndex).getType();
    }

    public ColumnCreator getColumnCreator() {
        return columnCreator;
    }
}
