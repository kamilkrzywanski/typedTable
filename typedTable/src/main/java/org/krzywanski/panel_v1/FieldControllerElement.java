package org.krzywanski.panel_v1;

import org.krzywanski.panel_v1.autopanel.TypedAutoPanel;
import org.krzywanski.panel_v1.fields.FieldValueController;
import org.krzywanski.panel_v1.validation.ValidatorDialog;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Objects;

public class FieldControllerElement {

    final TypedAutoPanel<?> owner;
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

    FieldValueController<Object, JComponent> fieldValueController;
    /**
     * Dialog for validation
     */
    ValidatorDialog<?> dialog;

    /**
     * Constructor
     * @param field              field from data class
     * @param propertyDescriptor property descriptor for element
     */
    public FieldControllerElement(TypedAutoPanel<?> owner, Field field, PropertyDescriptor propertyDescriptor) {
        this.owner = owner;
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

    public void setSecondComponent(JComponent secondComponent) {
        this.secondComponent = secondComponent;
    }


    public FieldValueController<Object, JComponent> getFieldValueController() {
        return fieldValueController;
    }

    public void setFieldValueController(FieldValueController<?, ?> fieldValueController) {
        this.fieldValueController = (FieldValueController<Object, JComponent>) fieldValueController;
    }

    public Component getEditorComponent() {
        if (secondComponent != null)
            return secondComponent;
        else
            return firstComponent;
    }

    public void setValidationDialog(ValidatorDialog<?> dialog) {
        this.dialog = dialog;
    }

    public ValidatorDialog<?> getValidationDialog() {
        return Objects.requireNonNullElseGet(dialog, () -> dialog = new ValidatorDialog<>(this, owner));
    }

    public void validate() {
        getValidationDialog().showIfErrorsPresent();
    }

    public TypedAutoPanel<?> getOwner() {
        return owner;
    }
}
