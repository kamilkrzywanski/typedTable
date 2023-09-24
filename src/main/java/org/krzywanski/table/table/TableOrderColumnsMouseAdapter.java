package org.krzywanski.table.table;

import net.miginfocom.swing.MigLayout;
import org.krzywanski.table.annot.MyTableColumn;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

/**
 * This adapter is listening for changes on table header
 * and once mouse is released new defintions of columns are saved
 */
class TableOrderColumnsMouseAdapter extends MouseAdapter {

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
            columns.put((String) column.getHeaderValue(), column.getWidth());
        }

        if (instance != null) instance.updateColumns(table.typeClass.getName(), columns);
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
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Bundle", Locale.getDefault());
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

    private void showColumnOptionsDialog() {
        JDialog dialog = new JDialog();
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
        dialog.setAlwaysOnTop(true);
        dialog.setLocationRelativeTo(table);
        dialog.pack();
        dialog.setVisible(true);

    }

    private void setColumnSize(int id, int width) {
        TableColumn tableColumn = table.getColumnModel().getColumn(id);
        tableColumn.setMinWidth(width);
        tableColumn.setMaxWidth(Integer.MAX_VALUE);
        tableColumn.setWidth(width);
        tableColumn.setPreferredWidth(width);
    }

    private void hideColumn(MouseEvent e) {
        //Protection to don't remove last column
        if (visibleColumnCount() > 1) {
            int tableColumnId = table.getTableHeader().columnAtPoint(e.getPoint());
            hideColumnById(tableColumnId);
        }
    }

    private long visibleColumnCount() {
        return Collections.list(table.getColumnModel().getColumns()).stream().filter(tableColumn -> tableColumn.getWidth() > 0).count();
    }

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

        TableColumn currentSortAsc = findCurrentSort(TypedTableDefaults.CARRET_ASC_SYMBOL).orElse(null);
        TableColumn currentSortDesc = findCurrentSort(TypedTableDefaults.CARRET_DESC_SYMBOL).orElse(null);

        int col = table.columnAtPoint(e.getPoint());

        Collections.list(table.getColumnModel().getColumns()).forEach(this::removeSortCharacters);

        if (currentSortAsc == null && currentSortDesc == null) {
            updateColumnSymbol(col, TypedTableDefaults.CARRET_ASC_SYMBOL, SortOrder.ASCENDING);
        }
        if (currentSortAsc != null && currentSortAsc == table.getColumnModel().getColumn(col)) {
            updateColumnSymbol(col, TypedTableDefaults.CARRET_DESC_SYMBOL, SortOrder.DESCENDING);
        }
        if (currentSortAsc != null && currentSortAsc != table.getColumnModel().getColumn(col)) {
            updateColumnSymbol(col, TypedTableDefaults.CARRET_ASC_SYMBOL, SortOrder.ASCENDING);
        }

        if (currentSortDesc != null) {
            table.getColumnModel().getColumn(col).setHeaderValue(table.getColumnModel().getColumn(col).getHeaderValue().toString().replaceAll(TypedTableDefaults.CARRET_DESC_SYMBOL, ""));
            table.setSortColumn(null);
        }
        table.getChangePageListeners().forEach(actionListener -> actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "firstPageAction")));


    }

    /**
     * update value of header of selected column
     *
     * @param column    - column index
     * @param newSymbol - new symbol of asceding, desceding
     * @param sortOrder - value from sort order enum
     */
    private void updateColumnSymbol(int column, String newSymbol, SortOrder sortOrder) {
        table.getColumnModel().getColumn(column).setHeaderValue(table.getColumnModel().getColumn(column).getHeaderValue() + newSymbol);
        table.setSortColumn(new SortColumn(table.columnCreator.getFieldByName(table.getColumnModel().getColumn(column).getHeaderValue()).getSecond().getName(), sortOrder));
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