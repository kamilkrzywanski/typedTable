package org.krzywanski.table.providers;

import java.util.Map;

public interface SizeProviderInterface {
    int getSize(String searchString, Map<String , String> extraParams);
}
