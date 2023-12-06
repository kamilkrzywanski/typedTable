package org.krzywanski.table.annot;

import javax.swing.*;

public enum ControlType {
    COMBOBOX(JComboBox.class),
    TEXTFIELD(JTextField.class),
    CHECKBOX(JCheckBox.class);

    final Class<?> clazz;
    ControlType(Class<?> clazz) {
        this.clazz = clazz;
    }
}
