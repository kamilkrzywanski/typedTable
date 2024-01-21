package org.krzywanski.table.panel;

import net.miginfocom.swing.MigLayout;
import org.krzywanski.table.panel.annot.PanelField;

import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PanelFieldCreator {
    final Class<?> dataClass;
    PanelFieldCreator(Class<?> dataClass) {
        this.dataClass = dataClass;
    }


    List<Component> getComponents() {
       return Arrays.stream(dataClass.getDeclaredFields())
               .map(this::createPanelField)
               .collect(Collectors.toList());

    }

    private Component createPanelField(Field field) {
        Component component = createComponent(field);
        return component;
    }

    private Component createComponent(Field field) {
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

        return new JLabel();
    }


    private Component createLongTextField(Field field) {
        return createLabelAndComponent(field, createFieldWithFormatter(NumberFormat.getNumberInstance()));
    }

    private Component createFloatTextField(Field field) {
        return createLabelAndComponent(field, createFieldWithFormatter(NumberFormat.getNumberInstance()));
    }

    private Component createDoubleTextField(Field field) {
        return createLabelAndComponent(field, createFieldWithFormatter(NumberFormat.getNumberInstance()));
    }

    private Component createIntegerTextField(Field field) {
        return createLabelAndComponent(field, createFieldWithFormatter(NumberFormat.getIntegerInstance()));
    }

    private Component createTextField(Field field) {
       return createLabelAndComponent(field, new JTextField());
    }

    private Component createCheckBox(Field field) {
        return new JCheckBox(findLabel(field));
    }

    private String findLabel(Field field) {
        if(field.isAnnotationPresent(PanelField.class)) {
            PanelField panelField = field.getAnnotation(PanelField.class);
            return panelField.label();
        }
        return field.getName();
    }


    Component createLabelAndComponent(Field field, Component component) {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout());
        panel.add(new JLabel(findLabel(field)));
        panel.add(component, "grow, push");
        return panel;
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
                            System.currentTimeMillis(), InputEvent.CTRL_MASK,
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
