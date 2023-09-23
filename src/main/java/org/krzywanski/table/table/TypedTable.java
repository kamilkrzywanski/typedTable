package org.krzywanski.table.table;

import org.krzywanski.table.annot.MyTableColumn;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.text.Format;
import java.util.*;

/**
 * Table which is created from Entity List
 *
 * @param <T> - type of table data
 */
public class TypedTable<T> extends JTable {

    static {
        TableWidthProvider.setProvider(new DefaultTableWidthProvider());
    }

    private final Map<Class<?>, Format> formatMap= new HashMap<>();

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
    TableWidthTool instance = TableWidthProvider.getInstance();
    /**
     * If dynamic provider used insead of list;
     */
    DataProvider<T> provider;
    /**
     * Handle for current data on list
     */
    List<T> currentData;
    final PaginationUtils paginationUtils;

    protected TypedTable(List<T> dataList, Class<? extends T> typeClass, DataProvider<T> provider) {
        super(new TypedTableModel(new ColumnCreator(typeClass)));
        columnCreator = new ColumnCreator(typeClass);
        this.typeClass = typeClass;
        this.dataList = dataList;
        this.provider = provider;
        this.currentData = dataList;
        this.paginationUtils = new PaginationUtils(provider,this);

        this.setColumnModel(new DefaultTableColumnModel());

        model = (DefaultTableModel) this.getModel();
        columnCreator.getTableColumns().forEach((field, tableColumn) -> model.addColumn(tableColumn.getHeaderValue()));

        fixHeadersSize();

        tableHeader.addMouseListener(new TableOrderColumnsMouseAdapter());
    }

    void fixHeadersSize() {
        columnCreator.getTableColumns().forEach((field, tableColumn) -> {
            this.getColumnModel().getColumn(this.getColumnModel()
                            .getColumnIndex(tableColumn.getHeaderValue()))
                    .setPreferredWidth(tableColumn.getPreferredWidth());

            if (instance != null && instance.getTable(typeClass.getCanonicalName()) != null) {
                Map<String, Integer> cols = instance.getTable(typeClass.getCanonicalName());

                Integer width = Optional.ofNullable(cols.get(tableColumn.getHeaderValue())).orElse(MyTableColumn.defaultWidth);

                this.getColumnModel().getColumn(this.getColumnModel()
                                .getColumnIndex(tableColumn.getHeaderValue()))
                        .setPreferredWidth(width);
            }

        });
    }

    /**
     * Filing table with data
     */
    protected void addData(int limit, int offset) {

        currentData = provider != null ? provider.getData(limit, offset) : dataList;
        model.getDataVector().clear();
        currentData.forEach(t -> {
            Vector<Object> element = new Vector<>();
            columnCreator.getTableColumns().forEach((field, tableColumn) -> {
                try {
                    element.add(field.getReadMethod().invoke(t));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            });
            model.addRow(element);

        });
    }

    public Pair<Integer, Integer> nextPageAction() {
       return paginationUtils.nextPageAction();
    }

    public Pair<Integer, Integer> lastPageAction() {
        return paginationUtils.lastPageAction();
    }

    public Pair<Integer, Integer> prevPageAction() {
        return paginationUtils.prevPageAction();
    }

    public Pair<Integer, Integer> firstPageAction() {
        return paginationUtils.firstPageAction();
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

            if (instance != null)
                instance.updateColumns(typeClass.getName(), columns);
        }


    }


    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
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

    public void addCustomFormatter(Class<?> classFormat, Format format){
        formatMap.put(classFormat,format);
    }

    public Map<Class<?>, Format> getFormatMap() {
        return formatMap;
    }
}
