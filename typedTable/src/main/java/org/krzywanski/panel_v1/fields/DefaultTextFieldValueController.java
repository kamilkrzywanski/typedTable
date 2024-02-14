package org.krzywanski.panel_v1.fields;

import javax.swing.border.Border;
import javax.swing.text.JTextComponent;

public interface DefaultTextFieldValueController<T> extends FieldValueController<T, JTextComponent> {
    public default void setEditable(boolean enabled) {
        getComponent().setEditable(enabled);
    }

    public default void setBorder(Border border) {
        getComponent().setBorder(border);
    }
}
