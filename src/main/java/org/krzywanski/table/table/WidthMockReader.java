package org.krzywanski.table.table;

import java.util.LinkedHashMap;
import java.util.Map;

public interface WidthMockReader {

    /**
     * probably faster way is get all columns at once
     * if required becouse of frequent update columns by multiple users (when {@link WidthMockReader}
     * will be refferenced to shared data inform me and will add another interface per Table
     * @return
     */
    LinkedHashMap<String,Map<String,Integer>> getTableList();
}
