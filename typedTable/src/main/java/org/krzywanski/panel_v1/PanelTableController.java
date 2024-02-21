package org.krzywanski.panel_v1;

import org.krzywanski.table.TypedTable;

public class PanelTableController<T> {
    final TypedTable<T> table;
    final AbstractTypedPanel<T> panel;

    public PanelTableController(TypedTable<T> table, AbstractTypedPanel<T> panel) {
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
            table.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);

        });
    }
}
