package org.krzywanski.table.providers;

import java.util.LinkedHashMap;

public interface TableWidthTool {

    /**
     * method to update columns of table
     * @param className - name of class
     * @param columnns - lost of data in format List<String - column name, Integer - width>
     * @param id - id of table instance to have multiple configs to the same class
     */
    void updateColumns(String className, LinkedHashMap<String,Integer> columnns, long id);

    /**
     * @param className - name of data class
     * @param  id - id of table instance to have multiple configs to the same class
     * @return - returns data for table with List<String - column name, Integer - width>
     */
    LinkedHashMap<String,Integer> getTable(String className, long id);
}
