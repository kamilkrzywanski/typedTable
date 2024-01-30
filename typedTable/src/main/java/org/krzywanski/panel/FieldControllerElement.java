package org.krzywanski.panel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyDescriptor;

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
     * First component - usually label
     */
    Component firstComponent;
    /**
     * Component for editing
     */
    Component secondComponent;

    /**
     * Constructor
     * @param type type of element
     * @param propertyDescriptor property descriptor for element
     * @param firstComponent label component
     * @param secondComponent component for editing
     */
    public FieldControllerElement(Class<?> type, PropertyDescriptor propertyDescriptor, Component firstComponent, Component secondComponent) {
        this.type = type;
        this.propertyDescriptor = propertyDescriptor;
        this.firstComponent = firstComponent;
        this.secondComponent = secondComponent;
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
}
