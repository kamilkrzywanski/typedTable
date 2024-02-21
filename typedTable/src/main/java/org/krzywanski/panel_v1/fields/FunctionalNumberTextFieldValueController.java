package org.krzywanski.panel_v1.fields;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Function;

public class FunctionalNumberTextFieldValueController implements DefaultTextFieldValueController<Number> {

    final ResourceBundle rb = ResourceBundle.getBundle("Messages", Locale.getDefault());

    private final JTextField textField;
    private final Function<String, Number> valueTransformer;

    public FunctionalNumberTextFieldValueController(JTextField textField, Function<String, Number> valueTransformer) {
        this.textField = textField;
        this.valueTransformer = valueTransformer;
    }

    @Override
    public Number getValue() {
        return valueTransformer.apply(textField.getText());
    }

    @Override
    public void setValue(FieldValueSupplier<Number> value) {
        textField.setText(value.getValue() != null ? value.getValue().toString() : "");
    }

    @Override
    public JTextComponent getComponent() {
        return textField;
    }

    @Override
    public Number getValueForValidation() throws IllegalArgumentException {
        try {
            return getValue();
        } catch (Exception e) {
            throw new IllegalArgumentException(rb.getString("number.format.error"));
        }
    }
}
