package org.krzywanski.panel_v1.fields;

import java.util.function.Supplier;

public interface FieldValueController<T> {
    Supplier<T> getValue();
    void setValue(FieldValueSupplier<T> value);
}
