package org.krzywanski.table.table;

import org.krzywanski.table.procressor.ColumnCreator;

import javax.swing.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Table which is created from Entity List
 * @param <T> - type of table data
 */
public class TypedTable<T> extends JTable {
    List<T> dataList = new ArrayList<>();

    ColumnCreator columnCreator = null;

    public TypedTable(List<T> dataList, Class<? extends T> typeClass) {
        super();
        columnCreator = new ColumnCreator(typeClass);
        columnCreator.getTableColumns().forEach(tableColumn -> columnModel.addColumn(tableColumn));
    }



}
