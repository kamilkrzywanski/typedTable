package org.krzywanski.panel_v1.fields;

import javax.swing.*;
import javax.swing.border.Border;

public class BooleanCheckBoxValueController implements FieldValueController<Boolean, JCheckBox> {

    private final JCheckBox checkBox;

    private final Border originalBorder;

    public BooleanCheckBoxValueController(JCheckBox checkBox) {
        this.checkBox = checkBox;
        this.originalBorder = checkBox.getBorder();
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

    @Override
    public void setBorder(Border border) {
        getComponent().setBorder(border);
    }

    @Override
    public void resetBorder() {
        getComponent().setBorder(originalBorder);
    }
}
