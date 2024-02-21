package org.krzywanski.panel_v1.manualPanel;

import org.krzywanski.panel_v1.fields.*;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.function.Function;

public class FieldControllerResolver {

    public FieldControllerResolver() {
    }

    public FieldValueController<?, ?> findFieldValueController(Class<?> dataClass, JComponent component) {

        switch (dataClass.getName()) {
            case "java.lang.String":
                return getStringValueControllerFor(component);
            case "java.lang.Integer":
                return getIntegerValueControllerFor(component, Integer::parseInt);
            case "java.lang.Short":
                return getIntegerValueControllerFor(component, Short::parseShort);
            case "java.lang.Long":
                return getIntegerValueControllerFor(component, Long::parseLong);
            case "java.lang.Double":
                return getDoubleValueControllerFor(component, Double::parseDouble);
            case "java.lang.Float":
                return getDoubleValueControllerFor(component, Float::parseFloat);
            case "java.math.BigDecimal":
                return getBigDecimalValueControllerFor(component);
        }

        return null;
    }

    private FieldValueController<?, ?> getDoubleValueControllerFor(JComponent component, Function<String, Number> valueTransformer) {
        if (component instanceof JFormattedTextField) {
            return new NumberJFormattedTextFieldValueController((JFormattedTextField) component);
        }
        if (component instanceof JTextField) {
            return new FunctionalNumberTextFieldValueController((JTextField) component, valueTransformer);
        }
        if (component instanceof JSpinner) {
            return new NumberSpinnerValueController((JSpinner) component, valueTransformer);
        }

        throw new IllegalArgumentException(String.format("Component %s is not supported for Double type", component.getClass().getName()));
    }

    public FieldValueController<?, ?> getStringValueControllerFor(JComponent component) {
        return new StringTextFieldValueController((JTextField) component);
    }

    public FieldValueController<?, ?> getIntegerValueControllerFor(JComponent component, Function<String, Number> valueTransformer) {
        if (component instanceof JFormattedTextField) {
            return new NumberJFormattedTextFieldValueController((JFormattedTextField) component);
        }
        if (component instanceof JTextField) {
            return new FunctionalNumberTextFieldValueController((JTextField) component, valueTransformer);
        }
        if (component instanceof JSpinner) {
            return new NumberSpinnerValueController((JSpinner) component, valueTransformer);
        }

        throw new IllegalArgumentException(String.format("Component %s is not supported for Integer type", component.getClass().getName()));
    }

    public FieldValueController<?, ?> getBigDecimalValueControllerFor(JComponent component) {
        if (component instanceof JFormattedTextField) {
            return new NumberJFormattedTextFieldValueController((JFormattedTextField) component);
        }
        if (component instanceof JTextField) {
            return new FunctionalNumberTextFieldValueController((JTextField) component, BigDecimal::new);
        }
        if (component instanceof JSpinner) {
            return new NumberSpinnerValueController((JSpinner) component, BigDecimal::new);
        }

        throw new IllegalArgumentException(String.format("Component %s is not supported for BigDecimal type", component.getClass().getName()));
    }

}
