package org.krzywanski.panel_v1.fields;

import javax.swing.*;
import javax.swing.border.Border;

public class ComboBoxValueController<T> implements FieldValueController<T, JComboBox<T>> {

    JComboBox<T> comboBox;


    public ComboBoxValueController(JComboBox<T> comboBox) {
        this.comboBox = comboBox;
    }
    @Override
    public T getValue() {
        return (T) comboBox.getSelectedItem();
    }

    @Override
    public void setValue(FieldValueSupplier<T> value) {
        comboBox.setSelectedItem(value.getValue());
    }

    @Override
    public void setEditable(boolean enabled) {
        comboBox.setEnabled(enabled);
    }

    @Override
    public JComboBox<T> getComponent() {
        return comboBox;
    }

    @Override
    public void setBorder(Border border) {
        getComponent().putClientProperty("JComponent.outline", "error");
    }

    @Override
    public void resetBorder() {
        getComponent().putClientProperty("JComponent.outline", null);
    }


}
