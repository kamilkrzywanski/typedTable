package org.krzywanski.table;

import org.krzywanski.table.annot.EnableMultiSort;
import org.krzywanski.table.annot.MyTableColumn;
import org.krzywanski.table.annot.ReflectionSort;
import org.krzywanski.table.constraints.ActionType;
import org.krzywanski.table.providers.*;
import org.krzywanski.table.renderer.TypedTableRenderer;
import org.krzywanski.table.utils.FieldMock;
import org.krzywanski.table.utils.Page;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
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
    final ColumnCreator columnCreator;
    /**
     * Handle for model
     */
    TypedTableModel model;

    /**
     * Entity for create table
     */
    final Class<? extends T> typeClass;
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
     * Property change listener for headers of table
     */
    final ChangeHeaderNamePropertyChangeListener listener;
    /**
     * If true first row will be selected after data is loaded
     */
    private boolean selectFirstRow = true;
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
        columnCreator = ((TypedTableModel) getModel()).getColumnCreator();
        this.listener = new ChangeHeaderNamePropertyChangeListener(columnCreator);
        this.typeClass = typeClass;
        this.dataList = dataList;
        this.provider = provider;
        this.currentData = dataList;
        this.paginationUtils = new PaginationUtils(provider, this);
        model = (TypedTableModel) this.getModel();
        installHeaderPropertyChangeListener();
        fixHeadersSize();
        tableHeader.addMouseListener(new TableOrderColumnsMouseAdapter(this, instance));
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
        columnCreator.getTableColumns().forEach(field -> {
            if (instance != null && columns != null) {

                int width = Optional.ofNullable(columns.get(columnCreator.getFieldByName(field.getTableColumn().getHeaderValue()).getName())).orElse(MyTableColumn.defaultWidth);

                if (width == 0) {
                    getColumn(field.getTableColumn().getHeaderValue()).setMinWidth(0);
                    getColumn(field.getTableColumn().getHeaderValue()).setMaxWidth(0);
                    getColumn(field.getTableColumn().getHeaderValue()).setWidth(0);
                } else
                    this.getColumnModel().getColumn(this.getColumnModel().getColumnIndex(field.getTableColumn().getHeaderValue())).setPreferredWidth(width);
            } else {
                this.getColumnModel().getColumn(this.getColumnModel().getColumnIndex(field.getTableColumn().getHeaderValue())).setPreferredWidth(field.getTableColumn().getPreferredWidth());
            }

        });
    }

    /**
     * Filing table with data
     */
    protected void addData(int limit, int offset, ActionType actionType) {

        currentData = provider != null ? provider.getData(limit, offset, getSortColumns(), getSearchPhase(), actionType, new HashMap<>(extraParams)) : dataList;
        model.getDataVector().clear();
        model.fireTableDataChanged();

        if (!getSortColumns().isEmpty() && getSortColumns().get(0) != null && typeClass.isAnnotationPresent(ReflectionSort.class))
            sortData(currentData, getSortColumns().get(0).getColumnName(), getSortColumns().get(0).getSortOrder());

        currentData.forEach(t -> {
            model.addRow(createDataRow(t));
        });
        revalidate();

        if (selectFirstRow && !currentData.isEmpty())
            setRowSelectionInterval(0, 0);

        if (!currentData.isEmpty())
            listeners.forEach(genericSelectionListener -> genericSelectionListener.getSelectedItem(getItemAt(0)));
    }

    private Vector<Object> createDataRow(T data){
        Vector<Object> element = new Vector<>();
        columnCreator.getTableColumns().forEach(fieldMock -> {
            try {
                element.add(fieldMock.invoke(data));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
        return element;
    }


    @SuppressWarnings({"rawtypes"})
    private void sortData(List<T> data, String columnName, SortOrder sortOrder) {
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
                    return (Comparable) fieldValue;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }, oreder));
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }

        if (sortOrder.equals(SortOrder.DESCENDING))
            Collections.reverse(data);

    }

    /**
     * Actions for pagination
     */
    public Page nextPageAction() {
        return paginationUtils.nextPageAction();
    }

    public Page lastPageAction() {
        return paginationUtils.lastPageAction();
    }

    public Page prevPageAction() {
        return paginationUtils.prevPageAction();
    }

    public Page firstPageAction() {
        return paginationUtils.firstPageAction();
    }

    /**
     * @return - returns item from selected row
     */
    public T getSelectedItem() {
        if (getSelectedRow() != -1) return getItemAt(getSelectedRow());
        return null;
    }

    /**
     * @param index - index of item
     * @return - return element at passed index if available
     */
    public T getItemAt(int index) {
        return currentData.get(index);
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

    /**
     * Adds GenericSelectionListener - listener which as parameter have selected ithem from row.
     *
     * @param listener - listener to invoke
     */
    public void addGenericSelectionListener(GenericSelectionListener<T> listener) {
        listeners.add(listener);
        getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                listener.getSelectedItem(getSelectedItem());
            }
        });
    }

    /**
     * Return value from selected row or default when row not found
     *
     * @param mapper       - mapper to map row to result
     * @param defaultValue - default value when row not found
     * @param <E>          - return type of result
     * @return - returns result of function
     */
    public <E> E getSelectedValueOrDefault(Function<T, E> mapper, E defaultValue) {
        T selectedItem = getSelectedItem();
        if (selectedItem == null || mapper == null) {
            return defaultValue;
        }
        return mapper.apply(selectedItem);
    }

    /**
     * @return - returns type class of current table
     */
    public Class<? extends T> getTypeClass() {
        return typeClass;
    }

    /**
     * Checks do pagination is available for current table
     *
     * @return - true if pagination is enabled
     */
    public boolean isPaginationEnabled() {
        return dataList == null && provider != null && provider.isPaginable();
    }

    /**
     * Add function to table at runtime
     *
     * @param columnName        - name of column to add
     * @param columnClass       - class of column to add
     * @param computingFunction - function passed to result cell
     * @param <C>               - class of result column
     */
    public <C> void addComputedColumn(String columnName, Class<C> columnClass, Function<T, C> computingFunction) {
        addColumn(columnName, columnClass, computingFunction);
    }

    /**
     * Add column to table at runtime
     *
     * @param columnName  - name of column to add
     * @param columnClass - class of column to add
     * @param function    - function passed to result cell
     * @param <C>         - class of result column
     */

    public <C> void addColumn(String columnName, Class<C> columnClass, Function<T, C> function) {
        TableColumn tableColumn = new TableColumn(columnCreator.getTableColumns().size());
        columnCreator.getTableColumns().add(new FieldMock(columnName, columnClass, function, tableColumn, true));
        tableColumn.setHeaderValue(columnName);
        try {
            SwingUtilities.invokeAndWait(() -> {
                model.addColumn(columnName);
                removeHeaderPropertyChangeListeners();
                installHeaderPropertyChangeListener();
                fixHeadersSize();
            });
        } catch (InterruptedException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param columnName - name of column to add
     * @param resultList - Tree set to in case of custom method to compare objects
     *                   TypeClass needs to implement Comparable interface if you use TreeSet without comparator
     */
    public void addMultiSelectColumn(String columnName, TreeSet<T> resultList) {

        if (resultList.comparator() == null && !Comparable.class.isAssignableFrom(typeClass))
            throw new RuntimeException("TypeClass needs to implement Comparable interface if you use TreeSet without comparator");

        addColumn(columnName, Boolean.class, t -> resultList.contains(t));
        getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && getColumnModel().getColumnIndex(columnName) == e.getColumn()) {
                if ((Boolean) getValueAt(e.getFirstRow(), e.getColumn()))
                    resultList.add(getItemAt(e.getFirstRow()));
                else
                    resultList.remove(getItemAt(e.getFirstRow()));
            }
        });
    }

    /**
     * Install default ChangeHeaderNamePropertyChangeListener for all columns
     */
    private void installHeaderPropertyChangeListener() {
        getColumnModel().
                getColumns().
                asIterator().
                forEachRemaining(tableColumn -> tableColumn.addPropertyChangeListener(listener));
    }

    /**
     * Remove default ChangeHeaderNamePropertyChangeListener for all columns
     */
    private void removeHeaderPropertyChangeListeners() {
        getColumnModel().
                getColumns().
                asIterator().
                forEachRemaining(tableColumn -> tableColumn.removePropertyChangeListener(listener));
    }

    public boolean isSelectFirstRow() {
        return selectFirstRow;
    }

    public void setSelectFirstRow(boolean selectFirstRow) {
        if(selectFirstRow)
            setRowSelectionInterval(0, 0);
        else
            clearSelection();
        this.selectFirstRow = selectFirstRow;
    }

    public void setDataAt(int row, T data){
        currentData.add(row, data);

        if (model.getDataVector().isEmpty())
            model.getDataVector().add(createDataRow(data));
        else
            model.getDataVector().set(row, createDataRow(data));
        model.fireTableDataChanged();
    }

    /**
     * Refresh data in current page
     */
    public void refreshData() {
        paginationUtils.refreshData();
    }
}
