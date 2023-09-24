package org.krzywanski.table.table;

import javax.swing.table.TableColumn;

public class TypedTableColumn extends TableColumn {

    /**
     * Name of field which column is associated because label of column might be changed
     */
    final Object tableColumnDbName;


    public TypedTableColumn(int modelIndex, int width, Object tableColumnDbName) {
        super(modelIndex, width);
        this.tableColumnDbName = tableColumnDbName;
    }

    public Object getTableColumnDbName() {
        return tableColumnDbName;
    }
}
