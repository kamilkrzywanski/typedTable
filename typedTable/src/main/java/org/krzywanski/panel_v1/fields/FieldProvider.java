package org.krzywanski.panel_v1.fields;

import javax.swing.*;

public interface FieldProvider<T, R extends JComponent> {

    default FieldValueController<T, R> getController() {
        return getController(getComponent());
    }

    R getComponent();

    FieldValueController<T, R> getController(JComponent component);
}
