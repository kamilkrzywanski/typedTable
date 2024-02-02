package org.krzywanski.panel_v1.fields;

import javax.swing.*;
import java.util.function.Supplier;

public class StringTextFieldValueController implements FieldValueController<String>{
    private final JTextField textField;

    public StringTextFieldValueController(JTextField textField) {
        this.textField = textField;
    }

    @Override
    public Supplier<String> getValue() {
        return textField::getText;
    }

    @Override
    public void setValue(FieldValueSupplier<String> value) {
        textField.setText(value.getValue());
    }

}
