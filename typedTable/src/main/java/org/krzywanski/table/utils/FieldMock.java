package org.krzywanski.table.utils;

import javax.swing.table.TableColumn;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Object to keep information about field, property descriptor and column name
 */
public class FieldMock {
    final String columnName;
    final Field field;
    final Class<?> type;
    final Function<Object, Object> functionToCompute;
    final PropertyDescriptor propertyDescriptor;
    final TableColumn tableColumn;
    final Boolean isEditable;

    @SuppressWarnings("unchecked")
    public <T, C> FieldMock(String columnName, Class<?> type, Function<T, C> functionToCompute, TableColumn tableColumn, Boolean isEditable) {
        this.columnName = columnName;
        this.field = null;
        this.type = type;
        this.functionToCompute = (Function<Object, Object>) functionToCompute;
        this.propertyDescriptor = null;
        this.tableColumn = tableColumn;
        this.isEditable = isEditable;
    }

    public FieldMock(String columnName, Field field, TableColumn tableColumn, Boolean isEditable) {
        this.columnName = columnName;
        this.field = field;
        this.type = field.getType();
        this.functionToCompute = null;
        this.tableColumn = tableColumn;
        this.isEditable = isEditable;
        this.propertyDescriptor = createPropertyDescriptor();
    }

    public Field getField() {
        return field;
    }

    public Class<?> getType() {
        return type;
    }

    public <X extends Annotation> X getAnnotation(Class<X> annotationClass) {
        if (field != null) {
            return field.getAnnotation(annotationClass);
        }
        return null;
    }

    public String getName() {
        if(field != null)
            return field.getName();
        return columnName;

    }

    public PropertyDescriptor getPropertyDescriptor() {
        return propertyDescriptor;
    }

    public Object invoke(Object o) throws InvocationTargetException, IllegalAccessException {
        if(propertyDescriptor != null)
           return propertyDescriptor.getReadMethod().invoke(o);
        else
          return functionToCompute.apply(o);
    }

    private PropertyDescriptor createPropertyDescriptor() {
        try {
            return new PropertyDescriptor(field.getName(), field.getDeclaringClass());
        } catch (IntrospectionException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Did you forget to add getter and setter for field " + field.getName() + " in class " + field.getDeclaringClass().getCanonicalName() + " ?");
            try {
                //MOCK IN CASE OF NO SETTER IN CASE OF READ ONLY FIELD
                Logger.getAnonymousLogger().log(Level.WARNING, "Field " + field.getName() + " will be not editable!");
                return new PropertyDescriptor(field.getName(), field.getDeclaringClass(), ReflectionUtils.IS_PREFIX + ReflectionUtils.capitalize(field.getName()), null);
            } catch (IntrospectionException ex) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Field needs to have at least getter for field " + field.getName() + " in class " + field.getDeclaringClass().getCanonicalName() + " !", ex);
                throw new RuntimeException(ex);
            }
        }
    }

    public TableColumn getTableColumn() {
        return tableColumn;
    }

    public Boolean getEditable() {
        return isEditable;
    }
}
