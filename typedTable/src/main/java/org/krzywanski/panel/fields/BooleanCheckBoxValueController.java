package org.krzywanski.panel.fields;

import javax.swing.*;
import java.util.function.Supplier;

public class BooleanCheckBoxValueController implements FieldValueController<Boolean> {

    private final JCheckBox checkBox;

    public BooleanCheckBoxValueController(JCheckBox checkBox) {
        this.checkBox = checkBox;
    }

    @Override
    public Supplier<Boolean> getValue() {
        return () -> checkBox.isSelected();
    }

    @Override
    public void setValue(FieldValueSupplier<Boolean> value) {
        checkBox.setSelected(value.getValue());
    }

}
