package org.krzywanski.panel_v1.fields;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.Locale;
import java.util.ResourceBundle;

public class NumberTextFieldValueController implements DefaultTextFieldValueController<Number> {

    final ResourceBundle rb = ResourceBundle.getBundle("Messages", Locale.getDefault());

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

    @Override
    public Number getValueForValidation() throws IllegalArgumentException {
        try {
            return (Number) textField.getFormatter().stringToValue(textField.getText());
        } catch (Exception e) {
            throw new IllegalArgumentException(rb.getString("number.format.error"));
        }
    }
}
