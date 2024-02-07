package org.krzywanski.panel_v1;

import org.krzywanski.panel_v1.autopanel.TypedAutoPanel;
import org.krzywanski.table.TypedTable;

public class PanelTableController<T> {
    final TypedTable<T> table;
    final TypedAutoPanel<T> panel;

    public PanelTableController(TypedTable<T> table, TypedAutoPanel<T> panel) {
        this.table = table;
        this.panel = panel;
        table.setRowSelectionInterval(0, 0);
        connectPanelWithTable();
    }

    private void connectPanelWithTable() {
        table.addGenericSelectionListener(panel::updateCurrentData);
        panel.addPanelChangeValueListener( (element, action) -> {

            if (action == DataAction.REMOVE) {
                table.refreshData();
                return;
            }
            int selectedRow = table.getSelectedRow();
            table.setDataAt(selectedRow, element);
            table.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
        });
    }
}
