package org.krzywanski.table;

import javax.swing.table.DefaultTableModel;
import java.beans.PropertyDescriptor;

public class TypedTableModel extends DefaultTableModel {
    ColumnCreator columnCreator;
    TypedTableModel(ColumnCreator columnCreator){
        super(columnCreator.getColumnsNames(), 0);
        this.columnCreator = columnCreator;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return ((PropertyDescriptor)columnCreator.getTableColumns().keySet().toArray()[columnIndex]).getPropertyType();
    }
}
