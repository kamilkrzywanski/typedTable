package org.krzywanski.table.table;

import java.util.LinkedHashMap;

public interface TableWidthTool {
    void updateColumns(String className, LinkedHashMap<String,Integer> columnns);

    /**
     * probably faster way is get all columns at once
     * will be refferenced to shared data inform me and will add another interface per Table
     * @return
     */
    LinkedHashMap<String,Integer> getTable(String className);
}
