package org.krzywanski.panel_v1.fields;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.math.BigDecimal;
import java.util.function.Supplier;

public class BigDecimalTextFieldValueController implements DefaultTextFieldValueController<BigDecimal>{
    private final JFormattedTextField textField;

    public BigDecimalTextFieldValueController(JFormattedTextField textField) {
        this.textField = textField;
    }

    @Override
    public BigDecimal getValue() {
        return (BigDecimal) textField.getValue();
    }

    @Override
    public void setValue(FieldValueSupplier<BigDecimal> value) {
        textField.setValue(value.getValue());
    }
    @Override
    public JTextComponent getComponent() {
        return textField;
    }


}
