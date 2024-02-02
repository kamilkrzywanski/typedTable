package org.krzywanski.panel_v1.fields;

import javax.swing.*;
import java.util.function.Supplier;

public class ComboBoxValueController<T> implements FieldValueController<T> {

    JComboBox<T> comboBox;

    public ComboBoxValueController(JComboBox<T> comboBox) {
        this.comboBox = comboBox;
    }
    @Override
    public Supplier<T> getValue() {
        return () -> (T) comboBox.getSelectedItem();
    }

    @Override
    public void setValue(FieldValueSupplier<T> value) {
        comboBox.setSelectedItem(value.getValue());
    }
}
