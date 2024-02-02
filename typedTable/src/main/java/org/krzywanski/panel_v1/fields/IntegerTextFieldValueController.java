package org.krzywanski.panel_v1.fields;

import javax.swing.*;
import java.util.function.Supplier;

public class IntegerTextFieldValueController implements FieldValueController<Integer>{
    private final JFormattedTextField textField;

    public IntegerTextFieldValueController(JFormattedTextField textField) {
        this.textField = textField;
    }

    @Override
    public Supplier<Integer> getValue() {
        return () -> (Integer) textField.getValue();
    }

    @Override
    public void setValue(FieldValueSupplier<Integer> value) {
        textField.setValue(value.getValue());
    }

}
