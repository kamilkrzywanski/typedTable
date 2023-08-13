package org.krzywanski.table.table;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Table which is created from Entity List
 *
 * @param <T> - type of table data
 */
public class TypedTable<T> extends JTable {
    List<T> dataList;

    /**
     * Tool for create columns label
     */
    ColumnCreator columnCreator;
    /**
     * Handle for model
     */
    DefaultTableModel model;

    /**
     * Entity for create table
     */
    Class<? extends T> typeClass;
    /**
     * Provide a custom sizes of columns when user change
     */
    TableWidthProvider instance = TableWidthProvider.getInstance();
    /**
     * If dynamic provider used insead of list;
     */
    DataProvider<T> provider;
    /**
     * Handle for current data on list
     */
    List<T> currentData;

    int offset = 0;

    protected TypedTable(List<T> dataList, Class<? extends T> typeClass, DataProvider<T> provider) {
        super(new TypedTableModel(new ColumnCreator(typeClass)));
        columnCreator = new ColumnCreator(typeClass);
        this.typeClass = typeClass;
        this.dataList = dataList;
        this.provider = provider;
        this.currentData = dataList;

        this.setColumnModel(new DefaultTableColumnModel());

        model = (DefaultTableModel) this.getModel();
        columnCreator.getTableColumns().forEach((field, tableColumn) -> {
            model.addColumn(tableColumn.getHeaderValue());
        });

        fixHeadersSize();

        tableHeader.addMouseListener(new TableOrderColumnsMouseAdapter());
    }

    void fixHeadersSize(){
        columnCreator.getTableColumns().forEach((field, tableColumn) -> {
            this.getColumnModel().getColumn(this.getColumnModel()
                            .getColumnIndex(tableColumn.getHeaderValue()))
                    .setPreferredWidth(tableColumn.getPreferredWidth());

            if (instance != null && instance.getReader() != null) {
                Map<String, Integer> cols = instance.getReader().getTableList().get(typeClass.getName());
                this.getColumnModel().getColumn(this.getColumnModel()
                                .getColumnIndex(tableColumn.getHeaderValue()))
                        .setPreferredWidth(cols.get(tableColumn.getHeaderValue()));
            }

        });
    }

    /**
     * Filing table with data
     */
    private void addData(int limit, int offset) {

        currentData = provider != null ? provider.getData(limit, offset) : dataList;
        model.getDataVector().clear();
        currentData.forEach(t -> {
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
     * This adapter is listening for changes on table header
     * and once mouse is released new defintions of columns are saved
     */
    class TableOrderColumnsMouseAdapter extends MouseAdapter {

        public void mouseReleased(MouseEvent arg0) {

            LinkedHashMap<String, Integer> columns = new LinkedHashMap<>();
            for (int i = 0; i < tableHeader.getColumnModel().getColumnCount(); i++) {
                TableColumn column = tableHeader.getColumnModel().getColumn(i);
                columns.put((String) column.getHeaderValue(), column.getWidth());
            }

            if (instance != null && instance.getWriter() != null)
                instance.getWriter().updateColumns(typeClass.getName(), columns);
        }


    }




    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    private Integer findCurrentLimit(){

        if(provider != null){
            return provider.limit;
        }
        return currentData.size();
    }

    public Pair<Integer, Integer> nextPageAction() {


        int limit = findCurrentLimit();
        int dataSize = provider != null ? provider.getSize() : 0;

        int lastPage = (int) Math.ceil((double) dataSize / limit);

        int currentPage = offset / limit;

        if (currentPage < lastPage - 1) {
            offset = Math.min(offset + limit, dataSize);
            currentPage++;
        }

        addData(limit, offset);

        return new Pair<>(currentPage + 1, lastPage);
    }

    public Pair<Integer, Integer> prevPageAction() {
        int limit = findCurrentLimit();

        int currentPage = offset / limit;

        if (currentPage > 0) {
            offset = Math.max(0, offset - limit);
            currentPage--;
        }

        addData(limit, offset);

        return new Pair<>(currentPage + 1, (int) Math.ceil((double) (provider != null ? provider.getSize() : 0) / limit));
    }

    public Pair<Integer, Integer> lastPageAction() {
        int limit = findCurrentLimit();
        int dataSize = provider != null ? provider.getSize() : 0;

        int lastPage = (int) Math.ceil((double) dataSize / limit);

        offset = (lastPage - 1) * limit;

        addData(limit, offset);

        return new Pair<>(lastPage, lastPage);
    }

    public Pair<Integer, Integer> firstPageAction() {
        int limit = findCurrentLimit();

        offset = 0;

        addData(limit, offset);

        return new Pair<>(1, (int) Math.ceil((double) (provider != null ? provider.getSize() : 0) / limit));
    }

    public T getSelectedItem() {
        if (getSelectedRow() != -1) return currentData.get(getSelectedRow());
        return null;
    }

    @Override
    public TableCellEditor getDefaultEditor(Class<?> columnClass) {
        return super.getDefaultEditor(columnClass);
    }

    @Override
    public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
        return new TypedTableRenderer(columnCreator);
    }
}
