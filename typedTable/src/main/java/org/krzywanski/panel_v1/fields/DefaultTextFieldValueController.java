package org.krzywanski.panel_v1.fields;

import javax.swing.text.JTextComponent;

public interface DefaultTextFieldValueController<T> extends FieldValueController<T, JTextComponent> {
    public default void setEditable(boolean enabled) {
        getComponent().setEditable(enabled);
    }
}
