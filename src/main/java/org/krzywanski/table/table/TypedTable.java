package org.krzywanski.table.table;

import org.krzywanski.table.annot.MyTableColumn;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Table which is created from Entity List
 * @param <T> - type of table data
 */
public class TypedTable<T> extends JTable {
    List<T> dataList = new ArrayList<>();

    /**
     * Tool for create columns label
     */
    ColumnCreator columnCreator;
    DefaultTableModel model;

    /**
     * Entity for create table
     */
    Class<? extends T> typeClass;
    Pagination pagination;

    /**
     * Provide a custom sizes of columns when user change
     */
    TableWidthProvider instance = TableWidthProvider.getInstance();
    protected TypedTable(List<T> dataList, Class<? extends T> typeClass, Pagination pagination) {
        super(new DefaultTableModel());
        this.typeClass = typeClass;
        this.dataList = dataList;
        this.setColumnModel(new MyColumnModel());

        model = (DefaultTableModel) this.getModel();
            columnCreator = new ColumnCreator(typeClass);
            columnCreator.getTableColumns().forEach( (field,tableColumn) -> {
                model.addColumn(tableColumn.getHeaderValue());

                if(instance != null && instance.getReader() != null) {
                   Map<String,Integer> cols =  instance.getReader().getTableList().get(typeClass.getName());
                    this.getColumnModel().getColumn(((DefaultTableColumnModel) this.getColumnModel())
                            .getColumnIndex(tableColumn.getHeaderValue()))
                            .setPreferredWidth(cols.get(tableColumn.getHeaderValue()));
                }

                this.getColumnModel().getColumn(((DefaultTableColumnModel) this.getColumnModel())
                        .getColumnIndex(tableColumn.getHeaderValue())).setCellRenderer(createRenderer(field));
            });
        tableHeader.addMouseListener( new TableOrderColumnsMouseAdapter());
        addData();
    }


    /**
     * Filing table with data
     */
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

    /**
     * Simple model for coulm
     */
    private class MyColumnModel extends DefaultTableColumnModel{
        private String fieldName;

        MyColumnModel(){
            super();
        }
    }

    /**
     * This adapter is listening for changes on table header
     * and once mouse is released new defintions of columns are saved
     */
    class TableOrderColumnsMouseAdapter extends MouseAdapter{

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


    }

    private TableCellRenderer createRenderer(Field field){
        TableCellRenderer tableCellRenderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value, boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                if( value instanceof Date) {
                    value = new SimpleDateFormat(Objects.toString(getFormat(field), "MM/dd/yy")).format(value);
                }
                return super.getTableCellRendererComponent(table, value, isSelected,
                        hasFocus, row, column);
            }
        };


        return tableCellRenderer;
    }

    private String getFormat(Field field){
        MyTableColumn annotation =  field.getAnnotation(MyTableColumn.class);
        String format = null;
        if(annotation!=null){
            if(!annotation.format().isEmpty())
                format = annotation.format();
        }
        return format;
    }

}
