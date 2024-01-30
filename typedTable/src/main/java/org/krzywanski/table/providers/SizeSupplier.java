package org.krzywanski.table.providers;

import java.util.Map;
import java.util.Optional;

public interface SizeSupplier {
    Integer size(String searchString, Map<String, String> extraParams);
}
