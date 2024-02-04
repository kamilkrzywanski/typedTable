package org.krzywanski.panel_v1.fields;

import org.krzywanski.panel_v1.autopanel.TextFieldWithTableSelect;
import org.krzywanski.table.TypedTable;

public class TableValueController<T> implements FieldValueController<T, TextFieldWithTableSelect<T>> {
    final TextFieldWithTableSelect<T> table;

    public TableValueController(TextFieldWithTableSelect<T> table) {
        this.table = table;
    }

    @Override
    public T getValue() {
        return table.getCurrentValue();
    }

    @Override
    public void setValue(FieldValueSupplier<T> value) {
        table.setTextField(value.getValue());
    }

    @Override
    public void setEditable(boolean enabled) {
        table.setEnabled(enabled);
    }

    @Override
    public TextFieldWithTableSelect<T> getComponent() {
        return table;
    }
}
