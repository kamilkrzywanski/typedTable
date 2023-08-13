package org.krzywanski.table.table;

import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Field;

public class TypedTableModel extends DefaultTableModel {
    ColumnCreator columnCreator;
    TypedTableModel(ColumnCreator columnCreator){
        this.columnCreator = columnCreator;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return ((Field)columnCreator.getTableColumns().keySet().toArray()[columnIndex]).getType();
    }
}
