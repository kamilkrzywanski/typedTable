package org.krzywanski.table.panel;

import org.krzywanski.table.panel.annot.PanelField;
import org.krzywanski.table.utils.Pair;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PanelFieldCreator {
    final Class<?> dataClass;
    PanelFieldCreator(Class<?> dataClass) {
        this.dataClass = dataClass;
    }


    Map<Component, Component> getComponents() {
       final Map<Component, Component> componentMap = new HashMap<>();
       Arrays.stream(dataClass.getDeclaredFields())
               .map(this::createPanelField)
               .forEach(componentComponentPair ->
                       componentMap.put(componentComponentPair.getFirst(),
                               componentComponentPair.getSecond()));

               return componentMap;
    }

    private Pair<Component, Component> createPanelField(Field field) {
        Pair<Component, Component> componentPair = createComponent(field);

        if(componentPair.getFirst() == null || componentPair.getSecond() == null) {
            System.out.println("Null label for field " + field.getName());
        }
        return createComponent(field);
    }

    private Pair<Component, Component> createComponent(Field field) {
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

        return new Pair<>(new JLabel(), new JLabel());
    }


    private Pair<Component, Component> createLongTextField(Field field) {
        return createLabelAndComponent(field, createFieldWithFormatter(NumberFormat.getNumberInstance()));
    }

    private Pair<Component, Component> createFloatTextField(Field field) {
        return createLabelAndComponent(field, createFieldWithFormatter(NumberFormat.getNumberInstance()));
    }

    private Pair<Component, Component> createDoubleTextField(Field field) {
        return createLabelAndComponent(field, createFieldWithFormatter(NumberFormat.getNumberInstance()));
    }

    private Pair<Component, Component> createIntegerTextField(Field field) {
        return createLabelAndComponent(field, createFieldWithFormatter(NumberFormat.getIntegerInstance()));
    }

    private Pair<Component, Component> createTextField(Field field) {
       return createLabelAndComponent(field, new JTextField());
    }

    private Pair<Component, Component> createCheckBox(Field field) {
        return new Pair<>(new JCheckBox(findLabel(field)), null);
    }

    private String findLabel(Field field) {
        if(field.isAnnotationPresent(PanelField.class)) {
            PanelField panelField = field.getAnnotation(PanelField.class);
            return panelField.label();
        }
        return field.getName();
    }


    Pair<Component, Component> createLabelAndComponent(Field field, Component component) {
        return new Pair<>(new JLabel(findLabel(field)), component);
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
