package org.krzywanski.table.table;

import net.miginfocom.swing.MigLayout;
import org.krzywanski.table.annot.MyTableColumn;
import org.krzywanski.table.annot.ReflectionSort;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * This adapter is listening for changes on table header
 * and once mouse is released new defintions of columns are saved
 */
class TableOrderColumnsMouseAdapter extends MouseAdapter {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("TableBundle", Locale.getDefault());
    final TypedTable<?> table;

    final TableWidthTool instance;

    public <T> TableOrderColumnsMouseAdapter(TypedTable<T> tTypedTable, TableWidthTool instance) {
        this.table = tTypedTable;
        this.instance = instance;
    }

    public void mouseReleased(MouseEvent arg0) {
        LinkedHashMap<String, Integer> columns = new LinkedHashMap<>();
        for (int i = 0; i < table.getTableHeader().getColumnModel().getColumnCount(); i++) {
            TableColumn column = table.getTableHeader().getColumnModel().getColumn(i);
            Pair<PropertyDescriptor, Field> columnPd =  table.columnCreator.getFieldByName(column.getHeaderValue());
            columns.put(columnPd.getSecond().getName(), column.getWidth());
        }

        if (instance != null) instance.updateColumns(table.typeClass.getName(), columns, table.id);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) updateSort(e);
        if (e.getButton() == MouseEvent.BUTTON3) showColumnsOptions(e);
    }

    /**
     * Show 2 columns options - hide current and options for all colums
     *
     * @param e - current mouse event
     */
    void showColumnsOptions(MouseEvent e) {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(new AbstractAction(resourceBundle.getString("hide.current.colum")) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                hideColumn(e);
            }
        });
        popupMenu.add(new AbstractAction(resourceBundle.getString("column.settings")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                showColumnOptionsDialog();
            }
        });


        popupMenu.show(table, e.getX(), e.getY());
    }

    /**
     * Creates a simple dialog with list of checkboxes with columns names
     */
    private void showColumnOptionsDialog() {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(table));
        dialog.setTitle(resourceBundle.getString("column.settings"));
        dialog.setLayout(new MigLayout());
        for (int i = 1; i < table.getColumnCount() + 1; i++) {
            JCheckBox checkBox = new JCheckBox(table.getColumnName(i - 1));
            if (table.getColumnModel().getColumn(i - 1).getWidth() > 0) checkBox.setSelected(true);
            int finalI = i - 1;
            checkBox.addItemListener(e -> {
                if (!checkBox.isSelected()) {
                    if (visibleColumnCount() > 1)
                        hideColumnById(finalI);
                    else
                        checkBox.setSelected(true);

                }
                if (checkBox.isSelected()){
                    setColumnSize(finalI, MyTableColumn.defaultWidth);
                }
                //Force update columns
                mouseReleased(null);
            });
            dialog.add(checkBox);
        }

        dialog.setModal(true);
        dialog.setLocationRelativeTo(table);
        dialog.pack();
        dialog.setVisible(true);

    }

    /**
     * Update columns with passed size
     * @param id - index of column
     * @param width - selected width
     */
    private void setColumnSize(int id, int width) {
        TableColumn tableColumn = table.getColumnModel().getColumn(id);
        tableColumn.setMinWidth(width);
        tableColumn.setMaxWidth(Integer.MAX_VALUE);
        tableColumn.setWidth(width);
        tableColumn.setPreferredWidth(width);
    }

    /**
     * hide column by mouse event
     * @param e - mouse event to locate column
     */
    private void hideColumn(MouseEvent e) {
        //Protection to don't remove last column
        if (visibleColumnCount() > 1) {
            int tableColumnId = table.getTableHeader().columnAtPoint(e.getPoint());
            hideColumnById(tableColumnId);
            mouseReleased(null);
        }
    }

    /**
     * Check number of columns with size != 0
     * @return - column colunt
     */
    private long visibleColumnCount() {
        return Collections.list(table.getColumnModel().getColumns()).stream().filter(tableColumn -> tableColumn.getWidth() > 0).count();
    }

    /**
     * set 0 width to column by passed id
     * @param id - index of column
     */
    private void hideColumnById(int id) {
        TableColumn tableColumn = table.getColumnModel().getColumn(id);
        tableColumn.setMinWidth(0);
        tableColumn.setMaxWidth(0);
        tableColumn.setWidth(0);
    }

    /**
     * Update surrent sort - change header character and send information to prowider about change
     *
     * @param e - current mouse event
     */
    private void updateSort(MouseEvent e) {

        int column = table.columnAtPoint(e.getPoint());

        String sortString = "";
        Field field = table.columnCreator.getFieldByName(table.getColumnModel().getColumn(column).getHeaderValue()).getSecond();
        if (field == null) return;
        MyTableColumn annotation = field.getAnnotation(MyTableColumn.class);
        if (annotation == null) return;
        if (!annotation.sortable()) return;

        if (annotation.sortString() != null && !annotation.sortString().isEmpty()) {
            sortString = annotation.sortString();
        } else {
            sortString = field.getName();
        }

        if (table.isMultiSortEnable()) multiColumnSortUpdate(sortString, column);
        else singeColumnSortUpdate(sortString, column);

        table.getChangePageListeners().forEach(actionListener -> actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "firstPageAction")));
    }

    private void multiColumnSortUpdate(String sortString, int column) {
        table.getSortColumns().stream().filter(sortColumn -> sortColumn.getColumnName().equals(sortString)).findAny().ifPresentOrElse(sortColumn -> {
            if (sortColumn.getSortOrder().equals(SortOrder.ASCENDING)) {
                sortColumn.sortOrder = SortOrder.DESCENDING;
                updateColumnSymbol(column, TypedTableDefaults.CARRET_DESC_SYMBOL, SortOrder.DESCENDING, sortString);
            } else {
                table.getSortColumns().remove(sortColumn);
                updateColumnSymbol(column, "", SortOrder.ASCENDING, sortString);
            }
        }, () -> {
            table.getSortColumns().add(new SortColumn(sortString, SortOrder.ASCENDING));
            updateColumnSymbol(column, TypedTableDefaults.CARRET_ASC_SYMBOL, SortOrder.ASCENDING, sortString);
        });
    }

    private void singeColumnSortUpdate(String sortString, int column){
        TableColumn currentSortAsc = findCurrentSort(TypedTableDefaults.CARRET_ASC_SYMBOL).orElse(null);
        TableColumn currentSortDesc = findCurrentSort(TypedTableDefaults.CARRET_DESC_SYMBOL).orElse(null);

        Collections.list(table.getColumnModel().getColumns()).forEach(this::removeSortCharacters);

        if (currentSortAsc == null && currentSortDesc == null) {
            updateColumnSymbol(column, TypedTableDefaults.CARRET_ASC_SYMBOL, SortOrder.ASCENDING, sortString);
        }
        if (currentSortAsc != null && currentSortAsc == table.getColumnModel().getColumn(column)) {
            updateColumnSymbol(column, TypedTableDefaults.CARRET_DESC_SYMBOL, SortOrder.DESCENDING, sortString);
        }
        if (currentSortAsc != null && currentSortAsc != table.getColumnModel().getColumn(column)) {
            updateColumnSymbol(column, TypedTableDefaults.CARRET_ASC_SYMBOL, SortOrder.ASCENDING, sortString);
        }

        if (currentSortDesc != null) {
            table.getColumnModel().getColumn(column).setHeaderValue(table.getColumnModel().getColumn(column).getHeaderValue().toString().replaceAll(TypedTableDefaults.CARRET_DESC_SYMBOL, ""));
            table.setSortColumn(Collections.emptyList());
        }
    }

    /**
     * update value of header of selected column
     *
     * @param column    - column index
     * @param newSymbol - new symbol of asceding, desceding
     * @param sortOrder - value from sort order enum
     */
    private void updateColumnSymbol(int column, String newSymbol, SortOrder sortOrder, String columnName) {
        table.getColumnModel().getColumn(column).setHeaderValue(findAndReplaceSymbols(table.getColumnModel().getColumn(column).getHeaderValue()) + newSymbol);

        if(!table.isMultiSortEnable())
            table.setSortColumn(Collections.singletonList(new SortColumn(columnName, sortOrder)));

        if(table.typeClass.isAnnotationPresent(ReflectionSort.class) && table.dataList != null)
            sortData(columnName, sortOrder);
    }

    private void sortData(String columnName, SortOrder sortOrder){
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(table.typeClass);
            final PropertyDescriptor sortByField = Arrays.stream(beanInfo.getPropertyDescriptors()).filter(propertyDescriptor -> propertyDescriptor.getName().equals(columnName)).findFirst().orElseThrow(() -> new RuntimeException("No such field"));
            table.dataList.sort(Comparator.comparing(entity -> {
                try {
                    Object fieldValue = sortByField.getReadMethod().invoke(entity);

                    // This check still passes if the type of fieldValue implements Comparable<U>,
                    // where U is an unrelated type from the type of fieldValue, but this is the
                    // best we can do here, since we don't know the type of field at compile time
                    if (!(fieldValue instanceof Comparable<?>) && fieldValue != null) {
                        throw new IllegalArgumentException("Field is not comparable!");
                    }
                    return (Comparable)fieldValue;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }));
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }

        if(sortOrder.equals(SortOrder.DESCENDING))
            Collections.reverse(table.dataList);

    }

    private String findAndReplaceSymbols(Object columnName){
        return columnName.toString().replaceAll(TypedTableDefaults.CARRET_ASC_SYMBOL, "").replaceAll(TypedTableDefaults.CARRET_DESC_SYMBOL, "");
    }

    /**
     * Remove sort symbols to get real name of column
     *
     * @param tableColumn - table column with sort characters
     */
    void removeSortCharacters(TableColumn tableColumn) {
        tableColumn.setHeaderValue(tableColumn.getHeaderValue().toString().replaceAll(TypedTableDefaults.CARRET_ASC_SYMBOL, ""));
        tableColumn.setHeaderValue(tableColumn.getHeaderValue().toString().replaceAll(TypedTableDefaults.CARRET_DESC_SYMBOL, ""));
    }

    /**
     * Find a current column with special sort character
     *
     * @param sort - sort character
     * @return - column which is currently used to sort
     */
    Optional<TableColumn> findCurrentSort(String sort) {
        return Collections.list(table.getColumnModel().getColumns()).stream().filter(tableColumn -> tableColumn.getHeaderValue().toString().endsWith(sort)).findFirst();
    }
}