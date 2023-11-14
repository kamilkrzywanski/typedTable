package org.krzywanski.table.table;

import java.util.Optional;

public interface SizeSupplier {
    Integer size(Optional<String> searchString);
}
