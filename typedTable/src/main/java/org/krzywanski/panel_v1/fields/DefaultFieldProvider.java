package org.krzywanski.panel_v1.fields;

import javax.swing.*;
import java.awt.*;
import java.util.function.Function;

public class DefaultFieldProvider<T, R extends JComponent> implements FieldProvider<T>{

    final FieldValueController<T, JComponent> fieldValueController;
    final R component;

    public DefaultFieldProvider(R component, Function<R, FieldValueController<T, R>> fieldValueController) {
        this.fieldValueController = (FieldValueController<T, JComponent>) fieldValueController.apply(component);
        this.component = component;
    }

    @Override
    public Component getComponent() {
        return component;
    }

    @Override
    public FieldValueController<T, JComponent> getController() {
        return fieldValueController;
    }
}
