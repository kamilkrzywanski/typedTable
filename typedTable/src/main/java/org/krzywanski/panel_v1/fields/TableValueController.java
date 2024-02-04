package org.krzywanski.panel_v1.fields;

import org.krzywanski.table.TypedTable;

public class TableValueController<T> implements FieldValueController<T, TypedTable<T>> {
    final TypedTable<T> table;

    public TableValueController(TypedTable<T> table) {
        this.table = table;
    }

    @Override
    public T getValue() {
        return table.getSelectedItem();
    }

    @Override
    public void setValue(FieldValueSupplier<T> value) {

    }

    @Override
    public void setEditable(boolean enabled) {

    }

    @Override
    public TypedTable<T> getComponent() {
        return table;
    }
}
