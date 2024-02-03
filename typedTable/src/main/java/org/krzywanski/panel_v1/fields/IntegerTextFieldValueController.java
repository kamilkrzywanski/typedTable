package org.krzywanski.panel_v1.fields;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.function.Supplier;

public class IntegerTextFieldValueController implements DefaultTextFieldValueController<Integer>{
    private final JFormattedTextField textField;

    public IntegerTextFieldValueController(JFormattedTextField textField) {
        this.textField = textField;
    }

    @Override
    public Integer getValue() {
        return (Integer) textField.getValue();
    }

    @Override
    public void setValue(FieldValueSupplier<Integer> value) {
        textField.setValue(value.getValue());
    }

    @Override
    public JTextComponent getComponent() {
        return textField;
    }
}
