package org.krzywanski.panel_v1.fields;

import javax.swing.text.JTextComponent;

public interface DefaultTextFieldValueController<T> extends FieldValueController<T, JTextComponent> {
    default void setEditable(boolean enabled) {
        getComponent().setEditable(enabled);
    }

    @Override
    default void resetBorder() {
        getComponent().putClientProperty("JComponent.outline", null);
    }

    @Override
    default void errorBorder() {
        getComponent().putClientProperty("JComponent.outline", "error");
    }
}
