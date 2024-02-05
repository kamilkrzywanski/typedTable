package org.krzywanski.panel_v1.fields;

import javax.swing.*;
import java.util.function.Supplier;

public class BooleanCheckBoxValueController implements FieldValueController<Boolean, JCheckBox> {

    private final JCheckBox checkBox;

    public BooleanCheckBoxValueController(JCheckBox checkBox) {
        this.checkBox = checkBox;
    }

    @Override
    public Boolean getValue() {
        return checkBox.isSelected();
    }

    @Override
    public void setValue(FieldValueSupplier<Boolean> value) {
        if(value.getValue() == null) {
            checkBox.setSelected(false);
        } else {
            checkBox.setSelected(value.getValue());
        }
    }

    @Override
    public void setEditable(boolean enabled) {
        checkBox.setEnabled(enabled);
    }

    @Override
    public JCheckBox getComponent() {
        return checkBox;
    }

}
