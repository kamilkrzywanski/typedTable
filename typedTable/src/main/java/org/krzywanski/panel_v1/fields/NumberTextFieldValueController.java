package org.krzywanski.panel_v1.fields;

import javax.swing.*;
import javax.swing.text.JTextComponent;

public class NumberTextFieldValueController implements DefaultTextFieldValueController<Number> {
    private final JFormattedTextField textField;

    public NumberTextFieldValueController(JFormattedTextField textField) {
        this.textField = textField;
    }

    @Override
    public Number getValue() {
        return (Number) textField.getValue();
    }

    @Override
    public void setValue(FieldValueSupplier<Number> value) {
        textField.setValue(value.getValue());
    }

    @Override
    public JTextComponent getComponent() {
        return textField;
    }
}
