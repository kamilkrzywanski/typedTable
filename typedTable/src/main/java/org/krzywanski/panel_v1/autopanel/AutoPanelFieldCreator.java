package org.krzywanski.panel_v1.autopanel;

import org.krzywanski.BundleTranslator;
import org.krzywanski.TypedFrameworkConfiguration;
import org.krzywanski.panel_v1.AbstractTypedPanel;
import org.krzywanski.panel_v1.FieldToolKit;
import org.krzywanski.panel_v1.TypedPanelFields;
import org.krzywanski.panel_v1.fields.*;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class AutoPanelFieldCreator<T> implements FieldBuilder<T> {
    /**
     * Translator for resource bundles if some has been defined
     */
    final BundleTranslator bundleTranslator = new BundleTranslator(Locale.getDefault(), TypedFrameworkConfiguration.resourceBundles);

    final Class<T> dataClass;
    /**
     * Map of controllers for custom field
     * Key - field name
     * Value - field controller
     */
    final Map<String, FieldValueController<?, ?>> fieldControllers = new HashMap<>();

    final AbstractTypedPanel<T> parentPanel;
    List<FieldControllerElement> components;

    public AutoPanelFieldCreator(Class<T> dataClass, TypedAutoPanel<T> parentPanel) {
        this.dataClass = dataClass;
        this.parentPanel = parentPanel;
    }


    @Override
    public List<FieldControllerElement> getComponents() {
        if(components == null) {
             components = Arrays.stream(dataClass.getDeclaredFields())
                     .filter(field -> findLabel(field) != null)
                     .map(this::createFieldControllerElement)
                     .map(FieldToolKit::installDocumentListener)
                     .collect(Collectors.toList());
        }

      return components;

    }

    private FieldControllerElement createFieldControllerElement(Field field) {
        try {
            return createComponent(new FieldControllerElement(parentPanel, field, new PropertyDescriptor(field.getName(), dataClass)));
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }

    }

    private FieldControllerElement createComponent(FieldControllerElement field) {

        //IN CASE OF USE addDataEditor() moethod
        if(fieldControllers.containsKey(field.getField().getName())){
            field.setFirstComponent(new JLabel(findLabel(field)));
            field.setSecondComponent(fieldControllers.get(field.getField().getName()).getComponent());
            field.setFieldValueController(fieldControllers.get(field.getField().getName()));
            return field;
        }

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
        return createLabelAndInstallControllerForTextField(field, createFieldWithFormatter(new DecimalFormat(), field));
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
        return findLabel(field.getField());

    }

    private String findLabel(Field field) {
        return TypedFrameworkConfiguration.fieldResolvers.stream()
                .filter(Objects::nonNull)
                .map(resolver -> resolver.apply(field))
                .filter(Objects::nonNull).findFirst().map(bundleTranslator::getTranslation).orElse(null);

    }


    FieldControllerElement createLabelAndInstallControllerForTextField(FieldControllerElement field, JFormattedTextField component) {
        field.setFirstComponent(new JLabel(findLabel(field)));
        field.setSecondComponent(component);

        switch (field.getType().getSimpleName()) {
            case "Integer":
            case "BigDecimal":
            case "Double":
            case "Float":
            case "Long":
            case "Short":
                field.setFieldValueController(new NumberTextFieldValueController(component));
                break;
            case "String":
                field.setFieldValueController(new StringTextFieldValueController(component));
                break;
        }
        FieldToolKit.installDefaultEditorKit(component);
        return field;
    }

    private FieldControllerElement createLabelAndInstallControllerForComboBox(FieldControllerElement field, JComboBox<?> jComboBox) {
        field.setFirstComponent(new JLabel(findLabel(field)));
        field.setSecondComponent(jComboBox);
        field.setFieldValueController(new ComboBoxValueController<>(jComboBox));
        return field;
    }

    private static JFormattedTextField createFieldWithFormatter(NumberFormat format, FieldControllerElement field) {
        NumberFormatter formatter = new NumberFormatter(format) {
            @Override
            public Object stringToValue(String text) throws ParseException {
                if (text.isEmpty())
                    return null;
                return super.stringToValue(text);
            }
        };
        //TODO create a factory for this
//        formatter.setAllowsInvalid(false);
        formatter.setValueClass(field.getType());
        return new JFormattedTextField(formatter);
    }

    public <R> void addDataEditor(String fieldName, Class<R> columnClass, FieldValueController<? extends R, ?> fieldValueController) {
        validateEditor(fieldName, columnClass);
        fieldControllers.put(fieldName, fieldValueController);
    }

    public void validateEditor(String fieldName, Class<?> columnClass){
        Arrays.stream(dataClass.getDeclaredFields())
                .filter(field -> field.getName().equals(fieldName))
                .findFirst()
                .ifPresent(field -> {
                    if(!field.getType().equals(columnClass)){
                        throw new RuntimeException(
                                "Field type {" + field.getType() + "} is not compatible with column type {" + columnClass + "} of class {" + dataClass.getName() + "}");
                    }
                });
    }
}
