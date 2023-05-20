package org.krzywanski.table.table;

import java.util.LinkedHashMap;
import java.util.Map;

public interface WidthMockWriter {
    public void updateColumns(String className, LinkedHashMap<String,Integer> columnns);
}
