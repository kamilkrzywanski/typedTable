package org.krzywanski.panel_v1.fields;

import javax.swing.*;

public interface FieldValueController<T, R extends JComponent> {
    T getValue();
    void setValue(FieldValueSupplier<T> value);

    void setEditable(boolean enabled);
    R getComponent();

    void errorBorder();

    void resetBorder();

    /**
     * This method is used to get value for validation
     *
     * @return value for validation
     */
    default T getValueForValidation() throws IllegalArgumentException {
        return getValue();
    }
}
