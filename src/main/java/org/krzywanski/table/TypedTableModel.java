package org.krzywanski.table;

import javax.swing.table.DefaultTableModel;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

public class TypedTableModel extends DefaultTableModel {
    ColumnCreator columnCreator;
    TypedTableModel(ColumnCreator columnCreator){
        super(columnCreator.getColumnsNames(), 0);
        this.columnCreator = columnCreator;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Field o = (Field) columnCreator.getTableColumns().keySet().toArray()[columnIndex];
        return o.getType();
    }
}
