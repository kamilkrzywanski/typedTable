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
        panel.updateCurrentData(table.getSelectedItem());
    table.addGenericSelectionListener(panel::updateCurrentData);
    }
}