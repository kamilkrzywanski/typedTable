package org.krzywanski.panel_v1.fields;

import javax.swing.*;

public interface FieldProvider<T> {
    JComponent getComponent();

    FieldValueController<T, JComponent> getController();
}
