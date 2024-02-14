package org.krzywanski.panel_v1.fields;

import javax.swing.*;
import javax.swing.border.Border;

public interface FieldValueController<T, R extends JComponent> {
    T getValue();
    void setValue(FieldValueSupplier<T> value);

    void setEditable(boolean enabled);
    R getComponent();

    void setBorder(Border border);

    void resetBorder();
}
