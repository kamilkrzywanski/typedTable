package org.krzywanski.table;

import javax.swing.*;

public class SortColumn {

    public SortColumn(String columnName, SortOrder sortOrder) {
        this.columnName = columnName;
        this.sortOrder = sortOrder;
    }

    String columnName;
    SortOrder sortOrder;

    public String getColumnName() {
        return columnName;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }
}
