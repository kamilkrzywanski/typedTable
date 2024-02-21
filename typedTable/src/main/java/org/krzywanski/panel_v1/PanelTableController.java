package org.krzywanski.panel_v1;

import org.krzywanski.table.TypedTablePanel;

public class PanelTableController<T> {
    final TypedTablePanel<T> table;
    final AbstractTypedPanel<T> panel;

    public PanelTableController(TypedTablePanel<T> table, AbstractTypedPanel<T> panel) {
        this.table = table;
        this.panel = panel;
        if (table.table.getRowCount() > 0)
            table.table.setRowSelectionInterval(0, 0);
        connectPanelWithTable();
    }

    private void connectPanelWithTable() {
        table.addGenericSelectionListener(panel::updateCurrentData);
        panel.addPanelChangeValueListener( (element, action) -> {

            if (action == DataAction.REMOVE || action == DataAction.INSERT) {
                if (action == DataAction.REMOVE && table.table.getDataList() != null)
                    table.table.getDataList().remove(element);

                table.refreshData();
                return;
            }
            int selectedRow = table.table.getSelectedRow();

            selectedRow = Math.max(0, selectedRow);
            table.setDataAt(selectedRow, element);
            table.table.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);

        });
    }
}
