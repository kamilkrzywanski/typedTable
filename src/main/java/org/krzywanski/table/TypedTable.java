package org.krzywanski.table;

import org.krzywanski.table.annot.EnableMultiSort;
import org.krzywanski.table.annot.MyTableColumn;
import org.krzywanski.table.annot.ReflectionSort;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.event.ActionListener;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.text.Format;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Table which is created from Entity List
 *
 * @param <T> - type of table data
 */
public class TypedTable<T> extends JTable {

    static {
        TableWidthProvider.setProvider(new DefaultTableWidthProvider());
    }

    List<GenericSelectionListener<T>> listeners = new ArrayList<>();

    /**
     * Map of extra params to send with request
     */
    final Map<String, String> extraParams = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    /**
     * List of ilsteners to execute when requested is change page by table
     */
    List<ActionListener> changePageListeners = new ArrayList<>();
    /**
     * List of columns to sort data
     */
    List<SortColumn> sortColumn = new ArrayList<>();
    /**
     * Map of custom formatters for columns
     */
    private final Map<Class<?>, Format> formatMap = new HashMap<>();
    /**
     * List of data to show on table if you don't want to use provider
     */
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
    TableDataProvider<T> provider;
    /**
     * Handle for current data on list
     */
    List<T> currentData;

    /**
     * tools for pagination
     */
    final PaginationUtils paginationUtils;
    /**
     * Supplier for search phase
     */
    Supplier<String> searchPhaseSupplier;

    protected final long id;
    /**
     * true if multi sort is enable
     */
    private final boolean multiSortEnable;

    /**
     * Default constructor if you want to keep the same sizes for multiple tables
     *
     * @param dataList  - data list - list od data class
     * @param typeClass - data class
     * @param provider  - provider of data with pagination requests
     */
    public TypedTable(List<T> dataList, Class<? extends T> typeClass, TableDataProvider<T> provider) {
        this(dataList, typeClass, provider, 0);
    }

    /**
     * Default constructor of table
     *
     * @param dataList  - data list - list od data class
     * @param typeClass - data class
     * @param provider  - provider of data with pagination requests
     * @param id        - identifier of instance of table to save widths of table if we use one entity in few places and want to each one have individual widths and columns
     */
    public TypedTable(List<T> dataList, Class<? extends T> typeClass, TableDataProvider<T> provider, long id) {
        super(new TypedTableModel(new ColumnCreator(typeClass, id)));
        this.id = id;
        this.multiSortEnable = typeClass.isAnnotationPresent(EnableMultiSort.class);
        columnCreator = new ColumnCreator(typeClass, id);
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
        getColumnModel().
                getColumns().
                asIterator().
                forEachRemaining(tableColumn -> tableColumn.addPropertyChangeListener(new ChangeHeaderNamePropertyChangeListener(columnCreator)));
        fixHeadersSize();
        tableHeader.addMouseListener(new TableOrderColumnsMouseAdapter(this, instance));
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return super.getColumnClass(column);
    }

    /**
     * Fixing size of columns on table create
     */
    void fixHeadersSize() {

        Map<String, Integer> columns = instance.getTable(typeClass.getCanonicalName(), id);
        columnCreator.getTableColumns().forEach((field, tableColumn) -> {
            if (instance != null && columns != null) {

                int width = Optional.ofNullable(columns.get(columnCreator.getFieldByName(tableColumn.getHeaderValue()).getSecond().getName())).orElse(MyTableColumn.defaultWidth);

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
    protected void addData(int limit, int offset, ActionType actionType) {

        currentData = provider != null ? provider.getData(limit, offset, getSortColumns(), getSearchPhase(), actionType, new HashMap<>(extraParams)) : dataList;
        model.getDataVector().clear();

        if(!getSortColumns().isEmpty() && getSortColumns().get(0) != null && typeClass.isAnnotationPresent(ReflectionSort.class))
            sortData(currentData, getSortColumns().get(0).getColumnName(), getSortColumns().get(0).getSortOrder());

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
        revalidate();
        if (!currentData.isEmpty())
            listeners.forEach(genericSelectionListener -> genericSelectionListener.getSelectedItem(getItemAt(0)));
    }

    @SuppressWarnings({"rawtypes"})
    private void sortData(List<T> data, String columnName, SortOrder sortOrder){
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(typeClass);
            final PropertyDescriptor sortByField = Arrays.stream(beanInfo.getPropertyDescriptors()).filter(propertyDescriptor -> propertyDescriptor.getName().equals(columnName)).findFirst().orElseThrow(() -> new RuntimeException("No such field"));
            Comparator<? super Comparable> oreder = Comparator.nullsLast(Comparator.naturalOrder());
            data.sort(Comparator.comparing(entity -> {
                try {
                    Object fieldValue = sortByField.getReadMethod().invoke(entity);
                    if (!(fieldValue instanceof Comparable<?>) && fieldValue != null) {
                        throw new IllegalArgumentException("Field is not comparable!");
                    }
                    return (Comparable)fieldValue;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }, oreder));
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }

        if(sortOrder.equals(SortOrder.DESCENDING))
            Collections.reverse(data);

    }
    /**
     * Actions for pagination
     */
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
        if (getSelectedRow() != -1) return getItemAt(getSelectedRow());
        return null;
    }

    public T getItemAt(int index) {
        return currentData.get(index);
    }

    @Override
    public TableCellEditor getDefaultEditor(Class<?> columnClass) {
        return super.getDefaultEditor(columnClass);
    }

    @Override
    public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
        return new TypedTableRenderer(columnCreator);
    }

    /**
     * Adds custom formatter for selected class
     *
     * @param classFormat - class to format
     * @param format      - format
     */
    public void addCustomFormatter(Class<?> classFormat, Format format) {
        formatMap.put(classFormat, format);
    }

    public Map<Class<?>, Format> getFormatMap() {
        return formatMap;
    }

    /**
     * @return - list of columns to sort data
     */
    public List<SortColumn> getSortColumns() {
        return sortColumn;
    }

    /**
     * Set current colum to sort data
     *
     * @param sortColumn - model of sorted column
     */
    public void setSortColumn(List<SortColumn> sortColumn) {
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

    /**
     * @return - list of listeners on change page
     */
    protected List<ActionListener> getChangePageListeners() {
        return changePageListeners;
    }

    /**
     * method to export table to Excel
     *
     * @param path - path to save Excel file
     * @throws IOException - in case of problem with save
     */
    public void exportToExcel(Path path) throws IOException {
        ExportUtils.writeToExcell(this, path);
    }

    /**
     * method to export table to CSV
     *
     * @param path - path to save Excel file
     * @throws IOException - in case of problem with save
     */
    public void exportToCsv(Path path) throws IOException {
        ExportUtils.writeToCsv(this, path);
    }

    public void addSearchPhaseSupplier(Supplier<String> searchPhaseSupplier) {
        this.searchPhaseSupplier = searchPhaseSupplier;
    }

    public String getSearchPhase() {
        return searchPhaseSupplier != null ? stringToNullable(searchPhaseSupplier.get()) : null;
    }

    private String stringToNullable(String string) {
        return string != null && string.isEmpty() ? null : string;
    }

    /**
     * @return - true if multi sort is enable
     */
    public boolean isMultiSortEnable() {
        return multiSortEnable;
    }

    public void addExtraParam(String key, String value) {
        extraParams.put(key, value);
    }

    public void removeExtraParam(String key) {
        extraParams.remove(key);
    }

    public void clearExtraParams() {
        extraParams.clear();
    }

    /**
     * @return - list of selected items from table
     */
    public List<T> getSelectedItems() {
        List<T> selectedItems = new ArrayList<>();
        for (int index : getSelectionModel().getSelectedIndices())
            selectedItems.add(currentData.get(index));
        return selectedItems;
    }

    public void addGenericSelectionListener(GenericSelectionListener<T> listener) {
        listeners.add(listener);
        getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                listener.getSelectedItem(getSelectedItem());
            }
        });
    }

    public <E> E getSelectedValueOrDefault(Function<T, E> mapper, E defaultValue) {
        T selectedItem = getSelectedItem();
        if (selectedItem == null || mapper == null) {
            return defaultValue;
        }
        return mapper.apply(selectedItem);
    }
}
