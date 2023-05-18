package org.krzywanski.table.table;

import org.krzywanski.table.procressor.ColumnCreator;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

/**
 * Table which is created from Entity List
 * @param <T> - type of table data
 */
public class TypedTable<T> extends JTable {
    List<T> dataList = new ArrayList<>();

    ColumnCreator columnCreator;
    DefaultTableModel model;
    public TypedTable(List<T> dataList, Class<? extends T> typeClass) {
        super(new DefaultTableModel());
        this.dataList = dataList;
        this.setColumnModel(new DefaultTableColumnModel());

        model = (DefaultTableModel) this.getModel();

            columnCreator = new ColumnCreator(typeClass);
            columnCreator.getTableColumns().forEach( (field,tableColumn) -> {
                model.addColumn(tableColumn.getHeaderValue());
                this.getColumnModel().getColumn(0).setPreferredWidth(333);


            });
            Vector v = new Vector();
        v.add("A");
        v.add("B");
        v.add("C");
        this.getColumnModel().getColumn(0).setPreferredWidth(333);
//        model.addRow(v);
        addData();
    }


    private void addData(){
        dataList.forEach(t -> {
            Vector<Object> element = new Vector<>();
            columnCreator.getTableColumns().forEach((field, tableColumn) -> {
                try {
                    element.add(field.get(t));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
        model.addRow(element);



        });


    }


}
