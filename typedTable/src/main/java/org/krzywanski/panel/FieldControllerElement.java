package org.krzywanski.panel;

import org.krzywanski.panel.fields.FieldValueController;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.function.Function;
import java.util.function.Supplier;

public class FieldControllerElement {
    /**
     * Type of elemnt in panel
     */
    Class<?> type;

    /**
     * Property descriptor for element
     */
    PropertyDescriptor propertyDescriptor;

    /**
     * Field value supplier
     */
    Field field;
    /**
     * First component - usually label
     */
    Component firstComponent;
    /**
     * Component for editing
     */
    Component secondComponent;

    FieldValueController<Object> fieldValueController;

    /**
     * Constructor
     * @param field              field from data class
     * @param propertyDescriptor property descriptor for element
     */
    public FieldControllerElement(Field field, PropertyDescriptor propertyDescriptor) {
        this.field = field;
        this.type = field.getType();
        this.propertyDescriptor = propertyDescriptor;
    }


    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public PropertyDescriptor getPropertyDescriptor() {
        return propertyDescriptor;
    }

    public void setPropertyDescriptor(PropertyDescriptor propertyDescriptor) {
        this.propertyDescriptor = propertyDescriptor;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Component getFirstComponent() {
        return firstComponent;
    }

    public void setFirstComponent(Component firstComponent) {
        this.firstComponent = firstComponent;
    }

    public Component getSecondComponent() {
        return secondComponent;
    }

    public void setSecondComponent(Component secondComponent) {
        this.secondComponent = secondComponent;
    }


    public FieldValueController<Object> getFieldValueController() {
        return fieldValueController;
    }

    public void setFieldValueController(FieldValueController<?> fieldValueController) {
        this.fieldValueController = (FieldValueController<Object>) fieldValueController;
    }
}
