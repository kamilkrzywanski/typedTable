package org.krzywanski.panel;

import org.krzywanski.panel.annot.PanelField;
import org.krzywanski.panel.fields.BooleanCheckBoxValueController;
import org.krzywanski.panel.fields.StringTextFieldValueController;
import org.krzywanski.table.utils.Pair;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.event.FocusAdapter;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PanelFieldCreator {
    final Class<?> dataClass;
    PanelFieldCreator(Class<?> dataClass) {
        this.dataClass = dataClass;
    }


    List<FieldControllerElement> getComponents() {
      return Arrays.stream(dataClass.getDeclaredFields())
               .map(this::createFieldControllerElement).collect(Collectors.toList());

    }

    private FieldControllerElement createFieldControllerElement(Field field) {
        try {
            return createComponent(new FieldControllerElement(field, new PropertyDescriptor(field.getName(), dataClass)));
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }

    }

    private FieldControllerElement createComponent(FieldControllerElement field) {
        if(field.getType().equals(Boolean.class)) {
            return createCheckBox(field);
        }
        if(field.getType().equals(String.class)) {
            return createTextField(field);
        }
        if(field.getType().equals(Integer.class)) {
            return createIntegerTextField(field);
        }
        if(field.getType().equals(Double.class)) {
            return createDoubleTextField(field);
        }
        if(field.getType().equals(Float.class)) {
            return createFloatTextField(field);
        }
        if(field.getType().equals(Long.class)) {
            return createLongTextField(field);
        }

        field.setFirstComponent(new JLabel("Not supported type + " + field.getType()));
        field.setSecondComponent(new JLabel("Not supported type"));
        return field;
    }


    private FieldControllerElement createLongTextField(FieldControllerElement field) {
        return createLabelAndComponent(field, createFieldWithFormatter(NumberFormat.getNumberInstance()));
    }

    private FieldControllerElement createFloatTextField(FieldControllerElement field) {
        return createLabelAndComponent(field, createFieldWithFormatter(NumberFormat.getNumberInstance()));
    }

    private FieldControllerElement createDoubleTextField(FieldControllerElement field) {
        return createLabelAndComponent(field, createFieldWithFormatter(NumberFormat.getNumberInstance()));
    }

    private FieldControllerElement createIntegerTextField(FieldControllerElement field) {
        return createLabelAndComponent(field, createFieldWithFormatter(NumberFormat.getIntegerInstance()));
    }

    private FieldControllerElement createTextField(FieldControllerElement field) {
       return createLabelAndComponent(field, new JFormattedTextField());
    }

    private FieldControllerElement createCheckBox(FieldControllerElement field) {
        JCheckBox checkBox = new JCheckBox(findLabel(field));
        field.setFirstComponent(checkBox);
        field.setFieldValueController(new BooleanCheckBoxValueController(checkBox));

        return field;
    }

    private String findLabel(FieldControllerElement field) {
        if(field.getField().isAnnotationPresent(PanelField.class)) {
            PanelField panelField = field.getField().getAnnotation(PanelField.class);
            return panelField.label();
        }
        return field.getField().getName();
    }


    FieldControllerElement createLabelAndComponent(FieldControllerElement field, JFormattedTextField component) {
        field.setFirstComponent(new JLabel(findLabel(field)));
        field.setSecondComponent(component);
        field.setFieldValueController(new StringTextFieldValueController(component));

        return field;
    }

    private static JFormattedTextField createFieldWithFormatter(NumberFormat format) {
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0); // Set minimum value as needed

        JFormattedTextField formattedTextField = new JFormattedTextField(formatter);
        formattedTextField.setColumns(10);

        // Add a tooltip for the formattedTextField
        formattedTextField.setToolTipText("Enter a valid integer.");
        formattedTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                try
                {
                    KeyEvent ke = new KeyEvent(evt.getComponent(), KeyEvent.KEY_PRESSED,
                            System.currentTimeMillis(), InputEvent.CTRL_DOWN_MASK,
                            KeyEvent.VK_F1, KeyEvent.CHAR_UNDEFINED);
                    evt.getComponent().dispatchEvent(ke);
                }
                catch (Throwable e1)
                {e1.printStackTrace();}
            }
        });

        return formattedTextField;
    }
}
