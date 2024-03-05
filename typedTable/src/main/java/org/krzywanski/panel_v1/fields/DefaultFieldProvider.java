package org.krzywanski.panel_v1.fields;

import javax.swing.*;
import java.util.function.Function;

public class DefaultFieldProvider<T, R extends JComponent> implements FieldProvider<T, R> {

    final Function<R, FieldValueController<T, R>> fieldValueController;
    final R component;

    @SuppressWarnings("unchecked")
    public DefaultFieldProvider(R component, Function<R, FieldValueController<T, R>> fieldValueController) {
        this.fieldValueController = fieldValueController;
        this.component = component;
    }

    @Override
    public R getComponent() {
        return component;
    }

    public FieldValueController<T, R> getController(JComponent component) {
        return fieldValueController.apply((R) component);
    }
}
