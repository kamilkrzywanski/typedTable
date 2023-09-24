package org.krzywanski.table.table;

import org.krzywanski.table.annot.MyTableColumn;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
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

    /**
     * List of ilsteners to execute when requested is change page by table
     */
    List<ActionListener> changePageListeners = new ArrayList<>();

    SortColumn sortColumn;

    private final Map<Class<?>, Format> formatMap = new HashMap<>();

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
        this.paginationUtils = new PaginationUtils(provider, this);

        this.setColumnModel(new DefaultTableColumnModel());

        model = (DefaultTableModel) this.getModel();
        columnCreator.getTableColumns().forEach((field, tableColumn) -> {
            model.addColumn(tableColumn.getHeaderValue());

            if (tableColumn.getWidth() == 0) {
                getColumn(tableColumn.getHeaderValue()).setMinWidth(0);
                getColumn(tableColumn.getHeaderValue()).setMaxWidth(0);
                getColumn(tableColumn.getHeaderValue()).setWidth(0);
            }

        });

        fixHeadersSize();

        tableHeader.addMouseListener(new TableOrderColumnsMouseAdapter(this, instance));
    }

    void fixHeadersSize() {
        columnCreator.getTableColumns().forEach((field, tableColumn) -> {

            if (instance != null && instance.getTable(typeClass.getCanonicalName()) != null) {
                Map<String, Integer> cols = instance.getTable(typeClass.getCanonicalName());

                int width = Optional.ofNullable(cols.get(tableColumn.getHeaderValue())).orElse(MyTableColumn.defaultWidth);

                if (width == 0) {
                    getColumn(tableColumn.getHeaderValue()).setMinWidth(0);
                    getColumn(tableColumn.getHeaderValue()).setMaxWidth(0);
                    getColumn(tableColumn.getHeaderValue()).setWidth(0);
                } else
                    this.getColumnModel().getColumn(this.getColumnModel().getColumnIndex(tableColumn.getHeaderValue())).setPreferredWidth(width);
            } else {
                this.getColumnModel().getColumn(this.getColumnModel().getColumnIndex(tableColumn.getHeaderValue())).setPreferredWidth(tableColumn.getPreferredWidth());
            }

        });
    }

    /**
     * Filing table with data
     */
    protected void addData(int limit, int offset, SortColumn sortOrder) {

        currentData = provider != null ? provider.getData(limit, offset, sortOrder) : dataList;
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

    public void addCustomFormatter(Class<?> classFormat, Format format) {
        formatMap.put(classFormat, format);
    }

    public Map<Class<?>, Format> getFormatMap() {
        return formatMap;
    }

    public SortColumn getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(SortColumn sortColumn) {
        this.sortColumn = sortColumn;
    }

    /**
     * Actions to update page label or another action when change is requested by sort
     *
     * @param actionListener - action listener to change page
     */
    public void addFistPageListener(ActionListener actionListener) {
        changePageListeners.add(actionListener);
    }

    protected List<ActionListener> getChangePageListeners() {
        return changePageListeners;
    }

    public void exportToExcel(Path path) throws IOException {
        ExportUtils.writeToExcell(this, path);
    }

    public void exportToCsv(Path path) throws IOException {
        ExportUtils.writeToCsv(this, path);
    }
}
