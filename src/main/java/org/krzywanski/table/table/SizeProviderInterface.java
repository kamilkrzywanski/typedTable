package org.krzywanski.table.table;

import java.util.Optional;

public interface SizeProviderInterface {
    int getSize(Optional<String> searchString);
}
