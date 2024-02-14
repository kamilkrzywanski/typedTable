package org.krzywanski.panel_v1.fields;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;
import java.math.BigDecimal;

public class BigDecimalTextFieldValueController implements DefaultTextFieldValueController<BigDecimal>{
    private final JFormattedTextField textField;
    private Border originalBorder;

    public BigDecimalTextFieldValueController(JFormattedTextField textField) {
        this.textField = textField;
        this.originalBorder = textField.getBorder();
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

    @Override
    public void resetBorder() {
        getComponent().setBorder(originalBorder);
    }

    @Override
    public void setBorder(Border border) {
        getComponent().setBorder(BorderFactory.createCompoundBorder(border, originalBorder));
    }


}
