package org.krzywanski.table.table;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

/**
 * Table which is created from Entity List
 * @param <T> - type of table data
 */
public class TypedTable<T> extends JTable {
    List<T> dataList = new ArrayList<>();

    ColumnCreator columnCreator;
    DefaultTableModel model;

    Class<? extends T> typeClass;

    TableWidthProvider instance = TableWidthProvider.getInstance();
    protected TypedTable(List<T> dataList, Class<? extends T> typeClass) {
        super(new DefaultTableModel());
        this.typeClass = typeClass;
        this.dataList = dataList;
        this.setColumnModel(new MyColumnModel());

        model = (DefaultTableModel) this.getModel();
            columnCreator = new ColumnCreator(typeClass);
            columnCreator.getTableColumns().forEach( (field,tableColumn) -> {
                model.addColumn(tableColumn.getHeaderValue());
                this.getColumnModel().getColumn(0).setPreferredWidth(333);


            });

        tableHeader.addMouseListener( new MouseAdapter() {

            public void mouseReleased(MouseEvent arg0){

                LinkedHashMap<String, Integer> columns = new LinkedHashMap<>();
                for(int i=0;i<tableHeader.getColumnModel().getColumnCount();i++ ) {
                    TableColumn column = tableHeader.getColumnModel().getColumn(i);
                    int tableColWidth = column.getWidth();
                    columns.put((String) column.getHeaderValue(),column.getWidth());
                }

                if(instance != null && instance.getWriter() != null)
                    instance.getWriter().updateColumns(typeClass.getName(),columns);
            }
        });
        addData();
    }


    private void addData() {
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


    private class MyColumnModel extends DefaultTableColumnModel{
        private String fieldName;

        MyColumnModel(){
            super();
        }



    }

    private class TableHeader{

        String field;

        String descritpon;

        public TableHeader(String field, String descritpon) {
            this.field = field;
            this.descritpon = descritpon;
        }

        public String getField() {
            return field;
        }

        public String getDescritpon() {
            return descritpon;
        }
    }
}
