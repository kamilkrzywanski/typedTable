package org.krzywanski.table.providers;

import java.util.Map;

public interface SizeSupplier {
    Integer size(String searchString, Map<String, String> extraParams);
}
