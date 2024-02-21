package org.krzywanski.panel_v1.manualPanel;

import org.krzywanski.panel_v1.fields.FieldValueController;
import org.krzywanski.panel_v1.fields.NumberJFormattedTextFieldValueController;
import org.krzywanski.panel_v1.fields.StringTextFieldValueController;

import javax.swing.*;

public class FieldControllerResolver {

    public FieldControllerResolver() {
    }

    public FieldValueController<?, ?> findFieldValueController(Class<?> dataClass, JComponent component) {

        switch (dataClass.getName()) {
            case "java.lang.String":
                return getStringValueControllerFor(component);
            case "java.lang.Integer":
                return getIntegerValueControllerFor(component);
            case "java.lang.Double":
                return getIntegerValueControllerFor(component);
            case "java.math.BigDecimal":
                return getBigDecimalValueControllerFor(component);
        }


        return null;
    }

    public FieldValueController<?, ?> getStringValueControllerFor(JComponent component) {
        return new StringTextFieldValueController((JTextField) component);
    }

    public FieldValueController<?, ?> getIntegerValueControllerFor(JComponent component) {
        if (component instanceof JFormattedTextField) {
            return new NumberJFormattedTextFieldValueController((JFormattedTextField) component);
        }

        throw new IllegalArgumentException(String.format("Component %s is not supported for Integer type", component.getClass().getName()));
    }

    public FieldValueController<?, ?> getBigDecimalValueControllerFor(JComponent component) {
        if (component instanceof JFormattedTextField) {
            return new NumberJFormattedTextFieldValueController((JFormattedTextField) component);
        }

        throw new IllegalArgumentException(String.format("Component %s is not supported for Integer type", component.getClass().getName()));
    }

}
