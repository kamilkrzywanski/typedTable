package org.krzywanski.panel_v1.fields;

import javax.swing.text.JTextComponent;

public interface DefaultTextFieldValueController<T> extends FieldValueController<T, JTextComponent> {
    public default void setEditable(boolean enabled) {
        getComponent().setEditable(enabled);
    }

    @Override
    public default void resetBorder() {
        getComponent().putClientProperty("JComponent.outline", null);
    }

    @Override
    public default void errorBorder() {
        getComponent().putClientProperty("JComponent.outline", "error");
    }
}
