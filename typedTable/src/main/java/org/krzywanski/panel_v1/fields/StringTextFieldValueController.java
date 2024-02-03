package org.krzywanski.panel_v1.fields;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.function.Supplier;

public class StringTextFieldValueController implements DefaultTextFieldValueController<String>{
    private final JTextField textField;

    public StringTextFieldValueController(JTextField textField) {
        this.textField = textField;
    }

    @Override
    public String getValue() {
        return textField.getText();
    }

    @Override
    public void setValue(FieldValueSupplier<String> value) {
        textField.setText(value.getValue());
    }
    @Override
    public JTextComponent getComponent() {
        return textField;
    }
}
