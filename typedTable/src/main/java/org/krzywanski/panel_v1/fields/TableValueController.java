package org.krzywanski.panel_v1.fields;

import org.krzywanski.panel_v1.autopanel.TextFieldWithTableSelect;
import org.krzywanski.table.TypedTablePanel;

import javax.swing.border.Border;

public class TableValueController<T> implements FieldValueController<T, TextFieldWithTableSelect<T>> {
    final TextFieldWithTableSelect<T> table;
    private Border originalBorder;

    public TableValueController(TypedTablePanel<T> table, String dialogTitle) {
        this.table = new TextFieldWithTableSelect<>(table, dialogTitle);
        this.originalBorder = this.table.getTextField().getBorder();
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

    @Override
    public void setBorder(Border border) {
        table.getTextField().setBorder(border);
    }

    @Override
    public void resetBorder() {
        table.getTextField().setBorder(originalBorder);
    }
}

