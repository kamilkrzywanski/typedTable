package org.krzywanski.table.table;

import javax.swing.table.TableColumn;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Optional;

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

        if (instance != null)
            instance.updateColumns(table.typeClass.getName(), columns);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {

            TableColumn currentSortAsc = findCurrentSort(TypedTableDefaults.CARRET_ASC_SYMBOL).orElse(null);
            TableColumn currentSortDesc = findCurrentSort(TypedTableDefaults.CARRET_DESC_SYMBOL).orElse(null);

            int col = table.columnAtPoint(e.getPoint());

            Collections.list(table.getColumnModel().getColumns()).forEach(this::removeSortCharacters);

            if (currentSortAsc == null && currentSortDesc == null){
                updateColumnSymbol(col, TypedTableDefaults.CARRET_ASC_SYMBOL, SortOrder.ASCENDING);
            }
            if (currentSortAsc != null && currentSortAsc == table.getColumnModel().getColumn(col)) {
                updateColumnSymbol(col, TypedTableDefaults.CARRET_DESC_SYMBOL, SortOrder.DESCENDING);
            }
            if (currentSortAsc != null && currentSortAsc != table.getColumnModel().getColumn(col)){
                updateColumnSymbol(col, TypedTableDefaults.CARRET_ASC_SYMBOL, SortOrder.ASCENDING);
            }

            if (currentSortDesc != null){
                table.getColumnModel().getColumn(col).setHeaderValue(table.getColumnModel().getColumn(col).getHeaderValue().toString().replaceAll(TypedTableDefaults.CARRET_DESC_SYMBOL, ""));
                table.setSortColumn(null);
            }
            table.getChangePageListeners().forEach(actionListener -> actionListener.actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,"firstPageAction")));

        }
    }

    /**
     * update value of header of selected column
     * @param column - column index
     * @param newSymbol - new symbol of asceding, desceding
     * @param sortOrder - value from sort order enum
     */
    private void updateColumnSymbol(int column, String newSymbol, SortOrder sortOrder){
        table.getColumnModel().getColumn(column).setHeaderValue(table.getColumnModel().getColumn(column).getHeaderValue() + newSymbol);
        table.setSortColumn(new SortColumn(table.columnCreator.getFieldByName(table.getColumnModel().getColumn(column).getHeaderValue()).getSecond().getName(), sortOrder));
    }

    void removeSortCharacters(TableColumn tableColumn) {
        tableColumn.setHeaderValue(tableColumn.getHeaderValue().toString().replaceAll(TypedTableDefaults.CARRET_ASC_SYMBOL, ""));
        tableColumn.setHeaderValue(tableColumn.getHeaderValue().toString().replaceAll(TypedTableDefaults.CARRET_DESC_SYMBOL, ""));
    }

    synchronized Optional<TableColumn> findCurrentSort(String sort) {
        return Collections.list(table.getColumnModel().getColumns()).stream().filter(tableColumn -> tableColumn.getHeaderValue().toString().endsWith(sort)).findFirst();
    }
}