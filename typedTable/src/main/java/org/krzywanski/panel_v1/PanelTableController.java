package org.krzywanski.panel_v1;

import org.krzywanski.panel_v1.autopanel.TypedAutoPanel;
import org.krzywanski.table.TypedTable;

public class PanelTableController<T> {
    final TypedTable<T> table;
    final TypedAutoPanel<T> panel;

    public PanelTableController(TypedTable<T> table, TypedAutoPanel<T> panel) {
        this.table = table;
        this.panel = panel;
        if (table.getRowCount() > 0)
            table.setRowSelectionInterval(0, 0);
        connectPanelWithTable();
    }

    private void connectPanelWithTable() {
        table.addGenericSelectionListener(panel::updateCurrentData);
        panel.addPanelChangeValueListener( (element, action) -> {

            if (action == DataAction.REMOVE || action == DataAction.INSERT) {
                table.refreshData();
                return;
            }
            int selectedRow = table.getSelectedRow();

            selectedRow = Math.max(0, selectedRow);
            table.setDataAt(selectedRow, element);
        });
    }
}
