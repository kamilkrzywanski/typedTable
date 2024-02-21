package org.krzywanski.panel_v1.fields;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Function;

public class NumberSpinnerValueController implements FieldValueController<Number, JSpinner> {

    final ResourceBundle rb = ResourceBundle.getBundle("Messages", Locale.getDefault());

    private final JSpinner spinner;
    private final Function<String, Number> valueTransformer;
    private final JTextField textField;

    public NumberSpinnerValueController(JSpinner spinner, Function<String, Number> valueTransformer) {
        this.spinner = spinner;
        this.valueTransformer = valueTransformer;
        this.textField = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();

    }

    @Override
    public Number getValue() {
        return valueTransformer.apply(textField.getText());
    }

    @Override
    public void setValue(FieldValueSupplier<Number> value) {
        spinner.setValue(value.getValue());
    }

    @Override
    public void setEditable(boolean enabled) {
        getComponent().setEnabled(enabled);
    }

    @Override
    public JSpinner getComponent() {
        return spinner;
    }

    @Override
    public void resetBorder() {
        getComponent().putClientProperty("JComponent.outline", null);
    }

    @Override
    public void errorBorder() {
        getComponent().putClientProperty("JComponent.outline", "error");
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
