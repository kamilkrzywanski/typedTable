package org.krzywanski.panel.fields;

import java.util.function.Supplier;

public interface FieldValueController<T> {
    Supplier<T> getValue();
    void setValue(FieldValueSupplier<T> value);
}
