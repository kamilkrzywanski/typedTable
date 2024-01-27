package org.krzywanski.table.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

/**
 * Object to keep information about field, property descriptor and column name
 */
public class FieldMock {
    final String columnName;
    final Field field;
    final Class<?> type;
    final Function<Object, Object> functionToCompute;

    final PropertyDescriptor propertyDescriptor;

    @SuppressWarnings("unchecked")
    public <T, C> FieldMock(String columnName, Class<?> type, Function<T, C> functionToCompute) {
        this.columnName = columnName;
        this.field = null;
        this.type = type;
        this.functionToCompute = (Function<Object, Object>) functionToCompute;
        this.propertyDescriptor = null;
    }

    public FieldMock(String columnName, Field field) {
        this.columnName = columnName;
        this.field = field;
        this.type = field.getType();
        this.functionToCompute = null;

        try {
            this.propertyDescriptor = new PropertyDescriptor(field.getName(), field.getDeclaringClass());
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }

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
}
