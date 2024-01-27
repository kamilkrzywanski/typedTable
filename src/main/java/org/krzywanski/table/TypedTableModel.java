package org.krzywanski.table;

import javax.swing.table.DefaultTableModel;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.LinkedList;

public class TypedTableModel extends DefaultTableModel {
    ColumnCreator columnCreator;
    TypedTableModel(ColumnCreator columnCreator){
        super(columnCreator.getColumnsNames(), 0);
        this.columnCreator = columnCreator;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        System.out.println(columnCreator.getTableColumns().size());
        return new LinkedList<>(columnCreator.getTableColumns().keySet()).get(columnIndex).getType();
    }

    public ColumnCreator getColumnCreator() {
        return columnCreator;
    }
}
