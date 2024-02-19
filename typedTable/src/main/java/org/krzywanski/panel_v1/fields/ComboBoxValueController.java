package org.krzywanski.panel_v1.fields;

import javax.swing.*;

public class ComboBoxValueController<T> implements FieldValueController<T, JComboBox<T>> {

    final JComboBox<T> comboBox;


    public ComboBoxValueController(JComboBox<T> comboBox) {
        this.comboBox = comboBox;
    }
    @Override
    public T getValue() {
        return comboBox.getItemAt(comboBox.getSelectedIndex());
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
    public void errorBorder() {
        getComponent().putClientProperty("JComponent.outline", "error");
    }

    @Override
    public void resetBorder() {
        getComponent().putClientProperty("JComponent.outline", null);
    }


}
