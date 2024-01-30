package org.krzywanski.table.providers;

import java.util.Map;
import java.util.Optional;

public interface SizeProviderInterface {
    int getSize(String searchString, Map<String , String> extraParams);
}
