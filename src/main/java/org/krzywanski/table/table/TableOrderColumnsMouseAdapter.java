package org.krzywanski.table.table;

import javax.swing.table.TableColumn;
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

            if (currentSortAsc == null && currentSortDesc == null)
                table.getColumnModel().getColumn(col).setHeaderValue(table.getColumnModel().getColumn(col).getHeaderValue() + TypedTableDefaults.CARRET_ASC_SYMBOL);

            if (currentSortAsc != null && currentSortAsc == table.getColumnModel().getColumn(col)) {
                table.getColumnModel().getColumn(col).setHeaderValue(table.getColumnModel().getColumn(col).getHeaderValue() + TypedTableDefaults.CARRET_DESC_SYMBOL);
            }
            if (currentSortAsc != null && currentSortAsc != table.getColumnModel().getColumn(col))
                table.getColumnModel().getColumn(col).setHeaderValue(table.getColumnModel().getColumn(col).getHeaderValue() + TypedTableDefaults.CARRET_ASC_SYMBOL);

            if (currentSortDesc != null)
                table.getColumnModel().getColumn(col).setHeaderValue(table.getColumnModel().getColumn(col).getHeaderValue().toString().replaceAll(TypedTableDefaults.CARRET_DESC_SYMBOL, ""));

        }
    }

    void removeSortCharacters(TableColumn tableColumn) {
        tableColumn.setHeaderValue(tableColumn.getHeaderValue().toString().replaceAll(TypedTableDefaults.CARRET_ASC_SYMBOL, ""));
        tableColumn.setHeaderValue(tableColumn.getHeaderValue().toString().replaceAll(TypedTableDefaults.CARRET_DESC_SYMBOL, ""));
    }

    synchronized Optional<TableColumn> findCurrentSort(String sort) {
        return Collections.list(table.getColumnModel().getColumns()).stream().filter(tableColumn -> tableColumn.getHeaderValue().toString().endsWith(sort)).findFirst();
    }
}