package org.krzywanski.panel_v1.fields;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.function.Supplier;

public class BigDecimalTextFieldValueController implements FieldValueController<BigDecimal>{
    private final JFormattedTextField textField;

    public BigDecimalTextFieldValueController(JFormattedTextField textField) {
        this.textField = textField;
    }

    @Override
    public Supplier<BigDecimal> getValue() {
        return () -> (BigDecimal) textField.getValue();
    }

    @Override
    public void setValue(FieldValueSupplier<BigDecimal> value) {
        textField.setValue(value.getValue());
    }

}
