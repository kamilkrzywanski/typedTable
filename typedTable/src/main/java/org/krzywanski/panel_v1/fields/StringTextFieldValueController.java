package org.krzywanski.panel_v1.fields;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;

public class StringTextFieldValueController implements DefaultTextFieldValueController<String>{
    private final JTextField textField;
    private Border originalBorder;
    public StringTextFieldValueController(JTextField textField) {
        this.textField = textField;
        this.originalBorder = textField.getBorder();
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

    @Override
    public void resetBorder() {
        getComponent().setBorder(originalBorder);
    }
}
