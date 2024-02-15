package org.krzywanski.panel_v1.fields;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.math.BigDecimal;

public class BigDecimalTextFieldValueController implements DefaultTextFieldValueController<BigDecimal>{
    private final JFormattedTextField textField;
    public BigDecimalTextFieldValueController(JFormattedTextField textField) {
        this.textField = textField;
    }

    @Override
    public BigDecimal getValue() throws NumberFormatException {
        try {
            return (BigDecimal) textField.getFormatter().stringToValue(textField.getText());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void setValue(FieldValueSupplier<BigDecimal> value) {
        textField.setValue(value.getValue());
    }
    @Override
    public JTextComponent getComponent() {
        return textField;
    }

    @Override
    public void resetBorder() {
        getComponent().putClientProperty("JComponent.outline", null);
    }

    @Override
    public void errorBorder() {
        getComponent().putClientProperty("JComponent.outline", "error");
    }


}
