package org.krzywanski.panel.fields;

public class StringTextFieldValueController implements FieldValueController<String>{
    private final javax.swing.JTextField textField;

    public StringTextFieldValueController(javax.swing.JTextField textField) {
        this.textField = textField;
    }

    @Override
    public java.util.function.Supplier<String> getValue() {
        return () -> textField.getText();
    }

    @Override
    public void setValue(FieldValueSupplier<String> value) {
        textField.setText(value.getValue());
    }

}
