package org.krzywanski.panel_v1;

import org.krzywanski.panel_v1.annot.PanelField;
import org.krzywanski.panel_v1.fields.*;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.event.FocusAdapter;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
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

        if(TypedPanelFields.getField(field.getType()) != null) {
            FieldProvider<?> fieldProvider = TypedPanelFields.getField(field.getType());

            field.setFirstComponent(new JLabel(findLabel(field)));
            field.setSecondComponent(fieldProvider.getComponent());
            field.setFieldValueController(fieldProvider.getController());
            return field;
        }

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
        if(field.getType().equals(Short.class)) {
            return createShortTextField(field);
        }
        if(field.getType().equals(BigDecimal.class)) {
            return createBigDecimalTextField(field);
        }
        if(field.getType().isEnum()) {
            return createEnumComboBox(field);
        }

        field.setFirstComponent(new JLabel("Not supported type + " + field.getType()));
        field.setSecondComponent(new JLabel("Not supported type"));
        return field;
    }

    private FieldControllerElement createEnumComboBox(FieldControllerElement field) {
        return createLabelAndInstallControllerForComboBox(field, new JComboBox<>(field.getType().getEnumConstants()));
    }

    private FieldControllerElement createBigDecimalTextField(FieldControllerElement field) {
        return createLabelAndInstallControllerForTextField(field, createFieldWithFormatter(NumberFormat.getNumberInstance(), field));
    }

    private FieldControllerElement createShortTextField(FieldControllerElement field) {
        return createLabelAndInstallControllerForTextField(field, createFieldWithFormatter(NumberFormat.getIntegerInstance(), field));
    }

    private FieldControllerElement createLongTextField(FieldControllerElement field) {
        return createLabelAndInstallControllerForTextField(field, createFieldWithFormatter(NumberFormat.getNumberInstance(), field));
    }

    private FieldControllerElement createFloatTextField(FieldControllerElement field) {
        return createLabelAndInstallControllerForTextField(field, createFieldWithFormatter(NumberFormat.getNumberInstance(), field));
    }

    private FieldControllerElement createDoubleTextField(FieldControllerElement field) {
        return createLabelAndInstallControllerForTextField(field, createFieldWithFormatter(NumberFormat.getNumberInstance(), field));
    }

    private FieldControllerElement createIntegerTextField(FieldControllerElement field) {
        return createLabelAndInstallControllerForTextField(field, createFieldWithFormatter(NumberFormat.getIntegerInstance(), field));
    }

    private FieldControllerElement createTextField(FieldControllerElement field) {
       return createLabelAndInstallControllerForTextField(field, new JFormattedTextField());
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


    FieldControllerElement createLabelAndInstallControllerForTextField(FieldControllerElement field, JFormattedTextField component) {
        field.setFirstComponent(new JLabel(findLabel(field)));
        field.setSecondComponent(component);

        switch (field.getType().getSimpleName()) {
            case "Integer":
                field.setFieldValueController(new IntegerTextFieldValueController(component));
                break;
            case "String":
                field.setFieldValueController(new StringTextFieldValueController(component));
                break;
            case "BigDecimal":
                field.setFieldValueController(new BigDecimalTextFieldValueController(component));
                break;
        }

        return field;
    }

    private FieldControllerElement createLabelAndInstallControllerForComboBox(FieldControllerElement field, JComboBox<?> jComboBox) {
        field.setFirstComponent(new JLabel(findLabel(field)));
        field.setSecondComponent(jComboBox);
        field.setFieldValueController(new ComboBoxValueController<>(jComboBox));
        return field;
    }

    private static JFormattedTextField createFieldWithFormatter(NumberFormat format, FieldControllerElement field) {
        NumberFormatter formatter = new NumberFormatter(format);
        //TODO create a factory for this
        formatter.setValueClass(field.getType());
        JFormattedTextField formattedTextField = new JFormattedTextField(formatter);

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
